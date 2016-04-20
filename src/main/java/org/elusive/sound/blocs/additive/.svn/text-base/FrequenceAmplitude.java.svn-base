package org.elusive.sound.blocs.additive;

import java.util.ArrayList;
import java.util.List;

public class FrequenceAmplitude {
	
	private double frequence;
//	private double amplitude;

	private List<TimeAmplitude> amplitudes = new ArrayList<TimeAmplitude>();

	transient private boolean selectionne;
	transient private double phase;
	transient private double angle;
	
	

	public FrequenceAmplitude(double frequence, double amplitude) {
		this.frequence = frequence;
//		this.amplitude = amplitude;
		amplitudes.add(new TimeAmplitude(0, amplitude));
	}
	
	
	public FrequenceAmplitude(double frequence, List<TimeAmplitude> amplitudes) {
		this.frequence = frequence;
		this.amplitudes = amplitudes;
	}


	public double getFrequence() {
		return frequence;
	}
	public void setFrequence(double frequence) {
		this.frequence = frequence;
	}
	
	public double getAmplitude() {
//		return amplitude;
		return amplitudes.get(0).getAmplitude();
	}
	
//	public void setAmplitude(double amplitude){
//		this.amplitude = amplitude;
//	}
	
	public boolean isSelectionne() {
		return selectionne;
	}
	public void setSelectionne(boolean selectionne) {
		this.selectionne = selectionne;
	}

	public double getPhase() {
		return phase;
	}
	public void setPhase(double phase) {
		this.phase = phase;
	}

	public double getAngle() {
		return angle;
	}
	public void setAngle(double angle) {
		this.angle = angle;
	}

	
	public static List<FrequenceAmplitude> cloneList( List<FrequenceAmplitude> frequences ){
		List<FrequenceAmplitude> ret = new ArrayList<FrequenceAmplitude>();
		for (FrequenceAmplitude fa : frequences) {
			List<TimeAmplitude> amplitudes = new ArrayList<TimeAmplitude>();
			for( TimeAmplitude ta : fa.amplitudes ){
				amplitudes.add(new TimeAmplitude(ta.getT(), ta.getAmplitude()));
			}
			FrequenceAmplitude newFa = new FrequenceAmplitude(fa.frequence, amplitudes);
//			FrequenceAmplitude newFa = new FrequenceAmplitude(fa.frequence, fa.amplitude);
			ret.add(newFa);
		}
		return ret;
	}
	
	public static List<FrequenceAmplitude> createListFrom(double[] frequences, double[] amplitudes){
		List<FrequenceAmplitude> ret = new ArrayList<FrequenceAmplitude>();
		int n = Math.min(frequences.length, amplitudes.length);
		for (int i = 0; i < n; i++) {
			ret.add(new FrequenceAmplitude(frequences[i], amplitudes[i]));
		}
		return ret;
	}



	public double getAmplitude(int t) {
//		boolean print = false;
//		if( Math.random() < 0.0005 ){
//			print = true;
//			System.out.println("FrequenceAmplitude.getAmplitude("+t+") [last:"+amplitudes.get(amplitudes.size()-1).getT()+"]");
//		}
		int l = amplitudes.size();
		int i = l-1;
		for(; i>0; i--){
			if( t >= amplitudes.get(i).getT() ){
				break;
			}
		}
		if( i == l-1 ){
			return amplitudes.get(l-1).getAmplitude(); //last amplitude
		}
		int tStart = amplitudes.get(i).getT();
		int tEnd = amplitudes.get(i+1).getT();
		
		double aStart = amplitudes.get(i).getAmplitude();
		
		return aStart + (amplitudes.get(i+1).getAmplitude() - aStart) * ( (t - tStart) / (double)(tEnd - tStart) );
		
	}


	public List<TimeAmplitude> getAmplitudes() {
		return amplitudes;
	}


	public void setAmplitudes(List<TimeAmplitude> amplitudes) {
		this.amplitudes = amplitudes;
	}
	
	
	private int indice = 0;
	public int getIndice() {
		return indice;
	}
	public void setIndice(int indice) {
		this.indice = indice;
	}


	public void addAmplitude(double amplitude, int t) {
		// TODO Auto-generated method stub
		for(int i = amplitudes.size()-1; i >= 0; i--){
			TimeAmplitude ta = amplitudes.get(i);
			if( t == ta.getT()){
				ta.setAmplitude(amplitude);
				return;
			}
			else if( t > ta.getT() ){
				amplitudes.add(i+1, new TimeAmplitude(t, amplitude));
				return;
			}
		}
		amplitudes.add(0, new TimeAmplitude(t, amplitude));
	}
	

}
