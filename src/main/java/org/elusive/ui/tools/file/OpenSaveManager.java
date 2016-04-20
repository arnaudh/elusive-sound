package org.elusive.ui.tools.file;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.elusive.main.tools.IOtools;
import org.elusive.test.OpaqueExample;
import org.elusive.ui.action.ActionCategory;
import org.elusive.ui.action.MyAction;
import org.elusive.ui.action.shortcut.MyKeyStrokes;
import org.elusive.ui.config.ConstantesUI;
import org.elusive.ui.resources.Icons;

public abstract class OpenSaveManager {


	private File openDirectory;
	private File savedFile = null;
	private String objectName;

	private List<String> extensions = new ArrayList<String>();

	private Component parentComponent = null;
	
	private MyAction openAction;
	private MyAction saveAction;
	private MyAction saveAsAction;
	
	private String lastSavedContent = null;

	public OpenSaveManager(Component parentComponent, File openDirectory, String objectName, String ext) {
		this.parentComponent = parentComponent;
		this.openDirectory = openDirectory;
		this.objectName = objectName;
		this.extensions.add(ext);

		openAction = new MyAction("Open", MyKeyStrokes.OPEN_KEY_STROKE, "Open", Icons.OPEN_ICON, ActionCategory.FILE) {
			@Override
			public void actionPerformed(ActionEvent e) {
				openAction();
			}
		};
		saveAction = new MyAction("Save", MyKeyStrokes.SAVE_KEY_STROKE, "Save", Icons.SAVE_ICON, ActionCategory.FILE) {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveAction();
			}
		};
		saveAsAction = new MyAction("Save as...", MyKeyStrokes.SAVE_AS_KEY_STROKE, "Save as...", Icons.SAVE_AS_ICON, ActionCategory.FILE) {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveAsAction();
			}
		};
	}
	
	public void openAction(){
		if( lastSavedContent != null && !lastSavedContent.equals(save()) ){
			//current content has changed
			int response = JOptionPane.showConfirmDialog(OpenSaveManager.this.parentComponent, "Current content modified. Do you wish to save it ?", "Save", JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE);
			if (response == JOptionPane.YES_OPTION) {
				saveAction.actionPerformed(null);
			}
		}
		
		FileOpenDialog dialogue = new FileOpenDialog("Open "+objectName, OpenSaveManager.this.parentComponent, OpenSaveManager.this.openDirectory, getExtensionsAsArray());
		File selectedFile = dialogue.getSelectedFile();
		
				
		open(selectedFile);
	}
	
	public void saveAction(){
		if (savedFile == null) {
			saveAsAction();
		} else {
			writeToSavedFile();
		}
		//TODO disable save, enable it with history
	}
	
	public void saveAsAction(){
		boolean fileChosen = choseNewSaveFile();
		if (fileChosen) {
			writeToSavedFile();
		}
	}

	protected String[] getExtensionsAsArray() {
		String[] ext;
		if (OpenSaveManager.this.extensions.isEmpty()) {
			ext = new String[] { "" };
		} else {
			ext = new String[OpenSaveManager.this.extensions.size()];
			for (int i = 0; i < OpenSaveManager.this.extensions.size(); i++) {
				ext[i] = OpenSaveManager.this.extensions.get(i);
			}
		}
		return ext;
	}


	public void addToMenu(JMenu menu) {
		menu.add(openAction.createMenuItem());
		menu.add(saveAction.createMenuItem());
		menu.add(saveAsAction.createMenuItem());
	}

	private boolean choseNewSaveFile() {
		FileOpenDialog dial = new FileSaveDialog("Save "+objectName, parentComponent, openDirectory, getExtensionsAsArray());
		File file = dial.getSelectedFile();
		if (file != null) {
			if (file.exists()) {
				int response = JOptionPane.showConfirmDialog(parentComponent, "The file \"" + file.getName() + "\" already exists. Overwrite ?", "Confirm", JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);
				if (response == JOptionPane.YES_OPTION) {
					savedFile = file;
					return true;
				}
			}else{
				savedFile = file;
				return true;
			}
		}
		return false;
	}

	public abstract String save();

	public abstract boolean open(String fileContent);

	public File getSavedFile() {
		return savedFile;
	}

	public boolean open(File file) {
		if( file == null ){
			return false;
		}
		File previousFile = savedFile;
		this.savedFile = file;
		boolean opened = open(IOtools.readFile(savedFile));
		if( !opened ){
			savedFile = previousFile;
			return false;
		}
		return true;
	}

	public List<String> getExtensions() {
		return extensions;
	}

	public Component getParentComponent() {
		return parentComponent;
	}

	public void setParentComponent(Component parentComponent) {
		this.parentComponent = parentComponent;
	}

	public void writeToSavedFile() {
		if (savedFile == null) {
			System.out.println("OpenSaveManager.writeToSavedFile() : savedFile null");
		} else {
			lastSavedContent = save();
			IOtools.writeToFile(savedFile, lastSavedContent);
			System.out.println("OpenSaveManager.writeToSavedFile() : written to file " + savedFile);
//			saveAction.setEnabled(false);
		}
	}

	public boolean openNewFile() {
		saveAction.actionPerformed(null);
		if( savedFile == null ){
			return false;
		}
		open(IOtools.readFile(savedFile));
		return true;
	}
	
	public MyAction getOpenAction() {
		return openAction;
	}

	public MyAction getSaveAction() {
		return saveAction;
	}

	public MyAction getSaveAsAction() {
		return saveAsAction;
	}

}
