package org.elusive.sound.normalise;

public abstract class Normalise {

	public static void normalise(float[] data){
		normalise(data, 0, data.length);
	}

	public static void normalise(float[] data, int debutLoad, int finLoad) {

		float max = 0;
		for (int i = debutLoad; i < finLoad; i++) {
			if( Math.abs(data[i]) > max ){
				max = Math.abs(data[i]);
			}
		}
		if( max <= 1 ){
			return;
		}		
//		System.out.println("Normalized (max was "+max+" in i="+iMax+")");
		for (int i = debutLoad; i < finLoad; i++) {
			data[i] /= max;
		}	
	}
}
