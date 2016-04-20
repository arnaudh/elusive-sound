package org.elusive.test;

public class ShortToFloat {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println(Short.MIN_VALUE);
		float max = - Short.MIN_VALUE;
		
		long tip = System.currentTimeMillis();
		
		for(short s = Short.MIN_VALUE; s < Short.MAX_VALUE; s++){
			float f = s/max;
			
			short s2 = (short) (max * f);
			
			if( s != s2 ){
				System.out.println(s+" != "+s2);
			}
		}
		
		long top = System.currentTimeMillis();
		
		System.out.println("time = "+(top - tip));
		
		System.out.println(Short.MAX_VALUE);

	}

}
