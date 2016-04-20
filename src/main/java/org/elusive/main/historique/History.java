package org.elusive.main.historique;


import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.tree.DefaultTreeModel;

import org.elusive.ui.action.MyAction;
import org.elusive.ui.action.shortcut.MyKeyStrokes;
import org.elusive.ui.fenetre.ShowableMenuFrame;
import org.elusive.ui.resources.Icons;

public class History {

	private JScrollPane pane = null;
	private JTree tree = null;
	private HistoryNode rootNode = null;
	private HistoryNode currentNode = null;
	private HistoryNode noeudRetablir = null;
	
	private ShowableMenuFrame showableFrame;

	private MyAction undoAction;
	private MyAction redoAction;
	private boolean changeActionNames;
	
	private int nbRow = 0;
	
	private List<HistoryListener> listeners = new ArrayList<HistoryListener>();

	public History(){
		this(false);
	}
	public History(boolean changeActionNames){
		rootNode = new HistoryNode(null);
		currentNode = rootNode;
		tree = new JTree(currentNode);
		tree.setCellRenderer(new CellRenderer(this));
		pane = new JScrollPane(tree);
		this.changeActionNames = changeActionNames;

		// Actions
		undoAction = new MyAction("Undo", MyKeyStrokes.UNDO_KEY_STROKE, "Undo", Icons.UNDO_ICON ) {
			@Override
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		};
		redoAction = new MyAction("Redo", MyKeyStrokes.REDO_KEY_STROKE, "Redo", Icons.REDO_ICON ) {
			@Override
			public void actionPerformed(ActionEvent e) {
				redo();
			}
		};
		undoAction.setEnabled(false);
		redoAction.setEnabled(false);
		
		// Showable frame
		showableFrame = new ShowableMenuFrame("History");
//		showableFrame.setMinimumSize(new Dimension(400, 500));
		showableFrame.add(this.getPanel());

	}
	
	public boolean execute(ActionHistorique ah){
		boolean b = ah.doAction();
		if( b ){
			noeudRetablir = new HistoryNode(ah);			
			currentNode.add(noeudRetablir);
			tree.expandRow(nbRow++);
			currentNode = noeudRetablir;
			updateListeners();
			updateUI();
		}
		return b;
	}
	
	public boolean undoable(){
		return (currentNode.getAction() != null);
	}
	
	public void undo(){
		assert( undoable() );
		currentNode.getAction().doReverse();
		currentNode = (HistoryNode) currentNode.getParent();
		updateListeners();
		updateUI();
	}
	
	private void updateListeners() {
		for (HistoryListener hl : listeners) {
			hl.afterAction();
		}		
	}

	public boolean redoable(){
		return (noeudRetablir != currentNode);
	}
	
	public void redo(){
		assert( redoable() );
		//trouve le noeud fils de noeudCourant qui est noeudRetablir ou un ancêtre de noeudRetablir
		HistoryNode noeudARetablir = getNoeudARetablir();
		noeudARetablir.getAction().doAction();
		currentNode = noeudARetablir;		
		updateListeners();
		updateUI();
	}

	private HistoryNode getNoeudARetablir() {
		HistoryNode noeudFils = noeudRetablir;
		while( noeudFils.getParent() != currentNode){
			noeudFils = (HistoryNode) noeudFils.getParent();
		}
		return noeudFils;
	}
	
	public void updateUI(){
		if( undoable() ){
			undoAction.setEnabled(true);
			if( changeActionNames ){
				undoAction.setName("Undo "+currentNode.getAction().getName());
			}
		}else{
			undoAction.setEnabled(false);
		}
		if( redoable() ){
			redoAction.setEnabled(true);
			if( changeActionNames ){
				redoAction.setName("Redo "+getNoeudARetablir().getAction().getName());
			}
		}else{
			redoAction.setEnabled(false);
		}
		tree.updateUI();
		pane.repaint();
	}
	
	public JScrollPane getPanel(){
		return pane;
	}

	public HistoryNode getNoeudCourant(){
		return currentNode;
	}
	public HistoryNode getNoeudRetablir(){
		return noeudRetablir;
	}
	
	public void addHistoriqueListener(HistoryListener hl){
		listeners.add(hl);
	}

	public void clear() {
		rootNode = new HistoryNode(null);
		currentNode = rootNode;
		noeudRetablir = null;
		((DefaultTreeModel)tree.getModel()).setRoot(rootNode);
	}

	public MyAction getUndoAction() {
		return undoAction;
	}

	public MyAction getRedoAction() {
		return redoAction;
	}

	public MyAction getShowAction() {
		return showableFrame.getShowAction();
	}
	
	
}
