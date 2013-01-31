package apriori;

import inputreader.FileInputReader;
import inputreader.IInputReader;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.Constants;

import model.CombinationMap;
import model.ItemPair;
import model.TransactSet;

import combinator.Combinator;

public class AprioriAlgorithm {
	
	public static void main(String args[]){
		Set<ItemPair> results = aprioritizer(); // final results	
	}

	private static void printItemPairs(Set<ItemPair> results) {
		for(ItemPair itemPair : results){
			System.out.println(itemPair.getItemPairs());
		}
		System.out.println("--------");
	}
	
	public static Set<ItemPair> aprioritizer() {
		IInputReader inputReader = new FileInputReader();
		List<TransactSet> input = inputReader.getInput();
		int totalNumberOfTransactions = input.size();
		CombinationMap combinationMap = getAllCombinationMap(input);
		//combinationMap.print();
		Set<ItemPair> singleItemPairs = combinationMap.getSingleElements();
		printItemPairs(singleItemPairs);
		int startCount = 1;
		Set<ItemPair> result = makePruning(singleItemPairs,combinationMap, startCount, totalNumberOfTransactions);
		
 		return result;
	}
	
	public static Set<ItemPair> makePruning(Set<ItemPair> itemPairsPassed,CombinationMap combinationMap, 
						int count, int totalNumOfTransact){
		
		Set<ItemPair> selectedItemPairs = new HashSet<ItemPair>();
		for(ItemPair itemPair : itemPairsPassed){			
			if(((combinationMap.getCount(itemPair)*100) / totalNumOfTransact) >= Constants.minSupport){
				selectedItemPairs.add(itemPair);
			}
		}

		
		if(selectedItemPairs.size() == 0){
			return itemPairsPassed;
		}
		System.out.println(count);
		printItemPairs(selectedItemPairs);
		
		ItemPair itemPairMerged = new ItemPair();
		for(ItemPair itemPairSelected : selectedItemPairs){		
			for(String str : itemPairSelected.getItemPairs()){
				itemPairMerged.addItemPair(str);
			}
		}
		itemPairsPassed = Combinator.allPossibleCombinations(itemPairMerged, count+1);
		//printItemPairs(itemPairsPassed);
		return makePruning(itemPairsPassed,combinationMap, count+1,totalNumOfTransact);
	}
		

	private static CombinationMap getAllCombinationMap(List<TransactSet> input) {
		CombinationMap combinationMap = new CombinationMap();
		for(TransactSet transactSet : input){			
				ItemPair itemPair = makeItemPair(transactSet);
				Set<ItemPair> combinationItemPairs = Combinator.allPossibleCombinations(itemPair);
				combinationMap.addNewCombination(combinationItemPairs);
		}
		return combinationMap;
	}

	private static ItemPair makeItemPair(TransactSet transactSet) {
		ItemPair itemPair = new ItemPair();		
		for(String str : transactSet.getTransactSet()){
			itemPair.addItemPair(str);
		}
		return itemPair;
	}

}
