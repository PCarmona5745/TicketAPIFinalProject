package com.scuzzyfox.user.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scuzzyfox.comment.dao.CommentDao;
import com.scuzzyfox.comment.service.CommentService;
import com.scuzzyfox.entity.Comment;
import com.scuzzyfox.entity.Ticket;
import com.scuzzyfox.entity.TicketRequest;
import com.scuzzyfox.entity.User;
import com.scuzzyfox.ticket.service.TicketService;
import com.scuzzyfox.user.dao.UserDao;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DefaultUserService implements UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private CommentService commentService;

	//this is causing a circular dependency, had to set allow circular dependencies to true to get code to compile
	@Autowired
	private TicketService ticketService;

	@Override
	public User fetchAUser(String username) {
		return userDao.fetchUser(username).orElseThrow(
				() -> new NoSuchElementException("Cannot fetch user " + username + " as they do not exist."));
	}

	@Override
	public User updateAUser(String username, String newUsername) {
		return userDao.updateUsername(username, newUsername);
	}

	@Override
	public User deleteAUser(String username) {
		User user = userDao.fetchUser(username).orElseThrow(
				() -> new NoSuchElementException("Cannot delete user " + username + " as they do not exist."));
		
		//delete all comments by user
		commentService.deleteAllCommentsByUser(user.getUserId());
		
		
//delete all tickets by user (delete all comments on the tickets first)
		List<Ticket> tickets = ticketService.fetchTicketsByUser(username);

		if (!tickets.isEmpty()) {
			List<Long> ids = new LinkedList<>();
			for (Ticket ticket : tickets) {
				ids.add(ticket.getTicketId());
			}

			for (Long id : ids) {
				//delete comments belonging to id
				commentService.deleteAllCommentsInTicket(id);
				
				
				ticketService.deleteTicket(id);
			}

		}

		return userDao.deleteUser(username);

	}

	@Override
	public User createAUser(String username) {

		return createUserButThrowErrorIfOneExists(username);

	}

	private User createUserButThrowErrorIfOneExists(String username) {

		return userDao.saveUser(username);

	}

	// fetches a user from the database and creates one if it doesn't exist
	public User createUserIfOneDoesNotExist(TicketRequest ticketRequest) {
		log.debug("Creating user if one does not exist with user={}", ticketRequest.getUsername());
		Optional<User> result = userDao.fetchUser(ticketRequest.getUsername());

		if (result.isEmpty()) {

			// create the user on the database and end the function
			return userDao.saveUser(ticketRequest.getUsername());
		}

		//
		return result.get();

	}

	// fetches a user from the database and creates one if it doesn't exist
	public User createUserIfOneDoesNotExist(String username) {
		log.debug("Creating user if one does not exist with user={}", username);
		Optional<User> result = userDao.fetchUser(username);

		if (result.isEmpty()) {

			// create the user on the database and end the function
			return userDao.saveUser(username);
		}

		//
		return result.get();

	}

}
