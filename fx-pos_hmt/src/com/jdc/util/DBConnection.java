package com.jdc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static final String URL="jdbc:mysql://localhost:3306/jdc_pos";
	private static final String USR="root";
	private static final String PASS="admin";
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL,USR,PASS);
	}
}
