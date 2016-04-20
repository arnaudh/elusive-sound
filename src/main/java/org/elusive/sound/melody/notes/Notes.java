package org.elusive.sound.melody.notes;

public class Notes {	
	
	public final static int premiereOctave = -2;
	public final static int derniereOctave = 8;
	public final static int nbOctaves = derniereOctave - premiereOctave + 1;
	public final static int nbNotes = 12;
	public final static int nbNotesTotal = (derniereOctave - premiereOctave + 1) * nbNotes;
	
	public final static String[] nomsSimples = new String[]{"do", "do#", "ré", "ré#", "mi", "fa", "fa#", "sol", "sol#", "la", "la#", "si"};
	public final static String[] nomsListe = new String[nbNotesTotal];
	public final static String[][] noms = new String[nbOctaves][nbNotes];
	public final static double[] frequencesListe = new double[nbNotesTotal];
	public final static double[][] frequences = new double[nbOctaves][nbNotes];
	static{
		for(int octave = premiereOctave; octave <= derniereOctave; octave ++){
			int o = octave - premiereOctave;
			for(int pitch = 0; pitch < nbNotes; pitch++){
				String nom = nomsSimples[pitch]+" "+pitch;

		        double absolute_picth = octave + (pitch - 9) / 12d; 
		        double frequence =  Math.pow(2, absolute_picth + 6) - 9d * Math.pow(2, absolute_picth);
				
				nomsListe[(octave - premiereOctave)*nbNotes + pitch] = nom;
				noms[(octave - premiereOctave)][pitch] = nom;
				
				frequencesListe[(octave - premiereOctave)*nbNotes + pitch] = frequence;
				frequences[(octave - premiereOctave)][pitch] = frequence;
			}
		}
	}
	
    public static double GetNoteFrequency(int octave, int pitch)
    {
    	return frequences[octave][pitch];
    }

	public static double GetNoteFrequency(int pitch) {
		return frequencesListe[pitch];
	}
    
    public static OctavePitch GetClosestOctavePitch(double frequency){
    	double minDelta = Double.MAX_VALUE;
    	int bestOctave = 0;
    	int bestPitch = 0;
    	for( int octave = 0 ; octave < nbOctaves; octave ++){
    		for( int note = 0; note < nbNotes; note++){
    			double delta = Math.abs(frequences[octave][note] - frequency);
    			if( delta < minDelta ){
    				minDelta = delta;
    				bestOctave = octave;
    				bestPitch = note;
    			}
    		}
    	}
    	return new OctavePitch(bestOctave, bestPitch);
    }

}
