package org.elusive.sound.melody.tabs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JPanel;

import org.elusive.main.tools.IOtools;
import org.elusive.sound.genetics.Geneticable;
import org.elusive.sound.melody.tabs.TabNote.SLIDE;



public class GuitarTab implements Geneticable {

	// This is what we save
	private String tabText = "";
	// TRANSIENT
	private transient List<Couplet> couplets = null;

	public GuitarTab() {
	}
	
	public GuitarTab(String tabtext) {
		this.tabText = tabtext;
	}

	public String toString() {
		return tabText;
	}

	// Lazy is the way to go for transient stuff
	public List<Couplet> getCouplets() {
		if( couplets == null ){
			String[] lines = tabText.split("\n");
			short corde = 5;
			int lastLineLength = 0;
			int offset = 0;
			int numLine = 0;
			int globalOffset = 0;
			
			this.couplets = new ArrayList<Couplet>();
			Couplet couplet = new Couplet();
			this.couplets.add(couplet);
			for (String ligne : lines) {
				boolean match = false;
				// System.out.println("NEW LINE :");
				// System.out.println(ligne);
				Pattern pattern = Pattern.compile("(?<=[-b~p/])((?:/|\\\\|r)?)(\\d+)([b~p/]*)");
				Matcher matcher = pattern.matcher(ligne);
				while (matcher.find()) {
					match = true;
					// System.out.println("MATCHER FIND");
					String group = matcher.group();
					int start = matcher.start(1);
					String prefix = matcher.group(1);
					String nombre = matcher.group(2);
					String suffix = matcher.group(3);
					// System.out.println("match = "+group);
					// System.out.println("start = "+start);
					// System.out.println("nombre = "+nombre);
					short frette = Short.parseShort(nombre);
//					System.out.println("offset("+offset+") + start = "+(offset+start));
					TabNote note = new TabNote(corde, frette, offset + start);
					note.setGlobalOffset(globalOffset + start);
					if (prefix.matches("/")) {
						note.setSlideBefore(SLIDE.SLIDE_UP);
					} else if (prefix.matches("\\\\")) {
						note.setSlideBefore(SLIDE.SLIDE_DOWN);
					}
					if (suffix.matches("[b~]+")) {
						note.setBend(true);
					} else if (suffix.matches("/")) {
						note.setSlideAfter(SLIDE.SLIDE_UP);
					}

					int offsetNote = note.getOffset();
//					System.out.println("note : "+note);
					// ************** INSERTION ****************** //
					boolean inserted = false;
					for (int i = 0, l = couplet.size(); i < l; i++) {
						int offsetTranche = couplet.get(i).get(0).getOffset();
						if (offsetTranche == offsetNote) {
//							System.out.println("insert in "+tab.tranches.get(i));
							couplet.get(i).add(note);
							inserted = true;
							break;
						} else if (offsetTranche > offsetNote) {
							Tranche newTranche = new Tranche();
							newTranche.add(note);
							couplet.add(i, newTranche);
							inserted = true;
							break;
						}
					}
					if (!inserted) {
						Tranche newTranche = new Tranche();
						newTranche.add(note);
						couplet.add(newTranche);
					}
				}
				if (ligne.contains("--")) {
					match = true;
					lastLineLength = ligne.length();
				}
				if (match) {
					corde--;
					if (corde < 0) { //nouveau couplet
						corde = 5;
						offset += lastLineLength;
						
						
						//on vérifie que le dernier couplet ajouté n'est pas un couplet vide)
						if( couplet.isEmpty() ){
							this.couplets.remove(couplet);						
						}
						couplet = new Couplet();
						this.couplets.add(couplet);
					}
				} else {
					corde = 5;
				}
				numLine ++;
				globalOffset += ligne.length() + 1;
			}
			//on vérifie que le dernier couplet ajouté n'est pas un couplet vide)
			if( couplet.isEmpty() ){
				this.couplets.remove(couplet);
			}
//			System.out.println("ici");
//			for( Couplet c : tab.couplets ){
//				System.out.println("couplet");
//				for (Tranche tranche : c) {
//					System.out.println("tranche : "+tranche);
//				}
//			}
		}
		return couplets;
	}

	public void setCouplets(List<Couplet> couplets) {
		this.couplets = couplets;
		tabText = CoupletsToString(couplets);
	}
	
	public static String CoupletsToString(List<Couplet> couplets){
		StringBuilder sbTotal = new StringBuilder();
		StringBuilder[] sbs = new StringBuilder[6];
		for( Couplet couplet : couplets){
			for (int i = 0; i < 6; i++) {
				sbs[i] = new StringBuilder('#');
			}
			//calcul du premier offset
			int minOffset = Integer.MAX_VALUE;
			for (Tranche tranche : couplet) {
				int offset = tranche.get(0).getOffset();
				if( offset < minOffset ) minOffset = offset;
			}
			minOffset -= 2;
			for ( Tranche tranche : couplet ) {
				for (TabNote note : tranche) {
					int corde = note.getCorde();
					int offset = note.getOffset() - minOffset;
					while( sbs[5-corde].length() < offset ){
						sbs[5-corde].append('-');
					}
					sbs[5-corde].append(note.toTabString());
				}//for Notes
			}//for Tranche
			
			int maxLenght = 0;
			for (StringBuilder sb : sbs) {
				int length = sb.length();
				if( length > maxLenght ) maxLenght = length;
			}
			for (StringBuilder sb : sbs) {
				while( sb.length() < maxLenght ){
					sb.append('-');
				}
				sbTotal.append(sb).append('\n');
			}
			sbTotal.append('\n');
		}//for Couplet
		return sbTotal.toString();
		
	}
	

	//	0 1  2 3  4 5 6  7 8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35
	//	C C# D D# E F F# G G# A A# B  C  C# D  D# E  F  F# G  G# A  A# B  C  C# D  D# E  F  F# G  G# A  A# B 
	public static int[] STRING_PITCHES = {4, 9, 14, 19, 23, 28};
	
	
	
	//////////////////// GENETICABLE /////////////////////

	@Override
	public Geneticable mutate(double strength) {
		// TODO Auto-generated method stub
		//ICIIIIII
		return this;
	}

	@Override
	public Geneticable combineWith(Geneticable gen) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public JPanel getPreviewPanel() {
		// TODO Auto-generated method stub
		return null;
	}
	

}

