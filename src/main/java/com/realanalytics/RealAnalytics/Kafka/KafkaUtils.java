package com.realanalytics.RealAnalytics.Kafka;

import java.util.HashMap;
import java.util.Map;

import com.realanalytics.RealAnalytics.Events.Cases.EventCase;

public class KafkaUtils {
	private static final Map<String, String> EventCaseTopicMapper
			= new HashMap<String,String>();
	
	static {
		EventCaseTopicMapper.put(EventCase.EventCases.LOGIN.name(), KafkaConstants.LOGIN_CASE_TOPIC);
		EventCaseTopicMapper.put(EventCase.EventCases.LOCATION.name(), KafkaConstants.LOC_CASE_TOPIC);
	}
	
	public static String getTopicForCase(String caseName) {
		return EventCaseTopicMapper.get(caseName);
	}
}
