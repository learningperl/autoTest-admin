package com.java.statics;

import java.io.FileInputStream;
import java.util.Properties;

public class property {

	public static String path = "";
	public static String url = "";
	private static Properties config = null;

	public property() {
		config = new Properties();
		url = property.class.getResource("").getPath().replaceAll("%20", " ");
		url = url.substring(url.indexOf("/")+1, url.length());
		try{
			url = url.substring(0,url.lastIndexOf("/com"));
			url = url.substring(0,url.lastIndexOf("/"));
		}catch (Exception e){
			
		}
		path =url + "/Test.properties";
		System.out.println(path);
	}

	public static String readRcErpURL(String name) {
		try {
			config.load(new FileInputStream(path));
			return config.getProperty(name);
		} catch (Exception e1) {
			path = "/" + path;
			try {
				config.load(new FileInputStream(path));
				return config.getProperty(name);
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
		return null;
	}

}
