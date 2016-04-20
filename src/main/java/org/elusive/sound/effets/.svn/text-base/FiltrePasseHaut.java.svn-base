package org.elusive.sound.effets;

import javax.swing.JPanel;

public class FiltrePasseHaut extends Effet {

	private double dt;
	private double RC;
	
	public FiltrePasseHaut(){
		this(0.01, 1);
	}
	
	public FiltrePasseHaut(double dt, double RC){
		this.dt = dt;
		this.RC = RC;
	}
	
	@Override
	public float[] applyTo(float[] data) {
		int n =data.length;
		float[] y = new float[n];
		double alpha = RC / (RC + dt);
		y[0] = data[0];
		for(int i = 1; i < n; i++){
			y[i] = (float) (alpha * (y[i-1] + data[i] - data[i-1]));
			//System.out.println("y["+i+"] = "+y[i]);
		}		
		return y;
	}


	@Override
	public JPanel createPanel() {
		// TODO Auto-generated method stub
		return null;
	}

}
