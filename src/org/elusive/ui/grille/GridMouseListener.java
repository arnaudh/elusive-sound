package org.elusive.ui.grille;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import org.elusive.main.historique.ActionHistoriqueMultiple;
import org.elusive.main.historique.action.DeplaceBloc;
import org.elusive.main.historique.action.ResizeBloc;
import org.elusive.sound.blocs.BlocPositionne;
import org.elusive.ui.events.MouseEventTools;

import java.awt.event.MouseListener;

public class GridMouseListener implements MouseListener {
	
	private GridPanel gridPanel;
	
	public GridMouseListener(GridPanel gridPanel) {
		super();
		this.gridPanel = gridPanel;
	}

	public void mouseReleased(MouseEvent e) {

		ArrayList<BlocPositionne> blocsFixes = gridPanel.getBlocsFixes();
		boolean ajoutable = true;

		switch (gridPanel.dragAction) {
		case BLOC_MOVE:
			// on vérifie qu'on peut tout déplacer
			for (BlocPositionne bp : gridPanel.getGrid().getBlocsSelectionnes()) {
				if (!gridPanel.getGrid().ajoutable(bp.getLigne() + gridPanel.decalageLigne, bp.getDebut() + gridPanel.decalageOffset, bp.getFin() + gridPanel.decalageOffset, blocsFixes)) {
					ajoutable = false;
					break;
				}
			}
			if (ajoutable) {
				ActionHistoriqueMultiple action = new ActionHistoriqueMultiple("Déplace Blocs");
				for (BlocPositionne bp : gridPanel.getGrid().getBlocsSelectionnes()) {
					action.addAction(new DeplaceBloc(gridPanel.getGrid(), bp, gridPanel.decalageLigne, gridPanel.decalageOffset));
				}
				gridPanel.getGrid().getHistorique().execute(action);
			} else {
				gridPanel.repaint();
			}
			break;
		case BLOC_RESIZE_LEFT:
			// on vérifie qu'on peut tout déplacer
			for (BlocPositionne bp : gridPanel.getGrid().getBlocsSelectionnes()) {
				if (!gridPanel.getGrid().ajoutable(bp.getLigne(), bp.getDebut() + gridPanel.decalageResize, bp.getFin(), blocsFixes)) {
					ajoutable = false;
					break;
				}
			}
			if (ajoutable) {
				ActionHistoriqueMultiple action = new ActionHistoriqueMultiple("Resize Blocs");
				for (BlocPositionne bp : gridPanel.getGrid().getBlocsSelectionnes()) {
					action.addAction(new ResizeBloc(gridPanel.getGrid(), bp, gridPanel.decalageResize, 0));
				}
				gridPanel.getGrid().getHistorique().execute(action);
			} else {
				gridPanel.repaint();
			}
			break;
		case BLOC_RESIZE_RIGHT:
			// on vérifie qu'on peut tout déplacer
			for (BlocPositionne bp : gridPanel.getGrid().getBlocsSelectionnes()) {
				if (!gridPanel.getGrid().ajoutable(bp.getLigne(), bp.getDebut(), bp.getFin() + gridPanel.decalageResize, blocsFixes)) {
					ajoutable = false;
					break;
				}
			}
			if (ajoutable) {
				ActionHistoriqueMultiple action = new ActionHistoriqueMultiple("Resize Blocs");
				for (BlocPositionne bp : gridPanel.getGrid().getBlocsSelectionnes()) {
					action.addAction(new ResizeBloc(gridPanel.getGrid(), bp, 0, gridPanel.decalageResize));
				}
				gridPanel.getGrid().getHistorique().execute(action);
			} else {
				gridPanel.repaint();
			}
			break;
		}
		gridPanel.dragAction = DragAction.NONE;

		if (gridPanel.getGrid().getSelection() != null && gridPanel.getGrid().getSelection().getDebut() == gridPanel.getGrid().getSelection().getFin()) {
			gridPanel.getGrid().setSelection( null );
		}

		gridPanel.decalageLigne = 0;
		gridPanel.decalageOffset = 0;
		gridPanel.decalageResize = 0;

	}

