package com.realanalytics.RealAnalytics.Pipeline.Rule.Conditions;

import java.util.LinkedList;
import java.util.Stack;
import java.util.regex.Pattern;

import com.realanalytics.RealAnalytics.Pipeline.Record;
import com.realanalytics.RealAnalytics.Pipeline.Rule.Verbs.Verb;

public class IfKey extends Condition {
	
	public IfKey(String targetAttr, String operator, String verb) {
		super(targetAttr, operator, verb);
	}
	
	@Override
	public boolean evaluate(String key, Record rec) {
		LinkedList<String> evaluation = new LinkedList<String>();
		Stack<Verb> verb = initVerb();
		while(verb.isEmpty())
			evaluation.addLast(verb.pop()
					.call(evaluation, key, rec));
		if (evaluation.size() == 0
				|| evaluation.getFirst().equalsIgnoreCase("")) {
			return true;
		}
		switch(this.operator) {
			case Condition.EQ: {
				if (key.equals(evaluation.getFirst()))
					return true;
				break;
			}
			case Condition.NE: {
				if (!key.equals(evaluation.getFirst()))
					return true;
				break;
			}
			case Condition.GE: {
				Integer keyInt = Integer.parseInt(key);
				Integer compInt = Integer.parseInt(evaluation.getFirst());
				if (keyInt >= compInt)
					return true;
				break;
			}
			case Condition.LE: {
				Integer keyInt = Integer.parseInt(key);
				Integer compInt = Integer.parseInt(evaluation.getFirst());
				if (keyInt <= compInt)
					return true;
				break;
			}
			case Condition.GT: {
				Integer keyInt = Integer.parseInt(key);
				Integer compInt = Integer.parseInt(evaluation.getFirst());
				if (keyInt > compInt)
					return true;
				break;
			}
			case Condition.LT: {
				Integer keyInt = Integer.parseInt(key);
				Integer compInt = Integer.parseInt(evaluation.getFirst());
				if (keyInt < compInt) 
					return true;
				break;
			}
			case Condition.MH: {
				Pattern pattern = Pattern.compile(evaluation.getFirst());
				if (pattern.matcher(key).matches())
					return true;
				break;
			}
		}
		return false;
	}
}
