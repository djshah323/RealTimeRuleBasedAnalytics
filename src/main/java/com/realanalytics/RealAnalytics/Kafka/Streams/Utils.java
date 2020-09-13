package com.realanalytics.RealAnalytics.Kafka.Streams;

import java.util.Map;

public class Utils {
	
	public final static int EVT_ACT_POS = 0;
	public final static int OP_POS = 1;
	public final static int MATCH_POS = 2;
	
	public static String buildUserByKey(Map<String, String> value) {
		StringBuilder newKey = new StringBuilder();
		newKey.append(value.get("eventActorId"))
		.append("_").append(value.get("operation"))
		.append("_").append("User" + ":" + value.get("eventActorId"));
		return newKey.toString();
	}
	
	public static String buildDeviceByKey(Map<String, String> value) {
		StringBuilder newKey = new StringBuilder();
		newKey.append(value.get("eventActorId"))
		.append("_").append(value.get("operation"))
		.append("_").append(value.get("deviceType") + ":" + value.get("deviceName"));
		return newKey.toString();
	}
	
	public static String buildCountryByKey(Map<String, String> value) {
		StringBuilder newKey = new StringBuilder();
		newKey.append(value.get("eventActorId"))
		.append("_").append(value.get("operation"))
		.append("_").append("Country" + ":" + value.get("country"));
		return newKey.toString();
	}
	
	public static String buildApplicationByKey(Map<String, String> value) {
		StringBuilder newKey = new StringBuilder();
		newKey.append(value.get("eventActorId"))
		.append("_").append(value.get("operation"))
		.append("_").append("Application" + ":" + value.get("application"));
		return newKey.toString();
	}
	
	public static String[] parseKey(String key) {
		return key.split("_");
	}
	
	public static String fetchFromParsedKey(int pos, String[] parsedKey) {
		switch (pos) {
			case EVT_ACT_POS:
					return parsedKey[EVT_ACT_POS];
			case OP_POS:
					return parsedKey[OP_POS];
			case MATCH_POS:
					return parsedKey[MATCH_POS];
			default:
					return "";
		}
			
		
	}
}
