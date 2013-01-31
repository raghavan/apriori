package unittester;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import inputreader.FileInputReader;
import inputreader.IInputReader;

import org.junit.Test;

import apriori.AprioriAlgorithm;

import combinator.Combinator;

public class AprioriTest {

	@Test
	public void testInputReader() {
		IInputReader inputReader = new FileInputReader();
		List<String> input = inputReader.getInput();
		assertEquals(7, input.size());
	}
	
	@Test
	public void testInputSetCount(){
		IInputReader inputReader = new FileInputReader();
		List<String> input = inputReader.getInput();
		assertEquals(3, input.get(0).split(",").length);		
	}
	
	@Test
	public void testInputSetValue(){
		IInputReader inputReader = new FileInputReader();
		List<String> input = inputReader.getInput();		
		String actual = input.get(3).split(",")[1].trim();
		assertTrue(actual.equalsIgnoreCase("Chicken"));		
	}
	
	@Test
	public void testAllPossibleCombination(){
		Combinator combinator = new Combinator();
		List<String> testList =  new ArrayList<String>();
		testList.add("beef");testList.add("chicken");testList.add("milk");
		List<String> allPosComb = combinator.allPossibleCombinations(testList);		
		int expectedCount = (int) Math.pow(2, testList.size()) -1 ;
		assertEquals(expectedCount, allPosComb.size());
	}
	
	@Test
	public void checkUniqInputCount(){
		AprioriAlgorithm aprioriAlgorithm = new AprioriAlgorithm();
		//List<String> uniqInput = aprioriAlgorithm.aprioritizer();
		//System.out.println(uniqInput);
		//assertEquals(6, uniqInput.size());
	}

}
