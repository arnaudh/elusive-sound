package org.elusive.sound.blocs.fm;

import java.util.Random;

import javax.swing.tree.DefaultMutableTreeNode;

import org.elusive.sound.flow.Timestamp;
import org.elusive.sound.genetics.RandomTools;



public class OperateurFM extends DefaultMutableTreeNode {

	private FMSynthetizer synth;
	
	private int amplitude = 50; // entre 0 et 100
	private double frequence = 440;
	private boolean actif = true;
	
	public transient double decalagePhase = 0;
	public transient double angle = 0;
	
	public OperateurFM() {
	}

	public OperateurFM(FMSynthetizer fmSynthetizer) {
		this.synth = fmSynthetizer;
	}

	public OperateurFM(int amplitude, double frequence) {
		super();
		this.amplitude = amplitude;
		this.frequence = frequence;
	}


	public double calcule(double t) {
		if (!actif) {
			return 0;
		}
		double ret = 0;

		if (children != null) {
			for (int i = 0; i < children.size(); i++) {
				OperateurFM op = (OperateurFM) children.get(i);
				ret += op.calcule(t);
			}
		}
		
		double K = 2 * Math.PI/ (double)44100;

//		if( T != 0 && facteur != 1 ){		
//			val = T  / Math.log(facteur) * Math.pow(facteur, -t/(double)T);
//		}else{
//			val = t;
//		}
		
		angle = K * frequence * t + ret + decalagePhase;
		
		return amplitude * Math.sin(angle);
	}

	public int getAmplitude() {
		return amplitude;
	}

	public void setAmplitude(int amplitude) {
		this.amplitude = amplitude;
		updated();
	}

	public double getFrequence() {
		return frequence;
	}

	public void setFrequence(double frequence) {
		this.frequence = frequence;
		updated();
	}
	public void setFrequenceNoUpdate(double frequence) {
		this.frequence = frequence;
	}

	public String toString() {
		return "OpFM[Intensité=" + amplitude + ", frequence=" + frequence + "]";
	}

	public void setActif(boolean actif) {
		this.actif = actif;
		updated();
	}

	public boolean isActif() {
		return actif;
	}

	private transient Timestamp timestamp = new Timestamp();
	protected void updated(){
		if( synth != null ){
			synth.update();
		}else{
			OperateurFM op = (OperateurFM) this.getParent();
			if( op != null ){
				op.updated();
			}
		}
		//TODO remove timestamp bullshit
		timestamp.update();
	}
	public Timestamp getTimestamp(){
		long tmp = timestamp.getValue();
		if (children != null) {
			for (int i = 0; i < children.size(); i++) {
				OperateurFM op = (OperateurFM) children.get(i);
				tmp = Math.max(tmp, op.getTimestamp().getValue());
			}
		}
		return new Timestamp(tmp);
	}
	public Object readResolve(){
		timestamp = new Timestamp();
		return this;
	}


	public String toRecursiveString(){
		if( this.getChildCount() == 0 ){
			return "[+]";
		}
		StringBuilder sb = new StringBuilder("[+(");
		if (children != null) {
			for (int i = 0; i < children.size(); i++) {
				OperateurFM op = (OperateurFM) children.get(i);
				sb.append(op.toRecursiveString());
			}
		}
		sb.append(")]");
		return sb.toString();
	}

	public OperateurFM mutate( double strength ){
		OperateurFM ope = new OperateurFM();
		int amp = amplitude;
		double fre = frequence;

		Random generator = new Random();
		if( generator.nextBoolean() ){
			amp = (int) RandomTools.modify(amp, strength, 0, 100);
		}else{
			fre = RandomTools.modify(fre, strength);
		}
		ope.setAmplitude(amp);
		ope.setFrequence(fre);
		if( children != null ){
			for(Object o : children){
				ope.add( ((OperateurFM)o).mutate(strength) );
			}
		}
		return ope;
	}
	
}
