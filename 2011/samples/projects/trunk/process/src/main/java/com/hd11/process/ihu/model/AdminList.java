package com.hd11.process.ihu.model;

import java.util.Iterator;
import java.util.LinkedList;


public class AdminList extends LinkedList<String>{
	
	private static final long serialVersionUID = 1L;

	public AdminList() {
		this.add("Foxi");
		this.add("Fix");
	}
	
	@Override
	public String toString() {
		StringBuffer strBuff = new StringBuffer();
		Iterator<String> it = this.iterator();
		while(it.hasNext()){
			strBuff.append(it.next());
			if(it.hasNext()){
				strBuff.append(",");
			}
		}
		return strBuff.toString();
	}
}
