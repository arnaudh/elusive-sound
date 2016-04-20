package org.elusive.sound.blocs.fm;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EventObject;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.CellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.elusive.ui.config.Colors;
import org.elusive.ui.widgets.FrequenceChooser;
import org.elusive.ui.widgets.SlowChangeListener;



public class OperateurEditor implements TreeCellEditor {

	private OperateurFM op;
	private JSpinner amplitude;
	private FrequenceChooser fc;
	public static Dimension dimPanel = new Dimension(400, 60);
	
	public OperateurEditor(){
	}
	
	public Component getTreeCellEditorComponent(final JTree tree, Object value,
			boolean isSelected, boolean expanded, boolean leaf, int row) {
		

		op = (OperateurFM) value;
		return createPanel(tree, op);

	}

	public void addCellEditorListener(CellEditorListener l) {
		// TODO Auto-generated method stub
		
	}

	public void cancelCellEditing() {
		// TODO Auto-generated method stub
		
	}

	//On s'en fout ??
	public Object getCellEditorValue() {
//		op.setIntensite((Integer) amplitude.getValue());
//		op.setFrequence((Double)fc.getValue());
//		return op;
		return null;
	}
	
	public boolean isCellEditable(EventObject event) {
        return true;

    }

	public void removeCellEditorListener(CellEditorListener l) {
		// TODO Auto-generated method stub
		
	}

	public boolean shouldSelectCell(EventObject anEvent) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean stopCellEditing() {
		System.out.println("OperateurRenderer.stopCellEditing()");
		op.setAmplitude((Integer) amplitude.getValue());
		op.setFrequence((Double)fc.getValue());
		return true;
	}
	
	public static JPanel createPanel(final JTree tree, final OperateurFM op){

//		SpinnerModel sm = new SpinnerNumberModel(op.getAmplitude(), 0, 100, 1);
		SpinnerModel sm = new SpinnerNumberModel(1, 0, 100, 1);
		final JSpinner amplitude = new JSpinner(sm);	
		amplitude.getEditor().setPreferredSize(new Dimension(60, 14)); //TODO
		final FrequenceChooser fc = new FrequenceChooser(op.getFrequence());
		
		JCheckBox ch_active = null;
		JButton plus = null;	
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		String str = "* sin( 2 Pi";
		JLabel label = new JLabel(str);

		ch_active = new JCheckBox();
		ch_active.setSelected(op.isActif());		
		ch_active.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean selected = ((JCheckBox)e.getSource()).isSelected();
				op.setActif(selected);
				op.updated();
			}
		});
		
		plus = new JButton("+");
		plus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				OperateurFM op2 = new OperateurFM(10, 10);

				DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();				
				treeModel.insertNodeInto(op2, op, op.getChildCount());
				
				//TODO? Expand Path adéquat
				for (int i = 0; i < 100; i++) {
					tree.expandRow(i);
				}
				op.updated();
			}
		});		
		plus.setPreferredSize(new Dimension(20, 20));
		
		panel.add(ch_active);
		panel.add(amplitude);		
		panel.add(label);
		panel.add(fc);
		panel.add(plus);
//		panel.setMaximumSize(new Dimension(0, 5));
		panel.setPreferredSize(OperateurEditor.dimPanel);
		
		//Design
		
		panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		panel.setBackground(Colors.COLOR_SYNTHETISEUR_FM);
		ch_active.setBackground(Colors.COLOR_SYNTHETISEUR_FM);
		amplitude.setBackground(Colors.COLOR_SYNTHETISEUR_FM);
		fc.setBackground(Colors.COLOR_SYNTHETISEUR_FM);
		plus.setBackground(Colors.COLOR_SYNTHETISEUR_FM);
		
		
		amplitude.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				op.setAmplitude((Integer) amplitude.getValue());
				op.setFrequence((Double)fc.getValue());
			}
		});
		fc.addSpinnerListener(new SlowChangeListener(200) {			
			@Override
			public void slowStateChanged(ChangeEvent e) {
				op.setAmplitude((Integer) amplitude.getValue());
				op.setFrequence((Double)fc.getValue());
				
			}
		});
		
		return panel;
	}


}
