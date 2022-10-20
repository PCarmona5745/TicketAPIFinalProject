package com.scuzzyfox.comment.service;

import java.util.List;

import com.scuzzyfox.entity.Comment;

public interface CommentService {

	void deleteAllCommentsByUser(Long userId);

	void deleteAllCommentsInTicket(Long id);

	Comment addCommentToTicket(Long ticketId, String comment, String username);

	List<Comment> fetchCommentsOnTicket(Long ticketId);

}
