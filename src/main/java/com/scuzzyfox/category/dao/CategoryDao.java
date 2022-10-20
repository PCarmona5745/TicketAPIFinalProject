package com.scuzzyfox.category.dao;


import java.util.Optional;

import com.scuzzyfox.entity.Category;
import com.scuzzyfox.entity.Ticket;

public interface CategoryDao {

	Optional<Category> fetchCategory(String categoryName);

	Category saveCategory(String categoryName);

	Optional<Category> fetchCategory(Long categoryId);

}
