package dm.cs583;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import dm.cs583.commons.Candidate;

public class Apriori {

	static HashMap<Integer, ArrayList<Integer>> transactions = new HashMap<Integer, ArrayList<Integer>>(); //Transactions in Memory
	static ArrayList<Integer> sortedItems = new ArrayList<Integer>(); //Sorted Items based on UID
	static HashMap<Integer,String> itemMap = new HashMap<Integer,String>(); // UID => Item
	static HashMap<String,Integer> invertedItemMap = new HashMap<String,Integer>(); // Item ==> UID
	static int numTrans; //Number of Transactions
	static int uid=0; //UID
	
	
	/**
	 * This is the initial Pass to generate F1 and to create item-UID mappings, sort etc.
	 * Reurns item frequency
	 * @param t
	 * @return
	 */
	public static HashMap<Integer, Integer> initialPass(File t) {
		// Key ==> item
		// Value ==> Count
		HashMap<Integer, Integer> freqSet = new HashMap<Integer, Integer>();
		
		/**
		 * 
		 * Calculate item Frequency, Do item mapping here.
		 */
		int transactionCount = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(t));
			String readLine = "";
			ArrayList<Integer> items;
			int itemNo=1;
			while ((readLine = br.readLine()) != null) {
				transactionCount++;
				items = getArrayList(readLine.split(","));
				
				transactions.put(transactionCount, items);
				for (int item : items) {
					if (freqSet.get(item) == null) {
						freqSet.put(item, 1);
					} else
						freqSet.put(item, freqSet.get(item) + 1);
				}
			}
			br.close();
			
