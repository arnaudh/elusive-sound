package org.elusive.sound.rythm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import org.elusive.sound.blocs.frequenceur.NotePlacee;
import org.elusive.sound.genetics.Geneticable;
import org.elusive.sound.melody.tabs.Couplet;
import org.elusive.sound.melody.tabs.GuitarTab;
import org.elusive.sound.melody.tabs.TabNote;
import org.elusive.sound.melody.tabs.Tranche;

public class Rythm implements Geneticable, Iterable<Hit>{

	// Hits are FRAMES only
	private List<Hit> hits;
	
	public Rythm() {
		hits = new ArrayList<Hit>();
	}

	
	public List<NotePlacee> applyTo( GuitarTab tab ){
//		System.out.println("***Rythm.applyTo(tab)");
		List<NotePlacee> notes = new ArrayList<NotePlacee>();
		
		List<Couplet> couplets = tab.getCouplets();
		int iTranche = -1;
		int offset = 0;
		grandeBoucle:for (Couplet couplet : couplets) {
			for (Tranche tranche : couplet) {
				iTranche ++;
				if( iTranche >= hits.size() ){
					break grandeBoucle;
				}
				offset += hits.get(iTranche).getOffset();
				int length = hits.get(iTranche).getLength();
				for (TabNote note : tranche) {
					List<NotePlacee> notesPlacees = note.toNotesPlacees(offset, length);
					notes.addAll(notesPlacees);
				}
			}
		}
		return notes;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder("Rythm[");
		for(int i = 0, l = hits.size(); i < l; i++){
			sb.append(hits.get(i).getOffset());
			sb.append(", ");
		}
		return sb.toString();
	}
	

	public void addHit(int hit) {
		int somme = 0;
		for (int i = 0, l = hits.size(); i < l; i++) {
			int offsetSuivant = hits.get(i).getOffset();
			somme += offsetSuivant;
			if( hit < somme ){
				int difference = somme - hit;
				hits.add(i, new Hit(offsetSuivant - difference));
				hits.get(i+1).setOffset(difference);
				return;
			}
		}
		hits.add(new Hit(hit - somme));
	}

	public void addHit(Hit hit) {
		hits.add(hit);
	}
	
	public void insertHit(Hit hit, int position){
		hits.set(position, hit);
	}
	
	
	////////////////// GENETICS //////////////////

	@Override
	public Geneticable mutate(double strength) {
		// TODO Auto-generated method stub
		Rythm rythm = new Rythm();
		
		//bag of balls
		List<Boolean> bag = new ArrayList<Boolean>();
		int i = 0;
		int n = (int)(strength* hits.size() * 0.5); //change one hit out of 2 when strength = 1
		for(; i < n; i++){ 
			bag.add(true);
		}
		for(; i < hits.size(); i++){
			bag.add(false);
		}

		Random rand = new Random();
		for(Hit hit : hits){
			int offset = hit.getOffset();
			int length = hit.getLength();
			if( bag.get(rand.nextInt(bag.size()-1)) ){
				//TODO change hit intelligently
				double f = rand.nextBoolean()?2:0.5;
				offset = (int) (offset * f);
			}

			rythm.addHit(new Hit(offset, length));
		}
		
		return rythm;
	}



	@Override
	public Geneticable combineWith(Geneticable gen) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public JPanel getPreviewPanel() {
		return null;
	}


	public int size() {
		return hits.size();
	}


	public Hit get(int i) {
		return hits.get(i);
	}


	@Override
	public Iterator<Hit> iterator() {
		return hits.iterator();
	}

	
}
