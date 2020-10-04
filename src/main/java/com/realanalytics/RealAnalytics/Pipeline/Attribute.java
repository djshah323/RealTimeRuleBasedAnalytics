package com.realanalytics.RealAnalytics.Pipeline;

public class Attribute {
	
	private String name;
	
	private String type;
	
	private Object value;

	public Attribute(String name, String type) {
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
}
