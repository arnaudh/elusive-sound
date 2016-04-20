package org.elusive.ui.status;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.text.StyledEditorKit.BoldAction;

/**
 * @author Arnaud
 * 
 */
public class StatusBar extends JPanel {

	private JLabel status = new JLabel();
	private JProgressBar progressBar = new JProgressBar(
			JProgressBar.HORIZONTAL, 0, 100);
	
	public StatusBar() {
		super(new BorderLayout());
		this.setPreferredSize( new Dimension(100, 25) );
		this.add(status, BorderLayout.LINE_START);
	}

	public void setStatus(String statusText) {
		status.setText(statusText);
	}

	public String getStatus() {
		return status.getText();
	}

	public void setProgress(int value) {
		progressBar.setValue(value);
		if (value == 100) {
			if (progressBar.getParent() == this) {
				this.remove(progressBar);
				this.revalidate();
				this.repaint();
			}
		} else if (progressBar.getParent() == null) {
			this.add(progressBar, BorderLayout.LINE_END);
			this.revalidate();
			this.repaint();
		}
	}

//	public static void main(String[] args) {
//
//		JFrame f = new JFrame();
//
//		StatusBar statusBar = new StatusBar();
//		JPanel contentPane = new JPanel(new BorderLayout());
//		contentPane.add(statusBar, BorderLayout.SOUTH);
//		f.setContentPane(contentPane);
//
//		f.setMinimumSize(new Dimension(600, 500));
//		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		f.setLocationRelativeTo(null);
//		f.setAlwaysOnTop(true);
//		f.setVisible(true);
//		
//		statusBar.setProgress(50);
////		statusBar.setBorder(BorderFactory.createLineBorder(Color.black));
//
//		Scanner in = new Scanner(System.in);
//
//		String line;
//		while ((line = in.next()) != null) {
//			switch (line.length()) {
//			case 1:
//			case 2:
//			case 3:
//				Integer i = Integer.parseInt(line);
//				statusBar.setProgress(i);
//				break;
//			default:
//				statusBar.setStatus(line);
//			}
//		}
//	}

}
