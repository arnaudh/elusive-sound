package org.elusive.ui.grille.line;

import java.awt.event.ActionEvent;

import org.elusive.ui.action.MyCheckAction;
import org.elusive.ui.grille.Grid;

public class GridLine {
	
	// Grid
	private Grid grid;
	
	//parameters
	private boolean on = true;
	
	//actions
	private MyCheckAction onAction;
	
	public GridLine(Grid grid_) {
		this.grid = grid_;
		onAction = new MyCheckAction("On", null, null, null, null) {
			@Override
			public void actionPerformed(ActionEvent e) {
				on = !on;
				grid.repaint();
			}
		};
	}
	
	public MyCheckAction getOnAction() {
		return onAction;
	}

	public boolean isOn() {
		return on;
	}
	
	public void setOn(boolean on) {
		if( on != this.on ){
			onAction.actionPerformed(null);
		}
	}
	
	
}
