package src;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import lib.Info;
import app.Editor;

public class Options {
	private static Properties prop = new Properties();
	
	
	public static void load() {
		File file = new File("settings.cfg");
		if(!file.exists()) {
			createDefaultConfig();
		} else {
			try {
				prop.load(new FileInputStream(file));
			} catch (IOException e) {
				Editor.onError(e);
			}
		}
	}
	
	public static void save() {
		File file = new File("settings.cfg");
		try {
			file.createNewFile();
			prop.store(new FileOutputStream(file), null);
		} catch (IOException e) {
			Editor.onError(e);
		}
	}
	
	
	private static void createDefaultConfig() {
		File file = new File("settings.cfg");
		
		try {
			file.createNewFile();
		} catch (IOException e) {
			Editor.onError(e);
		}
		
		prop.clear();
		prop.setProperty("window_width", "600");
		prop.setProperty("window_height", "600");
		prop.setProperty("full_screen", "false");	
		prop.setProperty("vsync", "true");
		prop.setProperty("map", "GreenVally.shf");


		try {
			prop.store(new FileOutputStream(file), null);
		} catch (IOException e) {
			Editor.onError(e);
		}
	
	}
	
	public static String get(String s) {
		if(prop.containsKey(s)) {
			return prop.getProperty(s);
		}
		
		Info.warning("param."+s+" was not found in config, using default value");
		return "knf";
	}
	
	public static void set(String s, String f) {
		prop.setProperty(s, f);
	}
	
}
