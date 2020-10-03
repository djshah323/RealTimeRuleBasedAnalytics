package com.realanalytics.RealAnalytics.Pipeline.Rule;

import java.util.Map;

import org.apache.kafka.streams.kstream.KStream;

import com.realanalytics.RealAnalytics.Pipeline.Record;

import static com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil.DO_ALLOW;
import static com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil.DO_MAP;
import static com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil.DO_DENY;
import static com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil.DO_WINDOW;


public abstract class Action {
	
	public static Map actionClasses = RuleUtil.createMap(new Object[] {
			DO_ALLOW,			DoAllow.class,
			DO_DENY,			DoDeny.class,
			DO_MAP,				DoMap.class,
			DO_WINDOW,			DoWindow.class});
	
	public abstract void apply(Condition condition, KStream<String, Record> stream);

}
