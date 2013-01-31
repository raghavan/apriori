package inputreader;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import util.Constants;

import model.TransactSet;


public class FileInputReader implements IInputReader {

	public List<TransactSet> getInput() {
		List<TransactSet> inputData = new ArrayList<TransactSet>();
			FileInputStream fstream = null;
			try {
				fstream = new FileInputStream(Constants.fileName);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			try {
				while ((strLine = br.readLine()) != null) {
					TransactSet transactSet = makeTransactSet(strLine);					
					inputData.add(transactSet);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					br.close();
				} catch (IOException e) {				
					e.printStackTrace();
				}
			}
			return inputData;
	}
	
	private TransactSet makeTransactSet(String strLine) {
		TransactSet transactSet = new TransactSet();
		for(String str : strLine.split(",")){
			transactSet.addTransactSet(str.trim());
		}
		return transactSet;
	}

}
