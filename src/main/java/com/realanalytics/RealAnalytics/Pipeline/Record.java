package com.realanalytics.RealAnalytics.Pipeline;

import java.util.HashMap;
import java.util.Map;

public class Record {
	
	Map<String, Attribute> attr;
	
	public Record() {
		attr = new HashMap<String, Attribute>();
	}
	
	public void add(Attribute attr) {
		this.attr.put(attr.getName(), attr);
	}
	
	public Attribute get(String name) {
		return this.attr.get(name);
	}
}
