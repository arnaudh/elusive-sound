package org.elusive.sound.blocs.additive.sequencer;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;

import org.elusive.sound.blocs.Bloc;
import org.elusive.sound.blocs.BlocGenerateur;
import org.elusive.sound.blocs.additive.AdditiveSynth;
import org.elusive.sound.blocs.additive.FrequenceAmplitude;
import org.elusive.sound.blocs.frequenceur.Frequencable;
import org.elusive.sound.flow.BlocUpdatedListener;
import org.elusive.sound.genetics.Geneticable;
import org.elusive.sound.genetics.RandomTools;
import org.elusive.ui.fenetre.ElusivePanel;
import org.elusive.ui.tools.BlocListManager;
import org.elusive.ui.tools.ButtonTools;
import org.elusive.ui.tools.JFrameTools;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

public class AdditiveSynthSequencer extends BlocGenerateur implements Frequencable, BlocUpdatedListener{	
	
	//all the synths used
	private EventList<Bloc> synths = new BasicEventList<Bloc>();	
	private List<AdditiveSynthEntry> sequence = new ArrayList<AdditiveSynthEntry>();
	/* IDEA OF SEQUENCE TEXT INPUT (volume ?)
	A 0
	B 1000
	A 1000
	B 1000
	A 1000
	*/
	
	private double frequence = 440;
	
	public AdditiveSynthSequencer() {
		synths.addListEventListener(new ListEventListener<Bloc>() {
			@Override
			public void listChanged(ListEvent<Bloc> listChanges) {
				for( Bloc b : synths ){
					b.addBlocUpdatedListener(AdditiveSynthSequencer.this);
				}
				update();
			}
		});
	}

	@Override
	public float[] generateNextData(int size) {
		float[] data = new float[size];
		
		//le maxi synth doit contenir toutes les FrequenceAmplitudes des synth (et initialisé sur le premier)
		AdditiveSynth maxiSynth = new AdditiveSynth();
		int globalOffset = 0;
		
		for(AdditiveSynthEntry entry : sequence){
			AdditiveSynth synth = entry.getSynth();
//			System.out.println("adding synth : "+synth.toDetailedString());
			synth.setFrequenceNoUpdate(frequence);
			
			globalOffset += entry.getOffset();
			for( FrequenceAmplitude fa : synth.getFrequences()){
				maxiSynth.addFrequenceAmplitudeTime(fa.getFrequence(), fa.getAmplitude(), globalOffset);
			}
//			System.out.println("MAXI SYNTH : "+maxiSynth.toDetailedString());
		}
		

		data = maxiSynth.generateNextData(size);
		
		
		return data;
	}

	public void addSynth(AdditiveSynth synth){
		synths.add(synth);
	}
	public void removeSynth(AdditiveSynth synth){
		synths.remove(synth);
	}
	
	@Override
	public void setFrequenceNoUpdate(double frequence) {
		this.frequence = frequence;
	}

	@Override
	public double getFrequence() {
		return frequence;
	}

	private transient ElusivePanel elusivePanel;
	@Override
	protected ElusivePanel createElusivePanel() {
		elusivePanel = new ElusivePanel("Additive Synth Sequencer");
		
		List<Class<? extends Bloc>> classes = new ArrayList<Class<? extends Bloc>>();
		classes.add(AdditiveSynth.class);
		BlocListManager manager = new BlocListManager(synths, classes);
				
		AdditiveSynthSequenceEditor sequenceEditor = new AdditiveSynthSequenceEditor(this);
		
		// LAYOUT
		elusivePanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		elusivePanel.add(manager);
		gbc.gridx = 1;
		gbc.weightx = 0;
		elusivePanel.add(sequenceEditor, gbc);
		
		return elusivePanel;
	}
	

	@Override
	public Geneticable mutate(double strength) {
		AdditiveSynthSequencer seq = new AdditiveSynthSequencer();
		for( Bloc bloc : synths ){
			AdditiveSynth synth = (AdditiveSynth) bloc.mutate(strength);
			seq.addSynth(synth);
		}
		for( AdditiveSynthEntry entry : sequence ){
				AdditiveSynth synth = (AdditiveSynth) seq.getSynths().get( synths.indexOf(entry.getSynth()) );
				AdditiveSynthEntry e = new AdditiveSynthEntry(synth, (int) RandomTools.modify(entry.getOffset(), strength) );
				seq.getSequence().add(e);
		}
		System.out.println("mutated to : "+seq);
		return seq;
	}

	@Override
	public void blocUpdated(Bloc b) {
		//synth updated
		this.update();
	}

	public List<Bloc> getSynths() {
		return synths;
	}

	public List<AdditiveSynthEntry> getSequence() {
		return sequence;
	}

	public void setSequence(List<AdditiveSynthEntry> sequence) {
		this.sequence = sequence;
		update();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("AdditiveSynthSequencer[\n");
		for( Bloc synth : synths ){
			sb.append( synth.toString() );
			sb.append("\n");
		}
		sb.append("SEQUENCE :");
		sb.append(AdditiveSynthEntry.sequenceToString(sequence, synths));
		return sb.toString();
	}



}
