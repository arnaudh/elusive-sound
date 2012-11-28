package org.elusive.sound.genetics;

import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GeneticableTest implements Geneticable {

	private double x;
	private JLabel label;

	public GeneticableTest(int xx) {
		x = xx;
	}

	@Override
	public Geneticable mutate(double strength) {
		double v = new Random().nextBoolean() ? 1 : -1;
		double f = (1 + v * new Random().nextDouble() * strength * 0.5);
		int xx = (int) (x * f + 0.5);
		return new GeneticableTest(xx);
	}

	JPanel panel;

	@Override
	public JPanel getPreviewPanel() {
		if (panel == null) {
			panel = new JPanel();
			label = new JLabel("x = " + x);
			panel.add(label);
		}
		return panel;
	}

	@Override
	public Geneticable combineWith(Geneticable gen) {
		// TODO Auto-generated method stub
		return this;
	}

	public String toString() {
		return "GenTest{" + x + "}";
	}

}
