package org.elusive.ui.keyboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.elusive.ui.tools.Tools;


public class KeyPanel extends JPanel {

	private String key;

	private Color colorKey = new Color(200, 200, 200);
	private Color colorKeyHover = new Color(170, 170, 170);
	private Color colorKeyPressed = new Color(100, 100, 100);

	public KeyPanel(final String key) {
		this.key = key;

		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setBackground(colorKey);
		this.setPreferredSize(new Dimension(40, 40));
		this.setMinimumSize(new Dimension(40, 40));
		this.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {
				KeyPanel.this.setBackground(colorKeyHover);
			}

			public void mousePressed(MouseEvent arg0) {
				KeyPanel.this.setBackground(colorKeyPressed);
			}

			public void mouseExited(MouseEvent arg0) {
				KeyPanel.this.setBackground(colorKey);
			}

			public void mouseEntered(MouseEvent arg0) {
				KeyPanel.this.setBackground(colorKeyHover);
			}

			public void mouseClicked(MouseEvent arg0) {
			}
		});
	}

	@Override
	public void paintComponent(Graphics g) {
		// System.out.println("paintComponent(key = "+key+")");
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.black);

		if (key.length() == 2) {
			g2.drawString(key.substring(0, 1), this.getWidth() / 2, 2 * this.getHeight() / 3);
			g2.drawString(key.substring(1, 2), this.getWidth() / 2, this.getHeight() / 3);

		} else {
			g2.drawString(key, this.getWidth() / 2, this.getHeight() / 2);
		}
	}
}
