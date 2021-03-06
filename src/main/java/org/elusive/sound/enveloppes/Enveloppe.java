package org.elusive.sound.enveloppes;

import java.util.ArrayList;

import javax.swing.JPanel;

import org.elusive.main.historique.History;
import org.elusive.sound.enveloppes.ui.EnveloppePanel2;
import org.elusive.sound.flow.Timestamp;



public class Enveloppe {

	protected ArrayList<PointSon> points;
	protected transient EnveloppePanel2 panel;
	
	protected boolean negativeValuesAccepted;
	

	public Enveloppe(ArrayList<PointSon> pts, boolean acceptsNegativeValues) {
		this.points = pts;
		this.negativeValuesAccepted = acceptsNegativeValues;
	}
	
	public void add(PointSon pt) {
		// il faut l'ajouter au bon endroit
		int i = 0;
		for (i = 0; i < points.size(); i++) {
			if (pt.getX() < points.get(i).getX()) {
				break;
			}
		}
		points.add(i, pt);
	}
	

	public ArrayList<PointSon> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<PointSon> points) {
		this.points = points;
	}

	public int getLength() {
		if( points.isEmpty() ) return 0;
		return points.get(points.size() - 1).getX();
	}
	
	public void applyTo(float[] data, int firstFrame) {

		for (int p = 0; p < points.size() - 1; p++) {
			PointSon p1 = points.get(p);
			PointSon p2 = points.get(p + 1);
			int nbFrames = p2.getX() - p1.getX();
			if( nbFrames == 0){
				continue;
			}
			double debut = p1.getY();
			double pente = (p2.getY() - p1.getY()) / (nbFrames); // c'est tout petit

			int last = Math.min(p2.getX() - p1.getX(), data.length - firstFrame);

			for (int i = 0; i < last; i++) {
				int dat = firstFrame + i;
				double coeff = (debut + i * pente);
				data[dat] = (float) (coeff * data[dat]);
			}
			firstFrame += last;

		}

	}

	/**
	 * 
	 * @param data2
	 * @param firstFrame
	 * @param periode mettre à 0 si on veut éteindre le son après
	 */
	public void applyTo(float[] data2, int firstFrame, int periode) {
		int nbApplications = 1;
		if( periode != 0){
			nbApplications = 1 + (data2.length - firstFrame) / periode;
		}
		int d = 0;
		for (int i = 0; i < nbApplications; i++) {
			applyTo(data2, firstFrame + i * periode);
			int last = Math.min(data2.length, firstFrame + (i+1) * periode);
			for(d = firstFrame + i * periode + this.getLength(); d < last ; d++){
				data2[d] = 0;
			}
		}
		if( periode == 0 ){
			for(; d < data2.length; d++){
				data2[d] = 0;
			}
		}
	}

	//Lazy loading
	public JPanel getPanel() {
		if( panel == null ){
//				panel = new EnveloppePanel(this);
				panel = new EnveloppePanel2(this, new History());
		}
		return panel;
	}



	public static Enveloppe create(int[] frames, float[] amplitudes, boolean acceptsNegativeValues) {
		if (frames.length != amplitudes.length) {
			return null;
		}
		ArrayList<PointSon> pts = new ArrayList<PointSon>();
		for (int i = 0; i < frames.length; i++) {
			pts.add(new PointSon(frames[i], amplitudes[i]));
		}
		return new Enveloppe(pts, acceptsNegativeValues);
	}
	

	public static Enveloppe createEnveloppeADSR() {
		return create(new int[]{0, 1000, 2000, 30000, 31000}, new float[]{0, 1, 0.5f, 0.5f, 0}, false);
	}
	
	public static Enveloppe createFlatEnveloppe(boolean acceptsNegativeValues){
		Enveloppe env = create(new int[]{0, 44100}, new float[]{1, 1}, acceptsNegativeValues);		
		return env;
	}

	public Timestamp getTimestamp() {
		long max = 0;
		for (PointSon ps: points) {
			if( ps.getTimestamp().getValue() > max){
				max = ps.getTimestamp().getValue();
			}
		}
		return new Timestamp(max);
	}

	public boolean isNegativeValuesAccepted() {
		return negativeValuesAccepted;
	}

	public void setNegativeValuesAccepted(boolean negativeValuesAccepted) {
		this.negativeValuesAccepted = negativeValuesAccepted;
	}



}
