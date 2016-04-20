package org.elusive.sound.blocs.additive;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.elusive.sound.analyse.DataConverter;
import org.elusive.sound.blocs.BlocGenerateur;
import org.elusive.sound.blocs.additive.ui.AdditiveSynthPanel;
import org.elusive.sound.blocs.frequenceur.Frequencable;
import org.elusive.sound.genetics.Geneticable;
import org.elusive.sound.genetics.RandomTools;
import org.elusive.ui.fenetre.ElusivePanel;

public class AdditiveSynth extends BlocGenerateur implements Frequencable {

	private List<FrequenceAmplitude> frequences = new ArrayList<FrequenceAmplitude>();
	

	public AdditiveSynth() {
	}

	@Override
	public float[] generateNextData(int size) {
		float[] data = new float[size];

		for (FrequenceAmplitude fa : frequences) {
			fa.setPhase(fa.getAngle());
		}
		
		int t = 0;
		double[] amplitudes = new double[frequences.size()];
		for (; t < size; t++) {

			//Il faut que la somme des amplitudes soit constante
			double somme = 0;
			int n = frequences.size();
			for (int i = 0; i < n; i ++) {
				amplitudes[i] = frequences.get(i).getAmplitude(t);
//				amplitudes[i] = frequences.get(i).getAmplitude();
				somme += amplitudes[i];
			}
			for (int i = 0; i < n; i ++) {
//				data[t] += calculeData(t, frequences.get(i)) * amplitudes[i] / somme;
				data[t] += calculeData(t, frequences.get(i)) * amplitudes[i];
			}
		}
		// Une fois en plus pour le fun :) (afin d'avoir les bonnes phases
		for (FrequenceAmplitude fa : frequences) {
			calculeData(t, fa);
		}
		return data;
	}
	

	private float calculeData(int t, FrequenceAmplitude fa) {
		double angle = 2 * Math.PI * (fa.getFrequence()) / 44100 * t + fa.getPhase();
		fa.setAngle(angle);
//		double amplitude = fa.getAmplitude(t);
		return (float) ( Math.sin(angle));
	}


	@Override
	protected ElusivePanel createElusivePanel() {
		AdditiveSynthPanel panel = new AdditiveSynthPanel(this);
		return panel;
	}


	private void setFrequencesAmplitudes(double[] frequences2, double[] amplitudes) {
		frequences = new ArrayList<FrequenceAmplitude>();
		int n = Math.min(frequences2.length, amplitudes.length);
		
		for (int i = 0; i < n; i++) {
			FrequenceAmplitude fa = new FrequenceAmplitude(frequences2[i], amplitudes[i]);
			frequences.add(fa);
		}
	}

	public static AdditiveSynth createFromFFT(float[] real) {
		DataConverter conv = new DataConverter();
		conv.convertDataToFreqAmpl(real);
		AdditiveSynth add = new AdditiveSynth();
		add.setFrequencesAmplitudes(conv.getFrequences(), conv.getAmplitudes());
		return add;
	}


	@Override
	public void setFrequenceNoUpdate(double frequence) {
		// il faut tout décaler proportionnellement
		double ratio = frequences.isEmpty() ? 0 : frequence / frequences.get(0).getFrequence();
		for (FrequenceAmplitude fa : frequences) {
			fa.setFrequence(fa.getFrequence()*ratio);
		}
	}

	@Override
	public double getFrequence() {
		return getF0();
	}

	/* (non-Javadoc)
	 * @see org.elusive.sound.additive.HasFrequenceAmplitudes#getFrequences()
	 */
	public List<FrequenceAmplitude> getFrequences() {
		return frequences;
	}

	/* (non-Javadoc)
	 * @see org.elusive.sound.additive.HasFrequenceAmplitudes#setFrequences(List<FrequenceAmplitude>)
	 */
	public void setFrequences(List<FrequenceAmplitude> frequences) {
		this.frequences = frequences;
	}

	public double getF0() {
		return frequences.isEmpty() ? 0 : frequences.get(0).getFrequence();
	}

	public void addFrequenceAmplitude(FrequenceAmplitude fa) {
		frequences.add(fa);
		update();
	}

	
	public void removeFrequenceAmplitude(FrequenceAmplitude fa) {
		frequences.remove(fa);
		update();
	}
	
	public void reinitPhases(){
		for(FrequenceAmplitude fa : frequences){
			fa.setAngle(0);
			fa.setPhase(0);
		}
	}
	

	@Override
	public Geneticable mutate(double strength) {
		AdditiveSynth mutatedSynth = new AdditiveSynth();
		Random generator = new Random();
		
		if( generator.nextDouble() < 0.5 * strength ){
			double fre = mutatedSynth.getF0();
			if( fre == 0 ){
				fre = 200;
			}
			fre = RandomTools.modify(fre, strength);
			double amp = RandomTools.modify(100, strength);
			mutatedSynth.addFrequenceAmplitude(new FrequenceAmplitude(fre, amp));
		}
		for( int i = 0, n = frequences.size(); i < n; i++){
			FrequenceAmplitude fa = frequences.get(i);
			double fre = fa.getFrequence();
			double amp = fa.getAmplitude();
			if( generator.nextBoolean() ){
				if( i != 0 )	fre = RandomTools.modify(fre, strength);
			}else{
				amp = RandomTools.modify(amp, strength);
			}
			mutatedSynth.addFrequenceAmplitude(new FrequenceAmplitude(fre, amp));
		}
		return mutatedSynth;
	}

	public void addFrequenceAmplitudeTime(double frequence, double amplitude, int t) {
		// TODO Auto-generated method stub
		System.out.println("AdditiveSynth.addFrequenceAmplitudeTime("+frequence+", "+amplitude+", "+t+")");
		for( FrequenceAmplitude fa : frequences ){
			if( areEqualFrequencies( fa.getFrequence(), frequence) ){
				fa.addAmplitude(amplitude, t);
				System.out.println(" - frequence existante : ");
				return;
			}
		}
		List<TimeAmplitude> amplitudes = new ArrayList<TimeAmplitude>();
		amplitudes.add(new TimeAmplitude(t, amplitude));
		FrequenceAmplitude fa = new FrequenceAmplitude(frequence, amplitudes);
		frequences.add(fa);
	}
	
	public String toDetailedString(){
		StringBuilder sb = new StringBuilder("Additive Synth\n");
		
		for(FrequenceAmplitude fa : frequences){
			sb.append("FA[ "+fa.getFrequence()+"Hz; amplitude=[");
			for(TimeAmplitude ta : fa.getAmplitudes()){
				sb.append(ta.getT()+":"+ta.getAmplitude()+", ");
			}
			sb.append("] ]\n");
		}
		
		return sb.toString();
	}
	

	private static final double EPSILONE = 0.01; //2 fréquences qui ne sont séparées que de epsilone sont considérées égales
	public static boolean areEqualFrequencies(double f1, double f2){
		return Math.abs(f2-f1) < EPSILONE;
	}
	

}
