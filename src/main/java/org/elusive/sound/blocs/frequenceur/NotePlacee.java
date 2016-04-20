package org.elusive.sound.blocs.frequenceur;


public class NotePlacee {

	private int start = 0;
	private double frequence = 440;
	private int length = 44100;

	public NotePlacee(int start, double frequence, int length) {
		this.start = start;
		this.frequence = frequence;
		this.length = length;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public double getFrequence() {
		return frequence;
	}

	public void setFrequence(double frequence) {
		this.frequence = frequence;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer("NotePlacee[");
		sb.append(" début = "+start);
		sb.append(", frequence = "+frequence);
		sb.append(", durée = "+length);
		sb.append(']');
		return sb.toString();
	}


}
