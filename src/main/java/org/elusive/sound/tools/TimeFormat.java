package org.elusive.sound.tools;

import java.util.ArrayList;

public enum TimeFormat {

	TEMPO, FRAMES, SECONDS;

	public String toString() {
		switch (this) {
		case SECONDS:
			return "Seconds";
		case TEMPO:
			return "Tempo";
		case FRAMES:
		default:
			return "Frames";
		}
	}

	private static String[] stringFormats;

	public static String[] getStrings() {
		if (stringFormats == null) {
			ArrayList<String> strings = new ArrayList<String>();
			for (TimeFormat format : TimeFormat.values()) {
				strings.add(format.toString());
			}
			stringFormats = new String[strings.size()];
			strings.toArray(stringFormats);
		}
		return stringFormats;
	}
}
