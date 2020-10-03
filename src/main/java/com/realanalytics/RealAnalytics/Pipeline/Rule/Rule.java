package com.realanalytics.RealAnalytics.Pipeline.Rule;

import org.apache.kafka.streams.kstream.KStream;

import com.realanalytics.RealAnalytics.Pipeline.Record;

public class Rule {
	
	String name;
	
	Condition condition = null;
	
	Action action = null;
	
	public Rule(String name) {
		this.name = name;
	}
	
	public void apply(KStream<String, Record> stream) {
		action.apply(condition, stream);
	}

	public void condition(Condition instantiate) {
		this.condition = instantiate;	
	}
	
	public void action(Action instantiate) {
		this.action = instantiate;
	}
}