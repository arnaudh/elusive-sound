package org.elusive.ui.tools.file;

import java.awt.Component;
import java.awt.FileDialog;
import java.io.File;

public class FileSaveDialog extends FileOpenDialog {

	public FileSaveDialog(String title, Component fen, File repertoire,
			String[] extensions) {
		super(title, fen, repertoire, extensions, true);
	}

}
