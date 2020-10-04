package com.realanalytics.RealAnalytics.Pipeline.Rule.Actions;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Predicate;

import com.realanalytics.RealAnalytics.Pipeline.Record;
import com.realanalytics.RealAnalytics.Pipeline.Rule.Conditions.Condition;

public class DoAllow extends Action {

	public DoAllow(String verb) {
	}
   
	@Override
	public KStream<String, Record> apply(Condition condition, KStream<String, Record> stream) {
		KStream<String, Record> streamSendBack = stream;
		try {
			if (condition != null) {
				streamSendBack = streamSendBack.filter((Predicate<String, Record>)(key, rec) -> {
					return condition.evaluate(key, rec);
				});	
			}
		} catch(Throwable e) {
			e.printStackTrace();
		}
		return streamSendBack;
	}
}
