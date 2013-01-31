package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CombinationMap {

	private static Map<ItemPair, Integer> combinationCount = new HashMap<ItemPair, Integer>();

	public Map<ItemPair, Integer> getCombinationCount() {
		return combinationCount;
	}

	public void addNewCombination(ItemPair itemPair) {
		int count = 1;
		if (combinationCount.containsKey(itemPair)) {
			count = combinationCount.get(itemPair) + 1;
		}
		combinationCount.put(itemPair, count);
	}

	public Integer getCount(ItemPair itemPair) {
		if (combinationCount.containsKey(itemPair))
			return combinationCount.get(itemPair);
		return 0;
	}

	public void print() {
		for (Map.Entry<ItemPair, Integer> entry : combinationCount.entrySet()) {
			System.out.println(entry.getKey().getItemPairs() + " = "
					+ entry.getValue());
		}
	}

	public void addNewCombination(Set<ItemPair> combinationItemPairs) {
		for (ItemPair itemPair : combinationItemPairs) {
			addNewCombination(itemPair);
		}
	}

	public Set<ItemPair> getSingleElements() {
		Set<ItemPair> singleElements = new HashSet<ItemPair>();
		for (ItemPair itemPair : combinationCount.keySet()) {
			if (itemPair.getItemPairs().size() == 1) {
				singleElements.add(itemPair);
			}
		}
		return singleElements;
	}

}
