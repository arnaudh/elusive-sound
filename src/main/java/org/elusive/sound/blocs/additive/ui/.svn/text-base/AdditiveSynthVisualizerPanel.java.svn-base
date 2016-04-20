package org.elusive.sound.blocs.additive.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

import org.elusive.main.historique.History;
import org.elusive.sound.blocs.additive.AdditiveSynth;
import org.elusive.sound.blocs.additive.FrequenceAmplitude;
import org.elusive.sound.blocs.additive.actions.AjouteFrequence;
import org.elusive.ui.panels.HorizontalViewPanel;

public class AdditiveSynthVisualizerPanel extends HorizontalViewPanel {

	private AdditiveSynth synth;
	private History history;

	// changing parameters
	private int pixelHover = -1; // pixels
	
	// listeners
	private MouseAdapter mouseAdapterAddFrequency;
	
	// static
	private static final Color BACKGROUND_COLOR = new Color(80, 30, 80);
	private static final Color AXES_COLOR = Color.WHITE;
	private static final Color F0_BIN_COLOR = Color.GREEN;
	private static final Color FREQUENCY_BINS_COLOR = Color.YELLOW;
	private static final Color FREQUENCY_BINS_SELECTED_COLOR = Color.CYAN;
	private static final Color FREQUENCY_BIN_OVERRIDE_COLOR = Color.RED;
	
	public AdditiveSynthVisualizerPanel(AdditiveSynth synth_, History history_) {
		super();
		this.synth = synth_;
		this.history = history_;
		this.backgroundColor = BACKGROUND_COLOR;
		this.axesColor = AXES_COLOR;
		this.horizontalUnit = "Hz";
		this.verticalUnit = "A";
		this.paintHorizontalAxis = true;
		this.paintVerticalAxis = false;
		this.minHorizontalOnset = 0;
		this.maxHorizontalOffset = 20000;
		this.horizontalOnset = minHorizontalOnset;
		this.minHorizontalUnitsPerPixel = 0.1; //Hz
		this.horizontalUnitsPerPixel = 1;
		this.verticalUnitOnset = 0; //TODO change?
		this.verticalUnitsPerPixel = 1;
		this.mouseAdapterAddFrequency = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					double f = pix2horizontalUnit(e.getX());
					double a = pix2verticalUnit(e.getY());
					AjouteFrequence af = new AjouteFrequence(synth, new FrequenceAmplitude(f, a));
					history.execute(af); 
				}
				repaint();
			}
		}; 
		this.addMouseListener(mouseAdapterAddFrequency);
		
		this.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				pixelHover = e.getX();
				repaint();
			}
			@Override
			public void mouseDragged(MouseEvent e) {
			}
		});
	}
	
	

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		// Adjust verticalUnitsPerPixel
		double maxAmplitude = 0;
		for( FrequenceAmplitude fa : synth.getFrequences()){
			if( fa.getAmplitude() > maxAmplitude ){
				maxAmplitude = fa.getAmplitude();
			}
		}
		this.verticalUnitsPerPixel = 1.2 * (maxAmplitude) / getHeight();
		if( verticalUnitsPerPixel == 0 ){
			verticalUnitsPerPixel = 1;
		}

		if (synth.getF0() > 0) {
			// F0
			g2.setColor(F0_BIN_COLOR);
			int pix = horizontalUnit2pix(synth.getF0());

			// les multiples, si on est dessus
			for (int i = 2; i < 100; i++) {
				int pixMultiple = horizontalUnit2pix(synth.getF0() * i);
				if (pixMultiple == pixelHover) {
					g2.drawLine(pixelHover, 0, pixelHover, getHeight());
					g2.drawString(i + " f0", pixelHover + 4, 12);
					break;
				}
				if (pixMultiple > getWidth()) {
					break;
				}
			}
		}

		// other frequency bins
		int n = synth.getFrequences().size();
		int iMax = -1;
		double amplitudeMax = -1;
		for (int i = 0; i < n; i++) {
			FrequenceAmplitude fa = synth.getFrequences().get(i);
			int x = horizontalUnit2pix(fa.getFrequence());
			int y = verticalUnit2pix(fa.getAmplitude());
			if( x == pixelHover ){
				g2.setColor(FREQUENCY_BIN_OVERRIDE_COLOR);
				if( Math.abs(fa.getAmplitude()) > amplitudeMax ){
					amplitudeMax = Math.abs(fa.getAmplitude());
					iMax = i;
				}
			}else{
					if( fa.isSelectionne() ){
					g2.setColor(FREQUENCY_BINS_SELECTED_COLOR);
				}else{
					g2.setColor(FREQUENCY_BINS_COLOR);
				}
			}
			g2.drawLine(x, verticalUnit2pix(0), x, y);
			
		}
		
		//Frequence / Amplitude survolée
		if( iMax != -1 ){
			String str = "f"+synth.getFrequences().get(iMax).getFrequence() + " / amplitude = " + synth.getFrequences().get(iMax).getAmplitude();
			g2.setColor(AXES_COLOR);
			g2.drawString(str, 5, 15);
		}
	}
	
	
}
