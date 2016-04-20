package org.elusive.ui.grille;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import org.elusive.main.historique.action.AjouteBloc;
import org.elusive.main.tools.IOtools;
import org.elusive.sound.blocs.BlocFichier;
import org.elusive.sound.tools.SoundTools;
import org.elusive.ui.action.MyAction;
import org.elusive.ui.action.shortcut.MyKeyStrokes;

public class Clipboard {

	private Grid grid;
	private float[] data;
	
	//actions
	private MyAction copyAction;
	private MyAction pasteAction;

	public Clipboard(Grid grid) {
		super();
		this.grid = grid;
		
		//init actions
		copyAction = new MyAction("Copy", MyKeyStrokes.SELECTION_COPY_KEY_STROKE , "Copy selection", null) {
			@Override
			public void actionPerformed(ActionEvent e) {
				copy();
			}
		};
		pasteAction = new MyAction("Paste", MyKeyStrokes.SELECTION_PASTE_KEY_STROKE , "Paste selection", null) {
			@Override
			public void actionPerformed(ActionEvent e) {
				paste();
			}
		};
		pasteAction.setEnabled(false);
	}
	
	private void copy(){
		if( grid.getSelection() != null ){
			data  = grid.getSelectedData();
			pasteAction.setEnabled(true);
		}
	}
	private void paste(){
		if( data!=null ){
			try {
				File file = IOtools.createTempFile("clipboard", ".wav", IOtools.getTemporaryFolder());
				SoundTools.writeDataToFile(data, file);
				BlocFichier bloc = new BlocFichier(file);
	
				AjouteBloc action = new AjouteBloc(grid, bloc);
				grid.getHistorique().execute(action);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public float[] getData() {
		return data;
	}

	public MyAction getCopyAction() {
		return copyAction;
	}

	public MyAction getPasteAction() {
		return pasteAction;
	}
	
	
	
}
