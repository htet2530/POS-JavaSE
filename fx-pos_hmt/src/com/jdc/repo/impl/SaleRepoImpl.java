package com.jdc.repo.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.jdc.entities.Category;
import com.jdc.entities.Item;
import com.jdc.entities.Sale;
import com.jdc.entities.SalesDetails;
import com.jdc.repo.SaleRepo;
import com.jdc.util.CheckString;
import com.jdc.util.DBConnection;
import com.jdc.util.PosException;

public class SaleRepoImpl implements SaleRepo {
	private static final String INSERT_SALE = "insert into sales(sales_date,sales_time,enable) values(?,?,?)";
	private static final String INSERT_DETAILS = "insert into sales_details(item_id,sales_id,unit_price,quantity,tax) values(?,?,?,?,?);";
	private static final String SELECT = "select sd.id ,s.sales_date,s.sales_time,c.name as Category,i.name,sd.unit_price,sd.quantity,sd.tax"
			+ " from sales_details sd" + " join sales s on s.id=sd.sales_id" + " join item i on i.id=sd.item_id"
			+ " join category c on c.id=i.category_id"
			+ " where 1=1";
	private static final SaleRepo INSTANCE = new SaleRepoImpl();
	private SaleRepoImpl() {

	}

	// TODO Auto-generated constructor stub
	public static SaleRepo getInstance() {
		return INSTANCE;
	}

	@Override
	public void create(Sale sale) {
		validate(sale);
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement prepSale = conn.prepareStatement(INSERT_SALE, Statement.RETURN_GENERATED_KEYS);
				PreparedStatement prepDetails = conn.prepareStatement(INSERT_DETAILS)) {
			prepSale.setDate(1, Date.valueOf(LocalDate.now()));
			prepSale.setTime(2, Time.valueOf(LocalTime.now()));
			prepSale.setBoolean(3, true);
			int result = prepSale.executeUpdate();
			if (result == 1) {
				ResultSet rs = prepSale.getGeneratedKeys();
				while (rs.next()) {
					int saleId = rs.getInt(1);
					for (SalesDetails detail : sale.getDetails()) {
						prepDetails.setInt(1, detail.getItemId());
						prepDetails.setInt(2, saleId);
						prepDetails.setInt(3, detail.getUnitPrice());
						prepDetails.setInt(4, detail.getQuantity());
						prepDetails.setInt(5, detail.getTax());
						prepDetails.executeUpdate();
					}
					break;
				}
			} else {

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void validate(Sale sale) {
		if (sale.getDetails().isEmpty()) {
			throw new PosException("Please add item into cart first!!!");
		}

	}

	@Override
	public List<SalesDetails> findReport(Category category, String name, LocalDate startDate, LocalDate endDate) {
		// result
		List<SalesDetails> salesDetails = new ArrayList<SalesDetails>();
		// dynamic query
		StringBuilder builder = new StringBuilder(SELECT);

		// set params
		List<Object> params = new ArrayList<Object>();

		if (category != null) {
			builder.append(" and c.id=?");
			params.add(category.getId());
		}
		if (!CheckString.empty(name)) {
			builder.append(" and i.name like ?");
			params.add(name.concat("%"));
		}
		if (startDate != null) {
			builder.append(" and s.sales_date >= ?");
			params.add(Date.valueOf(startDate));
		}
		if (null != endDate) {
			builder.append(" and s.sales_date <= ?");
			params.add(Date.valueOf(endDate));
		}

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement prep = conn.prepareStatement(builder.toString())) {
			// set params
			for (int i = 0; i < params.size(); i++) {
				prep.setObject(i + 1, params.get(i));
			}
			ResultSet result = prep.executeQuery();
			while (result.next()) {
				salesDetails.add(getSaleDetail(result));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return salesDetails;
	}

	private SalesDetails getSaleDetail(ResultSet result) throws SQLException {
		SalesDetails detail = new SalesDetails();
		detail.setId(result.getInt("id"));
		detail.setSaleDate(LocalDate.parse(result.getString("sales_date")));
		detail.setSaleTime(LocalTime.parse(result.getString("sales_time")));
		detail.setCategory(result.getString("Category"));
		detail.setItemName(result.getString("name"));
		detail.setUnitPrice(result.getInt("unit_price"));
		detail.setQuantity(result.getInt("quantity"));
		detail.setTax(result.getInt("tax"));
		return detail;
	}

}
