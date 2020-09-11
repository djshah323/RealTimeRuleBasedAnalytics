package com.realanalytics.RealAnalytics.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "AnalyticEvent")
public class AnalyticEvent {
	@Id
	private String eventId;	
	private String applicationName;
	private String eventDate;
	private String eventObject;
    private String eventObjectType;
    private String eventAction;
    private String eventActor;
    private String eventAudience;
    private String deviceType;
    private String browserType;
    private String evntCountry;
    private String thirdPartyApp;
    private String srcIp;
    
    public AnalyticEvent(String id) {
    	this.eventId = id;
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
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getBrowserType() {
		return browserType;
	}

	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}

	public String getEvntCountry() {
		return evntCountry;
	}

	public void setEvntCountry(String evntCountry) {
		this.evntCountry = evntCountry;
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

}
