package org.elusive.sound.blocs.additive.sequencer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;

import org.elusive.ui.fenetre.ElusivePanel;
import org.elusive.ui.texteditors.JTextPaneNoWrap;
import org.elusive.ui.tools.TextComponentTools;

public class AdditiveSynthSequenceEditor extends ElusivePanel {

	private AdditiveSynthSequencer synthSequencer;

	private JTextPane textpane;
	private String lastText = ""; 

	public AdditiveSynthSequenceEditor(AdditiveSynthSequencer synthSequencer) {
		super("Synth Sequence Editor");
		this.synthSequencer = synthSequencer;
		

		Document doc = new DefaultStyledDocument();
		textpane = new JTextPaneNoWrap();
		textpane.setDocument(doc );
		textpane.setText(AdditiveSynthEntry.sequenceToString(synthSequencer.getSequence(), synthSequencer.getSynths()));
		textpane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if( !textpane.getText().equals( lastText ) ){
					lastText = textpane.getText();
					recalculeSequence();
				}
			}
		});
		TextComponentTools.addUndoRedo(textpane);

		JScrollPane scrollPane = new JScrollPane(textpane);
		this.add(scrollPane);
	}

	private void recalculeSequence(){
		List<AdditiveSynthEntry> sequence = AdditiveSynthEntry.stringToSequence(textpane.getText(), synthSequencer.getSynths());
		synthSequencer.setSequence(sequence);
	}
	
}
