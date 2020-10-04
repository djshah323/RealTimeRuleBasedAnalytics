package com.realanalytics.RealAnalytics.Pipeline.Rule;

import java.text.MessageFormat;
import java.util.LinkedList;

import com.realanalytics.RealAnalytics.Pipeline.Record;

public class VerbAny extends Verb {

	public VerbAny(String arg) {
		super(arg);
	}

	@Override
	public String call(LinkedList<String> evaluation, String key, Record rec) {
		logger.info(MessageFormat.format("{0} returns key {1}", 
				VerbAny.class.getSimpleName(), key));
		return key;
	}

}
