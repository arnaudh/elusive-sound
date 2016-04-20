package org.elusive.sound.effets;

import javax.swing.JPanel;

public class Inverse extends Effet {
	
	public float[] applyTo(float[] data) {
		int length = data.length;
		int last = length - 1;
		float[] ret = new float[length];
		for(int n = 0; n < length; n++ ){
			ret[n] = data[last - n];
		}
		return ret;
	}

	@Override
	protected JPanel createPanel() {
		return new JPanel();
	}

}
