package org.elusive.ui.tools;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;

import org.elusive.ui.menus.MenuTools;

public abstract class PopupListener extends MouseAdapter {

	public void mousePressed(MouseEvent e) {
		maybeShowPopup(e);
	}

	public void mouseReleased(MouseEvent e) {
		maybeShowPopup(e);	
	}

	private void maybeShowPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			System.out.println("PopupListener.maybeShowPopup()");
			// clic droit
			JPopupMenu popup = showMenu(e);
			if( popup !=null ){
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	public abstract JPopupMenu showMenu(MouseEvent e);

}
