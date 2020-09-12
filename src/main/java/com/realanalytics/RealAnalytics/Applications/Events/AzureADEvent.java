package com.realanalytics.RealAnalytics.Applications.Events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.realanalytics.RealAnalytics.Applications.AppReferer;
import com.realanalytics.RealAnalytics.Data.AnalyticEvent;

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
	public String ObjectId;
	
	
	@EventRequirement
	public String UserId;
	
	
	@EventRequirement
	public String OrganizationId;
	
	
	@EventRequirement
	public String RecordType;
	
	
	public String UserKey;
	
	
	public String UserType;
	
	
	public String ResultStatus;
	
	
	public String ClientIP;
	
	
	public List<Map<String, String>> ExtendedProperties;
	
	
	public List<Map<String, String>> Actor;
	
	
	public String ActorContextId;
	
	
	public String TargetContextId;
	
	
	public String ApplicationId;
	
	
	public List<Map<String, String>> Target;


	public Map<String, String> unTrackedProperties;
	

	public static Map<String, String> appIdNameMapper = 
			new HashMap<String, String>();
	
	@Override
	public AnalyticEvent analyticEvent() {
		AnalyticEvent newEvent = new AnalyticEvent(this.Id);
		newEvent.setApplicationName(AppReferer.AzureAD.name());
		
		newEvent.setEventAction(Operation);
		newEvent.setStatus(ResultStatus);
		
		newEvent.setEventActor(UserId);
		newEvent.setEventActorId(Actor.get(0).get("ID"));
		
		newEvent.setDeviceType(AnalyticEvent.DeviceType.Browser.name());
		newEvent.setDeviceName(ExtendedProperties.get(0).get("Value"));
		newEvent.setDeviceAuth(ExtendedProperties.get(1).get("Value"));
		
		
		newEvent.setEventDate(CreationTime);
		newEvent.setEventTargetId(TargetContextId);
		
		newEvent.setEventObject(TargetContextId);
		
		newEvent.setSrcIp(this.ClientIP);
		
		newEvent.setThirdPartyApp(appIdNameMapper.get(ApplicationId));
		
		return newEvent;
	}
	
	public enum MicrosoftResources {
		AAD_Graph_API,
		Office_365_Exchange_Online,
		MS_Graph,
		Skype,
		Yammer,
		OneNote,
		WASMAPI,
		ManagementAPI,
		Teams,
		AzureKeyVault;
		
	}
	
	static {
		appIdNameMapper.put("00000002-0000-0000-c000-000000000000", MicrosoftResources.AAD_Graph_API.name());
		appIdNameMapper.put("00000002-0000-0ff1-ce00-000000000000", MicrosoftResources.Office_365_Exchange_Online.name());
		appIdNameMapper.put("00000003-0000-0000-c000-000000000000", MicrosoftResources.MS_Graph.name());
		appIdNameMapper.put("00000004-0000-0ff1-ce00-000000000000", MicrosoftResources.Skype.name());
		appIdNameMapper.put("00000005-0000-0ff1-ce00-000000000000", MicrosoftResources.Yammer.name());
		appIdNameMapper.put("2d4d3d8e-2be3-4bef-9f87-7875a61c29de", MicrosoftResources.OneNote.name());
		appIdNameMapper.put("797f4846-ba00-4fd7-ba43-dac1f8f63013", MicrosoftResources.WASMAPI.name());
		appIdNameMapper.put("c5393580-f805-4401-95e8-94b7a6ef2fc2", MicrosoftResources.ManagementAPI.name());
		appIdNameMapper.put("cc15fd57-2c6c-4117-a88c-83b1d56b4bbe", MicrosoftResources.Teams.name());
		appIdNameMapper.put("cfa8b339-82a2-471a-a3c9-0fc0be7a4093", MicrosoftResources.AzureKeyVault.name());
	}
}
