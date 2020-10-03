package com.realanalytics.RealAnalytics.Pipeline.Rule;

import java.util.Stack;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Predicate;

import com.realanalytics.RealAnalytics.Pipeline.Record;

public class DoDeny extends Action {

	public DoDeny(String verb) {
	}
	
	@Override
	public void apply(Condition condition, KStream<String, Record> stream) {
		if (condition != null) {
			stream.filter((Predicate<String, Record>)(key, rec) -> {
				return !condition.evaluate(key, rec);
			});	
		}
	}
}
