package org.elusive.ui.action;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.KeyStroke;

public abstract class MyCheckAction extends MyAction {

	public MyCheckAction(String name, KeyStroke accelerator, String tooltip, Icon bigIcon, ActionCategory category) {
		super(name, accelerator, tooltip, bigIcon, category);
		this.putValue(AbstractAction.SELECTED_KEY, false);
	}
	
	public void setSelected(boolean sel){
		this.putValue(AbstractAction.SELECTED_KEY, sel);
	}
	
	public boolean isSelected(){
		return (Boolean) this.getValue(AbstractAction.SELECTED_KEY);
	}
	
	public JCheckBox createSimpleCheckbox(){
		JCheckBox check = new JCheckBox(this);
		return check;
	}

}
