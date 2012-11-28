package org.elusive.sound.blocs.additive.actions;

import javax.swing.Icon;

import org.elusive.sound.blocs.additive.AdditiveSynth;
import org.elusive.sound.blocs.additive.FrequenceAmplitude;


public class AjouteFrequence extends ActionHistoriqueAdditive {
	
	
	private FrequenceAmplitude fa;
	

	public AjouteFrequence(AdditiveSynth synth, FrequenceAmplitude fa) {
		super(synth);
		this.fa = fa;
	}

	@Override
	public boolean doAction() {
		synth.addFrequenceAmplitude(fa);
		return true;
	}

	@Override
	public void doReverse() {
		synth.removeFrequenceAmplitude(fa);
	}

	@Override
	public String getName() {
		return "Ajoute Frequence";
	}

	@Override
	public String getInfo() {
		return "(f = "+fa.getFrequence()+", a = "+fa.getAmplitude()+")";
	}

	@Override
	public Icon getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

}
