package com.jdc.repo;

import java.time.LocalDate;
import java.util.List;

import com.jdc.entities.Category;
import com.jdc.entities.Sale;
import com.jdc.entities.SalesDetails;
import com.jdc.repo.impl.SaleRepoImpl;

public interface SaleRepo {
	public static SaleRepo getInstance() {
		return SaleRepoImpl.getInstance();
	}
	void create(Sale sale);
	List<SalesDetails> findReport(Category category,String item,LocalDate startDate,LocalDate endDate);
}
