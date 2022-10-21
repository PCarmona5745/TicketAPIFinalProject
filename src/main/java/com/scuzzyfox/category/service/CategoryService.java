package com.scuzzyfox.category.service;

import java.util.Optional;

import com.scuzzyfox.entity.Category;

public interface CategoryService {

	public Category createCategoryOrFetchIfExists(String categoryName);

	public Optional<Category> fetchCategory(String categoryName);
	

}
