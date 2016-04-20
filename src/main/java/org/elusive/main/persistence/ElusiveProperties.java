package org.elusive.main.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ElusiveProperties {


	private static Properties properties;
	
	private static File getFile() throws IOException{
		String file = ElusiveProperties.class.getResource("/properties/elusive.properties").getFile();
//		System.out.println("ElusiveProperties.getFile() filename:"+file);
		File f = new File(file);
		if( !f.exists() ){
			f.createNewFile();
		}
		return f;		
	}
	
	private static Properties getProperties() {
		if (properties == null) {
			properties = new Properties();
			try {
				FileInputStream MyInputStream = new FileInputStream(getFile());
				properties.load(MyInputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return properties;
	}
	
	public static void put(Object key, Object value){
		getProperties().put(key, value);
		try {
			getProperties().store(new FileOutputStream(getFile()), "Properties for ElusiveSound");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Object get(Object key){
		return getProperties().get(key);
	}
	
	public static final Object KEY_PROJECT = "project";
}
