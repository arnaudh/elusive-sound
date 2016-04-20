package org.elusive.sound.blocs;

import java.awt.Color;

import javax.swing.JPanel;

import org.elusive.sound.normalise.Normalise;



public abstract class BlocGenerateur extends Bloc {

	@Override
	public float[] generateData() {
		float[] data = generateDataNoNormalise();
		Normalise.normalise(data);
		return data;
	}
	
	@Override
	public float[] generateDataNoNormalise() {
		float[] data = generateNextData(end-start);
		return data;
	}
	
	public abstract float[] generateNextData(int size);

}
