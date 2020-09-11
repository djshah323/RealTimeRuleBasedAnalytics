package com.realanalytics.RealAnalytics.Events.Cases;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.realanalytics.RealAnalytics.Data.AnalyticEvent;
import com.realanalytics.RealAnalytics.Kafka.KafkaConstants;

@Service
public class EventCaseParser {

	public List<EventCase> buildCases(AnalyticEvent analyticEvent) {
		List<EventCase> cases = new ArrayList<>();
		cases.add(new EventCase(EventCase.EventCases.LOGIN.name(), analyticEvent));
		return cases;
	}

}
