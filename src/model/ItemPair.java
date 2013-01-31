package model;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class ItemPair {

	private Set<String> itemPairs = new TreeSet<String>();
	

	public Set<String> getItemPairs() {
		return itemPairs;
	}

	public void setItemPairs(Set<String> itemPairs) {
		this.itemPairs = itemPairs;
	}
	
	public void addItemPair(String itemPair) {
		itemPairs.add(itemPair);
	}
	
	public void addAllItemPairs(Set<String> itemPairs) {
		this.itemPairs.addAll(itemPairs);
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((itemPairs == null) ? 0 : itemPairs.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		ItemPair checkingItemPair = (ItemPair) obj;
		if(this.getItemPairs() == null)
			return false;
		if(checkingItemPair.getItemPairs().size() != this.getItemPairs().size())
			return false;
		return checkBothHasSameElements(this.getItemPairs(),checkingItemPair.getItemPairs());
	}

	private boolean checkBothHasSameElements(Set<String> itemPairs,
			Set<String> checkingItemPairs) {
		Iterator<String> itemIterate = itemPairs.iterator();
		while(itemIterate.hasNext()){
			if(!checkingItemPairs.contains(itemIterate.next()))
					return false;
		}
		return true;
	}
	
	
}
