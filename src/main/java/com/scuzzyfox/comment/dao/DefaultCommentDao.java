package com.scuzzyfox.comment.dao;

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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.scuzzyfox.entity.Comment;

@Component
public class DefaultCommentDao implements CommentDao {

	@Autowired
	NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public List<Comment> fetchCommentsOnTicket(Long ticketId) {
		String sql = "SELECT * FROM comments WHERE ticket_fk = :ticketId";
		Map<String, Object> params = new HashMap<>();
		params.put("ticketId", ticketId);

		return jdbcTemplate.query(sql, params, new RowMapper<>() {

			@Override
			public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {

				return Comment.builder().commentId(rs.getLong("comment_id")).commentText(rs.getString("comment_text"))
						.ticketFK(rs.getLong("ticket_fk")).userFK(rs.getLong("user_fk"))
						.commentOrder(rs.getInt("comment_order")).build();
			}

		});
	}

	@Override
	public void deleteComment(Long commentId) {
		String sql = "DELETE FROM comments WHERE comment_id = :commentId";
		Map<String, Object> params = new HashMap<>();
		params.put("commentId", commentId);

		jdbcTemplate.update(sql, params);

	}

	@Override
	public Comment saveComment(Comment comment) {
		String sql = "INSERT INTO comments (comment_text, ticket_fk, user_fk, comment_order)"
				+ "VALUES (:comment_text, :ticket_fk, :user_fk, :comment_order)";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue("comment_text", comment.getCommentText());
		source.addValue("ticket_fk", comment.getTicketFK());
		source.addValue("user_fk", comment.getUserFK());
		source.addValue("comment_order", comment.getCommentOrder());

		jdbcTemplate.update(sql, source, keyHolder);

		Long commentId = extractCommentId(keyHolder);

		return fetchComment(commentId).orElseThrow(() -> new NoSuchElementException(
				"Could not retrieve comment=" + commentId + " after it was just saved."));
	}

	private Long extractCommentId(KeyHolder keyHolder) {
		return keyHolder.getKey().longValue();
	}

	@Override
	public Optional<Comment> fetchComment(Long commentId) {
		String sql = "SELECT * FROM comments WHERE comment_id = :commentId";
		Map<String, Object> params = new HashMap<>();
		params.put("commentId", commentId);

		return Optional.ofNullable(jdbcTemplate.query(sql, params, new ResultSetExtractor<>() {

			@Override
			public Comment extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					return Comment.builder().commentId(rs.getLong("comment_id"))
							.commentText(rs.getString("comment_text")).ticketFK(rs.getLong("ticket_fk"))
							.userFK(rs.getLong("user_fk")).commentOrder(rs.getInt("comment_order")).build();
				}
				return null;
			}

		}));
	}

	@Override
	public List<Comment> fetchCommentsByUser(Long userId) {
		String sql = "SELECT * FROM comments WHERE user_fk = :userId";
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		
		return jdbcTemplate.query(sql, params, new RowMapper<>() {

			@Override
			public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {

				return Comment.builder().commentId(rs.getLong("comment_id")).commentText(rs.getString("comment_text"))
						.ticketFK(rs.getLong("ticket_fk")).userFK(rs.getLong("user_fk"))
						.commentOrder(rs.getInt("comment_order")).build();
			}

		});
	}

}
