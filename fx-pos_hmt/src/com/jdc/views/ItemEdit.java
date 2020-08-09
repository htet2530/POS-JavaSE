package com.jdc.views;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.jdc.entities.Category;
import com.jdc.entities.Item;
import com.jdc.repo.CategoryRepo;
import com.jdc.util.MessageHandler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ItemEdit implements Initializable {

    @FXML
    private Label message;
    @FXML
    private ComboBox<Category> category;
    @FXML
    private TextField name;
    @FXML
    private TextField price;
    @FXML
    private TextArea remark;
    private Consumer<Item> saveHandler;
    private CategoryRepo catRepo;
    private Item itemobj;

    @FXML
    void close() {
    	name.getScene().getWindow().hide();
    }

    @FXML
    void save() {
    	try {
    		Item item=new Item();
    		if(itemobj!=null) {
    			item.setId(itemobj.getId());
    		}
    		item.setName(name.getText());
    		item.setPrice(Integer.parseInt(price.getText()));
    		item.setRemark(remark.getText());
    		item.setCategory(category.getValue().getName());
    		item.setCategoryId(category.getValue().getId());
    		item.setEnable(true);
    		//get View Data
			saveHandler.accept(item);
			
			//hide window
			close();
		} catch (Exception e) {
			message.setText(e.getMessage());
		}
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		catRepo=CategoryRepo.getInstance();
		category.getItems().addAll(catRepo.findAll());
	}
	
	public static void show(Consumer<Item> saveHandler) {
		try{
    		FXMLLoader loader=new FXMLLoader(CategoryEdit.class.getResource("ItemEdit.fxml"));
    		Parent root=loader.load();
    		ItemEdit controller=loader.getController();
    		controller.saveHandler=saveHandler;
    		Stage stage=new Stage();
    		stage.setScene(new Scene(root));
    		stage.setTitle("Add New Item");
    		stage.show();
    	}catch (Exception e) {
    		MessageHandler.show(e);
		}
	}
	public static void update(Consumer<Item> updateHandler,Item item) {
		try{
    		FXMLLoader loader=new FXMLLoader(CategoryEdit.class.getResource("ItemEdit.fxml"));
    		Parent root=loader.load();
    		ItemEdit controller=loader.getController();
    		controller.saveHandler=updateHandler;
    		Stage stage=new Stage();
    		stage.setScene(new Scene(root));
    		stage.setTitle("Update Item");
    		stage.show();
    		controller.itemobj=item;
    		controller.insertData(item);
    	}catch (IOException e) {
    		MessageHandler.show(e);
		}
	}
	
	public void insertData(Item item) {
		List<Category> categories = catRepo.findAll();
		for (Category cat : categories) {
			if(cat.getName().equals(item.getCategory())) {
				category.setValue(cat);
			}
		}
		name.setText(item.getName());
		price.setText(String.valueOf(item.getPrice()));
		remark.setText(item.getRemark());
	}
	

}
