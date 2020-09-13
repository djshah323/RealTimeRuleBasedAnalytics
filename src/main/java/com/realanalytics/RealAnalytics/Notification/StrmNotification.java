package com.realanalytics.RealAnalytics.Notification;

import java.util.HashMap;
import java.util.Map;

public class StrmNotification 
		implements Notification {

	/*
	 * Number of occurrences
	 */
	private Long count = (long) 0;
	
	/*
	 * User
	 */
	private String actor;
	
	/*
	 * Window Start time
	 */
	private Long starttime;
	
	/*
	 * Window End time
	 */
	private Long endtime;
	
	/*
	 * AnalyticEvent field used for grouping
	 */
	private Map<String, String> groupedBy;
	
	/*
	 * AnalyticEvent operation
	 */
	private String operation;
	

	public StrmNotification(Long count,
					String actor,
					Long starttime,
					Long endtime,
					String operation,
					String groupByKey,
					String groupByValue) {
		groupedBy = new HashMap<String, String>();
		this.count = count;
		this.actor = actor;
		this.operation = operation;
		this.starttime = starttime;
		this.endtime = endtime;
		this.groupedBy.put(groupByKey, groupByValue);
	}
	
	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public Long getStarttime() {
		return starttime;
	}

	public void setStarttime(Long starttime) {
		this.starttime = starttime;
	}

	public Long getEndtime() {
		return endtime;
	}

	public void setEndtime(Long endtime) {
		this.endtime = endtime;
	}

	public String getGroupedBy(String key) {
		return groupedBy.get(key);
	}

	public void setGroupedBy(String groupedByKey, String groupedByValue) {
		this.groupedBy.put(groupedByKey, groupedByValue);
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
}
