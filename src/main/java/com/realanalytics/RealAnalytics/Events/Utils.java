package com.realanalytics.RealAnalytics.Events;

import java.text.MessageFormat;

import com.realanalytics.RealAnalytics.Applications.AppReferer;
import com.realanalytics.RealAnalytics.Exceptions.IllegalAppNameException;

public final class Utils {
	
	public static AppReferer getAppDetails(String appname)
		throws IllegalAppNameException {
		
		switch(appname.toLowerCase()) {
			case "azuread" : 
				return AppReferer.AzureAD;
			case "google" :
				return AppReferer.Google;
			default :
				throw new IllegalAppNameException();
		}
	} 

	public static String response(String message) {
		return MessageFormat.format("\"Message\":\"{0}\"", message);
	}
}
