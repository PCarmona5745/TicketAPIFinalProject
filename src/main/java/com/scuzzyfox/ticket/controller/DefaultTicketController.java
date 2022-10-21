package com.scuzzyfox.ticket.controller;

import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.scuzzyfox.comment.service.CommentService;
import com.scuzzyfox.entity.Category;
import com.scuzzyfox.entity.Comment;
import com.scuzzyfox.entity.Status;
import com.scuzzyfox.entity.Ticket;
import com.scuzzyfox.entity.TicketRequest;
import com.scuzzyfox.ticket.service.TicketService;

import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
public class DefaultTicketController implements TicketController {
	
	
	@Autowired
	private TicketService ticketService;
	
	
	@Autowired
	private CommentService commentService;

	@Override
	public Ticket createTicket( TicketRequest ticketRequest) {
		log.debug("Controller: createTicket={}",ticketRequest);
		return ticketService.createTicket(ticketRequest);
	}

	@Override
	public List<Ticket> fetchTicketsByUser(@Length(max = 15) String username) {
		log.debug("Controller: fetchTicketsBy={}",username);
		return ticketService.fetchTicketsByUser(username);
	}

	@Override
	public Category giveExistingTicketACategory(Long ticketId, String categoryName) {
		log.debug("Controller: attachTicketCategory. ticket={}, category={}",ticketId, categoryName);
		return ticketService.giveExistingTicketACategory(ticketId,categoryName);
	}

	@Override
	public Comment addCommentToTicket(Long ticketId, String comment, String username) {

		return ticketService.addCommentToTicket(ticketId,comment,username);
	}

	@Override
	public Ticket changeTicketStatus(Long ticketId, Status status) {
		log.debug("changing ticket="+ticketId+" status to"+ status);
		return ticketService.changeTicketStatus(ticketId, status);
	}

	@Override
	public List<Comment> fetchCommentsOnTicket(Long ticketId) {
		ticketService.fetchTicket(ticketId);
		return commentService.fetchCommentsOnTicket(ticketId);
	}

	@Override
	public List<Ticket> fetchTicketsByCategory(String categoryName) {
		
		return ticketService.fetchTicketsByCategory(categoryName);
	}

}
