package com.scuzzyfox.ticketcategory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scuzzyfox.entity.Category;
import com.scuzzyfox.ticketcategory.dao.TicketCategoryDao;

@Service
public class DefaultTicketCategoryService implements TicketCategoryService {
	
	@Autowired
	private TicketCategoryDao tcDao;

	@Override
	public Category giveTicketACategory(Long ticketId, Long categoryId) {
		
		
		
		
		return tcDao.saveTicketCategory(ticketId,categoryId);
	}

}
