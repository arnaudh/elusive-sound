package org.elusive.ui.grille;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import org.elusive.sound.blocs.BlocPositionne;
import org.elusive.ui.events.MouseEventTools;

public class GridMouseMoveListener implements MouseMotionListener {
	
	private GridPanel gridPanel;

	public GridMouseMoveListener(GridPanel gridPanel) {
		super();
		this.gridPanel = gridPanel;
	}

	public void mouseMoved(MouseEvent e) {
		if (MouseEventTools.isControlOrMetaDown(e)) {
			gridPanel.calculeCurseurFictif(e);
			gridPanel.repaint();
		} else {
			if (gridPanel.curseur_fictif != -1) {
				gridPanel.curseur_fictif = -1;
				gridPanel.repaint();
			}
		}
		if (e.isShiftDown() && gridPanel.getGrid().getSelection() != null) {
			if (Math.abs(e.getX() - gridPanel.offset2pixel(gridPanel.getGrid().getSelection().getFin())) < gridPanel.PIXEL_TOLERANCE || Math.abs(e.getX() - gridPanel.offset2pixel(gridPanel.getGrid().getSelection().getDebut())) < gridPanel.PIXEL_TOLERANCE) {
				gridPanel.setCursor(gridPanel.RESIZE_CURSOR);
			} else {
				gridPanel.setCursor(gridPanel.DEFAULT_CURSOR);
			}
		} else {
			BlocPositionne bp = gridPanel.blocAt(e.getPoint());
			if (bp != null) {
				if (Math.abs(e.getX() - gridPanel.offset2pixel(bp.getFin())) < gridPanel.PIXEL_TOLERANCE || Math.abs(e.getX() - gridPanel.offset2pixel(bp.getDebut())) < gridPanel.PIXEL_TOLERANCE) {
					gridPanel.setCursor(gridPanel.RESIZE_CURSOR);
				} else {
					gridPanel.setCursor(gridPanel.DEFAULT_CURSOR);
				}
			} else {
				gridPanel.setCursor(gridPanel.DEFAULT_CURSOR);
			}
		}
	}

	public void mouseDragged(MouseEvent e) {
		gridPanel.lastDrag = e.getPoint();
		int lastDraX = gridPanel.pixel2offset(gridPanel.lastDrag.x);
		if (MouseEventTools.isControlOrMetaDown(e)) {
			gridPanel.calculeCurseurFictif(e);
			lastDraX = gridPanel.curseur_fictif;
		}
		switch (gridPanel.dragAction) {
		case DATA_SELECTION:
			gridPanel.getGrid().getSelection().bouge(lastDraX);
			break;
		case BLOC_RESIZE_LEFT:
			gridPanel.decalageResize = lastDraX - gridPanel.blocLastPress.getDebut();
			for (BlocPositionne bp : gridPanel.getGrid().getBlocsSelectionnes()) {
				int a = bp.getLength();
				if (a < gridPanel.decalageResize) {
					gridPanel.decalageResize = a;
				}
			}
			break;
		case BLOC_RESIZE_RIGHT:
			gridPanel.decalageResize = lastDraX - gridPanel.blocLastPress.getFin();
			for (BlocPositionne bp : gridPanel.getGrid().getBlocsSelectionnes()) {
				int a = bp.getLength();
				if (a < -gridPanel.decalageResize) {
					gridPanel.decalageResize = -a;
				}
			}
			break;
		case GRID_MOVE:
			// on déplace la vue
			int newOffset = gridPanel.offsetGlobalLastPress - (gridPanel.pixel2offset(gridPanel.lastDrag.x) - gridPanel.pixel2offset(gridPanel.lastPress.x));
			int newY = gridPanel.yLastPress - (gridPanel.lastDrag.y - gridPanel.lastPress.y);
			gridPanel.getGrid().setOffset(newOffset);
			gridPanel.getGrid().setScrollVValue(newY);
			break;
		case BLOC_MOVE:
			int ancienDecalage = gridPanel.decalageLigne;
			gridPanel.decalageLigne = gridPanel.ligneAt(gridPanel.lastDrag) - gridPanel.ligneAt(gridPanel.lastPress);
			for (BlocPositionne bp : gridPanel.getGrid().getBlocsSelectionnes()) {
				// il faut s'assurer qu'on ne déborde pas des
				// lignes
				if (bp.getLigne() + gridPanel.decalageLigne < 0 || bp.getLigne() + gridPanel.decalageLigne >= gridPanel.getGrid().getLines().size()) {
					gridPanel.decalageLigne = ancienDecalage;
					break;
				}
			}
			gridPanel.decalageOffset = gridPanel.pixel2offset(gridPanel.lastDrag.x) - gridPanel.pixel2offset(gridPanel.lastPress.x);
			break;

		}

		gridPanel.repaint();
	}

}
