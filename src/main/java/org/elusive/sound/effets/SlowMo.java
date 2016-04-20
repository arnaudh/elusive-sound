package org.elusive.sound.effets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SlowMo extends Effet {
	
	private static final int SLIDER_COUNT = 100;
	
	
	private double slomo = 0.5;

	@Override
	public float[] applyTo(float[] data) {
		//TODO
		float[] ret = new float[data.length];
		for(int i = 0; i < data.length; i++){
			ret[i] = (float) (slomo * data[i]);
		}
		return ret;
	}

	@Override
	protected JPanel createPanel() {
		JPanel panel = new JPanel();
		

		final JLabel label = new JLabel("Slow "+slomo);
		final JSlider slider = new JSlider(1, SLIDER_COUNT, (int) (slomo*SLIDER_COUNT));
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				slomo = slider.getValue()/(double)SLIDER_COUNT;
				updated();
				label.setText("Slow "+slomo);
			}
		});
		
		panel.add(label);
		panel.add(slider);
		
		return panel;
	}

}
