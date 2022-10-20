package com.scuzzyfox.ticketcategory.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.scuzzyfox.category.dao.CategoryDao;
import com.scuzzyfox.entity.Category;
import com.scuzzyfox.ticket.dao.TicketDao;

@Component
public class DefaultTicketCategoryDao implements TicketCategoryDao {

	@Autowired
	NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	TicketDao ticketDao;
	
	@Autowired 
	CategoryDao categoryDao;

	@Override
	public Category saveTicketCategory(Long ticketId, Long categoryId) {

		// if ticketid AND categoryid already exist, throw error
		if (!fetchTicketCategory(ticketId, categoryId).isEmpty()) {
			throw new DuplicateKeyException("ticketId=" + ticketId + " already has categoryId=" + categoryId);
		}
		
		//if ticket does not exist, throw error
		ticketDao.fetchTicket(ticketId).orElseThrow(()-> new NoSuchElementException("could not find ticket=" + ticketId));
		
		//if category does not exist, throw error
		Category cat = categoryDao.fetchCategory(categoryId).orElseThrow(()-> new NoSuchElementException("could not find category=" + categoryId));

		// sql
		String sql = "INSERT INTO tickets_categories (ticket_fk, category_fk) VALUES (:ticketId, :categoryId)";

		// map
		Map<String, Object> params = new HashMap<>();

		params.put("ticketId", ticketId);
		params.put("categoryId", categoryId);

		// update
		jdbcTemplate.update(sql, params);

		// fetch category
		//cat = categoryDao.fetchCategory(categoryId).orElseThrow(()-> new NoSuchElementException("could not find category=" + categoryId));

		// return category

		return cat;
	}

	List<Map<String, Long>> fetchTicketCategory(Long ticketId, Long categoryId) {

		String sql = "SELECT * FROM tickets_categories WHERE ticket_fk = :ticketId AND category_fk = :categoryId";

		Map<String, Object> params = new HashMap<>();

		params.put("ticketId", ticketId);
		params.put("categoryId", categoryId);

		return jdbcTemplate.query(sql, params, new RowMapper<>() {

			@Override
			public Map<String, Long> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, Long> m = new HashMap<>();

				m.put("ticket_fk", rs.getLong("ticket_fk"));
				m.put("category_fk", rs.getLong("category_fk"));

				return m;

			}

		});

	}

}
