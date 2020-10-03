package com.realanalytics.RealAnalytics.Pipeline.Rule;


import static com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil.IF_KEY;
import static com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil.IF_VALUE;

import java.util.Map;
import java.util.Stack;

import com.realanalytics.RealAnalytics.Pipeline.Record;


public abstract class Condition {
	
	//operators
	
	static final String EQ = "eq";
	static final String MH = "match";
	static final String NE = "ne";
	static final String LT = "lt";
	static final String GT = "gt";
	static final String LE = "le";
	static final String GE = "ge";
	
	protected String operator;
	protected String targetAttr;
	
	protected Condition(String targetAttr, String operator) {
		this.operator = operator;
		this.targetAttr = targetAttr.split("-").length == 2 ? targetAttr.split("-")[1] 
					: targetAttr.split("-")[2];
		
	}
	
	public static Map conditionClasses = RuleUtil.createMap(new Object[] {
			IF_KEY,			IfKey.class,
			IF_VALUE,		IfValue.class});
	
	public abstract boolean evaluate(String key, Record value);
}
