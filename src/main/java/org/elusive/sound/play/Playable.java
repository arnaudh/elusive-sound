package org.elusive.sound.play;

public interface Playable extends Runnable {


	public void pause();
	public void stop();
	
	public boolean isPlaying();
	
}
