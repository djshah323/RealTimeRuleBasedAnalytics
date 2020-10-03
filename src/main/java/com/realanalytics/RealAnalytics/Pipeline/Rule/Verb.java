package com.realanalytics.RealAnalytics.Pipeline.Rule;

import static com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil.VERB_ANY;
import static com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil.VERB_CONCAT;
import static com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil.VERB_KEY;
import static com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil.VERB_PATTERN;
import static com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil.VERB_STRING;
import static com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil.VERB_VALUE;
import static com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil.VERB_WINDOW;

import java.util.Map;

public abstract class Verb {
	
	public static Map verbClasses = RuleUtil.createMap(new Object[] {
			VERB_ANY,				VerbAny.class,
			VERB_CONCAT,			VerbConcat.class,
			VERB_KEY,				VerbKey.class,
			VERB_PATTERN,			VerbPattern.class,
			VERB_STRING, 			VerbString.class,
			VERB_VALUE,				VerbValue.class,
			VERB_WINDOW, 			VerbWindow.class});
	
	public abstract void call();
}
