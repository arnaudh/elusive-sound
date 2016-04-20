package org.elusive.ui.tools.file;

import java.awt.Component;
import java.awt.FileDialog;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.JFrame;


public class FileOpenDialog {
	
	protected FileDialog dialog;
	protected File selectedFile;

	public FileOpenDialog(String title, Component fen, File repertoire,
			String[] extensions) {
		this(title, fen, repertoire, extensions, false);
	}
	protected FileOpenDialog(String title, Component fen, File repertoire, final String[] extensions, boolean save){
		dialog = new FileDialog(new JFrame(), title);
		dialog.setMode(save?FileDialog.SAVE:FileDialog.LOAD);
		dialog.setDirectory(repertoire==null?null:repertoire.getAbsolutePath());
		dialog.setFilenameFilter(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				boolean accept = false;
				for( String ext : extensions ){
					if( name.endsWith(ext) ){
						accept = true;
						break;
					}
				}
				return accept;
			}
		});
		dialog.setVisible(true);
		
		
		if( dialog.getFile() == null ){
			selectedFile = null;
		}else{
			selectedFile = new File(dialog.getDirectory()+dialog.getFile());
		}
		
		if( save ){
			if( this.getSelectedFile() == null ){
				return;
			}
			String path = this.getSelectedFile().getPath();
			boolean nomCorrect = false;
			for(String ext : extensions){
				if( path.endsWith(ext) ){
					nomCorrect = true;
					break;
				}
			}
			if( !nomCorrect ){
				selectedFile = new File(path+extensions[0]);
			}
		}
		
		//		super();
//		this.setDialogTitle(title);
//		this.setCurrentDirectory(repertoire);
//		this.setAcceptAllFileFilterUsed(false);
//		FileFilter ff = new FileFilter() {
//			@Override
//			public String getDescription() {
//				if( extensions.length == 0 ){
//					return "Tous types de fichiers";
//				}else{
//					String ret = "Fichiers";
//					for(String e : extensions){
//						ret += " "+e;
//					}
//					return ret;
//				}
//			}					
//			@Override
//			public boolean accept(File file) {
//				if( file.isDirectory()  ){
//					return true;
//				}
//				String name = file.getName().toLowerCase();
//				for(String s : extensions){
//					if( name.endsWith(s)){
//						return true;
//					}
//				}
//				return false;
//			}
//		};
//		this.addChoosableFileFilter(ff);
//		this.setFileFilter(ff);
//		
//		if( save ){
//			this.showSaveDialog(fen);
//			if( this.getSelectedFile() == null ){
//				return;
//			}
//			String path = this.getSelectedFile().getPath();
//			boolean nomCorrect = false;
//			for(String ext : extensions){
//				if( path.endsWith(ext) ){
//					nomCorrect = true;
//					break;
//				}
//			}
//			if( !nomCorrect ){
//				File file = new File(path+extensions[0]);
//				this.setSelectedFile(file);
//			}
//		}else{
//			this.showOpenDialog(fen);
//		}
	}

	


	public File getSelectedFile() {
		return selectedFile;
	}
	
	
	public static String[] getSupportedAudioFileExtensions(){
		return new String[]{".wav", ".mp3"};
	}
	

}
