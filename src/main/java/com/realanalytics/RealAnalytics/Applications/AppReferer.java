/**
 * 
 */
package com.realanalytics.RealAnalytics.Applications;

import com.realanalytics.RealAnalytics.Applications.Events.ApplicationEvent;
import com.realanalytics.RealAnalytics.Applications.Events.AzureADEvent;
import com.realanalytics.RealAnalytics.Applications.Events.GoogleEvent;
/**
 * @author SDhaval
 *
 */
public enum AppReferer {	
		/*
		 * Azure AD App reference
		 */
		AzureAD(AzureAD.class, AzureADEvent.class),
		/*
		 * Google App reference
		 */
		Google(Google.class, GoogleEvent.class);	
		
		private Class<? extends Application> appClass;
		private Class<? extends ApplicationEvent> appEventClass;
		
		AppReferer(Class app, Class appEvent) {
			this.appClass = app;
			this.appEventClass = appEvent;
		}
		
		public Class getApplicationClass() {
			return appClass;
		}
		
		public Class getApplicationEventClass() {
			return appEventClass;
		}
}
