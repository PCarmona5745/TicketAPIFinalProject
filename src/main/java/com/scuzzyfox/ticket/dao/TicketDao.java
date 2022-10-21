package com.scuzzyfox.ticket.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.validator.constraints.Length;

import com.scuzzyfox.entity.Status;
import com.scuzzyfox.entity.Ticket;
import com.scuzzyfox.entity.TicketRequest;
import com.scuzzyfox.entity.User;

public interface TicketDao {
	Ticket saveTicket(TicketRequest ticketRequest, User user);

	Optional<Ticket> fetchTicket(Ticket ticket);

	Optional<Ticket> fetchTicket(Long ticketId);

	List<Ticket> fetchTicketsByUser(@Length(max = 15) String username);

	void deleteTicket(Long ticketId);

	Ticket editStatus(Long ticketId, Status status);

	List<Ticket> fetchTicketsByIdList(List<Long> ticketIdList);
}
