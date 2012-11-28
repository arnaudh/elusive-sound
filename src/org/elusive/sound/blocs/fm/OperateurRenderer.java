package org.elusive.sound.blocs.fm;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTree;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;

import org.elusive.main.historique.CellRenderer;
import org.elusive.ui.widgets.FrequenceChooser;




public class OperateurRenderer implements TreeCellRenderer {
	
	private OperateurFM op = null;
	private JCheckBox ch_active = null;
	private JSpinner amplitude = null;
	private FrequenceChooser fc = null;
	private JButton plus = null;
	
	private OperateurFM parent = null;

	public Component getTreeCellRendererComponent(final JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		op = (OperateurFM) value;
		return OperateurEditor.createPanel(tree, op);
		
		/*
		OperateurFM op = (OperateurFM) value;
		
		JPanel panel = null;
		if( false ){
			panel = new JPanel();
			JLabel label = new JLabel("Root");
			panel.add(label);
			
		}else{

			panel = new JPanel();
			String str = op.getIntensite() + " * sin( 2Pi * "+ op.getFrequence()+" * t";
			if( op.isLeaf() ){
				str += " )";
			}else{
				str += " + ";
			}
			JLabel label = new JLabel(str);


			panel.add(label);
			// Dimension dim = new Dimension(200, 100);
		}
		
		panel.setPreferredSize(dimPanel);
		
		return panel;
		//*/
	}
	
	

}
