package org.elusive.sound.melody.tabs;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.elusive.main.performance.Chrono;

public class TabChord extends Tranche {

	private List<String> names = new ArrayList<String>();
	private String info;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(names.get(0));
		for (int i = 1, l = names.size(); i < l; i++) {
			sb.append(" or ");
			sb.append(names.get(i));
		}
		sb.append(' ');
		sb.append('[');
		for6: for (int i = 0; i < 6; i++) {
			if (i > 0)
				sb.append(' ');
			for (TabNote note : this) {
				if (note.getCorde() == i) {
					sb.append(note.getFrette());
					continue for6;
				}
			}
			sb.append('x');
		}
		sb.append(']');
		sb.append(' ');
		sb.append(info);

		return sb.toString();
	}

	public static TabChord createFromString(String str) {
		TabChord chord = new TabChord();

		str = str.replaceAll("\\s+\\[", "\\[");
		Pattern pattern = Pattern
				.compile("^([^\\[]+)\\[(x|\\d+) (x|\\d+) (x|\\d+) (x|\\d+) (x|\\d+) (x|\\d+)\\](.+)$");
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			// for(int i = 0; i <= matcher.groupCount(); i++){
			// System.out.println("group("+i+") = "+matcher.group(i)+";");
			// }
			String names = matcher.group(1);
			String[] strs = names.split("(\\s+or\\s+)");
			for (String s : strs) {
				chord.names.add(s);
			}
			for (int i = 2; i < 8; i++) {
				String s = matcher.group(i);
				if (s.equals("x")) {
					continue;
				} else {
					Integer n = Integer.parseInt(s);
					chord.add(new TabNote(i - 2, n, 0));
				}
			}
			chord.info = matcher.group(8).substring(1);
		} else {
			 System.err.println("TabChord - couldn't read chord from string : "+str);
		}
		return chord;
	}

	
	public static List<TabChord> allChords = null;
	public static List<TabChord> getAllChords(){
		if( allChords == null ){
			allChords = new ArrayList<TabChord>();
			try {
				FileInputStream fis = new FileInputStream(TabChord.class.getResource("/tabs/chords/allChords.txt").getFile());
				InputStreamReader isr = new InputStreamReader(fis);
				BufferedReader br = new BufferedReader(isr);
				String line;
				while( (line = br.readLine()) != null ){
					TabChord chord = TabChord.createFromString(line);
					allChords.add(chord);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return allChords;
	}
}
