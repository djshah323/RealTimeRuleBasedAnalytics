package com.realanalytics.RealAnalytics.Exceptions;

public final class IllegalAppNameException extends Exception {
	
	public IllegalAppNameException() {
		
	}
	
	@Override
	public String getMessage() {
		return "Bad Application Name";
	}
}
