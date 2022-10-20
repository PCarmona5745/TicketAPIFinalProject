package com.scuzzyfox.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.scuzzyfox.entity.User;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DefaultUserDao implements UserDao {

	@Autowired
	NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public Optional<User> fetchUser(Long userId) {
		String sql = "SELECT * FROM users WHERE user_id = :user_id";

		Map<String, Object> params = new HashMap<>();

		params.put("user_id", userId);

		User resultingUser = jdbcTemplate.query(sql, params, new ResultSetExtractor<>() {

			@Override
			public User extractData(ResultSet rs) throws SQLException, DataAccessException {

				if (rs.next()) {
					return User.builder().userId(rs.getLong("user_id")).username(rs.getString("username")).build();
				}

				return null;
			}

		});

		return Optional.ofNullable(resultingUser);
	}

	@Override
	public Optional<User> fetchUser(String username) {
		log.debug("UserDao fetching user={}", username);
		
		String sql = "SELECT * FROM users WHERE username = :username";

		Map<String, Object> params = new HashMap<>();

		params.put("username", username);
		User resultingUser = jdbcTemplate.query(sql, params, new ResultSetExtractor<>() {

			@Override
			public User extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					log.debug("UserDao found user={}", username);
					return User.builder().userId(rs.getLong("user_id")).username(rs.getString("username")).build();
				}
				log.debug("UserDao could not find user={}", username);
				return null;
			}

		});

		Optional<User> opt = Optional.ofNullable(resultingUser);

		return opt;
	}

	@Override
	public Optional<User> fetchUser(User user) {
		String sql = "SELECT * FROM users WHERE user_id = :user_id";

		Map<String, Object> params = new HashMap<>();

		params.put("user_id", user.getUserId());

		User resultingUser = jdbcTemplate.query(sql, params, new ResultSetExtractor<>() {

			@Override
			public User extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					return User.builder().userId(rs.getLong("user_id")).username(rs.getString("username")).build();
				}

				return null;
			}

		});

		return Optional.ofNullable(resultingUser);
	}

	@Override
	public User updateUsername(String username, String newUsername) {
		String sql = "UPDATE users SET username = :newUsername WHERE user_id= :userId";

		User user = User.builder().username(newUsername)
				.userId(fetchUser(username).orElseThrow(
						() -> new NoSuchElementException("Cannot update user as " + username + " does not exist"))
						.getUserId())
				.build();

		Map<String, Object> params = new HashMap<>();

		params.put("newUsername", newUsername);
		params.put("userId", user.getUserId());
		jdbcTemplate.update(sql, params);

		return user;

	}

	@Override
	public User deleteUser(String username) {
		User user = fetchUser(username).get(); //just using .get because the service already checks if the user exists.
		
		String sql = "DELETE FROM users WHERE user_id= :user_id";
		
		Map<String,Object> params = new HashMap<>();
		params.put("user_id", user.getUserId());
		
		jdbcTemplate.update(sql, params);
		return user;
	}

	@Override
	public User saveUser(String username) {
		String sql = "INSERT INTO users (username) VALUES (:username)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource source = new MapSqlParameterSource();
		
		source.addValue("username", username);
		
		jdbcTemplate.update(sql, source, keyHolder);
		
		Long userId = keyHolder.getKey().longValue();
		
		return User.builder()
				.username(username)
				.userId(userId)
				.build();
	}

}
