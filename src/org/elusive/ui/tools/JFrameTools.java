package org.elusive.ui.tools;

import java.awt.Component;

import javax.swing.JFrame;

public class JFrameTools {

	public static JFrame show( Component c ){
		return show(c, false);
	}
	
	public static JFrame show(Component c, boolean alwaysOnTop){
		JFrame frame = new JFrame();
		frame.getContentPane().add(c);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setAlwaysOnTop(alwaysOnTop);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		return frame;
	}
}
