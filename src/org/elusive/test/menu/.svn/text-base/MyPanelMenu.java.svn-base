package org.elusive.test.menu;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class MyPanelMenu extends JLabel{
	
	public String name;
	
	public MonMenu menu;

	public MonAction a1;
	public MonAction a2;

	public MyPanelMenu(String name) {
		super(name);
		this.name = name;
		
		menu = new MonMenu("File");
		
		MonMenu menuNew = new MonMenu("New");
		menuNew.add(new MonAction(new AbstractAction("Local") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("new local");
				this.setEnabled(false);
			}
		}, null));
		menu.add(menuNew);

		a1 = new MonAction(new AbstractAction("Undo") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("action 1");
			}
		}, KeyStroke.getKeyStroke('Z', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) );
		
		a2 = new MonAction(new AbstractAction("Redo") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("action 2");
			}
		}, KeyStroke.getKeyStroke('Y', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) );

		menu.add(a1);
		menu.add(a2);
		
	}



}
