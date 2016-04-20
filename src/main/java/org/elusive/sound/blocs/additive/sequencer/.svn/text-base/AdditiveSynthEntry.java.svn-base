package org.elusive.sound.blocs.additive.sequencer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.elusive.sound.blocs.Bloc;
import org.elusive.sound.blocs.additive.AdditiveSynth;

public class AdditiveSynthEntry {

	private AdditiveSynth synth;
	private Integer offset;

	public AdditiveSynthEntry(AdditiveSynth synth, Integer offset) {
		super();
		this.synth = synth;
		this.offset = offset;
	}

	public AdditiveSynth getSynth() {
		return synth;
	}

	public void setSynth(AdditiveSynth synth) {
		this.synth = synth;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public static String sequenceToString(List<AdditiveSynthEntry> sequence, List<Bloc> list) {
		StringBuilder sb = new StringBuilder();
		for (AdditiveSynthEntry entry : sequence) {
			char c = (char) ('A' + list.indexOf(entry.getSynth()));
			sb.append(c);
			sb.append(' ');
			sb.append(entry.getOffset());
			sb.append('\n');
		}
		return sb.toString();
	}

	public static List<AdditiveSynthEntry> stringToSequence(String str, List<Bloc> list) {
		List<AdditiveSynthEntry> ret = new ArrayList<AdditiveSynthEntry>();
		Scanner scanner = new Scanner(str);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.isEmpty()) {
				continue;
			}
			try {
				int n = line.charAt(0) - 'A';
				AdditiveSynth synth = (AdditiveSynth) list.get(n); //
				int offset = Integer.parseInt(line.substring(2)); //
//				System.out.println("offset:"+offset+" synth:"+synth);
				ret.add(new AdditiveSynthEntry(synth, offset));
			} catch( ArrayIndexOutOfBoundsException e ){
//				e.printStackTrace();
				continue;
			} catch (NumberFormatException e) {
//				e.printStackTrace();
				continue;
			}
		}
		return ret;
	}

}
