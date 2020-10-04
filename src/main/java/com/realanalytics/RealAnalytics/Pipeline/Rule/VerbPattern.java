package com.realanalytics.RealAnalytics.Pipeline.Rule;

import java.text.MessageFormat;
import java.util.LinkedList;

import com.realanalytics.RealAnalytics.Pipeline.Record;

public class VerbPattern extends Verb {

	public VerbPattern(String arg) {
		super(arg);
	}


	@Override
	public String call(LinkedList<String> evaluation, String key, Record rec) {
		logger.info(MessageFormat.format("{0} returns key {1}", 
				VerbPattern.class.getSimpleName(), this.arg));
		return this.arg;
	}


}
