package org.elusive.ui.fenetre;

import java.awt.Menu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenuItem;

import org.elusive.ui.action.ActionCategory;
import org.elusive.ui.action.MyAction;
import org.elusive.ui.action.MyCheckAction;

public class ShowableMenuFrame extends JFrame {


	private MyCheckAction showAction;
	
	public ShowableMenuFrame(String title) {
		super(title);
		this.addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent arg0) {
			}
			public void windowIconified(WindowEvent arg0) {
			}
			public void windowDeiconified(WindowEvent arg0) {
			}
			public void windowDeactivated(WindowEvent arg0) {
			}
			public void windowClosing(WindowEvent arg0) {
				showAction.setSelected(false);
			}
			public void windowClosed(WindowEvent arg0) {
			}
			public void windowActivated(WindowEvent arg0) {
			}
		});

		showAction = new MyCheckAction("Show "+title, null, "Show "+title+" frame", null, ActionCategory.VIEW) {
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowableMenuFrame.this.setVisible(showAction.isSelected());
			}
		};
	}

	public MyCheckAction getShowAction() {
		return showAction;
	}
	
	
}
