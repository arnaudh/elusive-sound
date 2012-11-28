package org.elusive.ui.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

public class FocusRequestListener extends MouseAdapter {

	private JComponent comp;

	public FocusRequestListener(JComponent comp) {
		this.comp = comp;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("FocusRequestListener.mousePressed()");
		comp.requestFocus();
	}
	
}
