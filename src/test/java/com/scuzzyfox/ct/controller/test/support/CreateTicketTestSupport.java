package com.scuzzyfox.ct.controller.test.support;

public class CreateTicketTestSupport extends BaseTest {
	protected String createTicketBody() {
		return "{\r\n"
				+ "   \"ticketTitle\":\"test title\",\r\n"
				+ "   \"ticketBody\":\"this is a test ticket body\",\r\n"
				+ "   \"username\":\"customUser\"\r\n"
				+ "}";
	}

}
