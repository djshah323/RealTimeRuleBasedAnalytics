package com.realanalytics.RealAnalytics.Pipeline;

import java.util.ArrayList;

public class Record {
	
	ArrayList<Attribute> attr;
	
	public Record() {
		attr = new ArrayList<Attribute>();
	}
	
	public void addAttribute(Attribute attr) {
		this.attr.add(attr);
	}
	
}
