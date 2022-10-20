package com.scuzzyfox.ct.controller.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.scuzzyfox.ct.controller.test.support.UserTestSupport;
import com.scuzzyfox.entity.User;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // creates a new profile for testing so it looks for application-test.yml
@Sql(scripts = { "classpath:sqlScripts/refreshTables.sql",
		"classpath:sqlScripts/populateUsers.sql" }, config = @SqlConfig(encoding = "utf-8"))
class UserTest extends UserTestSupport {

	@Autowired
	JdbcTemplate jdbcTemplate;

	
	@Test
	void testUserIsFetched() {
		String uri = String.format("%s?username=%s", getBaseUriForUser(), "test_man");

		ResponseEntity<User> response = getRestTemplate().exchange(uri, HttpMethod.GET, null,
				new ParameterizedTypeReference<>() {
				});

		assertThat(response.getBody().getUsername()).isEqualTo("test_man");
	}

	@Test
	void test2EqualUsernamesCannotBeMade() {
		String uri = String.format("%s?username=%s", getBaseUriForUser(), "test_man");

		int numRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "users");

		ResponseEntity<User> response = getRestTemplate().exchange(uri, HttpMethod.POST, null,
				new ParameterizedTypeReference<>() {
				});

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "users")).isEqualTo(numRows);

	}

	@Test
	void testUsernameThatIsTooLongCantBeMade() {
		String uri = String.format("%s?username=%s", getBaseUriForUser(), "invalidusername1");

		int numRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "users");

		ResponseEntity<User> response = getRestTemplate().exchange(uri, HttpMethod.POST, null,
				new ParameterizedTypeReference<>() {
				});

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "users")).isEqualTo(numRows);

		uri = String.format("%s?username=%s", getBaseUriForUser(), "validusername12");
		response = getRestTemplate().exchange(uri, HttpMethod.POST, null, new ParameterizedTypeReference<>() {
		});

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "users")).isEqualTo(numRows + 1);

		uri = String.format("%s?username=%s", getBaseUriForUser(), "VaLiduserName12");
		response = getRestTemplate().exchange(uri, HttpMethod.POST, null, new ParameterizedTypeReference<>() {
		});

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "users")).isEqualTo(numRows + 2);

	}

	
	@Test
	void testUsernameIsDeletedFromDatabase() {
		String uri = String.format("%s?username=%s", getBaseUriForUser(), "toBeDeleted");

		ResponseEntity<User> response = getRestTemplate().exchange(uri, HttpMethod.POST, null,
				new ParameterizedTypeReference<>() {
				});

		int numRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "users");
		User user = response.getBody();

		response = getRestTemplate().exchange(uri, HttpMethod.DELETE, null, new ParameterizedTypeReference<>() {
		});

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(user);
		assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "users")).isEqualTo(numRows - 1);

	}

}
