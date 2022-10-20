package com.scuzzyfox.ticketcategory.service;

import com.scuzzyfox.entity.Category;

public interface TicketCategoryService {

	Category giveTicketACategory(Long ticketId, Long categoryId);

}
