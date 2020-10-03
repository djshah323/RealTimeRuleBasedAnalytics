package com.realanalytics.RealAnalytics.Pipeline.Rule;

import java.util.Stack;

public class IfKey extends Condition implements HasVerb {
	
	Stack<Verb> verb;
	
	public IfKey(String targetAttr, String operator, String verb) {
		super(targetAttr, operator);
		
	}
}
