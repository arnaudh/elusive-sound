package org.elusive.sound.blocs;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.elusive.sound.genetics.Geneticable;
import org.elusive.ui.fenetre.ElusivePanel;



public class BlocTotal extends Bloc {

	private transient float[] data; //cache only, not to be XML-serialised
	
	public BlocTotal(float[] data){
		this.data = data;
	}
	
	public float[] generateData() {
		return data;
	}

	@Override
	protected ElusivePanel createElusivePanel() {
		ElusivePanel panel = new ElusivePanel("Master Controls");
		return panel;
	}

	@Override
	public Geneticable mutate(double strength) {
		return this;
	}



}
