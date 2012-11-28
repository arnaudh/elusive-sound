package org.elusive.sound.enveloppes;

import org.elusive.sound.flow.Timestamp;




public class PointSon {

	private int frame;
	private double amplitude;
	
	public PointSon(int x, double y){
		frame = x;
		amplitude = y;
	}

	public PointSon(PointSon pp) {
		frame = pp.frame;
		amplitude = pp.amplitude;
	}

	public int getX() {
		return frame;
	}

	public double getY() {
		return amplitude;
	}

	public void setLocation(int x, double y) {
		frame = x;
		amplitude = y;
		updated();
	}
	
	public String toString(){
		return "PointSon["+frame+", "+amplitude+"]";
	}
	
	private transient Timestamp timestamp = new Timestamp();
	protected void updated(){
		timestamp.update();
	}
	public Timestamp getTimestamp(){
		return timestamp;
	}
	public Object readResolve(){
		timestamp = new Timestamp();
		return this;
	}
	
}
