package org.elusive.main.historique.action;

import java.util.ArrayList;

import javax.swing.Icon;

import org.elusive.sound.blocs.BlocPositionne;
import org.elusive.sound.effets.Effet;
import org.elusive.ui.grille.Grid;




public class AjouteEffet extends ActionHistoriqueGrille{

	private Effet effet = null;
	private BlocPositionne bp;
	
	public AjouteEffet(Grid grille, Effet effet, BlocPositionne bp){
		super(grille);
		this.effet = effet;
		this.bp = bp;
	}

	@Override
	public Icon getIcon() {
		return effet.getIcon();
	}

	@Override
	public String getName() {
		return "Ajoute Effet";
	}

	@Override
	public String getInfo() {
		return bp.toString();
	}

	@Override
	public boolean doAction() {
		bp.addEffect(effet);
		return true;
	}

	@Override
	public void doReverse() {
		bp.enleveEffet(effet);		
	}
	
}
