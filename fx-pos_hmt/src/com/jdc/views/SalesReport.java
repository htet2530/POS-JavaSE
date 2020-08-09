package com.jdc.views;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.jdc.entities.Category;
import com.jdc.entities.Item;
import com.jdc.entities.SalesDetails;
import com.jdc.repo.CategoryRepo;
import com.jdc.repo.SaleRepo;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class SalesReport implements Initializable{

    @FXML
    private ComboBox<Category> category;
    @FXML
    private TextField item;
    @FXML
    private DatePicker from;
    @FXML
    private DatePicker to;
    @FXML
    private TableView<SalesDetails> table;
    @FXML
    private Label subTotal;
    @FXML
    private Label tax;
    @FXML
    private Label total;
    
    private SaleRepo repo;
    private CategoryRepo catRepo;

    @FXML
    void clear() {
    	item.clear();
    	category.setValue(null);
    	from.setValue(null);
    	to.setValue(null);
    	table.getItems().clear();
    }

    @FXML
    void search() {
    	table.getItems().clear();
    	table.getItems().addAll(repo.findReport(category.getValue(), item.getText(),from.getValue(),to.getValue()));
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		repo=SaleRepo.getInstance();
		catRepo=CategoryRepo.getInstance();
		category.getItems().addAll(catRepo.findAll());
		table.getItems().addListener(new ListChangeListener<SalesDetails>() {
			@Override
			public void onChanged(Change<? extends SalesDetails> c) {
				List<SalesDetails> list=table.getItems();
				subTotal.setText(String.valueOf(list.stream().mapToInt(d -> d.getSubTotal()).sum()));
				tax.setText(String.valueOf(list.stream().mapToInt(d->d.getTax()).sum()));
				total.setText(String.valueOf(list.stream().mapToInt(d->d.getTotal()).sum()));
			}
		});
	}
}
