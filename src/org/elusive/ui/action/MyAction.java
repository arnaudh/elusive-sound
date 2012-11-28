package org.elusive.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public abstract class MyAction extends AbstractAction {
	
	private ActionCategory category = ActionCategory.NONE;

	public MyAction(String name, KeyStroke accelerator, String tooltip, Icon bigIcon){
		this(name, accelerator, tooltip, bigIcon, null);
	}
	
	public MyAction(String name, KeyStroke accelerator, String tooltip, Icon bigIcon, ActionCategory cat){
		super(name);
		this.putValue(AbstractAction.ACCELERATOR_KEY, accelerator);
		this.putValue(AbstractAction.SHORT_DESCRIPTION, tooltip);
		this.putValue(AbstractAction.LARGE_ICON_KEY, bigIcon);
		this.category = cat;
	}
	
	public void setName(String name){
		this.putValue(AbstractAction.NAME, name);
	}
	
	public ActionCategory getCategory(){
		return this.category;
	}
	
	public JButton createSimpleIconButton(){
		JButton button = new JButton();
		attachToButton(button);
		return button;
	}

	public Component createSimpleTextButton() {
		JButton button = new JButton();
		button.setAction(this);
		button.setBorderPainted(true);
		return button;
	}
	
	public void attachToButton(JButton button){
		button.setAction(this);
		button.setText("");
		button.setBorderPainted(false);
	}

	public JMenuItem createMenuItem() {
		return  new JMenuItem(this);
	}

	
}
