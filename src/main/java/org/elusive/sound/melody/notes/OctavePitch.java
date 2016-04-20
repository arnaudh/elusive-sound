package org.elusive.sound.melody.notes;

public class OctavePitch {
	
	
	private int octave;
	private int pitch;
	public OctavePitch(int octave, int pitch) {
		super();
		this.octave = octave;
		this.pitch = pitch;
	}
	public int getOctave() {
		return octave;
	}
	public void setOctave(int octave) {
		this.octave = octave;
	}
	public int getPitch() {
		return pitch;
	}
	public void setPitch(int pitch) {
		this.pitch = pitch;
	}
	
	@Override
	public String toString() {
		return "[octave="+octave+", pitch="+pitch+"]";
	}
	
	

}
