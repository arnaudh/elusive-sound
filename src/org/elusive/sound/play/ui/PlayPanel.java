package org.elusive.sound.play.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.elusive.sound.play.Playable;
import org.elusive.ui.action.MyAction;
import org.elusive.ui.resources.Icons;

public class PlayPanel extends JPanel implements Playable {

	
	private Playable playable;
	private Thread th;
	
	private MyAction playAction;
	private MyAction pauseAction;
	private MyAction rewindAction;
	
	//UI
	private JButton playButton;
	private JButton rewindButton;

	public PlayPanel(Playable p){
		super(new GridBagLayout());
		this.playable = p;
		
		// actions
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
		rewindAction = new MyAction("Rewind", null, "Rewind", Icons.REWIND_ICON) {
			@Override
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		};
		
		playButton = playAction.createSimpleIconButton();
		rewindButton = rewindAction.createSimpleIconButton();
		
		// Layout
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.add(playButton, gbc);
		gbc.gridx ++;
		this.add(rewindButton, gbc);
		
	}
	
	
	public boolean play(){
		if( playable.isPlaying() ){
			return false;
		}
		th = new Thread(playable);
		th.start();
		pauseAction.attachToButton(playButton);
		return true;
	}
	
	@Override
	public void run() {
		play();
	}

	@Override
	public void pause() {
		playable.pause();
		playAction.attachToButton(playButton);
	}

	@Override
	public void stop() {
		playable.stop();
	}

	@Override
	public boolean isPlaying() {
		return playable.isPlaying();
	}
}
