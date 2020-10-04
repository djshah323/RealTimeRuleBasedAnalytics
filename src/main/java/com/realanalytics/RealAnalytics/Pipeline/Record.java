package com.realanalytics.RealAnalytics.Pipeline;

import java.util.HashMap;
import java.util.Map;

public class Record {
	
	public Map<String, Attribute> attr;
	
	public Record() {
		attr = new HashMap<String, Attribute>();
	}
	
	public void add(Attribute attr) {
		this.attr.put(attr.getName(), attr);
	}
	
	public Attribute get(String name) {
		return this.attr.get(name);
	}
	
	
	public static class RecordBuilder {
		private Record r;
		
		public RecordBuilder() {
			r = new Record();
		}
		
		public RecordBuilder addAttr(String name, String value) {
			Attribute attr = new Attribute(name, "String");
			attr.setValue(value);
			r.add(attr);
			return this;
		}
		
		public Record build() {
			return r;
		}
	}
}
