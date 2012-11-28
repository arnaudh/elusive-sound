package org.elusive.sound.blocs.wave;

import org.elusive.main.historique.History;
import org.elusive.sound.enveloppes.Enveloppe;
import org.elusive.sound.enveloppes.ui.EnveloppePanel2;
import org.elusive.ui.tools.JFrameTools;

public class WaveShaperPanel extends EnveloppePanel2 {

	private WaveShaper waveShaper;
	
	public static void main(String[] args) {
		WaveShaperPanel p =  new WaveShaperPanel(new WaveShaper(), new History());
		JFrameTools.show(p);
	}
	
	public WaveShaperPanel(WaveShaper waveShaper_, History history) {
		super(waveShaper_.enveloppe, history);
		this.waveShaper = waveShaper_;
		this.paintHorizontalAxis = true;
		this.verticalUnitOnset = -1;
		
	}

}
