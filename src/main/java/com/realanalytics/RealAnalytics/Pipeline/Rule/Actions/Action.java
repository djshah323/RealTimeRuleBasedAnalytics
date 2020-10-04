package com.realanalytics.RealAnalytics.Pipeline.Rule.Actions;

import java.util.Map;

import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.realanalytics.RealAnalytics.Pipeline.Record;
import com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil;
import com.realanalytics.RealAnalytics.Pipeline.Rule.Conditions.Condition;

import static com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil.DO_ALLOW;
import static com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil.DO_MAP;
import static com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil.DO_DENY;
import static com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil.DO_WINDOW;


public abstract class Action {
	
	protected  final Logger logger = 
            LoggerFactory.getLogger(Action.class);
	
	public static Map actionClasses = RuleUtil.createMap(new Object[] {
			DO_ALLOW,			DoAllow.class,
			DO_DENY,			DoDeny.class,
			DO_MAP,				DoMap.class,
			DO_WINDOW,			DoWindow.class});
	
	public abstract  KStream<String, Record>
			apply(Condition condition, KStream<String, Record> stream);

}
