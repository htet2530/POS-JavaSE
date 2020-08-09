package com.jdc.repo;

import java.util.List;

import com.jdc.entities.Category;
import com.jdc.repo.impl.CategoryRepoImpl;

public interface CategoryRepo {
	static CategoryRepo getInstance() {
		return CategoryRepoImpl.getInstance();
	}
	List<Category> findAll();
	void create(String name);
	void delete(String id);
}
