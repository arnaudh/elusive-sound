package org.elusive.test;

import org.elusive.sound.blocs.Bloc;

public class ReflectiviteTest {
	
	public static void main(String[] args) {
		 new ReflectiviteTest();
	}
	public ReflectiviteTest() {
		Class<?>[] classes = Bloc.class.getClasses();
		
		System.out.println("getClasses() : ");
		for(Class c : classes){
			System.out.println(c);
		}
		
		classes = Bloc.class.getDeclaredClasses();

		System.out.println("getClasses() : ");
		for(Class c : classes){
			System.out.println(c);
		}
	}
}
