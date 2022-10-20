package com.scuzzyfox.comment.dao;

import java.util.List;
import java.util.Optional;

import com.scuzzyfox.entity.Comment;

public interface CommentDao {
	
	List<Comment> fetchCommentsOnTicket(Long ticketId);
	
	void deleteComment(Long commentId);
	
	Comment saveComment(Comment comment);
	
	Optional<Comment> fetchComment(Long commentId);

	List<Comment> fetchCommentsByUser(Long userId);

}
