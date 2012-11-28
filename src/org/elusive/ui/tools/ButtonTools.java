package org.elusive.ui.tools;

import java.awt.Dimension;

import javax.swing.JButton;

public class ButtonTools {

	
	public static JButton createPlusButton(){
		JButton plus = new JButton("+");
		plus.setPreferredSize(new Dimension(15, 15));
		return plus;
	}
}
