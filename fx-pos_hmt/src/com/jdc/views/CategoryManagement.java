package com.jdc.views;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.jdc.entities.Category;
import com.jdc.repo.CategoryRepo;
import com.jdc.util.MessageHandler;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.TilePane;

public class CategoryManagement implements Initializable {
	@FXML
	private TilePane items;
	private CategoryRepo repo;

	@FXML
	void addNew() {
		CategoryEdit.show(this:: saveAction);
	}
	private void saveAction(String name) {
		repo.create(name);
		loadCategoryBox();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		repo = CategoryRepo.getInstance();
		loadCategoryBox();
//		showAlert();
	}
	private void loadCategoryBox() {
		items.getChildren().clear();
		List<Category> list=repo.findAll();
//		List<CategoryBox> box=list.stream().map(a-> new CategoryBox(a));
		items.getChildren().addAll(list.stream().map(a-> new CategoryBox(a)).collect(Collectors.toList()));
		items.getChildren().forEach(node->{
					node.setOnMouseClicked(event-> {
									if(event.getClickCount()==2) { 
										delete(node);
										}
									});
					});
	}
	
	private void delete(Node node) {
		Optional<ButtonType> answer = MessageHandler.confirm("Are you sure to delete?");
		if(answer.get().equals(ButtonType.OK)) {
			repo.delete(node.getId());
			loadCategoryBox();
		}
	}
	public void showAlert() {
		String message="id  name \n";
		List<Category> categories=repo.findAll();
		Iterator<Category> ite=categories.iterator();
		while(ite.hasNext()) {
			Category category=ite.next();
			message+=category.getId()+"  ";
			message+=category.getName()+"\n";
		}
		MessageHandler.show("Size: " + repo.findAll().size()+"\n"+message);
		MessageHandler.bringToFront();
	}
}
