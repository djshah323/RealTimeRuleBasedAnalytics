package com.realanalytics.RealAnalytics.Alerts;

public abstract class AlertBasic 
			implements Alert {
	/*
	 * Alert Severity
	 */
	protected Severity sev;
	
	/*
	 * Alert type
	 */
	protected AlertType type;
	
	/*
	 * Alert perpetrator  
	 */
	protected String actor;
	
	
}
