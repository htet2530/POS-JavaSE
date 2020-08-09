package com.jdc.views;

import com.jdc.entities.Category;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class CategoryBox extends HBox{
	public CategoryBox(Category category) {
		Label lblid=new Label(String.valueOf(category.getId()));
		Label lblname=new Label(category.getName());
		getChildren().addAll(lblid,lblname);
		getStyleClass().add("wid-200");
		getStyleClass().add("sp-20");
		getStyleClass().add("pad-20");
		getStyleClass().add("primary");
		getStyleClass().add("al-center-left");
		lblid.getStyleClass().add("white-text");
		lblname.getStyleClass().add("white-text");
		lblid.setId(lblid.getText());
		lblname.setId(lblname.getText());
		setCursor(Cursor.HAND);
		setId(String.valueOf(category.getId()));
	}
}
