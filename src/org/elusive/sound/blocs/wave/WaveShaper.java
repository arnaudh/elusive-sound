package org.elusive.sound.blocs.wave;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.elusive.sound.blocs.BlocGenerateur;
import org.elusive.sound.blocs.frequenceur.Frequencable;
import org.elusive.sound.enveloppes.Enveloppe;
import org.elusive.sound.enveloppes.PointSon;
import org.elusive.sound.flow.Timestamp;
import org.elusive.sound.genetics.Geneticable;
import org.elusive.sound.tempo.Tempo;
import org.elusive.ui.fenetre.ElusivePanel;
import org.elusive.ui.grille.Grid;


public class WaveShaper extends BlocGenerateur implements Frequencable {

	protected Enveloppe enveloppe = new Enveloppe(new ArrayList<PointSon>(), true);
	private double frequence = 440;

	public WaveShaper() {
	}

	public static WaveShaper createSawtooth() {
		ArrayList<PointSon> points = new ArrayList<PointSon>();
		points.add(new PointSon(0, -1));
		points.add(new PointSon(1000, 1));
		WaveShaper ws = new WaveShaper();
		ws.enveloppe = new Enveloppe(points, true);
		return ws;
	}

	@Override
	public float[] generateNextData(int size) {
		float[] data = new float[size];
		
		if( enveloppe.getPoints().isEmpty() ){
			return data;
		}
		
		
		int firstFrame = 0;

		// calcul de la période en frames
		int period = (int) (Grid.FRAMES_PER_SECOND / frequence);
		int length = enveloppe.getLength();
		double ratio = period / (double) length;

		while (firstFrame < size) {
			for (int p = 0; p < enveloppe.getPoints().size() - 1; p++) {
				PointSon p1 = enveloppe.getPoints().get(p);
				PointSon p2 = enveloppe.getPoints().get(p + 1);
				int nbFrames = (int) ((p2.getX() - p1.getX()) * ratio);
				if (nbFrames == 0) {
					continue;
				}
				double debut = p1.getY();
				double pente = (p2.getY() - p1.getY()) / (nbFrames); // c'est
																		// tout
																		// petit,
																		// genre
																		// 10^-5

				int last = Math.min(nbFrames, data.length - firstFrame);

				for (int i = 0; i < last; i++) {
					int dat = firstFrame + i;
					double coeff = (debut + i * pente);
					data[dat] = (float) (coeff);
				}
				firstFrame += last;

			}
		}

		return data;
	}

	@Override
	public void setFrequenceNoUpdate(double freq) {
		frequence = freq;
	}

	@Override
	public double getFrequence() {
		return frequence;
	}
	
	@Override
	public Timestamp getTimestamp() {
		long tmp = super.getTimestamp().getValue();
		long max = Math.max(super.getTimestamp().getValue(), enveloppe.getTimestamp().getValue());
		return new Timestamp(max);
	}

	@Override
	protected ElusivePanel createElusivePanel() {
		ElusivePanel panel = new ElusivePanel("WaveShaper");
		panel.add(enveloppe.getPanel());
		return panel;
	}

	@Override
	public Geneticable mutate(double strength) {
		// TODO Auto-generated method stub
		WaveShaper ws = new WaveShaper();
		ws.enveloppe = enveloppe;
		ws.frequence = frequence;
		return ws;
	}

}
