package org.elusive.ui.tools.file;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class DialogueDossier extends JFileChooser{

	public DialogueDossier(String title, Component fen, File repertoire){
		this(title, fen, repertoire, false);
	}
	public DialogueDossier(String title, Component fen, File repertoire, boolean save){
		super();
		this.setDialogTitle(title);
		this.setCurrentDirectory(repertoire);
		this.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    this.setAcceptAllFileFilterUsed(false);
		
		if( save ){
			this.showSaveDialog(fen);
		}else{
			this.showOpenDialog(fen);
		}
	}
	

}
