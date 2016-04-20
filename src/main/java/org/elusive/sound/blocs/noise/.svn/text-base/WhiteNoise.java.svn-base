package org.elusive.sound.blocs.noise;

import java.util.ArrayList;
import java.util.List;

import org.elusive.sound.blocs.BlocGenerateur;
import org.elusive.sound.genetics.Geneticable;
import org.elusive.ui.fenetre.ElusivePanel;


public class WhiteNoise extends BlocGenerateur {

	@Override
	public float[] generateNextData(int size) {
		float[] data = new float[size];
		for(int i = 0; i < size; i++){
			data[i] = (float) (2*Math.random() - 1);
		}
		return data;
	}

	@Override
	protected ElusivePanel createElusivePanel() {
		ElusivePanel panel = new ElusivePanel("White noise");
		return panel;
	}

	@Override
	public Geneticable mutate(double strength) {
		return new WhiteNoise();
	}

}
