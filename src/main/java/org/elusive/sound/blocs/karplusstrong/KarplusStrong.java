package org.elusive.sound.blocs.karplusstrong;

import java.util.ArrayList;
import java.util.List;

import org.elusive.sound.blocs.Bloc;
import org.elusive.sound.blocs.frequenceur.Frequencable;
import org.elusive.sound.blocs.noise.WhiteNoise;
import org.elusive.sound.genetics.Geneticable;
import org.elusive.sound.genetics.RandomTools;
import org.elusive.ui.fenetre.ElusivePanel;


public class KarplusStrong extends Bloc implements Frequencable {


	Bloc generateur = new WhiteNoise();
	private int N = 80000;
	private double frequence = 440;
	

	@Override
	public float[] generateData() {

		// generateur = new WhiteNoise();

		generateur.setSize(0, N);
		float[] dline = generateur.generateData();

		float[] y = new float[N];

		int ptr = 0;
		

		int D = (int) (44100 / (frequence));

		for (int n = 0; n < D; n++) {
			y[n] = dline[n];
		}

		for (int n = D; n < N; n++) {
			D = (int) (44100 / (frequence));
			y[n] = (y[n - D] + y[n - D + 1]) * 0.5f * 0.90f;

			// increment pointer
			ptr++;
			if (ptr >= D) {
				ptr = 0;
			}
		}

		return y;
	}


	@Override
	public double getFrequence() {
		return frequence;
	}

	@Override
	public void setFrequenceNoUpdate(double frequence) {
		this.frequence = frequence;
	}
	
	@Override
	protected ElusivePanel createElusivePanel() {
		ElusivePanel panel = new ElusivePanel("Karplus Strong guitar synth");
		return panel;
	}


	@Override
	public Geneticable mutate(double strength) {
		// TODO Auto-generated method stub
		KarplusStrong ks = new KarplusStrong();
		ks.generateur = generateur;
		ks.frequence = RandomTools.modify(frequence, strength); //TODO : sers à rien (dans un frequenceur)
		ks.N = N;
		return ks;
	}

}
