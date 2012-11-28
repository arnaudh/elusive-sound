package org.elusive.sound.genetics.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.elusive.sound.genetics.Geneticable;

public class GeneticChoicePanel extends JPanel {

	private Geneticable gen;
	private JButton chooseButton;
	private JButton plusButton;
	private GeneticChoiceListener listener;
	

	public GeneticChoicePanel(GeneticChoiceListener genListener, final List<GeneticableAction> actions) {
		this.listener = genListener;
		chooseButton = new JButton("choose");
		chooseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				listener.geneticableChosen(gen);
			}
		});
		if( actions != null ){
			plusButton = new JButton("+");
			plusButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					JPopupMenu popup = new JPopupMenu();
					for(GeneticableAction ac : actions){
						JMenuItem item = new JMenuItem(ac);
						ac.setGeneticable(gen);
						popup.add(item);
					}
					popup.show(plusButton, e.getX(), e.getY());
				}
			});
		}

		// Layout
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(chooseButton);
		if( plusButton != null ){
			this.add(plusButton);
		}
		this.setBorder(BorderFactory.createLineBorder(Color.white, 1));
		
	}

	public void setGeneticable(Geneticable geneticable) {
		if (gen != null) {
			this.remove(gen.getPreviewPanel());
		}
		this.gen = geneticable;
		this.add(gen.getPreviewPanel());
		this.revalidate();
		this.repaint();
	}

	
	

}
