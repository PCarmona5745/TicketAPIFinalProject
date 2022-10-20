package com.scuzzyfox.category.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scuzzyfox.category.dao.CategoryDao;
import com.scuzzyfox.entity.Category;

@Service
public class DefaultCategoryService implements CategoryService {

	@Autowired
	private CategoryDao categoryDao;

	@Override
	public Category createCategoryOrFetchIfExists(String categoryName) {
		
		Optional<Category> catOpt = categoryDao.fetchCategory(categoryName);

		if (catOpt.isEmpty()) {
			return categoryDao.saveCategory(categoryName);
		}
		return catOpt.get();
	}

}
