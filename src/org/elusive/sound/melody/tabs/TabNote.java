package org.elusive.sound.melody.tabs;

import java.util.ArrayList;
import java.util.List;

import org.elusive.sound.blocs.frequenceur.NotePlacee;
import org.elusive.sound.melody.notes.Notes;
import org.elusive.sound.melody.notes.OctavePitch;


public class TabNote {
	
	private int offset;
	private int corde;
	private int frette;
	private SLIDE slideBefore = SLIDE.NO_SLIDE;
	private SLIDE slideAfter = SLIDE.NO_SLIDE;
	private boolean bend = false;
	
	private int globalOffset;
	

	enum SLIDE{
		NO_SLIDE,
		SLIDE_DOWN,
		SLIDE_UP;
	}
	
	public TabNote(int corde, int frette, int offset) {
		this.offset = offset;
		this.corde = corde;
		this.frette = frette;
	}
	
	public String toString(){
		return "GuitarTabNote["+corde+", "+offset+", "+frette+"]";
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getCorde() {
		return corde;
	}

	public void setCorde(int corde) {
		this.corde = corde;
	}

	public int getFrette() {
		return frette;
	}

	public void setFrette(int frette) {
		this.frette = frette;
	}

	public boolean isBend() {
		return bend;
	}

	public void setBend(boolean bend) {
		this.bend = bend;
	}

	public void setSlideBefore(SLIDE slideBefore) {
		this.slideBefore = slideBefore;
	}

	public SLIDE getSlideBefore() {
		return slideBefore;
	}

	public void setSlideAfter(SLIDE slideAfter) {
		this.slideAfter = slideAfter;
	}

	public SLIDE getSlideAfter() {
		return slideAfter;
	}
	
	public double getFrequence() {
		double fre = Notes.GetNoteFrequency( GuitarTab.STRING_PITCHES[corde] + getFrette());
		return fre;
	}
	
	//TODO not finished !
	public static TabNote createFromFrequency(double f){
		OctavePitch octavePitch = Notes.GetClosestOctavePitch(f);
		int totalPitch = octavePitch.getOctave() * Notes.nbNotes + octavePitch.getPitch();
		
		int corde = 0;
		int frette = 0;
		for( int guitarString = 0; guitarString < 6; guitarString++){
			if( totalPitch > GuitarTab.STRING_PITCHES[guitarString] ){
				corde = guitarString;
				frette = totalPitch - GuitarTab.STRING_PITCHES[guitarString];
			}
		}
		TabNote note = new TabNote(corde, frette, 0);
		System.out.println("frequency = "+f+" :: octavePitch = "+octavePitch+" totalPitch = "+totalPitch+" :: note = "+note);
		return note;
	}
	
	public static void main(String[] args) {
		for( double frequency = 0; frequency < 1000; frequency++){
			TabNote note = createFromFrequency(frequency);
		}
	}
	
	public List<NotePlacee> toNotesPlacees(int debut, int duree) {
		List<NotePlacee> notesPlacees = new ArrayList<NotePlacee>();
//		System.out.println(":::::::::::: toNotesPlacees, frequence = "+getFrequence());
		if( getSlideBefore().equals(SLIDE.SLIDE_UP) ){
//			System.out.println(":::SLIDE UPPPP");
			int dureePalier = 50;
			int nbPaliers = duree / dureePalier;
			double frequenceDepart = getFrequence() - 50;
			double augmentation = getFrequence() - frequenceDepart;
			System.out.println("frequenceDépart : "+frequenceDepart);
//			System.out.println("augmentation = "+augmentation);
			for (int i = 0; i < nbPaliers; i++) {
				double freq =  frequenceDepart + i*augmentation/(double)nbPaliers;
//				System.out.println("freq["+i+"] = "+freq);
				
				dureePalier = (int) (44100 / freq);
				notesPlacees.add(new NotePlacee(debut, freq, dureePalier));		
				debut += dureePalier;	
			}
		}else{
			notesPlacees.add(new NotePlacee(debut, getFrequence(), duree));
		}
		
		
		return notesPlacees;
	}

	public String toTabString() {
		String s = this.getFrette() + "";
		if (this.isBend()) {
			s += "b";
		}
		switch (this.getSlideBefore()) {
		case SLIDE_DOWN:
			s = "\\" + s;
			break;
		case SLIDE_UP:
			s = "/" + s;
			break;
		}
		switch (this.getSlideAfter()) {
		case SLIDE_DOWN:
			s = s + "\\";
			break;
		case SLIDE_UP:
			s = s + "/";
			break;
		}
		return s;
	}


	public int getGlobalOffset() {
		return globalOffset;
	}

	public void setGlobalOffset(int globalOffset) {
		this.globalOffset = globalOffset;
	}

}
