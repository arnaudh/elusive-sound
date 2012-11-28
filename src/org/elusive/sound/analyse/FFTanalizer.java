package org.elusive.sound.analyse;

import org.elusive.main.historique.action.AjouteBloc;
import org.elusive.sound.blocs.additive.AdditiveSynth;
import org.elusive.sound.blocs.additive.FrequenceAmplitude;

import edu.emory.mathcs.jtransforms.fft.FloatFFT_1D;

public class FFTanalizer extends Analyser{
	
	public FFTanalizer() {
	}
	
	private float seuil = 100;

	private double[] frequences;
	private double[] amplitudes;

	public void analyse(float[] data) {
		analyseWithoutShowing(data);
		double f0 = -1;
		double aMax = -1;
		for (int i = 0, n = frequences.length; i < n; i++) {
			if( Math.abs(amplitudes[i]) > aMax ){
				aMax = Math.abs(amplitudes[i]);
				f0 = frequences[i];
			}
		}
		AdditiveSynth synth = new AdditiveSynth();
		synth.setFrequences(FrequenceAmplitude.createListFrom(frequences, amplitudes));
		AjouteBloc action = new AjouteBloc(grille, synth);
		grille.getHistorique().execute(action);
//		AdditiveSynthVisualizerPanel fdv = new AdditiveSynthVisualizerPanel(synth, new History());
//		fdv.setMinimumSize(new Dimension(400, 300));
//		JFrame frame = new JFrame("FFT Results");
//		frame.setContentPane(fdv);
//		frame.pack();
//		frame.setVisible(true);
		
		
//		FrequenceDataVisualizer.createSimpleFrameVisualiser("Viz", frequences, amplitudes);				
	}

	public void analyseWithoutShowing(float[] data) {
		int size = data.length * 2;
		
		
		FloatFFT_1D fft = new FloatFFT_1D(data.length);
		
		float[] result = new float[size];
		for (int i = 0; i < data.length; i++) {
			result[i] = data[i];
		}
		fft.realForward(result);
		

		float[] realResult = new float[result.length/2];
		float[] imaginaryResult = new float[result.length/2];
		for(int i = 0; i < realResult.length; i++ ){
			realResult[i] = result[i*2+1];
			imaginaryResult[i] = result[2*i];
		}
		
		DataConverter conv = new DataConverter();
		conv.convertDataToFreqAmpl(realResult);
		frequences = conv.getFrequences();
		amplitudes = conv.getAmplitudes();
	}

	private void seuille(float[] data){
		for (int i = 0, l = data.length; i < l; i++) {
			if( Math.abs(data[i]) < seuil ){
				data[i] = 0;
			}
		}
	}

	public double getDominantFrequency() {
		double fMax = -1;
		double amplitudeMax = 0;
		for (int i = 0, l = frequences.length; i < l; i++) {
			if( Math.abs(amplitudes[i]) > amplitudeMax ){
				fMax = frequences[i];
				amplitudeMax = Math.abs(amplitudes[i]);
			}
		}
		System.out.println("dominant frequency = "+fMax);
		return fMax;
	}


}
