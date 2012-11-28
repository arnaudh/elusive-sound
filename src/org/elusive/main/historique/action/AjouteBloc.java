package org.elusive.main.historique.action;


import javax.swing.Icon;

import org.elusive.sound.blocs.Bloc;
import org.elusive.sound.blocs.BlocPositionne;
import org.elusive.ui.fenetre.Fenetre;
import org.elusive.ui.grille.Grid;




public class AjouteBloc extends ActionHistoriqueGrille{

	private Bloc bloc = null;
	private BlocPositionne bp = null;

	public AjouteBloc(Grid g, Bloc b){
		super(g);
		this.bloc = b;
	}
	public AjouteBloc(Grid g, BlocPositionne bp){
		super(g);
		this.bp = bp;
	}
	
	@Override
	public boolean doAction() {
		if( bp == null ){
			bp = grille.newBloc(bloc);
		}else{ //ce n'est pas la première fois qu'on le fait - il faut le mettre à la même ligne
			grille.newBloc(bp);
		}
		if( bp == null ){
			System.out.println("Pas de place disponible");
			return false;
		}
		return true;
	}

	@Override
	public void doReverse() {
		grille.remove(bp);
	}

	public String getName() {
		return "Nouveau Bloc";
	}

	public Icon getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getInfo() {
		return bloc.toString();
	}

}
