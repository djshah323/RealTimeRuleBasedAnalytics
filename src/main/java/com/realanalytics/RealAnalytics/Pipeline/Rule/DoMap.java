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
	public void apply(Condition condition, KStream<String, Record> stream) {
		if (condition != null) {
			stream.filter((key, rec) -> {
				return condition.evaluate(key, rec);
			});	
		}
		stream.map((key, rec) -> {
			LinkedList<String> evals = new LinkedList<String>();
			while(verb.isEmpty()) {
				evals.addLast(verb.pop()
						.call(evals, key, rec));
			} return new KeyValue<String, Record>(evals.getFirst(), rec);
		});
	}
}
