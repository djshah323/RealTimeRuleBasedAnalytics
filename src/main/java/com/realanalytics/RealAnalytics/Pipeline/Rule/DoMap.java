package com.realanalytics.RealAnalytics.Pipeline.Rule;

import java.util.LinkedList;
import java.util.Stack;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;

import com.realanalytics.RealAnalytics.Pipeline.Record;

public class DoMap extends Action implements HasVerb {
	
	private Stack<Verb> verb;

	public DoMap(String verb) {
		this.verb = Verb.resolveVerbs(verb);
	}
	
	@Override
	public KStream<String, Record> apply(Condition condition, KStream<String, Record> stream) {
		KStream<String, Record> streamSendBack = stream;
		String conditionLogText = condition != null ? 
					condition.getClass().getSimpleName() : "None";				
		this.logger.info("Action: " + DoMap.class.getSimpleName());
		this.logger.info("Verb size: " + verb.size());
		this.logger.info("Condition: " + conditionLogText);
		if (condition != null) {
			streamSendBack = streamSendBack.filter((key, rec) -> {
				return condition.evaluate(key, rec);
			});	
		}
		streamSendBack = streamSendBack.map((key, rec) -> {
			if (verb.size() == 0) {
				return new KeyValue<String, Record>(key, rec);
			} else if (verb.size() == 1) {
				return new KeyValue<String, Record>(verb.pop().call(null, key, rec), rec);
			} else {
				LinkedList<String> evals = new LinkedList<String>();
				while(verb.isEmpty()) {
					this.logger.info("Inside verb stack");
					evals.addLast(verb.pop()
							.call(evals, key, rec));
				} return new KeyValue<String, Record>(evals.getFirst(), rec);
			}			
		});
		return streamSendBack;
	}
}
