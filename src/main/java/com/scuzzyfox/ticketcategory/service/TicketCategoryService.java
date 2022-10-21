package com.scuzzyfox.ticketcategory.service;

import java.util.List;

import com.scuzzyfox.entity.Category;

public interface TicketCategoryService {

	Category giveTicketACategory(Long ticketId, Long categoryId);

	List<Long> fetchTicketIdsByCategory(Long categoryId);

}
