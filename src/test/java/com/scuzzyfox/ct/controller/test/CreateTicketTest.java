package com.scuzzyfox.ct.controller.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import com.scuzzyfox.ct.controller.test.support.CreateTicketTestSupport;
import com.scuzzyfox.entity.Ticket;
import com.scuzzyfox.entity.User;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // creates a new profile for testing so it looks for application-test.yml
@Sql(scripts = { "classpath:sqlScripts/refreshTables.sql",
		"classpath:sqlScripts/populateUsers.sql" }, config = @SqlConfig(encoding = "utf-8"))
class CreateTicketTest extends CreateTicketTestSupport {

	

	@Test
	void testCreateTicketWithNewUserReturnsSuccess201AndUserIsOnDatabase() {

        // given: a ticket as JSON		
		String body = createTicketBody();
		String uri = getBaseUriForCt();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> bodyEntity = new HttpEntity<>(body, headers);

		// when: the ticket creation request is sent
		ResponseEntity<Ticket> responseTicket = getRestTemplate().exchange(uri, HttpMethod.POST, bodyEntity,
				Ticket.class);

		// then: a 201 status is returned
		assertThat(responseTicket.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		// and: the returned ticket is accurate
		assertThat(responseTicket.getBody()).isNotNull();

		Ticket ticket = responseTicket.getBody();

		assertThat(ticket.getTicketTitle()).isEqualTo("test title");
		assertThat(ticket.getTicketBody()).isEqualTo("this is a test ticket body");
		assertThat(ticket.getUser().getUsername()).isEqualTo("customUser");
		
		System.out.println(ticket);

		// and: the new user was created on the database
		uri = String.format("%s?username=%s", getBaseUriForUser(), ticket.getUser().getUsername());

		ResponseEntity<User> responseUser = getRestTemplate().exchange(uri, HttpMethod.GET, null,
				new ParameterizedTypeReference<>() {
				});

		assertThat(responseUser.getBody()).isEqualTo(ticket.getUser());

	}

	

	

}
