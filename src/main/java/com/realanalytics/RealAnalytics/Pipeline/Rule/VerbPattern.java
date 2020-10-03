package com.realanalytics.RealAnalytics.Pipeline.Rule;

import java.util.LinkedList;

import com.realanalytics.RealAnalytics.Pipeline.Record;

public class VerbPattern extends Verb {

	public VerbPattern(String arg) {
		super(arg);
	}


	@Override
	public String call(LinkedList<String> evaluation, String key, Record rec) {
		return this.arg;
	}


}
