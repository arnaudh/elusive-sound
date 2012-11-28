package org.elusive.ui.tools;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import org.elusive.ui.action.shortcut.MyKeyStrokes;

public class TextComponentTools {

	public static int getRow(JTextComponent editor, int pos) {
		int rn = (pos == 0) ? 1 : 0;
		try {
			int offs = pos;
			while (offs > 0) {
				offs = Utilities.getRowStart(editor, offs) - 1;
				rn++;
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return rn;
	}

	public static int getColumn(JTextComponent editor, int pos) {
		try {
			return pos - Utilities.getRowStart(editor, pos) + 1;
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static void addUndoRedo(JTextComponent textcomp) {
		final UndoManager undo = new UndoManager();
		undo.setLimit(-1); //unlimited
		Document doc = textcomp.getDocument();
		// Listen for undo and redo events
		doc.addUndoableEditListener(new UndoableEditListener() {
			public void undoableEditHappened(UndoableEditEvent evt) {
				UndoableEdit edit = evt.getEdit();
				if (edit instanceof DefaultDocumentEvent && ((DefaultDocumentEvent) edit).getType() == DefaultDocumentEvent.EventType.CHANGE) {
					return;
				}
				undo.addEdit(evt.getEdit());
			}
		});
		// Create an undo action and add it to the text component
		textcomp.getActionMap().put("Undo", new AbstractAction("Undo") {
			public void actionPerformed(ActionEvent evt) {
				try {
					if (undo.canUndo()) {
						undo.undo();
					}
				} catch (CannotUndoException e) {
					e.printStackTrace();
				}
			}
		});
		// Bind the undo action to ctl-Z
		textcomp.getInputMap().put(MyKeyStrokes.UNDO_KEY_STROKE, "Undo");
		// Create a redo action and add it to the text component
		textcomp.getActionMap().put("Redo", new AbstractAction("Redo") {
			public void actionPerformed(ActionEvent evt) {
				try {
					if (undo.canRedo()) {
						undo.redo();
					}
				} catch (CannotRedoException e) {
					e.printStackTrace();
				}
			}
		});
		// Bind the redo action to ctl-Y
		textcomp.getInputMap().put(MyKeyStrokes.REDO_KEY_STROKE, "Redo");
	}

	public static int getRowPosition(JTextComponent textcomp, int line) {
		int pos = 0;
		try {
			for (int i = 0; i < line; i++) {
				// System.out.println("getRowEnd = "+Utilities.getRowEnd(textcomp,
				// pos));
				pos = Utilities.getRowEnd(textcomp, pos) + 1;
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
			return -1;
		}
		return pos;
	}
}
