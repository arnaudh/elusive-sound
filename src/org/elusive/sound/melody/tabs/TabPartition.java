package org.elusive.sound.melody.tabs;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.elusive.sound.blocs.frequenceur.NotePlacee;
import org.elusive.sound.genetics.Geneticable;
import org.elusive.sound.melody.partition.Partition;
import org.elusive.sound.melody.tabs.ui.TabPartitionEditor;
import org.elusive.sound.rythm.Hit;
import org.elusive.sound.rythm.Rythm;
import org.elusive.ui.fenetre.ElusivePanel;
import org.elusive.ui.menus.MenuTools;
import org.elusive.ui.tools.PopupListener;

public class TabPartition extends Partition {

	private Rythm rythm = new Rythm();
	private GuitarTab tab = new GuitarTab();

	public TabPartition() {
	}

	@Override
	public ElusivePanel createElusivePanel() {
		final TabPartitionEditor editor = new TabPartitionEditor(this);
		ElusivePanel panel = new ElusivePanel("Tab Partition");
		panel.add(editor);
		return panel;
	}

	private void recalculeNotes() {
		notes = rythm.applyTo(tab);
		updated();
	}

	public static TabPartition createFromNotes(List<NotePlacee> notes) {
		// TODO
		Rythm rythm = new Rythm();
		GuitarTab tab = new GuitarTab();
		List<Couplet> couplets = new ArrayList<Couplet>();
		Couplet couplet = new Couplet();
		couplets.add(couplet);
		tab.setCouplets(couplets);
		for(NotePlacee note : notes){
			rythm.addHit(new Hit(note.getStart(), note.getLength()));
			couplet.add(new Tranche(TabNote.createFromFrequency(note.getFrequence())));
		}
		TabPartition partition = new TabPartition();
		partition.setRythm(rythm);
		partition.setTab(tab);
		return partition;
	}

	public Rythm getRythm() {
		return rythm;
	}

	public void setRythm(Rythm rythm) {
		this.rythm = rythm;
		recalculeNotes();
	}

	public GuitarTab getTab() {
		return tab;
	}

	public void setTab(GuitarTab tab) {
		this.tab = tab;
		recalculeNotes();
	}
	
	
	@Override
	public void setNotes(List<NotePlacee> notes) {
		//TODO create rythm and tab from notes
		super.setNotes(notes);
	}

	// ////////////////////////// GENETICS /////////////////////////////////

	private boolean mutateRythm = true;
	private boolean mutateMelody = true;

	@Override
	public Geneticable mutate(double strength) {
		// TODO Auto-generated method stub
		TabPartition partition = new TabPartition();
		if (mutateRythm) {
			partition.setRythm((Rythm) rythm.mutate(strength));
		} else {
			partition.setRythm((Rythm) rythm.mutate(0));
		}
		if( mutateMelody ){
			partition.setTab( (GuitarTab) tab.mutate(strength) );
		}else{
			partition.setTab( (GuitarTab) tab.mutate(0) );			
		}

		return partition;
	}

	@Override
	public Geneticable combineWith(Geneticable gen) {
		// TODO Auto-generated method stub
		return this;
	}

}
