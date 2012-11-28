package org.elusive.ui.fenetre;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.elusive.main.tools.IOtools;
import org.elusive.sound.blocs.BlocFichier;
import org.elusive.sound.record.MicrophoneRecorder;
import org.elusive.sound.record.MicrophoneRecorderListener;
import org.elusive.ui.action.MyAction;
import org.elusive.ui.config.Colors;
import org.elusive.ui.grille.Grid;
import org.elusive.ui.resources.Icons;
import org.elusive.ui.volume.VolumeController;

public class Controls extends JPanel {

	private Fenetre fenetre;
	private MicrophoneRecorder mic;
	private VolumeController volumeController;
	
	// actions
	private MyAction playAction;
	private MyAction pauseAction;
	private MyAction rewindAction;
	private MyAction recordAction; //TODO better
	private MyAction stopRecordAction; //TODO better
	private MyAction zoomInAction;
	private MyAction zoomOutAction;
	
	private float volume = 0.5f;

	// UI
	private JSlider echelle = null;
	private JButton playButton;
	private JButton recordButton;

	// static
	private final static int[] SCALE_FRAME_CORRESPONDENCE = { -20, -18, -16, -14, -12, -10, -9, -8, -7, -6, -5, -4, -3, -2, -1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 14, 16, 18, 20, 24, 28, 32, 36, 40, 50, 60, 80, 100, 120, 140, 160, 180, 200, 250, 300, 350, 400, 500, 600, 800, 1000, 1200, 1500, 2000, 2500, 3000, 3500, 4000, 5000 };
	private final static int SCALE_INITIAL_VALUE = 40;
	private int framesPerPixel = SCALE_FRAME_CORRESPONDENCE[SCALE_INITIAL_VALUE];
	private final static float VOLUME_MAX = 1;


	public Controls(Fenetre fenetre_) {
		this.fenetre = fenetre_;

		mic = new MicrophoneRecorder(new MicrophoneRecorderListener() {
			@Override
			public void startedRecording() {
				stopRecordAction.attachToButton(recordButton);
			}
			@Override
			public void soundRecorded(File file) {
				fenetre.grille.newBloc(new BlocFichier(file));
			}
		});
		volumeController = new VolumeController(VOLUME_MAX) {
			@Override
			public void volumeChanged(float vol) {
				volume = vol;
			}
			
			@Override
			public float getVolume() {
				return volume;
			}
		};
		
		this.setBackground(Colors.MAIN_BACKGROUND_1);

		initActions();
		initPanel();

		controls = this;
	}
	
	// Static access
	private static Controls controls;
	public static Controls getControls(){
		return controls;
	}

	private void initActions() {
		playAction = new MyAction("Play", null, "Play", Icons.PLAY_ICON) {
			@Override
			public void actionPerformed(ActionEvent e) {
				play();
			}
		};
		pauseAction = new MyAction("Pause", null, "Pause", Icons.PAUSE_ICON) {
			@Override
			public void actionPerformed(ActionEvent e) {
				pause();
			}
		};
		rewindAction = new MyAction("Rewind", null, "Rewind to start", Icons.REWIND_ICON) {
			@Override
			public void actionPerformed(ActionEvent e) {
				rewind();
			}
		};
		recordAction = new MyAction("Record", null, "Record from microphone", Icons.RECORD_ICON) {
			@Override
			public void actionPerformed(ActionEvent e) {
				record();
			}
		};
		stopRecordAction = new MyAction("Stop recording", null, "Stop recording from the microphone", Icons.RECORD_ICON_ON) {
			@Override
			public void actionPerformed(ActionEvent e) {
				stopRecord();
			}
		};
		zoomInAction = new MyAction("Zoom in", null, "Zoom in", Icons.ZOOM_IN_ICON) {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoomIn();
			}
		};
		zoomOutAction = new MyAction("Zoom out", null, "Zoom out", Icons.ZOOM_OUT_ICON) {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoomOut();
			}
		};
	}


	private void record() {
		try {
			File file = IOtools.createTempFile("record", ".wav", IOtools.getTemporaryFolder());
			mic.recordOnto(file);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private void stopRecord(){
		mic.stopRecord();
		recordAction.attachToButton(recordButton);
	}

	private void zoomIn() {
		int val = echelle.getValue();
		if (val != echelle.getMinimum())
			echelle.setValue(val - 1);
		updateZoomEnabled();
	}

	private void zoomOut() {
		int val = echelle.getValue();
		if (val != echelle.getMaximum())
			echelle.setValue(val + 1);
		updateZoomEnabled();
	}

	private void updateZoomEnabled() {
		zoomInAction.setEnabled(echelle.getValue() > echelle.getMinimum());
		zoomOutAction.setEnabled(echelle.getValue() < echelle.getMaximum());
	}

	private void initPanel() {
		echelle = new JSlider(JSlider.HORIZONTAL, 0, SCALE_FRAME_CORRESPONDENCE.length - 1, SCALE_INITIAL_VALUE);
		echelle.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int oldPos = fenetre.grille.getPanelGrille().offset2pixel(fenetre.grille.curseur);
				framesPerPixel = SCALE_FRAME_CORRESPONDENCE[echelle.getValue()];
				fenetre.grille.updateScrollH(oldPos);
			}
		});

		
		playButton = playAction.createSimpleIconButton();
		recordButton = recordAction.createSimpleIconButton();
		// Layout
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridx = 5;
		this.add(volumeController.getVolumeButton(), gbc);
		gbc.gridx ++;
		this.add(volumeController.createVolumeSlider(JSlider.HORIZONTAL), gbc);

		gbc.insets = new Insets(0, 40, 0, 0);
		gbc.gridx ++;
		this.add(rewindAction.createSimpleIconButton(), gbc);
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.gridx ++;
		this.add(playButton, gbc);
		gbc.gridx ++;
		gbc.insets = new Insets(0, 0, 0, 40);
		this.add(recordButton, gbc);

		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.gridx ++;
		this.add(zoomInAction.createSimpleIconButton(), gbc);
		gbc.gridx ++;
		this.add(echelle, gbc);
		gbc.gridx ++;
		this.add(zoomOutAction.createSimpleIconButton(), gbc);
	}
	
	public int getFramesPerPixel() {
		return framesPerPixel;
	}

	public void play() {
		new Thread(fenetre.grillePlayer).start();
		pauseAction.attachToButton(playButton);
	}

	public void pause() {
		fenetre.grillePlayer.pause();
		playAction.attachToButton(playButton);
	}
	
	public void rewind(){
		fenetre.grille.curseur = 0;
		fenetre.grille.repaint();
	}


	public void changeZoom(int n) {
		if (n > 0) {
			for (int i = 0; i < n; i++) {
				zoomIn();
			}
		} else if (n < 0) {
			for (int i = 0; i > n; i--) {
				zoomOut();
			}
		}

	}
	
	public void setVolume(float vol){
		volumeController.setVolume(vol);
	}
	public float getVolume(){
		return volume;
	}
	

}
