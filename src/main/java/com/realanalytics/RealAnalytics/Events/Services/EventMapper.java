package com.realanalytics.RealAnalytics.Events.Services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.realanalytics.RealAnalytics.Applications.AppReferer;
import com.realanalytics.RealAnalytics.Data.AnalyticEvent;

@Service
public class EventMapper {


	public AnalyticEvent translateToAnalyticEvent(AppReferer application, Map<String, Object> rawEvent) {
		AnalyticEvent newEvent = null;
		if(application == AppReferer.AzureAD) {
			newEvent = new AnalyticEvent((String)rawEvent.get("Id"));
			newEvent.setApplicationName(application.name());
			//newEvent.setDeviceType((((Map<String, Object>)(List)rawEvent.get("ExtendedProperties")).get(0)));
			newEvent.setEventAction((String)rawEvent.get("Operation"));
			newEvent.setEventActor((String)rawEvent.get("UserId"));
			newEvent.setEventDate((String)rawEvent.get("CreationTime"));
			//newEvent.setEventObject((String)((Map<String,Object>)rawEvent.get("Target")).get("ID"));
			newEvent.setSrcIp((String)rawEvent.get("ClientIP"));
		}
		return newEvent;
	}


}
