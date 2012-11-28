package org.elusive.sound.rythm.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;

import org.elusive.sound.rythm.Hit;
import org.elusive.sound.rythm.Rythm;
import org.elusive.ui.grille.Grid;


public class RythmRecorder extends JButton {

	private Rythm rythm = new Rythm();
	private boolean recording = false;
	private long previousHit = -1;
	private RythmRecorderListener listener;

	public RythmRecorder(RythmRecorderListener rrl) {
		super("Record");
		
		listener = rrl;

		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!recording) {
					recording = true;
					RythmRecorder.this.setText("recording...");
				} else {
					recording = false;
					RythmRecorder.this.setText("Record");
					listener.whenRecordFinished(rythm);
					rythm = new Rythm();
					previousHit = -1;
				}
			}
		});
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if( e.getKeyCode() == KeyEvent.VK_A ){
					long difference = System.currentTimeMillis() - previousHit;
					int length = (int) ((difference) / (double) 1000 * Grid.FRAMES_PER_SECOND);
					rythm.get(rythm.size()-1).setLength(length);
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if( e.getKeyCode() == KeyEvent.VK_A ){
					hit();
				}
			}
		});

	}

	private void hit() {
		if (previousHit == -1) {
			previousHit = System.currentTimeMillis();
		}
		long difference = System.currentTimeMillis() - previousHit;
		previousHit += difference;
		int offset = (int) ((difference) / (double) 1000 * Grid.FRAMES_PER_SECOND);
		Hit hit = new Hit(offset);
		rythm.addHit(hit);
		listener.addedHit(hit);
	}



}
