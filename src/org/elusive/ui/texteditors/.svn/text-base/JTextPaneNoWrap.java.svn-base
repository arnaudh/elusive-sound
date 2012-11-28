package org.elusive.ui.texteditors;

import java.awt.Component;

import javax.swing.JTextPane;
import javax.swing.plaf.ComponentUI;

public class JTextPaneNoWrap extends JTextPane {

	@Override
	public boolean getScrollableTracksViewportWidth() {
		Component parent = getParent();
		ComponentUI ui = getUI();
		return parent != null ? (ui.getPreferredSize(this).width <= parent.getSize().width) : true;
	}
}
