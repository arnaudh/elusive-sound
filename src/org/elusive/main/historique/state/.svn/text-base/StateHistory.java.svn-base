package org.elusive.main.historique.state;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class StateHistory<T> {
	
	
	private DefaultMutableTreeNode root;
	private DefaultMutableTreeNode current;
	private DefaultMutableTreeNode redoNode;
	
	private StateHistoryListener<T> listener;
	
	
	public StateHistory(StateHistoryListener<T> listener){
		this.listener = listener;
		root = new DefaultMutableTreeNode();
		current = root;
		redoNode = current;
	}
	
	public void clear(){
		root = new DefaultMutableTreeNode();
		current = root;
		redoNode = current;
	}
	
	public void add(T object){
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(object);
		current.add(node);
		current = node;
		redoNode = node;
	}
	
	public boolean isPreviousPossible(){
		return current.getParent() != root;
	}
	
	public void previous(){
		if( !isPreviousPossible() ){
			return;
		}
		TreeNode parent = current.getParent();
		if( parent != null ){
			current = (DefaultMutableTreeNode) parent;
		}
		listener.stateHasChanged( (T) current.getUserObject() );
	}
	
	public boolean isNextPossible(){
		return current != redoNode;
	}
	
	public void next(){
		if( !isNextPossible() ){
			return;
		}
		current = getNextNode();
		listener.stateHasChanged( (T) current.getUserObject() );
	}
	
	private DefaultMutableTreeNode getNextNode(){
		DefaultMutableTreeNode son = redoNode;
		while( son.getParent() != current ){
			son = (DefaultMutableTreeNode) son.getParent();
		}
		return son;
	}

}
