package com.realanalytics.RealAnalytics.Pipeline.Rule;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Predicate;

import com.realanalytics.RealAnalytics.Pipeline.Record;

public class DoAllow extends Action {

	public DoAllow(String verb) {
	}
   
	@Override
	public KStream<String, Record> apply(Condition condition, KStream<String, Record> stream) {
		KStream<String, Record> streamSendBack = stream;
		String conditionLogText = condition != null ? 
				condition.getClass().getSimpleName() : "None";
		this.logger.info("Action: " + DoAllow.class.getSimpleName());
		this.logger.info("Verb size: " + 0);
		this.logger.info("Condition: " + conditionLogText);
		if (condition != null) {
			streamSendBack = streamSendBack.filter((Predicate<String, Record>)(key, rec) -> {
				return condition.evaluate(key, rec);
			});	
		}
		return streamSendBack;
	}
}