	public void mousePressed(MouseEvent e) {
		// TODO : corriger le bug
		// "qd on press en dessous des lignes ça prend le bloc de la dernière ligne"
		gridPanel.lastPress = e.getPoint();
		gridPanel.offsetGlobalLastPress = gridPanel.getGrid().getOffset();

		gridPanel.yLastPress = gridPanel.getGrid().getSrollVValue();
		gridPanel.blocLastPress = gridPanel.blocAt(gridPanel.lastPress);

		if (e.isAltDown()) {
			// view
			gridPanel.dragAction = DragAction.GRID_MOVE;
			// enDeplacementGrille = true;
		} else if (e.isShiftDown()) {
			// data gridPanel.getGrid().getSelection()
			gridPanel.dragAction = DragAction.DATA_SELECTION;
			if (gridPanel.getGrid().getSelection() != null) {
				if (Math.abs(e.getX() - gridPanel.offset2pixel(gridPanel.getGrid().getSelection().getFin())) < gridPanel.PIXEL_TOLERANCE) {
					gridPanel.getGrid().getSelection().setDeuxiemeFin();
					// enResizeSelection = true;
				} else if (Math.abs(e.getX() - gridPanel.offset2pixel(gridPanel.getGrid().getSelection().getDebut())) < gridPanel.PIXEL_TOLERANCE) {
					gridPanel.getGrid().getSelection().setDeuxiemeDebut();
					// enResizeSelection = true;
				} else {
					gridPanel.getGrid().setSelection( new Selection(gridPanel.pixel2offset(gridPanel.lastPress.x)) );
					// enResizeSelection = true;
				}
			} else {
				int selectionStart = (gridPanel.curseur_fictif != -1) ? gridPanel.curseur_fictif : gridPanel.pixel2offset(gridPanel.lastPress.x);
				gridPanel.getGrid().setSelection( new Selection(selectionStart) );
				// enResizeSelection = true;
			}
		} else if (MouseEventTools.isControlOrMetaDown(e)) {
			// bloc gridPanel.getGrid().getSelection()
			if (gridPanel.blocLastPress != null) {
				if (gridPanel.getGrid().getBlocsSelectionnes().contains(gridPanel.blocLastPress)) {
					gridPanel.getGrid().getBlocsSelectionnes().remove(gridPanel.blocLastPress);
				} else {
					gridPanel.getGrid().getBlocsSelectionnes().add(gridPanel.blocLastPress);
				}
			}

		} else {
			// blocs
			if (gridPanel.blocLastPress == null) {
				// rectangular bloc gridPanel.getGrid().getSelection()
//				System.out.println("GrillePanel.GrillePanel(...).new MouseListener() {...}.mousePressed() TODO rectangular bloc gridPanel.getGrid().getSelection()");
			} else {
				if (Math.abs(e.getX() - gridPanel.offset2pixel(gridPanel.blocLastPress.getFin())) < gridPanel.PIXEL_TOLERANCE) {
					gridPanel.dragAction = DragAction.BLOC_RESIZE_RIGHT;
					// enResizeDroit = true;
					// enResizeGauche = false;
				} else if (Math.abs(e.getX() - gridPanel.offset2pixel(gridPanel.blocLastPress.getDebut())) < gridPanel.PIXEL_TOLERANCE) {
					gridPanel.dragAction = DragAction.BLOC_RESIZE_LEFT;
					// enResizeDroit = false;
					// enResizeGauche = true;
				} else {
					gridPanel.dragAction = DragAction.BLOC_MOVE;
				}
				if (!gridPanel.getGrid().getBlocsSelectionnes().contains(gridPanel.blocLastPress)) {
					gridPanel.getGrid().getBlocsSelectionnes().clear();
					gridPanel.getGrid().getBlocsSelectionnes().add(gridPanel.blocLastPress);
				}
			}

		}
		gridPanel.repaint();
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
		gridPanel.requestFocus();
		switch (e.getButton()) {
		case 1:
			gridPanel.lastClic = e.getPoint();
			BlocPositionne bloc = gridPanel.blocAt(gridPanel.lastClic);
			switch (e.getClickCount()) {
			case 1:
				if (bloc == null) {
					gridPanel.getGrid().getBlocsSelectionnes().clear();
					gridPanel.getGrid().setSelection( null );
					if (gridPanel.curseur_fictif >= 0) {
						gridPanel.getGrid().curseur = gridPanel.curseur_fictif;
						gridPanel.curseur_fictif = -1;
					} else {
						gridPanel.getGrid().curseur = gridPanel.pixel2offset(gridPanel.lastClic.x);
					}
					gridPanel.getGrid().repaint();
				} else {
					if (!MouseEventTools.isControlOrMetaDown(e)) {
						gridPanel.getGrid().getBlocsSelectionnes().clear();
						gridPanel.getGrid().getBlocsSelectionnes().add(bloc);
						gridPanel.getGrid().repaint();
					}
				}
				break;
			case 2:
				if (bloc != null) {
					gridPanel.getGrid().fenetre.showBlocControls(bloc);
				} else {
					gridPanel.getGrid().fenetre.controles.play();
				}

				break;
			}

			break;
		}
	}

}
