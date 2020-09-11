package com.realanalytics.RealAnalytics.Events.Services;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.realanalytics.RealAnalytics.Applications.AppReferer;
import com.realanalytics.RealAnalytics.Applications.Events.EventRequirement;
import com.realanalytics.RealAnalytics.Identity.BadEventException;


@Service
public class EventSanity {
	
	
	public void check(AppReferer application, Map<String, Object> rawEvent) 
			throws BadEventException {
		Class appClass = application.getApplicationClass();
		Field[] fields = appClass.getDeclaredFields();
		List<Field> allFields = new ArrayList<Field>(Arrays.asList(fields));
		List<Field> mandatoryFields = allFields.stream().filter(
				field -> field.isAnnotationPresent(EventRequirement.class)).collect(Collectors.toList());
		for(Field field : mandatoryFields) {
			if (!rawEvent.containsKey(field.getName()))
				throw new BadEventException();
		}
	}
}
