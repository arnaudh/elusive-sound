package org.elusive.sound.blocs.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import org.elusive.sound.blocs.BlocPositionne;

public class BlocPositionnePanel extends JPanel {

	private BlocPositionne bp;
	
	private int hauteurLigne;
	private int echelle;

	public BlocPositionnePanel(BlocPositionne bp) {
		super();
		this.bp = bp;
	}
	
	@Override
	public void repaint() {
		int length = bp.getLength();
		super.repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(bp.getBloc().getOutsideColor());
		g2.fill(this.getBounds());
	}
	
}
