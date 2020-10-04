package com.realanalytics.RealAnalytics.Pipeline.Rule.Conditions;

import java.util.LinkedList;
import java.util.Stack;
import java.util.regex.Pattern;

import com.realanalytics.RealAnalytics.Pipeline.Attribute;
import com.realanalytics.RealAnalytics.Pipeline.Record;
import com.realanalytics.RealAnalytics.Pipeline.Rule.Verbs.Verb;

public class IfValue extends Condition {
	
	public IfValue(String targetAttr, String operator, String verb) {
		super(targetAttr, operator, verb);
	}

	@Override
	public boolean evaluate(String key, Record rec) {
		Stack<Verb> verb = initVerb();
		LinkedList<String> evaluation = new LinkedList<String>();
		Attribute attr = rec.get(this.targetAttr);
		if (attr != null) {
			String value = (String) attr.getValue();
			while(verb.isEmpty())
				evaluation.addLast(verb.pop()
						.call(evaluation, key, rec));	
			if (evaluation.size() == 0 
					|| evaluation.getFirst().equalsIgnoreCase(""))
				return true;
			switch(this.operator) {
				case Condition.EQ: {
					if (value.equals(evaluation.getFirst()))
						return true;
					break;
				}
				case Condition.NE: {
					if (!value.equals(evaluation.getFirst()))
						return true;
					break;
				}
				case Condition.GE: {
					Integer valueInt = Integer.parseInt(value);
					Integer compInt = Integer.parseInt(evaluation.getFirst());
					if (valueInt >= compInt)
						return true;
					break;
				}
				case Condition.LE: {
					Integer valueInt = Integer.parseInt(value);
					Integer compInt = Integer.parseInt(evaluation.getFirst());
					if (valueInt <= compInt)
						return true;
					break;
				}
				case Condition.GT: {
					Integer valueInt = Integer.parseInt(value);
					Integer compInt = Integer.parseInt(evaluation.getFirst());
					if (valueInt > compInt)
						return true;
					break;
				}
				case Condition.LT: {
					Integer valueInt = Integer.parseInt(value);
					Integer compInt = Integer.parseInt(evaluation.getFirst());
					if (valueInt < compInt) 
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
		}

		return false;
	}
}
