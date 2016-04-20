package org.elusive.sound.rythm.ui;

import org.elusive.sound.rythm.Hit;
import org.elusive.sound.rythm.Rythm;

public interface RythmRecorderListener {

	public void addedHit(Hit hit);
	public void whenRecordFinished(Rythm rythm);
}
