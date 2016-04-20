package org.elusive.util;

public class XmlTestTools {

	
	public static boolean containsProjectPaths(String str){
		if( str.contains("sound.") ){
			return true;
		}
		return false;
	}
}
