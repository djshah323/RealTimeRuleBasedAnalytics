package com.realanalytics.RealAnalytics.Pipeline.Rule.Actions;

import java.util.LinkedList;
import java.util.Stack;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;

import com.realanalytics.RealAnalytics.Pipeline.Record;
import com.realanalytics.RealAnalytics.Pipeline.Rule.Conditions.Condition;
import com.realanalytics.RealAnalytics.Pipeline.Rule.Verbs.Verb;

public class DoMap extends ActionWithVerb {

	public DoMap(String verb) {
		super(Verb.resolveVerbs(verb));
	}
	
	@Override
	public KStream<String, Record> apply(Condition condition, KStream<String, Record> stream) {
		KStream<String, Record> streamSendBack = stream;
		try {
			if (condition != null) {
				streamSendBack = streamSendBack.filter((key, rec) -> {
					return condition.evaluate(key, rec);
				});	
			}
			streamSendBack = streamSendBack.map((key, rec) -> {
				Stack<Verb> verb = initVerb();
				if (verb.size() == 0) {
					return new KeyValue<String, Record>(key, rec);
				} else if (verb.size() == 1) {
					return new KeyValue<String, Record>(verb.pop().call(null, key, rec), rec);
				} else {
					LinkedList<String> evals = new LinkedList<String>();
					while(verb.isEmpty()) {
						evals.addLast(verb.pop()
								.call(evals, key, rec));
					} return new KeyValue<String, Record>(evals.getFirst(), rec);
				}			
			});
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return streamSendBack;
	}
}
