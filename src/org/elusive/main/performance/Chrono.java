package org.elusive.main.performance;

public class Chrono {

	static long dernierTemps;
	
	public Chrono(){
		
	}
	
	public static void tip(){
		dernierTemps = System.currentTimeMillis();
//		System.out.println(" == tip ==");
	}
	
	public static void top(){
		top("");
	}
	
	public static void top(String str){
		long nouveauTemps = System.currentTimeMillis();
		double duree = (nouveauTemps - dernierTemps)/(double)1000;
		System.out.println(" == top("+str+") == "+duree+" s");
		dernierTemps = nouveauTemps;
	}
}
