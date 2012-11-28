package org.elusive.main.historique;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import org.elusive.main.historique.action.ActionHistoriqueGrille;

public class ActionHistoriqueMultiple extends ActionHistorique {

	List<ActionHistorique> actions = new ArrayList<ActionHistorique>();
	private String name;
	
	public ActionHistoriqueMultiple(String name) {
		this.name = name;
	}
	
	public void addAction(ActionHistorique action){
		actions.add(action);
	}

	@Override
	public boolean doAction() {
		boolean undo = false;
		int i = 0;
		for (; i < actions.size(); i++) {
			ActionHistorique ah = actions.get(i);
			boolean b = ah.doAction();
			if( !b ){
				undo = true;
				break;
			}
		}
		if( undo ){
			for(; i >=0; i--){
				actions.get(i).doReverse();
			}
			return false;
		}
		return true;
	}

	@Override
	public void doReverse() {
		for (ActionHistorique ah : actions) {
			ah.doReverse();
		}		
	}

	@Override
	public Icon getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("nb actions : "+actions.size());
		return sb.toString();
	}

	@Override
	public String getName() {
		return name;
	}

}
