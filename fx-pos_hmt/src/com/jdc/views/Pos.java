package com.jdc.views;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.jdc.entities.Item;
import com.jdc.entities.Sale;
import com.jdc.entities.SalesDetails;
import com.jdc.repo.ItemRepo;
import com.jdc.repo.SaleRepo;
import com.jdc.util.MessageHandler;
import com.jdc.util.TaxRateSetting;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import jdk.nashorn.internal.runtime.regexp.joni.ast.QuantifierNode;

public class Pos implements Initializable {

	@FXML
	private TextField id;

	@FXML
	private TextField item;

	@FXML
	private TableView<Item> items;

	@FXML
	private Label headTotal;

	@FXML
	private TableView<SalesDetails> saleItems;

	@FXML
	private TableColumn<SalesDetails, Integer> quentity;

	@FXML
	private Label subTotal;

	@FXML
	private Label tax;

	@FXML
	private Label total;

	private ItemRepo itemRepo;
	private SaleRepo saleRepo;

	@FXML
	void addToCart(MouseEvent event) {
		if (event.getClickCount() == 2) {
			Item item = items.getSelectionModel().getSelectedItem();
			if (null != item) {
				SalesDetails sale = saleItems.getItems().stream().filter(a -> a.getItemId() == item.getId()).findFirst()
						.orElse(null);
				if (null == sale) {
					sale = new SalesDetails();
					sale.setItemId(item.getId());
					sale.setQuantity(1);
					sale.setUnitPrice(item.getPrice());
					sale.setItemName(item.getName());
					sale.setCategory(item.getCategory());
					saleItems.getItems().add(sale);
				} else {
					// update existing sale details items
					sale.setQuantity(sale.getQuantity() + 1);
					calculateValue();
					saleItems.refresh();
				}
				sale.setTax(sale.getSubTotal() / 100 * TaxRateSetting.getTaxRate());
//				calculateValue();
			}
		}
	}

	private void calculateValue() {
		subTotal.setText(String.valueOf(saleItems.getItems().stream().mapToInt(detail -> detail.getSubTotal()).sum()));
		tax.setText(String.valueOf(saleItems.getItems().stream().mapToInt(detail -> detail.getTax()).sum()));
		total.setText(String.valueOf(saleItems.getItems().stream().mapToInt(detail -> detail.getTotal()).sum()));
	}

	@FXML
	void clear() {
		saleItems.getItems().clear();
		id.clear();
		item.clear();
		items.getItems().clear();
	}

	@FXML
	void paid() {
		try {
		Sale sale = new Sale();
		sale.setDetails(saleItems.getItems());
		saleRepo.create(sale);
		item.clear();
		id.clear();
		items.getItems().clear();
		clear();
		}catch(Exception e){
			MessageHandler.show(e);
		}
	}

	@FXML
	void print() {
		// TODO:
	}

	@FXML
	void remove() {
		SalesDetails removeItem = saleItems.getSelectionModel().getSelectedItem();
		if (removeItem != null) {
			saleItems.getItems().remove(removeItem);
			calculateValue();
		}else {
			MessageHandler.show("Please Select the Item first!");
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		itemRepo = ItemRepo.getInstance();
		saleRepo = SaleRepo.getInstance();
		id.textProperty().addListener((a, b, c) -> search());
		item.textProperty().addListener((a, b, c) -> search());
		headTotal.textProperty().bind(total.textProperty());
		saleItems.getItems().addListener(new ListChangeListener<SalesDetails>() {
			@Override
			public void onChanged(Change<? extends SalesDetails> c) {
				calculateValue();
			}
		});
//		StringCon
		quentity.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {
			@Override
			public String toString(Integer object) {
				if(null!=object) {
					return object.toString();
				}
				return null;
			}
			@Override
			public Integer fromString(String string) {
				try {
					if(!string.isEmpty() && null!=string) {
						return Integer.parseInt(string);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
			}
		}));
		saleItems.setEditable(true);
		//change value after editing quantity table column
		quentity.setOnEditCommit(event-> {
			SalesDetails detail = event.getRowValue();
			detail.setQuantity(event.getNewValue());
			saleItems.refresh();
			calculateValue();
		});
	}

	private void search() {
		id.getText();
		List<Item> itemList = itemRepo.findFromPOS(id.getText(), item.getText());
		items.getItems().clear();
		items.getItems().addAll(itemList);
	}

}
