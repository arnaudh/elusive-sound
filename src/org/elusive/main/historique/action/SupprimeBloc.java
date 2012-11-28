package org.elusive.main.historique.action;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.elusive.sound.blocs.BlocPositionne;
import org.elusive.ui.fenetre.ElusivePanel;
import org.elusive.ui.grille.Grid;

public class SupprimeBloc extends ActionHistoriqueGrille {
	
	private BlocPositionne blocASupprimer;
	
	public SupprimeBloc(Grid g, BlocPositionne bp) {
		super(g);
		this.blocASupprimer = bp;
	}

	@Override
	public boolean doAction() {
		grille.remove(blocASupprimer);
		return true;
	}

	@Override
	public void doReverse() {
		grille.getBlocs().add(blocASupprimer);
	}

	@Override
	public String getName() {
		return "Supprime Bloc";
	}

	@Override
	public String getInfo() {
		return blocASupprimer.toString();
	}

	@Override
	public Icon getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

}
