package com.jdc.repo;

import java.util.List;

import com.jdc.entities.Category;
import com.jdc.entities.Item;
import com.jdc.repo.impl.ItemRepoImpl;


public interface ItemRepo {
	static ItemRepo getInstance() {
		return ItemRepoImpl.getInstance();
	}
	List<Item> findItems(Category category,String name,Boolean enable);
	List<Item> findFromPOS(int id,String name);
	void create(Item item);
	List<Item> findFromPOS(String id, String name);
	void save(Item item);
	void update(Item item);
	void delete(Item item);
}
