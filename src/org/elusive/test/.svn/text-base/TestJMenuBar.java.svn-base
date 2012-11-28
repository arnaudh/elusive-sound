package org.elusive.test;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.KeyStroke;

public class TestJMenuBar extends javax.swing.JFrame {
	public TestJMenuBar() {
		super("TestJMenuBar");

		javax.swing.JMenuBar sbar = new javax.swing.JMenuBar();
		javax.swing.JMenu smenu = new javax.swing.JMenu("South menu");
		javax.swing.JMenuItem item = new javax.swing.JMenuItem("item");
		item.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("iem SOUTH");
			}
		});
		smenu.add(item);
		sbar.add(smenu);

		javax.swing.JMenuBar cbar = new javax.swing.JMenuBar();
		javax.swing.JMenu cmenu = new javax.swing.JMenu("Center menu");
		javax.swing.JMenuItem citem = new javax.swing.JMenuItem("item");
		citem.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		citem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("iem CENTER");
			}
		});
		cmenu.add(citem);
		cbar.add(cmenu);

		javax.swing.JPanel panel = new javax.swing.JPanel();
		panel.setLayout(new java.awt.BorderLayout());
		panel.add(new javax.swing.JLabel("north label in JPanel"), java.awt.BorderLayout.NORTH);
		panel.add(cbar, java.awt.BorderLayout.CENTER);
		panel.setBorder(new javax.swing.border.LineBorder(java.awt.Color.RED));

		java.awt.Container pane = getContentPane();
		pane.setLayout(new java.awt.BorderLayout());
		pane.add(new javax.swing.JLabel("north label"), java.awt.BorderLayout.NORTH);
		pane.add(new javax.swing.JLabel("west label"), java.awt.BorderLayout.WEST);
		pane.add(panel, java.awt.BorderLayout.CENTER);
		pane.add(sbar, java.awt.BorderLayout.SOUTH);
	}

	public static void main(String[] args) {
		TestJMenuBar app = new TestJMenuBar();
		app.setSize(300, 300);
		app.setDefaultCloseOperation(EXIT_ON_CLOSE);
		app.setVisible(true);
	}
}