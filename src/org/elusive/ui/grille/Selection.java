package org.elusive.ui.grille;

import org.elusive.ui.action.MyAction;

public class Selection {

	private int premierPoint;
	private int deuxiemePoint;

	public Selection(int debut) {
		this.premierPoint = debut;
		this.deuxiemePoint = debut;
	}

	public int getDebut() {
		return Math.min( premierPoint, deuxiemePoint);
	}

	public int getFin() {
		return Math.max( premierPoint, deuxiemePoint);
	}

	public void bouge(int lastDragX) {
		deuxiemePoint = lastDragX;
	}

	//Met le min dans premierPoint et le max dans deuxiemePoint
	public void setDeuxiemeFin() {
		int min = Math.min(premierPoint, deuxiemePoint);
		deuxiemePoint = Math.max(premierPoint, deuxiemePoint);
		premierPoint = min;		
	}
	//L'inverse
	public void setDeuxiemeDebut() {
		int min = Math.min(premierPoint, deuxiemePoint);
		premierPoint = Math.max(premierPoint, deuxiemePoint);
		deuxiemePoint = min;		
	}
	

	
}
