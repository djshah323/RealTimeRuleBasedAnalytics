package com.realanalytics.RealAnalytics.Pipeline.Rule.Verbs;

import java.text.MessageFormat;
import java.util.LinkedList;

import com.realanalytics.RealAnalytics.Pipeline.Record;

public class VerbKey extends Verb {

	public VerbKey(String arg) {
		super(arg);
	}

	@Override
	public String call(LinkedList<String> evaluation, String key, Record rec) {
		logger.info(MessageFormat.format("{0} returns key {1}", 
				VerbKey.class.getSimpleName(), key));
		return key;
	}

}
