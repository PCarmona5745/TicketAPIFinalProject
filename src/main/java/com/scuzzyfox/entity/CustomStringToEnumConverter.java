package com.scuzzyfox.entity;

import org.springframework.core.convert.converter.Converter;

public class CustomStringToEnumConverter implements Converter<String, Status> {
	@Override
	public Status convert(String source) {

		try {
			return Status.valueOf(source.toUpperCase());
		} catch (IllegalArgumentException e) {
			return Status.PENDING;
		}
	}
}
