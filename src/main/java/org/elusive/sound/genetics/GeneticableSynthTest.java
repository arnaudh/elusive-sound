package org.elusive.sound.genetics;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.elusive.main.persistence.xml.XmlTools;
import org.elusive.main.tools.IOtools;
import org.elusive.sound.blocs.additive.AdditiveSynth;
import org.elusive.sound.blocs.additive.FrequenceAmplitude;
import org.elusive.sound.blocs.frequenceur.Frequenceur;
import org.elusive.sound.genetics.ui.GeneticChoiceListener;
import org.elusive.sound.genetics.ui.GeneticExplorerPanel;
import org.elusive.sound.genetics.ui.GeneticableAction;
import org.elusive.sound.melody.tabs.TabPartition;
import org.elusive.sound.play.BufferedData;
import org.elusive.sound.play.PlayableAudio;
import org.elusive.sound.play.ui.PlayPanel;
import org.elusive.ui.resources.Icons;
import org.elusive.ui.tools.file.OpenSaveManager;

public class GeneticableSynthTest {
	

	public static void main(String[] args) {
		GeneticExplorer gen = new GeneticExplorer();
//		gen.setPopulationSize(1);
		
		AdditiveSynth synth = new AdditiveSynth();
		synth.addFrequenceAmplitude(new FrequenceAmplitude(200, 100));
		
		Frequenceur fre = new Frequenceur(synth);
		fre.setSize(0, 200000); //5 secondes
		gen.init(fre);

		List<GeneticableAction> actions = GeneticableAction.getDefaultBlocList();
		GeneticExplorerPanel genPanel = new GeneticExplorerPanel(gen, actions);
		
		
		JFrame frame = new JFrame();
		frame.getContentPane().add(genPanel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
