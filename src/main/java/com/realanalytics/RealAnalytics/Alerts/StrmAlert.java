package com.realanalytics.RealAnalytics.Alerts;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "alerts")
public final class StrmAlert implements Alert {

	@Id
	private String id;
	
	private String user;
	
	private Severity severity ;
	
	private AlertType type;
	
	private long policyId;
	
	private String policyName;
	
	private String appname;
	
	private long timeMillis;
	
	public StrmAlert(String user,
			Severity sev, 
			AlertType type, 
			long policyId, 
			String policyName,
			String appname) {
		id = UUID.randomUUID().toString();
		this.user = user;
		severity = sev;
		this.type = type;
		this.policyId = policyId;
		this.policyName = policyName;
		this.timeMillis = System.currentTimeMillis();
		this.appname = appname;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setPolicyId(long policyId) {
		this.policyId = policyId;
	}
	
	@Override
	public long getPolicyId() {
		return policyId;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}
	
	@Override
	public String getPolicyName() {
		return policyName;
	}

	public void setSeverity(Severity severity) {
		this.severity = severity;
	}
	
	@Override
	public Severity severity() {
		return severity;
	}

	public void setType(AlertType type) {
		this.type = type;
	}
	
	@Override
	public AlertType type() {
		return type;
	}

	public void setTimeMillis(long timeMillis) {
		this.timeMillis = timeMillis;
	}
	
	@Override
	public long timeInMillis() {
		return timeMillis;
	}

	@Override
	public String user() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public String application() {
		return this.appname;
	}
 	
}
