package com.scuzzyfox.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
	PENDING, ACTIVE, CLOSED, RESOLVED;
	
	private static Map<String,Status> namesMap = new HashMap<String,Status>(4);
	
	static {
		namesMap.put("pending", PENDING);
		namesMap.put("active", ACTIVE);
		namesMap.put("closed", CLOSED);
		namesMap.put("resolved", RESOLVED);
	}
	
	@JsonCreator
	public static Status forValue(String value) {
		return namesMap.get(StringUtils.lowerCase(value));
	}
	
	@JsonValue
	public String toValue() {
		for (Entry<String,Status> entry : namesMap.entrySet()) {
			if(entry.getValue()==this)
				return entry.getKey();
		}
		return null;
	}
}
