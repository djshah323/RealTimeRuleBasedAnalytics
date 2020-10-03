package com.realanalytics.RealAnalytics.Pipeline.Rule;

public class IfValue extends Condition implements HasVerb {

	Verb verb;
	
	public IfValue(String targetAttr, String operator, String verb) {
		super(targetAttr, operator);
		
	}
}
