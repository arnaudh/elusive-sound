package org.elusive.main.historique;

import javax.swing.tree.DefaultMutableTreeNode;



public class HistoryNode extends DefaultMutableTreeNode{

	private ActionHistorique ah = null;
	
	public HistoryNode(ActionHistorique ah){
		this.ah = ah;
	}
	
	public ActionHistorique getAction(){
		return this.ah;
	}
	
	public String toString(){
		if( ah == null ){
			return "NoeudRacine";
		}
		return ah.getName();
	}
	

	public static void afficheNoeud(HistoryNode node, int decalage){
		String str = "";
		for(int i = 0; i<decalage; i++){
			str += " ";
		}
		System.out.println(str+node);
		for(int i = 0; i<node.getChildCount(); i++){
			afficheNoeud((HistoryNode) node.getChildAt(i), decalage+3);
		}
	}
}
