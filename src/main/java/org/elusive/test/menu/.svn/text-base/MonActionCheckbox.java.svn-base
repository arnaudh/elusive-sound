package org.elusive.test.menu;

import java.awt.event.ItemListener;

import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MonActionCheckbox extends MonAction {

	public boolean checked = false;
	public ItemListener listener;	
	
	public MonActionCheckbox(ItemListener listener, KeyStroke keyStroke) {
		super(null, keyStroke);
		this.listener = listener;
	}

	@Override
	public JMenuItem toJMenuItem() {
		JCheckBoxMenuItem item = new JCheckBoxMenuItem();
		item.addItemListener(listener);
		item.setAccelerator(keyStroke);
		return item;
	}
	
}
