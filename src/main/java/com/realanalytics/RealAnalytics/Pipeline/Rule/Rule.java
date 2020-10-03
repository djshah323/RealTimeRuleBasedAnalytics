package com.realanalytics.RealAnalytics.Pipeline.Rule;

import org.apache.kafka.streams.StreamsBuilder;

public class Rule {
	
	String name;
	
	Condition condition = null;
	
	Action action = null;
	
	public Rule(String name) {
		this.name = name;
	}
	
	public void apply(StreamsBuilder builder) {
		action.apply(condition, builder);
	}

	public void condition(Condition instantiate) {
		this.condition = instantiate;	
	}
	
	public void action(Action instantiate) {
		this.action = instantiate;
	}
}