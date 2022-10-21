package com.scuzzyfox.ticketcategory.dao;

import java.util.List;

import com.scuzzyfox.entity.Category;

public interface TicketCategoryDao {

	Category saveTicketCategory(Long ticketId, Long categoryId);

	List<Long> fetchTicketIdsByCategory(Long categoryId);
	
	

}
