package com.realanalytics.RealAnalytics.Events.Cases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realanalytics.RealAnalytics.Data.AnalyticEvent;

public class EventCase {
	
	private String caseName;
	private AnalyticEvent event;
	
	public EventCase(String caseName, AnalyticEvent event) {
		this.setCaseName(caseName);
		this.setEvent(event);
	}

	public String getCaseName() {
		return caseName;
	}

	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}

	public String getEvent() {
		ObjectMapper mapper = new ObjectMapper();
		String evenStr = "";
		try {
			evenStr = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(event);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return evenStr;
	}

	public void setEvent(AnalyticEvent event) {
		this.event = event;
	}
	
	public enum EventCases {
		LOGIN,
		LOCATION;
	}
}
