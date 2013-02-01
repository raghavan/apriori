package dm.cs583.commons;

import java.util.ArrayList;

public class Candidate {

	private ArrayList<Integer> items = new ArrayList<Integer>();
	private int count=0;
	
	public Candidate(){
		
	}
	public Candidate(ArrayList<Integer> items,int count){
		this.items=items;
		this.count=count;
	}

	public ArrayList<Integer> getItems() {
		return items;
	}

	public void setItems(ArrayList<Integer> items) {
		this.items = items;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	
}
