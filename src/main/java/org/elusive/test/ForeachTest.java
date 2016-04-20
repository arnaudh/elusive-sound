package org.elusive.test;

import java.util.ArrayList;

import org.elusive.sound.effets.Effet;

public class ForeachTest {

	
	
	public static void main(String[] args) {
		ArrayList<Effet> effs = null;
		
		for (Effet effet : effs) { //NPE
			
			System.out.println("tasa");
		}
	}
}
