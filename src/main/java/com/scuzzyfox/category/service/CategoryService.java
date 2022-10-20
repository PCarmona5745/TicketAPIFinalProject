package com.scuzzyfox.category.service;

import com.scuzzyfox.entity.Category;

public interface CategoryService {

	public Category createCategoryOrFetchIfExists(String categoryName);
	

}
