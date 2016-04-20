package org.elusive.sound.analyse;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.elusive.sound.blocs.frequenceur.Frequenceur;
import org.elusive.sound.blocs.frequenceur.NotePlacee;
import org.elusive.sound.melody.partition.Partition;
import org.elusive.sound.melody.tabs.TabPartition;
import org.elusive.sound.rythm.Hit;
import org.elusive.sound.rythm.Rythm;
import org.elusive.sound.rythm.ui.RythmRecorder;
import org.elusive.sound.rythm.ui.RythmRecorderAdapter;

public class PartitionExtractor extends Analyser {

	private int fenetre = 10000;
	private Partition partition;

	@Override
	public void analyse(final float[] data) {
		RythmRecorder rec = new RythmRecorder(new RythmRecorderAdapter() {
			@Override
			public void whenRecordFinished(Rythm rythm) {
				analyse(data, rythm);
			}
		});
		JFrame frame = new JFrame("Rythm Recorder");
		frame.getContentPane().add(rec);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void analyse(float[] data, Rythm rythm) {
		List<NotePlacee> notes = new ArrayList<NotePlacee>();
		int offset = 0;
		for (Hit h : rythm) {
			offset += h.getOffset();
			if (offset > data.length) {
				break;
			}
			int fin = Math.min(data.length, offset + fenetre);
			float[] snippet = new float[fin - offset];
			for (int i = offset; i < fin; i++) {
				snippet[i - offset] = data[i];
			}

			FFTanalizer fft = new FFTanalizer();

			fft.analyseWithoutShowing(snippet);
			double f = fft.getDominantFrequency();
			NotePlacee note = new NotePlacee(offset, f, 22000);
			notes.add(note);
		}
		partition = TabPartition.createFromNotes(notes);
		Frequenceur fre = new Frequenceur();
		fre.setPartition(partition);
		grille.newBloc(fre);

		// grille.newRythm(rythm);
	}

}
