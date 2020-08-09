package com.jdc.repo.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jdc.entities.Category;
import com.jdc.entities.Item;
import com.jdc.repo.ItemRepo;
import com.jdc.util.CheckString;
import com.jdc.util.DBConnection;

public class ItemRepoImpl implements ItemRepo {
	private static final ItemRepo INSTANCE = new ItemRepoImpl();

	private ItemRepoImpl() {

	}

	public static ItemRepo getInstance() {
		return INSTANCE;
	}

	private static final String INSERT = "INSERT INTO " + "ITEM(name,price,remark,enable,category_id) "
			+ "Values(?,?,?,?,?)";
	private static final String SELECT = "SELECT i.id id,i.name,i.price price,i.remark remark,"
			+ "i.enable enable,c.id categoryId,c.name category" + " from Item i join category c on "
			+ "c.id=i.category_id ";

	private static final String UPDATE = "Update Item set name=?,price=?,remark=?,enable=?,category_id=? where id=?";
	private static final String DELETECHILD="delete from sales_details where item_id=?";
	private static final String DELETEPARENT="Delete from Item where id=?";
	
	@Override
	public List<Item> findItems(Category category, String name, Boolean enable) {
		// result
		List<Item> items = new ArrayList<Item>();
		// dynamic query
		StringBuilder builder = new StringBuilder(SELECT);

		// set params
		List<Object> params = new ArrayList<Object>();

		builder.append("where i.enable=?");
		params.add(enable);

		if (category != null) {
			builder.append(" and c.id=?");
			params.add(category.getId());
		}
		if (!name.isEmpty() || null != name) {
			builder.append(" and i.name like ?");
			params.add(name.concat("%"));
		}

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement prep = conn.prepareStatement(builder.toString())) {
			// set params
			for (int i = 0; i < params.size(); i++) {
				prep.setObject(i + 1, params.get(i));
			}
			ResultSet result = prep.executeQuery();
			while (result.next()) {
				items.add(getItem(result));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

	private Item getItem(ResultSet result) throws SQLException {
		Item item = new Item();
		item.setCategory(result.getString("category"));
		item.setCategoryId(result.getInt("categoryId"));
		item.setId(result.getInt("id"));
		item.setName(result.getString("name"));
		item.setEnable(result.getBoolean("enable"));
		item.setPrice(result.getInt("price"));
		item.setRemark(result.getString("remark"));
		return item;
	}

	@Override
	public List<Item> findFromPOS(String id, String name) {
		List<Item> list = new ArrayList<Item>();
		List<Object> params = new ArrayList<Object>();
		if (!CheckString.empty(id) || !CheckString.empty(name)) {
			StringBuilder builder = new StringBuilder(SELECT);
			builder.append("where i.enable=?");
			params.add(true);
			if (!CheckString.empty(id)) {
				builder.append(" and i.id =?");
				params.add(id);
			}
			if (!CheckString.empty(name)) {
				builder.append(" and i.name like ?");
				params.add(name.concat("%"));
			}
			try (Connection conn = DBConnection.getConnection();
					PreparedStatement prep = conn.prepareStatement(builder.toString())) {
				// set params
				for (int i = 0; i < params.size(); i++) {
					prep.setObject(i + 1, params.get(i));
				}
				ResultSet result = prep.executeQuery();
				while (result.next()) {
					list.add(getItem(result));
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	@Override
	public void save(Item item) {
		//validation null test
		if(item.getId()>0) {
			update(item);
		}else {
			create(item);
		}
	}
	@Override
	public void create(Item item) {
		// TODO: validation
		try (Connection conn = DBConnection.getConnection(); PreparedStatement prep = conn.prepareStatement(INSERT)) {
			prep.setString(1, item.getName());
			prep.setInt(2, item.getPrice());
			prep.setString(3, item.getRemark());
			prep.setBoolean(4, item.isEnable());
			prep.setInt(5, item.getCategoryId());
			prep.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Item> findFromPOS(int id, String name) {
		return null;
	}

	@Override
	public void update(Item item) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement prep = conn.prepareStatement(UPDATE)) {
			prep.setString(1, item.getName());
			prep.setInt(2, item.getPrice());
			prep.setString(3, item.getRemark());
			prep.setBoolean(4, item.getEnable());
			prep.setInt(5, item.getCategoryId());
			prep.setInt(6, item.getId());
			prep.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(Item item) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement prepChild = conn.prepareStatement(DELETECHILD);
				PreparedStatement prepParent=conn.prepareStatement(DELETEPARENT)) {
			prepChild.setInt(1, item.getId());
			prepChild.executeUpdate();
			prepParent.setInt(1, item.getId());
			prepParent.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
