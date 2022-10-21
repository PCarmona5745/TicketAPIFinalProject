package com.scuzzyfox.ticket.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.scuzzyfox.entity.Status;
import com.scuzzyfox.entity.Ticket;
import com.scuzzyfox.entity.TicketRequest;
import com.scuzzyfox.entity.User;
import com.scuzzyfox.user.dao.UserDao;

@Component
public class DefaultTicketDao implements TicketDao {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	private UserDao userDao;

	@Override
	public Ticket saveTicket(TicketRequest ticketRequest, User user) {

		String sql = "INSERT INTO tickets (ticket_title, ticket_body, user_fk) "
				+ "VALUES (:ticket_title, :ticket_body, :user_fk)";

		MapSqlParameterSource source = new MapSqlParameterSource();

		KeyHolder keyHolder = new GeneratedKeyHolder();

		source.addValue("ticket_title", ticketRequest.getTicketTitle());
		source.addValue("ticket_body", ticketRequest.getTicketBody());
		source.addValue("user_fk", user.getUserId());

		jdbcTemplate.update(sql, source, keyHolder);

		Long ticketPK = extractTicketPK(keyHolder); // get the PK of the ticket that was just made

		return fetchTicket(ticketPK).orElseThrow(() -> new NoSuchElementException(
				"Could not find ticket that was just made on the database. ticketId=" + ticketPK)); // return the ticket
																									// from the database
																									// (auto-made
																									// timestamp, PK,
		// etc.)

	}

	private Long extractTicketPK(KeyHolder keyHolder) {

		String ticketId = "GENERATED_KEY";

		List<Map<String, Object>> keyList = keyHolder.getKeyList();

		for (Map<String, Object> key : keyList) {
			// System.out.println(map);
			if (key.containsKey(ticketId)) {
				return ((Number) key.get(ticketId)).longValue();
			}
		}

		return null;
	}

	@Override
	public Optional<Ticket> fetchTicket(Ticket ticket) {

		String sql = "SELECT * FROM tickets WHERE ticket_id = :ticket_id";
		Map<String, Object> params = new HashMap<>();
		params.put("ticket_id", ticket.getTicketId());

		User user = userDao.fetchUser(ticket.getUser().getUserId()).orElseThrow(() -> new NoSuchElementException(
				"User attached to ticketId=" + ticket.getTicketId() + " does not exist"));

		return Optional.ofNullable(jdbcTemplate.query(sql, params, new ResultSetExtractor<Ticket>() {

			@Override
			public Ticket extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {

					return Ticket.builder().ticketId(rs.getLong("ticket_id")).ticketTitle(rs.getString("ticket_title"))
							.ticketBody(rs.getString("ticket_body"))
							.created(rs.getTimestamp("created").toLocalDateTime())
							.status(Status.valueOf(rs.getString("status"))).user(user).build();
				}
				return null;
			}

		}));
	}

	@Override
	public Optional<Ticket> fetchTicket(Long ticketId) {
		String sql = "SELECT * FROM tickets WHERE ticket_id = :ticket_id";
		Map<String, Object> params = new HashMap<>();
		params.put("ticket_id", ticketId);
		return Optional.ofNullable(jdbcTemplate.query(sql, params, new ResultSetExtractor<Ticket>() {

			@Override
			public Ticket extractData(ResultSet rs) throws SQLException, DataAccessException, NoSuchElementException {

				if (rs.next()) {
					User user = userDao.fetchUser(rs.getLong("user_fk")).orElseThrow(() -> new NoSuchElementException(
							"User attached to ticketId=" + ticketId + " does not exist"));

					return Ticket.builder().ticketId(rs.getLong("ticket_id")).ticketTitle(rs.getString("ticket_title"))
							.ticketBody(rs.getString("ticket_body"))
							.created(rs.getTimestamp("created").toLocalDateTime())
							.status(Status.valueOf(rs.getString("status"))).user(user).build();
				}
				return null;
			}

		}));
	}

	@Override
	public List<Ticket> fetchTicketsByUser(@Length(max = 15) String username) {
		// fetch user
		User user = userDao.fetchUser(username).get();

		String sql = "SELECT * FROM tickets WHERE user_fk = :user_fk";
		Map<String, Object> params = new HashMap<>();
		params.put("user_fk", user.getUserId());

		return jdbcTemplate.query(sql, params, new RowMapper<Ticket>() {

			@Override
			public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
				return Ticket.builder().ticketId(rs.getLong("ticket_id")).ticketTitle(rs.getString("ticket_title"))
						.ticketBody(rs.getString("ticket_body")).created(rs.getTimestamp("created").toLocalDateTime())
						.status(Status.valueOf(rs.getString("status"))).user(user).build();
			}

		});
	}

	@Override
	public void deleteTicket(Long ticketId) {
		String sql = "DELETE FROM tickets WHERE ticket_id = :ticketId";
		Map<String, Object> params = new HashMap<>();
		params.put("ticketId", ticketId);
		jdbcTemplate.update(sql, params);

	}

	@Override
	public Ticket editStatus(Long ticketId, Status status) {
		String sql = "UPDATE tickets SET status = :status WHERE ticket_id= :ticketId";
		Map<String, Object> params = new HashMap<>();
		System.out.println("trying to add enum " + status);
		params.put("status", status.toString().toUpperCase());
		params.put("ticketId", ticketId);

		jdbcTemplate.update(sql, params);

		return fetchTicket(ticketId).get();
	}

	@Override
	public List<Ticket> fetchTicketsByIdList(List<Long> ticketIdList) {
		List<Ticket> tickets = new LinkedList<>();
		for (Long id : ticketIdList) {
			tickets.add(fetchTicket(id).get());
		}
		return tickets;
	}

}
