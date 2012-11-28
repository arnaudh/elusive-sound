package org.elusive.test.menu;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;


public class MonMenu extends MonMenuOuAction {
	
	public String name;
	public List<MonMenuOuAction> liste;

	public MonMenu(String name) {
		this.name = name;
		this.liste = new ArrayList<MonMenuOuAction>();
	}
	
	public void add( MonMenuOuAction ac ){
		liste.add(ac);
	}
	
	
	public JMenu toJMenu(){
		JMenu menu = new JMenu(name);
		for(MonMenuOuAction m : liste){
			if( m instanceof MonMenu){
				menu.add(((MonMenu) m).toJMenu());
			}else if( m instanceof MonAction ){
				menu.add(((MonAction) m).toJMenuItem());
			}else if( m instanceof MonSeparateur ){
				menu.addSeparator();
			}
		}
		return menu;
	}
	
	public static MonMenu merge(MonMenu m1, MonMenu m2){
		MonMenu menu = new MonMenu(m1.name);
		//TODO
		
		return menu;
	}
	

}
