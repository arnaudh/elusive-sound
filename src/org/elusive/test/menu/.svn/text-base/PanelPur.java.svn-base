package org.elusive.test.menu;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class PanelPur extends JPanel {

	public JMenu menu;
	
	public PanelPur() {
		
		menu = new JMenu("File");
		JMenuItem item;
		
		
		item = new JMenuItem(new AbstractAction("Undo") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("undo");
//				this.setEnabled(false);
			}
		});
		item.setAccelerator(KeyStroke.getKeyStroke('Z', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.add(item);
		
		JMenu m = new JMenu("New");
		item = new JMenuItem(new AbstractAction("A") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("new A");
			}
		});
		item.setAccelerator(KeyStroke.getKeyStroke('A', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		m.add(item);
		item = new JMenuItem(new AbstractAction("B") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("new B");
				this.setEnabled(false);
			}
		});
		item.setAccelerator(KeyStroke.getKeyStroke('B', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		m.add(item);
		menu.add(m);

		
		JCheckBoxMenuItem check = new JCheckBoxMenuItem("cheeeeeck");
		check.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean selected = e.getStateChange() == ItemEvent.SELECTED ? true : false;
				System.out.println("selected = "+selected);
			}
		});
		menu.add(check);
		
	}
	
	
}
