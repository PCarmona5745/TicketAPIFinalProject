package com.scuzzyfox.category.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
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

import com.scuzzyfox.entity.Category;

@Component

public class DefaultCategoryDao implements CategoryDao {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public Optional<Category> fetchCategory(String categoryName) {
		String sql = "SELECT * FROM categories WHERE category_name = :categoryName";

		Map<String, Object> params = new HashMap<>();

		params.put("categoryName", categoryName);

		Category cat = jdbcTemplate.query(sql, params, new ResultSetExtractor<Category>() {

			@Override
			public Category extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					return Category.builder().categoryId(rs.getLong("category_id"))
							.categoryName(rs.getString("category_name")).build();
				}

				return null;
			}
		});

		return Optional.ofNullable(cat);
	}

	@Override
	public Category saveCategory(String categoryName) {
		String sql = "INSERT INTO categories (category_name) VALUES (:categoryName)";
		KeyHolder keyholder = new GeneratedKeyHolder();

		MapSqlParameterSource source = new MapSqlParameterSource();

		source.addValue("categoryName", categoryName);

		jdbcTemplate.update(sql, source, keyholder);

		Long categoryId = extractCategoryPK(keyholder);

		return fetchCategory(categoryId).orElseThrow(() -> new NoSuchElementException(
				"Could not find category=" + categoryName + ", categoryId=" + categoryId + " after creating it"));

	}

	@Override
	public Optional<Category> fetchCategory(Long categoryId) {
		String sql = "SELECT * FROM categories WHERE category_id = :categoryId";

		Map<String, Object> params = new HashMap<>();

		params.put("categoryId", categoryId);

		Category cat = jdbcTemplate.query(sql, params, new ResultSetExtractor<Category>() {

			@Override
			public Category extractData(ResultSet rs) throws SQLException, DataAccessException {
				// TODO Auto-generated method stub
				if (rs.next()) {
					return Category.builder().categoryId(rs.getLong("category_id"))
							.categoryName(rs.getString("category_name")).build();
				}
				return null;
			}
		});

		return Optional.ofNullable(cat);
	}

	private Long extractCategoryPK(KeyHolder keyHolder) {
		// ((Number)keyHolder.getKeys().get("ticket_id")).longValue();

		List<Map<String, Object>> keyList = keyHolder.getKeyList();

		for (Map<String, Object> key : keyList) {
			// System.out.println(map);
			if (key.containsKey("GENERATED_KEY")) {
				return ((Number) key.get("GENERATED_KEY")).longValue();
			}
		}

		return null;
	}

}
