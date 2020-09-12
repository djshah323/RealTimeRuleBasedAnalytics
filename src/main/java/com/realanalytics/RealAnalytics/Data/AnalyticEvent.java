package com.realanalytics.RealAnalytics.Data;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "AnalyticEvent")
public class AnalyticEvent {
	
	public enum DeviceType {
		Browser,
		Service
	}
	
	public enum AuthType {
		Password,
		OAuth2,
		PersonalToken
	}
	
	@Id
	private final String eventId;	
	private String applicationName;
	private String eventDate;
	private String eventObject;
	private String eventObjectId;
    private String eventObjectType;
    private String eventAction;
    private String eventActor;
    private String eventActorId;
    private String eventTarget;
    private String eventTargetId;
    private String eventAudience;
    private Map<String, String> device;
    private String eventCountry;
    private String thirdPartyApp;
    private String srcIp;
    private Map<String, String> otherProperties;
    
    public AnalyticEvent(String id) {
    	this.eventId = id;
    	device = new HashMap<String, String>();
    	otherProperties = new HashMap<String, String>();
    }

    public String getEventId() {
    	return eventId;
    }
    
	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public String getEventObject() {
		return eventObject;
	}

	public void setEventObject(String eventObject) {
		this.eventObject = eventObject;
	}

	public String getEventObjectType() {
		return eventObjectType;
	}

	public void setEventObjectType(String eventObjectType) {
		this.eventObjectType = eventObjectType;
	}

	public String getEventAction() {
		return eventAction;
	}

	public void setEventAction(String eventAction) {
		this.eventAction = eventAction;
	}

	public String getEventActor() {
		return eventActor;
	}

	public void setEventActor(String eventActor) {
		this.eventActor = eventActor;
	}

	public String getEventAudience() {
		return eventAudience;
	}

	public void setEventAudience(String eventAudience) {
		this.eventAudience = eventAudience;
	}

	public String getDeviceType() {
		return this.device.get("deviceType");
	}

	public String getDeviceName() {
		return this.device.get("deviceName");
	}
	
	public String getDeviceAuth() {
		return this.device.get("deviceAuth");
	}
	
	public void setDeviceType(String deviceType) {
		this.device.put("deviceType", deviceType);
	}

	public void setDeviceName(String name) {
		this.device.put("deviceName", name);
	}

	public void setDeviceAuth(String name) {
		if (name.contains(AuthType.OAuth2.name()))
			this.device.put("deviceAuth", AuthType.OAuth2.name());
		else if (name.contains(AuthType.Password.name()))
			this.device.put("deviceAuth", AuthType.Password.name());
		else if (name.contains(AuthType.PersonalToken.name()))
			this.device.put("deviceAuth", AuthType.PersonalToken.name());
	}
	
	public String getEventCountry() {
		return eventCountry;
	}

	public void setEventCountry(String eventCountry) {
		this.eventCountry = eventCountry;
	}

	public String getThirdPartyApp() {
		return thirdPartyApp;
	}

	public void setThirdPartyApp(String thirdPartyApp) {
		this.thirdPartyApp = thirdPartyApp;
	}

	public String getSrcIp() {
		return srcIp;
	}

	public void setSrcIp(String srcIp) {
		this.srcIp = srcIp;
	}

	public String getEventObjectId() {
		return eventObjectId;
	}

	public void setEventObjectId(String eventObjectId) {
		this.eventObjectId = eventObjectId;
	}

	public String getEventActorId() {
		return eventActorId;
	}

	public void setEventActorId(String eventActorId) {
		this.eventActorId = eventActorId;
	}

	public String getEventTarget() {
		return eventTarget;
	}

	public void setEventTarget(String eventTarget) {
		this.eventTarget = eventTarget;
	}

	public String getEventTargetId() {
		return eventTargetId;
	}

	public void setEventTargetId(String eventTargetId) {
		this.eventTargetId = eventTargetId;
	}

}
