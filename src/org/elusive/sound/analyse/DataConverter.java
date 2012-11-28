package org.elusive.sound.analyse;

import java.util.ArrayList;
import java.util.List;

public class DataConverter {

	private double[] frequences;
	private double[] amplitudes;
	
	private float[] data;
	
	public DataConverter() {
	}
	
	public void convertDataToFreqAmpl(float[] data){
		List<Double> frequencesArray = new ArrayList<Double>();
		List<Double> amplitudesArray = new ArrayList<Double>();
		for (int i = 0, l = data.length; i < l; i++) {
			if( data[i] != 0 ){
				frequencesArray.add( (double) (i * 44100d / l));
				amplitudesArray.add((double) data[i]);
			}
		}
		int nbFrequences = frequencesArray.size();
		frequences = new double[nbFrequences];
		amplitudes = new double[nbFrequences];
		for (int i = 0; i < nbFrequences; i++) {
			frequences[i] = frequencesArray.get(i);
			amplitudes[i] = amplitudesArray.get(i);
		}
	}

	public double[] getFrequences() {
		return frequences;
	}

	public void setFrequences(double[] frequences) {
		this.frequences = frequences;
	}

	public double[] getAmplitudes() {
		return amplitudes;
	}

	public void setAmplitudes(double[] amplitudes) {
		this.amplitudes = amplitudes;
	}

	public float[] getData() {
		return data;
	}

	public void setData(float[] data) {
		this.data = data;
	}
}
