package com.scuzzyfox.ct.controller.test.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import lombok.Getter;

public class BaseTest {
	
	@LocalServerPort
	private int serverPort;
	
	@Autowired
	@Getter
	private TestRestTemplate restTemplate;
	
	protected String getBaseUriForCt() {
		return String.format("http://localhost:%d/ct", serverPort);
	}
	
	protected String getBaseUriForUser() {
		return String.format("http://localhost:%d/user", serverPort);
	}

}
