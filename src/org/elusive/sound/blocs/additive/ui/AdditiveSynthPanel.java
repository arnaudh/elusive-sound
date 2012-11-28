package org.elusive.sound.blocs.additive.ui;

import org.elusive.sound.blocs.additive.AdditiveSynth;
import org.elusive.ui.action.MyAction;
import org.elusive.ui.fenetre.ElusivePanel;

public class AdditiveSynthPanel extends ElusivePanel {

	private AdditiveSynth synth;
	
	// actions
	
	public AdditiveSynthPanel(AdditiveSynth synth_) {
		super("Additive Synth");
		this.synth = synth_;
		
		AdditiveSynthVisualizerPanel viz = new AdditiveSynthVisualizerPanel(synth, history);
		
		// Actions
		actions.add(history.getUndoAction());
		actions.add(history.getRedoAction());
		
		// Layout
		this.add(viz);
	}

}
