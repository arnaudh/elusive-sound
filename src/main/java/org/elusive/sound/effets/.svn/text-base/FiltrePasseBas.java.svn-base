package org.elusive.sound.effets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class FiltrePasseBas extends Effet {

	private double dt;
	private double RC;

	public FiltrePasseBas() {
		this(0.01, 1);
	}

	public FiltrePasseBas(double dt, double RC) {
		this.dt = dt;
		this.RC = RC;
	}

	protected JPanel createPanel() {
		JPanel panel = new JPanel();

		JLabel label_dt = new JLabel("dt");
		SpinnerModel sm_dt = new SpinnerNumberModel(dt, 0.0, 10.0, .001);
		final JSpinner spinner_dt = new JSpinner(sm_dt);
		spinner_dt.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				dt = (Double) spinner_dt.getValue();
				updated();
			}
		});

		JLabel label_RC = new JLabel("RC");
		SpinnerModel sm_RC = new SpinnerNumberModel(RC, 0, 100, 1);
		final JSpinner spinner_RC = new JSpinner(sm_RC);
		spinner_RC.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				RC = (Double) spinner_RC.getValue();
				updated();
			}
		});
		panel.add(label_dt);
		panel.add(spinner_dt);
		panel.add(label_RC);
		panel.add(spinner_RC);

		return panel;
	}

	@Override
	public float[] applyTo(float[] data) {
		int n = data.length;
		float[] y = new float[n];
		//
		// double f = 300;
		// double r = 0.12;
		// double c = 1.0 / Math.tan(Math.PI * f / 44100);
		//
		// double a1 = 1.0 / ( 1.0 + r * c + c * c);
		// double a2 = 2* a1;
		// double a3 = a1;
		// double b1 = 2.0 * ( 1.0 - c*c) * a1;
		// double b2 = ( 1.0 - r * c + c * c) * a1;
		//
		// // out(n) = a1 * in + a2 * in(n-1) + a3 * in(n-2) - b1*out(n-1) -
		// b2*out(n-2)
		//
		// for(int i = 2; i < n; i++){
		// y[i] = (float) (a1 * data[i] + a2 * data[i-1] + a3 * data[i-2] - b1 *
		// y[i-1] - b2 * y[i-2]);
		// }

		double alpha = dt / (RC + dt);

		System.out.println("alpha = " + alpha + " (" + dt + " / (" + RC + " + " + dt + ") )");

		y[0] = data[0];
		for (int i = 1; i < n; i++) {
			// System.out.println("y[i-1] = "+y[i-1]+" + "+alpha+" * ("+data[i]+" - "+y[i-1]+")");

			y[i] = (float) (y[i - 1] + alpha * (data[i] - y[i - 1]));
			// System.out.println("y["+i+"] = "+y[i]);
		}
		return y;
	}

}
