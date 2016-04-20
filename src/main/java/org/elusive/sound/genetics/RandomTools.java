package org.elusive.sound.genetics;

import java.util.Random;

public class RandomTools {


	public static double modify(double x, double strength) {
		double v = new Random().nextGaussian() * 0.3; // 70% between -0.3 and
														// 0.3
		double xx = x * (1 + v * strength);
		return xx;
	}

	public static double modify(int x, double strength, double min, double max) {
		double xx = modify(x, strength);
		if( xx < min ){
			xx = min;
		}
		if( xx > max ){
			xx = max;
		}
		return xx;
	}
}
