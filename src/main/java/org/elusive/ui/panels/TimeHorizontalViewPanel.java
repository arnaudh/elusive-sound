package org.elusive.ui.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;

import org.elusive.sound.tools.TimeFormat;
import org.elusive.ui.tools.JFrameTools;

public class TimeHorizontalViewPanel extends HorizontalViewPanel {

	public static void main(String[] args) {
		TimeHorizontalViewPanel panel = new TimeHorizontalViewPanel();
		JFrameTools.show(panel).setSize(500, 300);
	}
	
	protected TimeFormat format = TimeFormat.TEMPO;
	
	//UI
	protected JComboBox formatComboBox;
	
	public TimeHorizontalViewPanel() {
		formatComboBox = new JComboBox(TimeFormat.getStrings());
		formatComboBox.setSelectedItem(format.toString());
		formatComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				format = TimeFormat.values()[formatComboBox.getSelectedIndex()];
				repaint();
			}
		});
		
		//Layout
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createLineBorder(Color.RED));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.SOUTHEAST;
	    gbc.weighty = 1;
	    gbc.weightx = 1;
	    gbc.insets = new Insets(0, 0, 12, 0);
		this.add(formatComboBox, gbc);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		//change scale parameters
		
		super.paintComponent(g);
		
		//put back scale parameters
	}
	
}
