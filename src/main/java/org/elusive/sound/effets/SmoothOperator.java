package org.elusive.sound.effets;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SmoothOperator extends Effet {

	private double threshold = 0.3;
	private int size = 500;
	
	@Override
	public float[] applyTo(float[] data) {
		//début
		if( Math.abs(data[0]) > threshold ){
			smoothGaussian(data, 0);
		}
		//fin
		if( Math.abs(data[data.length-1]) > threshold ){
			smoothGaussian(data, data.length-1);
		}
		//milieu
		for(int i = 1, l=data.length ;i < l; i++){
			if( Math.abs(data[i] - data[i-1]) > threshold ){
				smoothGaussian(data, i);
			}
		}
		return data;
	}

	private float[] smoothGaussian(float[] data, int index){
		int filterSize = this.size;
		int start = index - filterSize/2;
		int end = index + filterSize/2;
		if( start < 0 ) start = 0;
		if( end > data.length ) end = data.length;
		
		start -= index;
		end -= index;
		filterSize = end - start;
		int sigma = filterSize / 4;
		if( sigma == 0 ){
			throw new ArithmeticException("le sigma est nul (size="+size+")");
		}
		for (int i = start; i < end; i++) {
			double d = 1-Math.exp(-i*i/(double)(sigma*sigma));
			data[index+i] *= d;
		}
		
		return data;
	}
	

	@Override
	protected JPanel createPanel() {
		JPanel panel = new JPanel();


		SpinnerModel sm_threshold = new SpinnerNumberModel(threshold, 0, 1, 0.01);
		final JSpinner spinner_threshold = new JSpinner(sm_threshold);	
		spinner_threshold.addChangeListener(new ChangeListener() {			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				threshold = (Double) spinner_threshold.getValue();
				updated();
			}
		});
		spinner_threshold.getEditor().setPreferredSize(new Dimension(23, 14));
		
		JLabel label_threshold = new JLabel("Seuil");

		SpinnerModel sm = new SpinnerNumberModel(size, 0, 10000, 10);
		final JSpinner spinner_size = new JSpinner(sm);	
		spinner_size.addChangeListener(new ChangeListener() {			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				size = (Integer) spinner_size.getValue();
				updated();
			}
		});

		JLabel label_size = new JLabel("Taille");

		panel.add(label_threshold);
		panel.add(spinner_threshold);
		panel.add(label_size);
		panel.add(spinner_size);
		
		return panel;
	}

}
