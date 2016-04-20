package org.elusive.sound.blocs.additive.actions;

import java.util.Iterator;
import java.util.List;

import javax.swing.Icon;

import org.elusive.sound.blocs.additive.AdditiveSynth;
import org.elusive.sound.blocs.additive.FrequenceAmplitude;


public class SupprimeFrequence extends ActionHistoriqueAdditive {

	private FrequenceAmplitude frequenceAmplitude = null;
	
	public SupprimeFrequence(AdditiveSynth synth, double frequence) {
		super(synth);
		for( FrequenceAmplitude fa : synth.getFrequences() ){
			if( fa.getFrequence() == frequence ){
				frequenceAmplitude = fa;
			}
		}	
	}
	
	public SupprimeFrequence(AdditiveSynth synth, FrequenceAmplitude fa) {
		super(synth);
		frequenceAmplitude = fa;
	}

	@Override
	public boolean doAction() {
		boolean b = synth.getFrequences().remove(frequenceAmplitude);
		return b;
	}

	@Override
	public void doReverse() {
		synth.getFrequences().add(frequenceAmplitude);
	}

	@Override
	public Icon getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
