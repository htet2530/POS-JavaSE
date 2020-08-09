package com.jdc.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PosSetting {

	private static final String FILE = "pos.properties";
	
	static void init() throws IOException {
		File file = new File(FILE);
		
		if(file.exists())
			System.out.println("Already exists ...");
		else 
			file.createNewFile();
	}
	
	static void writeData(String name, String password) {
		try (BufferedWriter out = new BufferedWriter(new FileWriter(FILE))){
			
			String split = ":";
			
			out.write("pos.user.login" + split + name);
			out.newLine();
			out.write("pos.user.password".concat(split).concat(password));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
