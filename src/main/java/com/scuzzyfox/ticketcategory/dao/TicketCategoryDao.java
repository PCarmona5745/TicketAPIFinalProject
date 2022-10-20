package com.scuzzyfox.ticketcategory.dao;

import com.scuzzyfox.entity.Category;

public interface TicketCategoryDao {

	Category saveTicketCategory(Long ticketId, Long categoryId);
	
	

}
