package com.realanalytics.RealAnalytics.Pipeline.Rule.Verbs;

import java.text.MessageFormat;
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
		logger.info(MessageFormat.format("{0} returns key {1}", 
				VerbValue.class.getSimpleName(), (String) attr.getValue()));
		return (String) attr.getValue();
	}

}
