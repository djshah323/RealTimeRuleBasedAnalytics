package com.realanalytics.RealAnalytics.Alerts;

public interface Alert {
	
	public enum Severity {
		LOW(0),
		NORMAL(1),
		HIGH(2);
		
		private int level;
		
		Severity(int level) {
			this.level = level;
		}
	}
	
	public enum AlertType {
		Proximity,
		FailedLogin,
		SharedLogin,
		RiskyLogin,
		DDOS;
	}
	
	
}
