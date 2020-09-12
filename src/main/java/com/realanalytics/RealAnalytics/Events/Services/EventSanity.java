package com.realanalytics.RealAnalytics.Events.Services;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realanalytics.RealAnalytics.Applications.AppReferer;
import com.realanalytics.RealAnalytics.Applications.Events.ApplicationEvent;
import com.realanalytics.RealAnalytics.Applications.Events.EventRequirement;
import com.realanalytics.RealAnalytics.Identity.BadEventException;


@Service
public class EventSanity<T extends ApplicationEvent> {
	private static final Logger logger = 
            LoggerFactory.getLogger(EventSanity.class);
	
	@Autowired
	ObjectMapper mapper;
	
	@SuppressWarnings("unchecked")
	public T check(AppReferer application, String rawEvent) 
			throws BadEventException {
		try {
			Map<String, Object> eventJson = (HashMap<String, Object>) 
					mapper.readValue(rawEvent, HashMap.class);	
			Class<?> appClass = application.getApplicationClass();
			Field[] fields = appClass.getDeclaredFields();
			List<Field> allFields = new ArrayList<Field>(Arrays.asList(fields));
			List<Field> mandatoryFields = allFields.stream().filter(
					field -> field.isAnnotationPresent(EventRequirement.class))
					.collect(Collectors.toList());
			for(Field field : mandatoryFields) {
				if (!eventJson.containsKey(field.getName()))
					throw new BadEventException();
			}		
			return (T) mapper.readValue(rawEvent, application.getApplicationEventClass());
		} catch (IOException e) {
			logger.warn("IOException parsing rawevent to application event");
		} catch (BadEventException be) {
			logger.warn("BadEventException parsing rawevent to application event");
		}
		return null;
	}
}