			/**
			 * Add unique items here. I don't want another hashing earlier.
			 */
			for(int item:freqSet.keySet()) sortedItems.add(item);
			Collections.sort(sortedItems);

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		numTrans=transactions.size();
		return freqSet;
	}

	/**
	 * 
	 * Split the transaction items and get it back - To calculate frequencies.
	 * 
	 * @param split
	 * @return
	 */
	private static ArrayList<Integer> getArrayList(String[] split) {
		ArrayList<Integer> items = new ArrayList<Integer>();
		for (String s : split) {
			if(invertedItemMap.get(s)!=null) items.add(invertedItemMap.get(s));
			else
			{
				uid++;
				invertedItemMap.put(s, uid);
				itemMap.put(uid,s);
				items.add(uid);
			}
		}
		return items;
	}
	
	/**
	 * Main Apriori algorithm
	 * 
	 * 
	 * @param itemFreq
	 * @param minsupValue
	 * @return
	 */
	
	public static HashMap<Integer, ArrayList<Candidate>> doApriori(HashMap<Integer, Integer> itemFreq,double minsupValue){
		HashMap<Integer,ArrayList<Candidate>> freqSets = new HashMap<Integer,ArrayList<Candidate>>();
		int fCount=1;
	/**
	 * Use support value (out of 1) and generate F1
	 * 
	 */
		ArrayList<Candidate> freqSetContents = new ArrayList<Candidate>();
		for(int item:sortedItems){
			
			if((double)itemFreq.get(item)/numTrans >=minsupValue){
				
				Candidate frequency = new Candidate();
				ArrayList<Integer> itemSet = new ArrayList<Integer>();
				itemSet.add(item);
				frequency.setItems(itemSet);
				frequency.setCount(itemFreq.get(item));
				freqSetContents.add(frequency);
			}
		}
		freqSets.put(fCount,freqSetContents);
		//Increment Frequency LEVEL. 
		fCount++;
				
		//F2 to .... Fn
		ArrayList<Candidate> candidateGenOutput = new ArrayList<Candidate>();
		int k=0;
		
		while(freqSets.get(fCount-1).size()!=0){
			k++;
			
		
			freqSetContents = new ArrayList<Candidate>();
			
			candidateGenOutput=generateCandidates(freqSets.get(fCount-1),k-1);	//Send in Previous Frequency LEVEL and LEVEL number
			
			//Check Transactions
			ArrayList<Candidate> tempCand=new ArrayList<Candidate>();
			for(ArrayList<Integer> t:transactions.values()){
				for(int i=0;i<candidateGenOutput.size();i++){
					if(t.containsAll(candidateGenOutput.get(i).getItems())) candidateGenOutput.get(i).setCount(candidateGenOutput.get(i).getCount()+1);
					
				}
			}
			
		for(Candidate c:candidateGenOutput){	
			if((double)c.getCount()/numTrans >=minsupValue){
				freqSetContents.add(c);
				
			}
		}
		freqSets.put(fCount, freqSetContents);
		fCount++;
			
		}
		
		return freqSets;
		
	}

	private static void printCandidates(ArrayList<Candidate> candidateGenOutput) {
		
		for(Candidate i:candidateGenOutput){
			System.out.println("C:: " +i.getItems());
			
		}
		
	}

	/**
	 * Generate Candidates. Prune candidates by checking subsets.
	 * Finding the transaction occurrence will take place in Apriori. 
	 * Use String matching!! :)
	 * 
	 * @param freqSets
	 * @param k
	 * @return
	 */
	private static ArrayList<Candidate> generateCandidates(
			ArrayList<Candidate> freqSets, int k) {
		
		ArrayList<ArrayList<String>> candidates = new ArrayList<ArrayList<String>>();
		HashMap<String,String> frequencySplitMap = new HashMap<String,String>(); //To create Candidates
		ArrayList<String> keyOrder = new ArrayList<String>(); //To iterate through frequencySplitMap
		ArrayList<String> unprunedCandidates = new ArrayList<String>(); // Unpruned Candidates
		ArrayList<Candidate> prunedCandidates = new ArrayList<Candidate>(); // pruned Candidates
		ArrayList<String> frequencies = new ArrayList<String>(); // Frequencies
		
		//Load Frequencies as String for Pruning
		for(int i=0;i<freqSets.size();i++){
			frequencies.add(getString(freqSets.get(i).getItems(),true));
			
		}
	
		
		if(k==0){ //This is to generate C2 from F1
			
			//Do the mapping
		for(int i=0;i<freqSets.size()-1;i++){
			String prevItem=String.valueOf(freqSets.get(i).getItems().get(0));
			for(int j=i+1;j<freqSets.size();j++){
				String curItem=String.valueOf(freqSets.get(j).getItems().get(0));
				if(frequencySplitMap.get(prevItem)!=null) {
					String temp=frequencySplitMap.get(prevItem);
					temp = temp+","+curItem;
					frequencySplitMap.put(prevItem,temp);
				}else{
					String temp = "";
					temp = temp+","+curItem;
					frequencySplitMap.put(prevItem,temp.substring(1,temp.length()));
				}
				
			}
			keyOrder.add(prevItem);
		}
		
		//Generate Unpruned Candidates
		updateUnprunedCandidates(unprunedCandidates,frequencySplitMap,keyOrder,k+1);
		}else{
			for(int i=0;i<freqSets.size()-1;i++){
				ArrayList<Integer> map1Set = freqSets.get(i).getItems();
				
				String map1Key=getString(map1Set,false);
				String map1Value=String.valueOf(map1Set.get(map1Set.size()-1));
			
				for(int j=i+1;j<freqSets.size();j++){
					ArrayList<Integer> map2Set = freqSets.get(j).getItems();
					String map2Key=getString(map2Set,false);
					String map2Value=String.valueOf(map2Set.get(map2Set.size()-1));
					if(map1Key.equalsIgnoreCase(map2Key)){
						if(frequencySplitMap.get(map1Key)!=null){
							String mapValue=frequencySplitMap.get(map1Key)+","+map2Value;
							frequencySplitMap.put(map1Key,mapValue);
						}
						else{
							frequencySplitMap.put(map1Key,map1Value+","+map2Value);
							
						}
					}
				}
				if(!keyOrder.contains(map1Key))keyOrder.add(map1Key);
			}

			// Now We have pairs of item sets that differ in last item ONLY. 
			// Join the pairs. Give the LEVEL number (my k has 0-based index)
			
			updateUnprunedCandidates(unprunedCandidates,frequencySplitMap,keyOrder,k+1);
						
		}
		
		//Prune here
		for(String candidate:unprunedCandidates){
			String[] candidateCombinations=returnCombinations(candidate,k+1);
			boolean validCandidate=true;
			for(String c:candidateCombinations){
				if(!frequencies.contains(c)) {validCandidate=false; break;}
			}
			if(validCandidate){
				ArrayList<Integer> temp=getIntListFromString(candidate.split(","));
				
				prunedCandidates.add(new Candidate(temp,0));
			}
		}
		
		
		return prunedCandidates;
	}
	
	private static ArrayList<Integer> getIntListFromString(String[] split) {
		ArrayList<Integer> itemSets = new ArrayList<Integer>();
		for(String s:split){
			itemSets.add(Integer.parseInt(s));
		}
		return itemSets;
	}

	private static String getString(ArrayList<Integer> items,boolean getAll) {
		String concatenatedString="";
		if(!getAll)
		for(int i=0;i<items.size()-1;i++){
			concatenatedString=concatenatedString+String.valueOf(items.get(i))+",";
		}
		else
			for(int i=0;i<items.size();i++){
				concatenatedString=concatenatedString+String.valueOf(items.get(i))+",";
			}
		return concatenatedString.substring(0, concatenatedString.length()-1);
	}

	/**
	 * 
	 * Generate UnprunedCandidates
	 * 
	 * @param unprunedCandidates
	 * @param frequencySplitMap
	 * @param keyOrder
	 * @param level 
	 */

	private static void updateUnprunedCandidates(
			ArrayList<String> unprunedCandidates,
			HashMap<String, String> frequencySplitMap,
			ArrayList<String> keyOrder, int level) {
		for(String key:keyOrder){
			String[] combinations=returnCombinations(frequencySplitMap.get(key),level);
			for(String s:combinations)
			unprunedCandidates.add(key+","+s);
			
			
		}
			
	}
