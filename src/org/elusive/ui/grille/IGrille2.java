/***********************************************************************************************
 * Egis
 * (C)opyright 2011 Ipsosenso - Tous droits réservés.
 * 
 * Code réalisé par Ipsosenso.
 *
 * @date 27 mai 2011 @author Arnaud -  IGrille.java
 ***********************************************************************************************/

package org.elusive.ui.grille;

import java.util.List;

import org.elusive.sound.blocs.Bloc;
import org.elusive.sound.blocs.BlocPositionne;

/**
 * @author Arnaud
 *
 */
public interface IGrille2 {
	
//	public static boolean ajoutable(BlocPositionne bp, List<BlocPositionne> bps);
	public void setInsertPosition(int ligne, int offset);
	
	public Selection getSelection();
	public List<BlocPositionne> getBlocs();
	public List<BlocPositionne> getSelectedBlocs();

	public void addToSelection(BlocPositionne b);
	public void removeFromSelection(BlocPositionne b);
	
	public int getCurseur();
	public void setCurseur(int curseur);

	public void cut();
	public void copy();
	public void paste();
	public void delete();

	public void addBloc(Bloc b);
	
	public void addBloc(BlocPositionne bp);
	public void removeBloc(BlocPositionne bp);

	public void moveBloc(BlocPositionne bp, int lineShift, int offsetShift);
	public void moveBlocs(List<BlocPositionne> bp, int lineShift, int offsetShift);
	
	public void undo();
	public void redo();
	
	
	
//	public void clearSelectedLines();
	
	//En construction...

}
