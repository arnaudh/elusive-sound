package org.elusive.ui.tools;

public class Tools {

	public static void pause(int ms){
		pause(null, ms);
	}
	public static void pause(String str){
		pause(str, 1000);
	}
	
	public static void pause(String str, int ms){
		if( str != null ) System.out.println(str);
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
