package com.scuzzyfox.ticket.service;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.scuzzyfox.entity.Category;
import com.scuzzyfox.entity.Comment;
import com.scuzzyfox.entity.Status;
import com.scuzzyfox.entity.Ticket;
import com.scuzzyfox.entity.TicketRequest;
import com.scuzzyfox.user.service.DefaultUserService;
import com.scuzzyfox.user.service.UserService;

public interface TicketService {

	Ticket createTicket( TicketRequest ticketRequest);

	List<Ticket> fetchTicketsByUser(@Length(max = 15) String username);

	Category giveExistingTicketACategory(Long ticketId, String categoryName);

	void deleteTicket(Long id);

	Comment addCommentToTicket(Long ticketId, String comment, String username);

	Ticket changeTicketStatus(Long ticketId, Status status);

	Ticket fetchTicket(Long ticketId);

	List<Ticket> fetchTicketsByCategory(String categoryName);


}
