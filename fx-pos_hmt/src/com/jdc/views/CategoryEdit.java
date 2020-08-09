package com.jdc.views;

import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CategoryEdit {

    @FXML
    private Label message;
    @FXML
    private TextField name;
    private Consumer<String> saveHandler;
    
    public static void show(Consumer<String> saveHandler) {
    	try{
    		FXMLLoader loader=new FXMLLoader(CategoryEdit.class.getResource("CategoryEdit.fxml"));
    		Parent root=loader.load();
    		CategoryEdit controller=loader.getController();
    		controller.saveHandler=saveHandler;
    		Stage stage=new Stage();
    		stage.setScene(new Scene(root));
    		stage.setTitle("Create New Category");
    		stage.show();
    	}catch (Exception e) {
    		e.printStackTrace();
		}
    }
    @FXML
    void close() {
    	name.getScene().getWindow().hide();
    }

    @FXML
    void save() {
    	try {
			//save in db
			saveHandler.accept(name.getText());
			//hide window
			close();
		} catch (Exception e) {
			message.setText(e.getMessage());
		}
    }

}
