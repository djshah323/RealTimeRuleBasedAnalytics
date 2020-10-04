package com.realanalytics.RealAnalytics.Pipeline.Rule.Actions;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Predicate;

import com.realanalytics.RealAnalytics.Pipeline.Record;
import com.realanalytics.RealAnalytics.Pipeline.Rule.Conditions.Condition;

public class DoDeny extends Action {

	public DoDeny(String verb) {
	}
	
	@Override
	public KStream<String, Record> apply(Condition condition, KStream<String, Record> stream) {
		KStream<String, Record> streamSendBack = stream;
		try {
			String conditionLogText = condition != null ? 
					condition.getClass().getSimpleName() : "None";
			this.logger.info("Action: " + DoDeny.class.getSimpleName());
			this.logger.info("Verb size: " + 0);
			this.logger.info("Condition: " + conditionLogText);
			if (condition != null) {
				streamSendBack = streamSendBack.filter((key, rec) -> {
					return !condition.evaluate(key, rec);
				});	
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return streamSendBack;
	}
}
