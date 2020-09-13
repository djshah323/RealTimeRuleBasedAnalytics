package com.realanalytics.RealAnalytics.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "User")
public class User {
	@Id
	private String id ;
	
	private String firstName;
	
	private String lastName;
	
	@Indexed(unique=true)
	private String email;
	
	private String appname;
	
	private String location;
	
	public User(String firstName, String lastName, String email, String location) {
		id = java.util.UUID
				.nameUUIDFromBytes(email.getBytes()).toString();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.setLocation(location);
	}
	public String getId() {
		return id;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getEmail() {
		return email;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

}
