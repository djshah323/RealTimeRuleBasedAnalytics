package com.realanalytics.RealAnalytics.Pipeline.Rule;

import static com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil.VERB_ANY;
import static com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil.VERB_CONCAT;
import static com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil.VERB_KEY;
import static com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil.VERB_PATTERN;
import static com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil.VERB_STRING;
import static com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil.VERB_VALUE;
import static com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil.VERB_WINDOW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.realanalytics.RealAnalytics.Pipeline.Record;

public abstract class Verb {
	
	String arg;
	
	public static Map verbClasses = RuleUtil.createMap(new Object[] {
			VERB_ANY,				VerbAny.class,
			VERB_CONCAT,			VerbConcat.class,
			VERB_KEY,				VerbKey.class,
			VERB_PATTERN,			VerbPattern.class,
			VERB_STRING, 			VerbString.class,
			VERB_VALUE,				VerbValue.class,
			VERB_WINDOW, 			VerbWindow.class});
	
	public abstract String call(LinkedList<String> evaluation, String key, Record rec);
	
	public Verb(String arg) {
		this.arg = arg;
	}
	
	public static Stack<Verb> resolveVerbs(String verbString) {
		Stack<Verb> resolvedVerbs = new Stack<>();
		try {
			String[] verbs = verbString.split("#");
			for (String verb: verbs) {
				Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(verb);
				String arg = "";
			     while(m.find()) {
			       arg = m.group(1);  
			       break;
			     }
			    String actualName = verb.replaceAll("(", "")
			    		.replaceAll(")", "")
			    		.replaceAll(arg, "");
			    resolvedVerbs.push((Verb)
			    		RuleUtil.instantiate(Verb.verbClasses, 
			    				actualName, 
			    				new Class[] {String.class}, 
			    				new Object[] {arg}));
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return resolvedVerbs;		
	}
}
