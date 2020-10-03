package com.realanalytics.RealAnalytics.Pipeline.Rule;

import java.util.LinkedList;

import com.realanalytics.RealAnalytics.Pipeline.Attribute;
import com.realanalytics.RealAnalytics.Pipeline.Record;

public class VerbValue extends Verb {

	public VerbValue(String arg) {
		super(arg);
	}

	@Override
	public String call(LinkedList<String> evaluation, String key, Record rec) {
		Attribute attr = rec.get(this.arg);
		return (String) attr.getValue();
	}

}
