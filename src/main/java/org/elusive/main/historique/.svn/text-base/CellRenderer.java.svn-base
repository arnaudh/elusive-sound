package org.elusive.main.historique;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;



public class CellRenderer implements TreeCellRenderer {

	private History historique;
	public CellRenderer(History historique) {
		this.historique = historique;
	}
	
	public Component getTreeCellRendererComponent(JTree arg0, Object arg1,
			boolean arg2, boolean arg3, boolean arg4, int arg5, boolean arg6) {

		
		HistoryNode node = (HistoryNode) arg1;
		ActionHistorique action = node.getAction();
		JPanel panel = new JPanel();
		JLabel label;
		if( action == null ){
			label = new JLabel("Racine");
		}else{
			Icon icon = action.getIcon();
			JLabel labelIcon = new JLabel(icon);
			panel.add(labelIcon);
			StringBuffer sb = new StringBuffer();
			if( node == historique.getNoeudCourant() ){
				panel.setBackground(new Color(200, 200, 255));
			}else if( node == historique.getNoeudRetablir() ){
				panel.setBackground(new Color(180, 230, 230));
			}
			sb.append(action.getName()+" "+action.getInfo());
			label = new JLabel(sb.toString());
		}
		
		panel.add(label);
		
		
		return panel;
	}

}
