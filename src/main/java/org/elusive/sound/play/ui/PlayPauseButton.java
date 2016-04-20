package org.elusive.sound.play.ui;

import java.awt.event.ActionEvent;

import javax.swing.JButton;

import org.elusive.sound.play.Playable;
import org.elusive.ui.action.MyAction;
import org.elusive.ui.resources.Icons;

public class PlayPauseButton extends JButton implements Playable {


	private Playable playable;
	private Thread th;
	
	private MyAction playAction;
	private MyAction pauseAction;

	public PlayPauseButton(Playable playable) {
		super();
		this.playable = playable;
		
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
		playAction.attachToButton(this);
	}

	public boolean play(){
		if( playable.isPlaying() ){
			return false;
		}
		th = new Thread(playable);
		th.start();
		pauseAction.attachToButton(this);
		return true;
	}
	
	@Override
	public void run() {
		play();
	}

	@Override
	public void pause() {
		playable.pause();
		playAction.attachToButton(this);
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
