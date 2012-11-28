package org.elusive.test;

public class GaussianTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int index = 1000000;
		int distance = 50;
		int sigma = distance / 2;
		for (int i = -2*sigma; i < 2*sigma; i++) {
			double d = 1-Math.exp(-i*i/(double)(sigma*sigma));
			System.out.println("d["+(index+i)+"] = "+d);
		}

	}

}
