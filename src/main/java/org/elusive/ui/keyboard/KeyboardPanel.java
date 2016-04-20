package org.elusive.ui.keyboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class KeyboardPanel extends JPanel {

	String[] keys1 = {"A", "Z", "E", "R", "T", "Y", "U", "I", "O", "P", "^¨", "$£"};
	String[] keys2 = {"Q", "S", "D", "F", "G", "H", "J", "K", "L", "M", "ù%", "*µ"};
	String[] keys3 = {"W", "X", "C", "V", "B", "N", ",?", ";.", ":/", "!§"};
	
	public KeyboardPanel() {
		super(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
//		gbc.gridheight = 1;
//		gbc.weightx = 1;
//		gbc.weighty = 1;
//		gbc.fill = GridBagConstraints.BOTH;
		
		for(String k : keys1){
			gbc.gridx +=3;
			KeyPanel key = new KeyPanel(k);
			System.out.println("add("+k+", "+gbc.gridx+")");
			this.add(key, gbc);
		}
		gbc.gridy++;
		gbc.gridx = 2;
		gbc.gridwidth = 4;
		for(String k : keys2){
			gbc.gridx +=3;
			KeyPanel key = new KeyPanel(k);
			System.out.println("add("+k+", "+gbc.gridx+")");
			this.add(key, gbc);
		}
		gbc.gridy++;
		gbc.gridx = 3;
		gbc.gridwidth = 3;
		for(String k : keys3){
			gbc.gridx +=3;
			KeyPanel key = new KeyPanel(k);
			System.out.println("add("+k+", "+gbc.gridx+")");
			this.add(key, gbc);
		}
		
	}
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame();
				frame.getContentPane().add(new KeyboardPanel());
				frame.setMinimumSize(new Dimension(600, 200));
				
				frame.setLocationRelativeTo(null);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);				
			}
		});
		
	}
}
