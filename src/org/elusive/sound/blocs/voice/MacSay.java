package org.elusive.sound.blocs.voice;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.elusive.main.Main;
import org.elusive.main.tools.IOtools;
import org.elusive.sound.blocs.Bloc;
import org.elusive.sound.genetics.Geneticable;
import org.elusive.sound.tools.SoundFileReader;
import org.elusive.sound.tools.SoundTools;
import org.elusive.ui.fenetre.ElusivePanel;

public class MacSay extends Bloc {

	private static String[] VOICES = new String[] { "Alex", "Agnes", "Kathy", "Princess", "Vicki", "Victoria", "Bruce", "Fred", "Junior", "Ralph", "Albert", "Bad News", "Bahh", "Bells", "Boing", "Bubbles",
			"Cellos", "Derangeed", "Good News", "Hysterical", "Pipe Organ", "Trinoids", "Whisper", "Zavrox" };

	private String speach = "put your hands up";
	private String voice = VOICES[0];

	@Override
	public Geneticable mutate(double strength) {
		// TODO Intéressant !
		MacSay say = new MacSay();
		say.setSpeach(speach + " ha");
		return say;
	}

	@Override
	public float[] generateData() {
		// TODO Auto-generated method stub
		if (speach == null) {
			return null;
		}
		if (Main.isMacOSX()) {
			File file = null;
			try {
				file = IOtools.createTempFile(speach.substring(0, Math.min(20, speach.length())), ".WAV", IOtools.getTemporaryFolder());
			} catch (IOException e1) {
				e1.printStackTrace();
				return null;
			}

			System.out.println("EXECUTING say -v " + voice + " -o " + file.getAbsolutePath() + " " + speach);
			try {
				Runtime.getRuntime().exec(new String[] { "say", "-v", voice, "-o", file.getAbsolutePath(), speach }).waitFor();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			SoundFileReader reader = new SoundFileReader(file);
			try {
				reader.read();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			}

			boolean b = file.delete();
			System.out.println("DELETE SAYS : " + b);

			return reader.getLeftData();
		} else {
			return null;
		}
	}

	@Override
	protected ElusivePanel createElusivePanel() {
		ElusivePanel ep = new ElusivePanel("Mac Say");
		ep.setLayout(new FlowLayout());

		final JTextField speachField = new JTextField(speach);
		speachField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				speach = speachField.getText();
				update();
			}
		});
		ep.add(speachField);

		final JComboBox combo = new JComboBox(VOICES);
		combo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("MacSay.createElusivePanel().new ActionListener() {...}.actionPerformed()");
				voice = (String) combo.getSelectedItem();
				update();
			}
		});
		ep.add(combo);
		
		return ep;
	}

	public String getSpeach() {
		return speach;
	}

	public void setSpeach(String speach) {
		this.speach = speach;
	}

}
