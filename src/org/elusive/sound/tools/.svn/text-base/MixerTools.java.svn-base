package org.elusive.sound.tools;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;

public class MixerTools {
	
	public static void main(String[] args) {
		listMixers();
	}

	public static void listMixers() {
		try {
			Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
			for (Mixer.Info info : mixerInfos) {
				Mixer m = AudioSystem.getMixer(info);
				Line.Info[] lineInfos = m.getSourceLineInfo();
				for (Line.Info lineInfo : lineInfos) {
					System.out.println(info.getName() + "---" + lineInfo);
					Line line = m.getLine(lineInfo);
					System.out.println("\t-----" + line);
				}
				lineInfos = m.getTargetLineInfo();
				for (Line.Info lineInfo : lineInfos) {
					System.out.println(m + "---" + lineInfo);
					Line line = m.getLine(lineInfo);
					System.out.println("\t-----" + line);
				}
			}
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
}
