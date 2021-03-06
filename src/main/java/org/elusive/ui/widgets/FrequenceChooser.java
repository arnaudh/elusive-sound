package org.elusive.ui.widgets;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import org.elusive.sound.melody.notes.Notes;
import org.elusive.ui.grille.Grid;
import org.elusive.ui.resources.Icons;



public class FrequenceChooser extends JPanel {

	private JSpinner spinner = null;
	private Dimension dimSpinner = new Dimension(60, 14);
	private JButton machin;
	private JButton machin2;
	
	public FrequenceChooser(double val){
		SpinnerModel sm = new SpinnerNumberModel(val, 0.0, 100000.0, 1);
		spinner = new JSpinner(sm);
		
		spinner.getEditor().setPreferredSize(dimSpinner);

		
		machin = new JButton(Icons.FREQUENCE_ICON);
		machin.setPreferredSize(new Dimension(Icons.FREQUENCE_ICON.getIconWidth()+6, Icons.FREQUENCE_ICON.getIconHeight()+6));
		machin.addActionListener(new FrequenceMouseListener(this, machin));
		
		machin2 = new JButton(Icons.FREQUENCE_ICON);
		machin2.setPreferredSize(new Dimension(Icons.FREQUENCE_ICON.getIconWidth()+6, Icons.FREQUENCE_ICON.getIconHeight()+6));
		machin2.addActionListener(new TempoMouseListener(this, machin2));
		
		spinner.getEditor().setLocale(Locale.ENGLISH);

		this.add(spinner);
		this.add(machin);
//		this.add(machin2);
		
	}
	
	public double getValue(){
		return (Double) spinner.getValue();
	}
	
	public void addSpinnerListener(SlowChangeListener sl){
		spinner.addChangeListener(sl);
	}

	public void setValue(double val) {
		spinner.setValue(val);	
	}

	
	@Override
	public void setBackground(Color c){
		super.setBackground(c);
		if( machin != null ){
			machin.setBackground(c);
			machin2.setBackground(c);
		}
	}
}


class FrequenceMouseListener implements ActionListener {

	private Popup pop = null;
	private Component parent;
	private JTable table;
	
	
	public FrequenceMouseListener(final FrequenceChooser fc, Component parent){
		this.parent = parent;
		
		final NumberFormat nf = new DecimalFormat("# ###.###");
		table = new JTable(Notes.nbNotes+1, Notes.nbOctaves+1);
		table.setCellSelectionEnabled(true);
		table.addMouseListener(new MouseListener() {			
			public void mouseReleased(MouseEvent e) {}			
			public void mousePressed(MouseEvent e) {}			
			public void mouseExited(MouseEvent e) {}			
			public void mouseEntered(MouseEvent e) {}			
			public void mouseClicked(MouseEvent e) {
				Point p = e.getPoint();
				int row = table.rowAtPoint(p);
				int column = table.columnAtPoint(p);
				if( row != 0 && column != 0 ){
					String str = (String) table.getValueAt(row, column);
					try {
						Number value = nf.parse(str);
						double d = value.doubleValue();
						fc.setValue(d);
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					pop.hide();
				}
			}
		});
		//Clavier à gauche
		for(int note = 0; note < Notes.nbNotes; note++){
			String nom = Notes.nomsSimples[note];
			nom = " "+nom;
			if( nom.endsWith("#") ){
				//TODO : case noire
			}
			table.setValueAt(nom, note+1, 0);
			
		}

		//Octaves en haut
		for(int octave = Notes.premiereOctave; octave <= Notes.derniereOctave; octave++){
			table.setValueAt(octave, 0, octave - Notes.premiereOctave + 1);
			
		}
		
		
		//Fréquences
		for(int octave = 0; octave < Notes.nbOctaves; octave++){
			for(int note = 0; note < Notes.nbNotes; note++){
				//JLabel label = new JLabel(Note.noms[octave][note]);
				String str = nf.format(Notes.frequences[octave][note]);
				//str = str.replace(',', ' ');
				table.setValueAt(str, note+1, octave+1);
			}
		}
		
		table.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {
				pop.hide();
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
		});
		
		table.setBorder(BorderFactory.createLineBorder(Color.gray));
	}
	
	public void actionPerformed(ActionEvent e) {
		System.out.println("FrequenceChooser : mouseClicked");
		if( pop != null ){
			pop.hide();
		}
		PopupFactory popupFactory = PopupFactory.getSharedInstance();		

		int largeurPrevue = 910;
		int hauteurPrevue = 210;
		int x =  parent.getLocationOnScreen().x+parent.getWidth() - largeurPrevue/2;
		int y = parent.getLocationOnScreen().y - hauteurPrevue/2;
		x = Math.max(x, 10);
		x = Math.min(x, Toolkit.getDefaultToolkit().getScreenSize().width - largeurPrevue);
		y = Math.max(y, 10);
		y = Math.min(y, Toolkit.getDefaultToolkit().getScreenSize().height - hauteurPrevue);
		pop = popupFactory.getPopup(parent, table, x, y);
		pop.show();
		
	}


}



class TempoMouseListener implements ActionListener {

	private Popup pop = null;
	private JTable table;
	private Component parent;
	
	public TempoMouseListener(final FrequenceChooser fc, Component parent) {
		this.parent = parent;

		
		table = new JTable(2, 10);
		table.setCellSelectionEnabled(true);
		table.addMouseListener(new MouseAdapter() {			
			public void mouseClicked(MouseEvent e) {
				Point p = e.getPoint();
				int row = table.rowAtPoint(p);
				int column = table.columnAtPoint(p);
				if( row != 0 && column != 0 ){
					Double value = (Double) table.getValueAt(row, column);
					fc.setValue(value);
					pop.hide();
				}
			}
		});
		table.setRowHeight(20);

		table.setValueAt(" tempo", 0, 0);
		table.setValueAt(" fréquence", 1, 0);
		int i = 1;
		for(double t = 1.0/64; t <= 4; t *= 2, i++ ){
			String s = "";
			if( t < 1 ){
				s = "1/"+(int)(1/t);
			}else{
				s = ""+(int)t;
			}
			table.setValueAt(s, 0, i);
			
			double val = Grid.getTempo().getFrequence() / t;
			table.setValueAt(val, 1, i);
			
		}

		table.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {
				pop.hide();
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
		});
		
		table.setBorder(BorderFactory.createLineBorder(Color.gray));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if( pop != null ){
			pop.hide();
		}
		PopupFactory popupFactory = PopupFactory.getSharedInstance();		

		int largeurPrevue = 750;
		int hauteurPrevue = 35;
		int x =  parent.getLocationOnScreen().x+parent.getWidth() - largeurPrevue/2;
		int y = parent.getLocationOnScreen().y - hauteurPrevue/2;
		x = Math.max(x, 10);
		x = Math.min(x, Toolkit.getDefaultToolkit().getScreenSize().width - largeurPrevue);
		y = Math.max(y, 10);
		y = Math.min(y, Toolkit.getDefaultToolkit().getScreenSize().height - hauteurPrevue);
		pop = popupFactory.getPopup(parent, table, x, y);
		pop.show();
		
	}
	
	
}

