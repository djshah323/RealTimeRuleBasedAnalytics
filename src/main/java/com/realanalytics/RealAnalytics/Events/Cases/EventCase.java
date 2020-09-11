package com.realanalytics.RealAnalytics.Events.Cases;

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

	public AnalyticEvent getEvent() {
		return event;
	}

	public void setEvent(AnalyticEvent event) {
		this.event = event;
	}
	
	public enum EventCases {
		LOGIN,
		LOCATION;
	}
}
