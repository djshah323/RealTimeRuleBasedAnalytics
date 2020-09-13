package com.realanalytics.RealAnalytics.Policy;

public interface Policy {
	
	public long getPolicyId();
	
	public String getPolicyName();
	
	public String getMatchType();
	
	public String getMatchValue();
	
	public String getMatchCriteria();

	public String getOperation();
}
