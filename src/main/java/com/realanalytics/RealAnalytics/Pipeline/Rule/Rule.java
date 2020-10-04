package com.realanalytics.RealAnalytics.Pipeline.Rule;

import org.apache.kafka.streams.kstream.KStream;

import com.realanalytics.RealAnalytics.Pipeline.Record;
import com.realanalytics.RealAnalytics.Pipeline.Rule.Actions.Action;
import com.realanalytics.RealAnalytics.Pipeline.Rule.Conditions.Condition;

public class Rule {
	
	private String name;
	
	private Condition condition = null;
	
	private Action action = null;
	
	public Rule(String name) {
		this.name = name;
	}
	
	public KStream<String, Record> apply(KStream<String, Record> stream) {
		return action.apply(condition, stream);
	}

	public void condition(Condition instantiate) {
		this.condition = instantiate;	
	}
	
	public Condition getCondition() {
		return this.condition;
	}
	
	public void action(Action instantiate) {
		this.action = instantiate;
	}
	
	public Action getAction() {
		return this.action;
	}

	public String getName() {
		return name;
	}
}