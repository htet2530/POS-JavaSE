package com.jdc.util;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class MessageHandler {
	private static AlertType type;
	public static Alert alert=new Alert(null);
	public static String title="POS-information";
	static {
		alert.setHeaderText(null);
		alert.setResizable(true);
		alert.setTitle(title);
	}
	public static void show(Exception e) {
		type = e instanceof PosException ? 
				AlertType.WARNING : AlertType.ERROR;
		alert.setAlertType(type);
		alert.setContentText(e.getMessage());
		alert.show();
	}
	public static void show(String message) {
		alert.setAlertType(AlertType.INFORMATION);
		alert.setContentText(message);
		alert.show();
	}
	
	public static Optional<ButtonType> confirm(String message){
		alert.setAlertType(AlertType.CONFIRMATION);
		alert.setContentText(message);
		return alert.showAndWait();
	}
	public static void bringToFront() {
		Stage window = (Stage)alert.getDialogPane().getScene().getWindow();
		window.setAlwaysOnTop(true);
	}
	
}
