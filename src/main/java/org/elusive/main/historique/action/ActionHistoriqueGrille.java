package org.elusive.main.historique.action;

import org.elusive.ui.grille.Grid;
import org.elusive.main.historique.ActionHistorique;

public abstract class ActionHistoriqueGrille extends ActionHistorique {
	
	protected Grid grille;
	
	public ActionHistoriqueGrille(Grid g) {
		grille = g;
	}

}
