package org.elusive.main.historique.action;



import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.elusive.main.persistence.xml.XmlTools;
import org.elusive.sound.blocs.BlocPositionne;
import org.elusive.ui.grille.Grid;




public class DeplaceBloc extends ActionHistoriqueGrille {

	private int decalageLigne;
	private int decalageOffset;
	private BlocPositionne bp;
	
	
	public DeplaceBloc(Grid grille, BlocPositionne bp, int decalageLigne, int decalageOffset) {
		super(grille);
		this.decalageLigne = decalageLigne;
		this.decalageOffset = decalageOffset;
		this.bp = bp;
	}


	@Override
	public boolean doAction() {
		bp.setLigne(bp.getLigne() + decalageLigne);
		bp.setDebut(bp.getDebut() + decalageOffset);
		return true;
	}

	@Override
	public void doReverse() {
		bp.setLigne(bp.getLigne() - decalageLigne);
		bp.setDebut(bp.getDebut() - decalageOffset);		
	}
	
	@Override
	public String getName() {
		return "Déplace Bloc";
	}
	
	@Override
	public String getInfo(){
		return "(ligne += "+decalageLigne+", offset+="+decalageOffset+")";
	}

	public Icon getIcon() {
		Icon icon = new ImageIcon(getClass().getResource("/resources/icon/deplace.png"));
		return null;
	}

}
