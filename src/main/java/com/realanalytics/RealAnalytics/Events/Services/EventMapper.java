package com.realanalytics.RealAnalytics.Events.Services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.realanalytics.RealAnalytics.Applications.AppReferer;
import com.realanalytics.RealAnalytics.Applications.Events.ApplicationEvent;
import com.realanalytics.RealAnalytics.Data.AnalyticEvent;

@Service
public class EventMapper {


	public AnalyticEvent createAnalyticEvent(ApplicationEvent appevent) {
		return appevent.analyticEvent();
	}


}
