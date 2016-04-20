package org.elusive.sound.genetics.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.elusive.sound.genetics.GeneticExplorer;
import org.elusive.sound.genetics.GeneticableTest;

public class GeneticExplorerPanel extends JPanel {

	private GeneticExplorer genExp;

	// UI
	private List<GeneticChoicePanel> panels;
	private JButton previous;
	private JButton next;

	public GeneticExplorerPanel(GeneticExplorer gen, List<GeneticableAction> actionsChoicePanel) {
		super();
		this.genExp = gen;
		gen.setPanel(this);

		JPanel barreControle = new JPanel();
		previous = new JButton("<");
		next = new JButton(">");
		previous.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				genExp.getHistory().previous();
				updatedGenExpPanel();
			}
		});
		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				genExp.getHistory().next();
				updatedGenExpPanel();
			}
		});
		barreControle.add(previous);
		barreControle.add(next);

		JPanel panelChoices = new JPanel();
		panelChoices.setLayout(new GridLayout(0, (int) Math.sqrt(gen.getPopulationSize())));
		panels = new ArrayList<GeneticChoicePanel>();
		for (int i = 0; i < gen.getPopulationSize(); i++) {
			GeneticChoicePanel pan = new GeneticChoicePanel(gen, actionsChoicePanel);
			panels.add(pan);
			panelChoices.add(pan);
		}

		updatedGenExpPanel();

		// LAYOUT
		this.add(barreControle);
		this.add(panelChoices);
	}

	public void updatedGenExpPanel() {
		for (int i = 0, n = genExp.getPopulation().size(); i < n; i++) {
			panels.get(i).setGeneticable(genExp.getPopulation().get(i));
		}
		previous.setEnabled(genExp.getHistory().isPreviousPossible());
		next.setEnabled(genExp.getHistory().isNextPossible());
	}

	public static void main(String[] args) {
		GeneticExplorer gen = new GeneticExplorer();
		gen.init(new GeneticableTest(100));
		
		GeneticExplorerPanel pan = new GeneticExplorerPanel(gen, null);

		JFrame frame = new JFrame();
		frame.getContentPane().add(pan);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
