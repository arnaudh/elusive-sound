package org.elusive.main.historique.action;


import java.util.ArrayList;

import javax.swing.Icon;

import org.elusive.sound.blocs.BlocPositionne;
import org.elusive.ui.grille.Grid;



public class ResizeBloc extends ActionHistoriqueGrille {

	private int left;
	private int right;
	private BlocPositionne bp;
	
	public ResizeBloc(Grid grille, BlocPositionne bp, int left, int right ){
		super( grille);
		this.bp = bp;
		this.left = left;
		this.right = right;
	}

	@Override
	public boolean doAction() {
		bp.resize(left, right);
		return true;
	}

	@Override
	public void doReverse() {
		bp.resize(-left, -right);
	}
	
	@Override
	public String getName() {
		return "Resize Bloc";
	}
	
	@Override
	public String getInfo(){
		return "( "+left+", "+right+")";
	}

	@Override
	public Icon getIcon() {
		return null;
	}


}
