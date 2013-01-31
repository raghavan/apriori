package model;

import java.util.Set;
import java.util.TreeSet;

public class TransactSet {

	private Set<String> transactSet = new TreeSet<String>();

	public Set<String> getTransactSet() {
		return transactSet;
	}

	public void setTransactSet(Set<String> transactSet) {
		this.transactSet = transactSet;
	}

	public void addTransactSet(String transactElement) {
		this.transactSet.add(transactElement);
	}

	public void addAllTransactSet(Set<String> transactSet) {
		this.transactSet.addAll(transactSet);
	}

}
