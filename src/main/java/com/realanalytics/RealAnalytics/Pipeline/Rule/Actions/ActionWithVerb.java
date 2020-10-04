package com.realanalytics.RealAnalytics.Pipeline.Rule.Actions;

import java.util.Stack;

import com.realanalytics.RealAnalytics.Pipeline.Rule.Verbs.HasVerb;
import com.realanalytics.RealAnalytics.Pipeline.Rule.Verbs.Verb;

public abstract class ActionWithVerb 
	extends Action implements HasVerb {

	private Stack<Verb> verb;
	
	public ActionWithVerb(Stack<Verb> verb) {
		this.verb = verb;
	}

	@Override
	public Stack<Verb> initVerb() {
		Stack<Verb> newStack = new Stack<Verb>();
		newStack.addAll(verb);
		return newStack;
	} 
	
}
