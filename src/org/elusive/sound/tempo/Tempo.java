package org.elusive.sound.tempo;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.elusive.sound.play.BufferedData;
import org.elusive.sound.play.PlayableAudio;
import org.elusive.sound.tools.SoundFileReader;
import org.elusive.ui.grille.Grid;

public class Tempo{
	
	private double bpm;
	private transient PlayableAudio metronome;
	
	public Tempo(double bpm){
		this.bpm = bpm;
	}
	
	public void setBpm(double bpm){
		this.bpm = bpm;
	}
	
	public double getBpm() {
		return bpm;
	}

	public int getIntervalleFrames(){
		return (int) (60 / bpm * Grid.FRAMES_PER_SECOND);
	}

	public double getFrequence() {
		return bpm/60;
	}
	
	public PlayableAudio getMetronome() {
		if( metronome == null ){
			SoundFileReader sfr = new SoundFileReader(new File(getClass().getResource("/resources/sound/click.wav").getFile()));
			try {
				sfr.read();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			}
			final float[] metronomeSound = sfr.getLeftData();
			metronome = new PlayableAudio(new BufferedData() {
				@Override
				public void initData() {
					
				}
				@Override
				public float[] getNextData() {
					int nbFrames = getIntervalleFrames();
					int maxFrame = Math.min(nbFrames, metronomeSound.length);
					float[] data = new float[nbFrames];
					for( int i = 0; i < maxFrame; i++){
						data[i] = metronomeSound[i];
					}
					return data;
				}
			});
		}
		return metronome;
	}
	
	public String toString(){
		return "Tempo[bpm = "+bpm+"]";
	}
	

	
	
}
