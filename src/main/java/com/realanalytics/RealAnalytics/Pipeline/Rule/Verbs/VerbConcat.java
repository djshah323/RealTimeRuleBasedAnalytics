package com.realanalytics.RealAnalytics.Pipeline.Rule.Verbs;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.LinkedList;

import com.realanalytics.RealAnalytics.Pipeline.Record;

public class VerbConcat extends Verb {

	public VerbConcat(String arg) {
		super(arg);
	}

	@Override
	public String call(LinkedList<String> evaluation, String key, Record rec) {
		StringBuilder returnValue = new StringBuilder();
		Iterator itr = evaluation.iterator();
		while (itr.hasNext()) {
			String concatValue = (String) itr.next();
			returnValue.append(concatValue);
		}
		evaluation.clear();
		logger.info(MessageFormat.format("{0} returns key {1}", 
				VerbConcat.class.getSimpleName(), returnValue.toString()));
		return returnValue.toString();
	}

}
