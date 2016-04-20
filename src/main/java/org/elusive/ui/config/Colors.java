package org.elusive.ui.config;

import java.awt.Color;

public interface Colors {
	
	// General
	public static Color MAIN_BACKGROUND_1 = new Color(20, 20, 20);
	public static Color MAIN_BACKGROUND_2 = new Color(50, 20, 50);
	public static Color MAIN_FOREGROUND_1 = Color.WHITE;
	
	// Grid
	public static Color GRID_BACKGROUND_COLOR = MAIN_BACKGROUND_2;
	public static Color GRID_LINES = Color.WHITE;
	public static Color CURSEUR = Color.red;
	public static Color CURSEUR_FICTIF = new Color(200, 200, 250);
	public static Color SELECTION = new Color(0.2f, 0.2f, 0.2f, 0.5f);
	
	// Elusive Panel
	public static Color ELUSIVE_PANEL_BACKGROUND = MAIN_BACKGROUND_1;
	
	//Bloc colors
	public static Color COLOR_ROUND_RECT = Color.BLACK;
	public static Color COLOR_DATA = Color.BLACK;
	public static Color OUTSIDE_BLOC_COLOR = Color.WHITE;
	
	public static Color COLOR_ERROR = new Color(255, 0, 0);
	public static Color OUTSIDE_COLOR_DEFAULT = Color.blue;
	public static Color OUTSIDE_COLOR_RYTHM = Color.blue;
	
	public static Color COLOR_BLOC_FICHIER = new Color(100, 100, 240);
	public static Color COLOR_BLOC_TOTAL = new Color(200, 250, 200);
	public static Color COLOR_FREQUENCEUR = new Color(250, 250, 0);
	public static Color COLOR_SYNTHETISEUR_FM = new Color(100, 200, 100);
	public static Color COLOR_WAVESHAPER = Color.cyan;
	public static Color COLOR_KARPLUS_STRONG = Color.gray;
	public static Color COLOR_ADDITIVE_SYNTH = Color.MAGENTA;
	public static Color COLOR_ADDITIVE_SYNTH_SEQUENCER = Color.MAGENTA;
	
	// Tab Editor
	public static Color BACKGROUND_TAB_EDITOR = MAIN_BACKGROUND_2.brighter().brighter();
	public static Color TAB_EDITOR_CARET = Color.WHITE;
	public static Color TAB_EDITOR_TEXT = Color.WHITE;
	public static Color TAB_EDITOR_NOTE = Color.BLUE;
	public static Color TAB_EDITOR_NOTE_HIT = Color.GREEN;
}
