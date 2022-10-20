package com.scuzzyfox.ticket.service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scuzzyfox.category.service.CategoryService;
import com.scuzzyfox.comment.service.CommentService;
import com.scuzzyfox.entity.Category;
import com.scuzzyfox.entity.Comment;
import com.scuzzyfox.entity.Status;
import com.scuzzyfox.entity.Ticket;
import com.scuzzyfox.entity.TicketRequest;
import com.scuzzyfox.entity.User;
import com.scuzzyfox.ticket.dao.TicketDao;
import com.scuzzyfox.ticketcategory.service.TicketCategoryService;
import com.scuzzyfox.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DefaultTicketService implements TicketService {
	
	@Autowired
	private TicketDao ticketDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private TicketCategoryService ticketCategoryService;
	
	@Autowired
	private CommentService commentService;
	
	
	

	@Override
	public Ticket createTicket(@Valid TicketRequest ticketRequest) {
		log.debug("Create ticket service TicketRequest={}", ticketRequest);
		User user = userService.createUserIfOneDoesNotExist(ticketRequest);
		
		return ticketDao.saveTicket(ticketRequest, user);
	}


	@Override
	public List<Ticket> fetchTicketsByUser(@Length(max = 15) String username) {
		//fetch user, throw error if not found
		User user = userService.fetchAUser(username);
		
		List<Ticket> list = ticketDao.fetchTicketsByUser(username);
		
		if(list.isEmpty()) {
			throw new NoSuchElementException("No tickets under user=" + username + " found.");
		}
		
		return list;
	}


	@Override
	public Category giveExistingTicketACategory(Long ticketId,String categoryName) {
		
		//vvv throws an error if it cant find one
		System.out.println("Fetching ticket id=" + ticketId);
		Ticket ticket = fetchTicket(ticketId);
		System.out.println("Found ticket=" + ticket);
		
		
		
		System.out.println("Fetching category=" + categoryName);
		Category category = categoryService.createCategoryOrFetchIfExists(categoryName);
		System.out.println("Found/created category=" + category);
		
		
		//TODO attach category to ticket
		
		 Category cat = ticketCategoryService.giveTicketACategory(ticket.getTicketId(), category.getCategoryId());
		 
		 return category;
		
	}


	public Ticket fetchTicket(Long ticketId) {
		Ticket ticket = ticketDao.fetchTicket(ticketId).orElseThrow(()->new NoSuchElementException("Could not find ticket of ticketId="+ticketId));
		return ticket;
	}


	@Override
	public void deleteTicket(Long id) {
		ticketDao.deleteTicket(id);
		
	}


	@Override
	public Comment addCommentToTicket(Long ticketId, String comment, String username) {
		return commentService.addCommentToTicket(ticketId, comment, username);
	}


	@Override
	public Ticket changeTicketStatus(Long ticketId, Status status) {
		
		return ticketDao.editStatus(ticketId, status);
	}
	
	

}
