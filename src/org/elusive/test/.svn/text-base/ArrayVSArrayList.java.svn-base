package org.elusive.test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.elusive.main.performance.Chrono;

public class ArrayVSArrayList {

	
	public static void main(String[] args) {
		
		int n = 1000000;
		
		double [] a = new double[n];
		List<Double> l = new ArrayList<Double>();
		
		
		for(int i = 0; i < n; i++){
			double d = 10;
			a[i] = d;
			l.add(d);
		}
		
		for(int k = 0; k < 10; k++){
		
			Chrono.tip();
			for (int i = 0; i < n; i++) {
				double d = a[i];
			}
			Chrono.top("Array");
			
			Chrono.tip();
			for (int i = 0; i < n; i++) {
				double d = l.get(i);
			}
			Chrono.top("LISTE");
			
		}
		
	}
}
