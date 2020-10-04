package com.realanalytics.RealAnalytics.Pipeline.Rule.Conditions;


import static com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil.IF_KEY;
import static com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil.IF_VALUE;

import java.util.Map;
import java.util.Stack;

import com.realanalytics.RealAnalytics.Pipeline.Record;
import com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil;
import com.realanalytics.RealAnalytics.Pipeline.Rule.Verbs.HasVerb;
import com.realanalytics.RealAnalytics.Pipeline.Rule.Verbs.Verb;


public abstract class Condition implements HasVerb {
	
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
	private Stack<Verb> verb;
	
	protected Condition(String targetAttr, String operator, String verb) {
		this.operator = operator;
		this.targetAttr = targetAttr.split("-").length == 2 ? targetAttr.split("-")[1] 
					: targetAttr.split("-")[2];
		this.verb = Verb.resolveVerbs(verb);
	}
	
	public static Map conditionClasses = RuleUtil.createMap(new Object[] {
			IF_KEY,			IfKey.class,
			IF_VALUE,		IfValue.class});
	
	public abstract boolean evaluate(String key, Record value);
	
	@Override
	public Stack<Verb> initVerb() {
		Stack<Verb> newStack = new Stack<Verb>();
		newStack.addAll(verb);
		return newStack;
	}
}
