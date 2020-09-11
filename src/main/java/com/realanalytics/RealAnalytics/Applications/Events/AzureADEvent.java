package com.realanalytics.RealAnalytics.Applications.Events;

import java.util.Map;

public class AzureADEvent implements ApplicationEvent {

	@EventRequirement
	public String Id;
	
	
	@EventRequirement
	public String Version;
	
	
	@EventRequirement
	public String CreationTime;
	
	
	@EventRequirement
	public String Operation;
	
	
	@EventRequirement
	public String UserId;
	
	
	public String ResultStatus;
	
	
	public String ClientIP;
	
	
	public Map<String, String> ExtendedProperties;
	
	
	public Map<String, String> Actor;
	
	
	public String ActorContextId;
	
	
	public String TargetContextId;
	
	
	public String ApplicationId;
	
	
	public Map<String, String> Target;
}
