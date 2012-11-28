package org.elusive.sound.melody.partition;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.elusive.sound.blocs.frequenceur.NotePlacee;
import org.elusive.sound.flow.Timestamp;
import org.elusive.sound.flow.Updatable;
import org.elusive.sound.genetics.Geneticable;
import org.elusive.ui.fenetre.ElusivePanel;


public abstract class Partition implements Geneticable {

	protected List<NotePlacee> notes = new ArrayList<NotePlacee>();
	
	public void setNotes(List<NotePlacee> notes) {
		this.notes = notes;
		updated();
	}
	
	public List<NotePlacee> getNotes() {
		return notes;
	}
	
	public abstract ElusivePanel createElusivePanel();
	
	
	protected Updatable updateListener;
	public void setUpdateListener(Updatable lis){
		updateListener = lis;
	}

	protected transient Timestamp timestamp = new Timestamp();
	protected void updated(){
		timestamp.update();
		if( updateListener != null ){
			updateListener.update();
		}
	}
	public Timestamp getTimestamp(){
		return timestamp;
	}
	public Object readResolve(){
		timestamp = new Timestamp();
		return this;
	}
	
	
	///////// GENETICS //////////
	@Override
	public JPanel getPreviewPanel() {
		return createElusivePanel();
	}

}
