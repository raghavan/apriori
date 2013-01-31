package combinator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.ItemPair;

import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

import com.sun.corba.se.spi.ior.MakeImmutable;

public class Combinator {

	public static Set<ItemPair> allPossibleCombinations(ItemPair itemPair) {
		// Create an initial vector/set
		ICombinatoricsVector<String> initialSet = Factory.createVector(itemPair
				.getItemPairs());

		// Create an instance of the subset generator
		Generator<String> gen = Factory.createSubSetGenerator(initialSet);

		Set<ItemPair> allPossibleCombinations = new HashSet<ItemPair>();
		// Print the subsets
		for (ICombinatoricsVector<String> subSet : gen) {
			if (subSet.getVector().size() >= 1) {
				List<String> obtainedValues = subSet.getVector();
				ItemPair itemPairFromCombination = makeItemPair(obtainedValues);
				allPossibleCombinations.add(itemPairFromCombination);
			}
		}
		return allPossibleCombinations;
	}

	private static ItemPair makeItemPair(List<String> obtainedValues) {
		ItemPair itemPair = new ItemPair();
		for (String str : obtainedValues) {
			itemPair.addItemPair(str);
		}
		return itemPair;
	}

	public static Set<ItemPair> allPossibleCombinations(ItemPair itemPair,
			int combinationCount) {
		// Create an initial vector/set
		ICombinatoricsVector<String> initialSet = Factory.createVector(itemPair
				.getItemPairs());

		// Create an instance of the subset generator
		Generator<String> gen = Factory.createSimpleCombinationGenerator(initialSet, combinationCount);


		Set<ItemPair> allPossibleCombinations = new HashSet<ItemPair>();
		// Print the subsets
		for (ICombinatoricsVector<String> subSet : gen) {
			if (subSet.getVector().size() == combinationCount) {
				List<String> obtainedValues = subSet.getVector();
				ItemPair itemPairFromCombination = makeItemPair(obtainedValues);
				allPossibleCombinations.add(itemPairFromCombination);
			}
		}
		return allPossibleCombinations;
	}

	private static String getCSVString(List<String> obtainedValues) {
		StringBuilder csv = new StringBuilder();
		for (String str : obtainedValues) {
			csv.append(str.trim() + ",");
		}
		csv.deleteCharAt(csv.length() - 1);
		// System.out.println(csv.toString());
		return csv.toString();
	}

	public static void main(String args[]) {
		Set<String> testList = new HashSet<String>();
		testList.add("acs");
		testList.add("dad");
		testList.add("aad");
		ItemPair itemPair = new ItemPair();
		itemPair.setItemPairs(testList);
		Set<ItemPair> allPosComb = Combinator.allPossibleCombinations(itemPair,2);
		for (ItemPair str : allPosComb) {
			System.out.println(str.getItemPairs());
		}
	}

}
