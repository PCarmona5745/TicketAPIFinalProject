package com.scuzzyfox.comment.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scuzzyfox.comment.dao.CommentDao;
import com.scuzzyfox.entity.Comment;
import com.scuzzyfox.entity.User;
import com.scuzzyfox.user.dao.UserDao;

@Service
public class DefaultCommentService implements CommentService {

	@Autowired
	CommentDao commentDao;
	
	@Autowired
	UserDao userDao;

	@Override
	public void deleteAllCommentsByUser(Long userId) {
		List<Comment> comments = commentDao.fetchCommentsByUser(userId);

		if (!comments.isEmpty()) {
			for (Comment comment : comments) {
				commentDao.deleteComment(comment.getCommentId());
			}
		}

	}

	@Override
	public void deleteAllCommentsInTicket(Long id) {
		List<Comment> comments = commentDao.fetchCommentsOnTicket(id);

		if (!comments.isEmpty()) {
			for (Comment comment : comments) {
				commentDao.deleteComment(comment.getCommentId());
			}
		}

	}

	@Override
	public Comment addCommentToTicket(Long ticketId, String c, String username) {
		List<Comment> comments = commentDao.fetchCommentsOnTicket(ticketId);
		int commentOrder = comments.size()+1;
		
		User user = userDao.fetchUser(username).orElseThrow(()->new NoSuchElementException("could not find user="+username));
		
		Comment comment = Comment.builder()
				.ticketFK(ticketId)
				.commentOrder(commentOrder)
				.commentText(c)
				.userFK(user.getUserId())
				.build();
		
		return commentDao.saveComment(comment);
	}

	@Override
	public List<Comment> fetchCommentsOnTicket(Long ticketId) {
		return commentDao.fetchCommentsOnTicket(ticketId);
	}

}