/**
 * Returns all possible combinations for generating candidates
 * @param level 
 * 
 * @param string
 * @return
 */
	private static String[] returnCombinations(String itemSet, int level) {
		if(itemSet==null) return new String[]{};
		String strArray[]=itemSet.split(",");
		
		ArrayList<String> combinations=new ArrayList<String>();
			for(int i=0;i<strArray.length-1;i++){
				String temp=strArray[i];
				for(int j=i+1;j<strArray.length;j++){
					
					if(strArray.length-j>=level-1) 
						{
						for(int m=j;m<j+(level-1);m++){
							temp=temp+","+strArray[m];
							
						}
						if(!combinations.contains(temp)&&temp!=null)combinations.add(temp); 
						}
					
					temp=strArray[i];
				}
			}
	
			if(level==1||combinations==null) combinations.add(strArray[strArray.length-1]);
			
		return combinations.toArray(new String[]{});
	}

	public static void main(String[] args) {
	HashMap<Integer,Integer> itemFreq=initialPass(new File("sample.dat"));
	HashMap<Integer, ArrayList<Candidate>> freqs = doApriori(itemFreq,0.30);
	System.out.println(itemMap);
	System.out.println("Number of Transactions::"+numTrans);
	System.out.println("Item Frequency:: "+itemFreq);
	System.out.println("Sorted Items:: "+sortedItems);
	printFreq(freqs);


	}
	public static void printFreq(HashMap<Integer, ArrayList<Candidate>> freqs)
	{
		for(int i:freqs.keySet()){
			System.out.println("F"+i+"::");
			String freq="";
			for(Candidate c:freqs.get(i))
			{
				String finalSet="{";
				for(int itemNo:c.getItems())
				finalSet=finalSet+itemMap.get(itemNo)+",";
				finalSet=finalSet+"}";
				freq=freq+finalSet;
			}
			System.out.println(freq);
		}
	}
}
