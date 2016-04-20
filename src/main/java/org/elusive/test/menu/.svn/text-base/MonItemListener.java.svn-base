package org.elusive.test.menu;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;

public abstract class MonItemListener implements ItemListener {
	
	public List<JMenuItem> items = new ArrayList<JMenuItem>();

	@Override
	public void itemStateChanged(ItemEvent e) {
		System.out.println("MonItemListener.itemStateChanged()");
		boolean selected = e.getStateChange() == 1 ? true : false;
		for(JMenuItem it : items){
			if( it.isSelected() != selected ){
				System.out.println(it+" different ");
				it.setSelected(selected);
			}
		}
		stateChanged(selected);
		
	}
	
	public abstract void stateChanged(boolean selected);

}
