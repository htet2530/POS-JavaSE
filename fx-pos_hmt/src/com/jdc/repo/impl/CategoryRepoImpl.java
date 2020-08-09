package com.jdc.repo.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jdc.entities.Category;
import com.jdc.repo.CategoryRepo;
import com.jdc.util.DBConnection;
import com.jdc.util.PosException;

public class CategoryRepoImpl implements CategoryRepo {
	private static final CategoryRepoImpl REPO=new CategoryRepoImpl();
	
	private static final String SELECT_ALL="select * from category order by id";
	private static final String INSERT="insert into category (name) values (?)";
	private static final String DELETE="delete from category where id=?";

	private CategoryRepoImpl() { }
	
	public static CategoryRepoImpl getInstance() {
		return REPO;
	}
	@Override
	public List<Category> findAll() {
		ArrayList<Category> list=new ArrayList<>();
		try(Connection conn=DBConnection.getConnection();
			Statement stmt=conn.createStatement()){
			
			ResultSet result = stmt.executeQuery(SELECT_ALL);
			
			while(result.next()) {
				Category category=new Category();
				category.setId(result.getInt("id"));
				category.setName(result.getString("name"));
				list.add(category);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void create(String name) {
		if(name.isEmpty() || null==name) {
			throw new PosException("Category name must not be empty !!!");
		}
		
		//TODO: if exists
		List<Category> categories=findAll();
		Iterator<Category> ite=categories.iterator();
		List<String> names=new ArrayList<String>();
		while(ite.hasNext()) {
			Category category=ite.next();
			names.add(category.getName());
		}
		if(names.contains(name)) {
			throw new PosException("Category name is already exists.");
		}
		//throws PosException
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement prep = conn.prepareStatement(INSERT)) {
			prep.setString(1, name);
			prep.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
	}

	@Override
	public void delete(String id) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement prep = conn.prepareStatement(DELETE)) {
			prep.setString(1, id);
			prep.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
