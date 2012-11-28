package org.elusive.test.menu;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MonAction extends MonMenuOuAction {
	
	public Action action;
	public KeyStroke keyStroke;
	
	public MonAction(Action action, KeyStroke keyStroke) {
		this.action = action;
		this.keyStroke = keyStroke;
	}

	public JMenuItem toJMenuItem(){
		JMenuItem item = new JMenuItem();
		item.setAction(action);
		item.setAccelerator(keyStroke);
		return item;
	}
	
	public void setName(String name){
		action.putValue(Action.NAME, name);
	}
	
	public void setEnabled(boolean b){
		action.setEnabled(b);
	}
	
	
}
