package org.elusive.sound.blocs.frequenceur;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.elusive.main.historique.action.AjouteBloc;
import org.elusive.main.listeners.BlocSelectedListener;
import org.elusive.sound.blocs.Bloc;
import org.elusive.sound.blocs.BlocPositionne;
import org.elusive.sound.blocs.additive.AdditiveSynth;
import org.elusive.sound.blocs.fm.FMSynthetizer;
import org.elusive.sound.blocs.karplusstrong.KarplusStrong;
import org.elusive.sound.melody.tabs.TabChord;
import org.elusive.sound.melody.tabs.TabChordChooserListener;
import org.elusive.ui.dndjtable.DragDropRowListener;
import org.elusive.ui.dndjtable.DragDropRowTableUI;
import org.elusive.ui.grille.Grid;

public class FrequencableManager extends JPanel {

	private JButton addButton;
	private JTable frequencableTable;
	private BlocSelectedListener blocListener;

	public FrequencableManager(BlocSelectedListener bsl) {
		super();
		this.setLayout(new GridBagLayout());
		this.blocListener = bsl;

		Object[][] data = new Frequencable[0][1];
		final DefaultTableModel dm = new DefaultTableModel(data, new String[] { "" }) {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};
		frequencableTable = new JTable(dm);
		frequencableTable.setUI(new DragDropRowTableUI());
		frequencableTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Point p = e.getPoint();
				if (e.getClickCount() == 2) {
					int row = frequencableTable.rowAtPoint(p);
					int column = frequencableTable.columnAtPoint(p);
					Frequencable fre = (Frequencable) frequencableTable.getValueAt(row, column);
					blocListener.blocSelected((Bloc) fre);
				}
			}
		});

		addButton = new JButton("+");
		addButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JPopupMenu popup = new JPopupMenu();
				for( final Class<? extends Frequencable> c : Bloc.listeFrequencables ){
					JMenuItem item = new JMenuItem(c.getSimpleName());
					item.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							try {
								Frequencable fre = c.newInstance();
								addFrequencable(fre);
							} catch (InstantiationException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}
						}
					});
					popup.add(item);
				}
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(addButton, gbc);
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		this.add(frequencableTable, gbc);
	}

	public void addFrequencable(Frequencable fre){
		((DefaultTableModel) frequencableTable.getModel()).addRow(new Frequencable[] { fre });
	}

	public static void main(String[] args) {

		FrequencableManager fm = new FrequencableManager(new BlocSelectedListener() {
			@Override
			public void blocSelected(Bloc bloc) {
				System.out.println("FrequencableManager.main(...).new BlocSelectedListener() {...}.blocSelected() : " + bloc);
			}
		});

		JFrame frame = new JFrame();
		frame.getContentPane().add(fm);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}

}
