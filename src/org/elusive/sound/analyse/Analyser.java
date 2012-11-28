package org.elusive.sound.analyse;

import org.elusive.ui.grille.Grid;

public abstract class Analyser {
	
	public abstract void analyse(float[] data);
	
	protected Grid grille;
	public void setGrille(Grid grille){
		this.grille = grille;
	}

}
