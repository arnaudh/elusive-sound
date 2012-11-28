package org.elusive.sound.melody.tabs.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.elusive.sound.melody.tabs.TabChord;
import org.elusive.sound.melody.tabs.TabChordChooserListener;

public class TabChordChooser extends JPanel {

	// listeners
	private List<TabChordChooserListener> listeners = new ArrayList<TabChordChooserListener>();

	// UI
	private JTextField searchField;
	private JCheckBox regexCheckbox;
	private JTable resultsTable;
	private TableRowSorter<TableModel> sorter;

	private static final Color colorError = new Color(250, 100, 100);

	public TabChordChooser(TabChordChooserListener listener) {
		this();
		listeners.add(listener);
	}

	public TabChordChooser() {
		super();

		searchField = new JTextField(1);
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				filter();
			}
		});
		searchField.requestFocusInWindow();

		regexCheckbox = new JCheckBox("regex");
		regexCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				filter();
			}
		});

		List<TabChord> chords = TabChord.getAllChords();
		Object[][] data = new TabChord[chords.size()][1];
		for (int i = 0, n = chords.size(); i < n; i++) {
			data[i][0] = chords.get(i);
		}
		TableModel dm = new DefaultTableModel(data, new String[] { "" }){
		    public boolean isCellEditable(int rowIndex, int mColIndex) {
		        return false;
		    }
		};
		resultsTable = new JTable(dm);
		resultsTable.addMouseListener(new MouseAdapter() {			
			public void mouseClicked(MouseEvent e) {
				Point p = e.getPoint();
				if( e.getClickCount() == 2 ){
					int row = resultsTable.rowAtPoint(p);
					int column = resultsTable.columnAtPoint(p);
					TabChord tabChord = (TabChord) resultsTable.getValueAt(row, column);
					for(TabChordChooserListener listener : listeners){
						listener.tabChordChosen(tabChord);
					}
				}
			}
		});
		sorter = new TableRowSorter<TableModel>(dm);
		resultsTable.setRowSorter(sorter);

		JScrollPane scrollPane = new JScrollPane(resultsTable);

		// LAYOUT
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0; 
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL; 
		this.add(searchField, gbc);
		gbc.gridx = 1; 
		gbc.weightx = 0;
		this.add(regexCheckbox, gbc);
		gbc.gridx = 0; 
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.BOTH; 
		this.add(scrollPane, gbc);
	}
	
	private void filter(){
		searchField.setBackground(Color.white);
		String regex = searchField.getText();
		if (!regexCheckbox.isSelected()) {
			regex = Pattern.quote(regex);
		}
		try {
			RowFilter<? super TableModel, ? super Integer> filter = RowFilter
					.regexFilter(regex);
			sorter.setRowFilter(filter);
		} catch (PatternSyntaxException e) {
			searchField.setBackground(colorError);
			return;
		}
	}
	
	

//	public static void main(String[] args) {
//
//		JFrame frame = new JFrame();
//
//		TabChordChooser chooser = new TabChordChooser();
//		frame.add(chooser);
//
//		frame.pack();
//		frame.setLocationRelativeTo(null);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setVisible(true);
//	}

}
