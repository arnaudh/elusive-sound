package org.elusive.ui.tools;

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
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.elusive.main.listeners.BlocSelectedListener;
import org.elusive.sound.blocs.Bloc;
import org.elusive.ui.dndjtable.DragDropRowTableUI;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.EventTableModel;

public class BlocListManager extends JPanel {

	// 
	private EventList<Bloc> blocList = new BasicEventList<Bloc>(); //the data
	private List<Class<? extends Bloc>> classes = Bloc.liste;
	private List<BlocSelectedListener> listeners = new ArrayList<BlocSelectedListener>();
	
	// UI
	private JButton addButton;
	private JTable blocsTable;
	private JPanel panelBloc;
	

	public BlocListManager(EventList<Bloc> blocList, List<Class<? extends Bloc>> classes) {
		this.blocList = blocList;
		this.classes = classes;

//		Object[][] data = new Bloc[0][1];
//		final DefaultTableModel dm = new DefaultTableModel(data, new String[] { "" }) {
//			public boolean isCellEditable(int rowIndex, int mColIndex) {
//				return false;
//			}
//		};
		
		TableFormat<Bloc> format = new TableFormat<Bloc>() {
			@Override
			public Object getColumnValue(Bloc baseObject, int column) {
				// TODO Auto-generated method stub
				switch (column) {
				case 0:
					char c = (char) ('A' + BlocListManager.this.blocList.indexOf(baseObject));
					return c;
				default: 
					return baseObject;
				}
			}
			@Override
			public String getColumnName(int column) {
				return null;
			}
			@Override
			public int getColumnCount() {
				return 2;
			}
		};
		EventTableModel<? extends Bloc> model = new EventTableModel<Bloc>(blocList, format);
		blocsTable = new JTable(model);
//		blocsTable.setUI(new DragDropRowTableUI());
		blocsTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Point p = e.getPoint();
				if (e.getClickCount() == 2) {
					int row = blocsTable.rowAtPoint(p);
					int column = blocsTable.columnAtPoint(p);
					Bloc bloc = (Bloc) blocsTable.getValueAt(row, 1);
					panelBloc.removeAll();
					panelBloc.add(bloc.getElusivePanel());
					panelBloc.revalidate();
					panelBloc.repaint();
					for( BlocSelectedListener lis : listeners ){
						lis.blocSelected(bloc);
					}
				}
			}
		});

		addButton = ButtonTools.createPlusButton();
		addButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (BlocListManager.this.classes.size() == 1) {
					try {
						Bloc bloc = (Bloc) BlocListManager.this.classes.get(0).newInstance();
						addBloc(bloc);
					} catch (InstantiationException ex) {
						ex.printStackTrace();
					} catch (IllegalAccessException ex) {
						ex.printStackTrace();
					}
				} else {
					JPopupMenu popup = new JPopupMenu();
					for (final Class c : BlocListManager.this.classes) {
						JMenuItem item = new JMenuItem(c.getSimpleName());
						item.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								try {
									Bloc bloc = (Bloc) c.newInstance();
									addBloc(bloc);
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
			}
		});
		
		panelBloc = new JPanel(new BorderLayout());
		
		// LAYOUT
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(addButton, gbc);
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		this.add(blocsTable, gbc);
		gbc.gridy = 2;
		gbc.weightx = 1;
		gbc.weighty = 2;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(panelBloc, gbc);
	}

	public void addBloc(Bloc bloc) {
		//TODO change
//		((DefaultTableModel) blocsTable.getModel()).addRow(new Bloc[] { bloc });
		blocList.add(bloc);
	}

	public EventList<? extends Bloc> getBlocList() {
		return blocList;
	}

	public void setBlocList(EventList<Bloc> blocList) {
		this.blocList = blocList;
	}

	public List<Class<? extends Bloc>> getClasses() {
		return classes;
	}

	public void setClasses(List<Class<? extends Bloc>> classes) {
		this.classes = classes;
	}
	
	public void addBlocSelectedListener( BlocSelectedListener listener ){
		listeners.add(listener);
	}

}
