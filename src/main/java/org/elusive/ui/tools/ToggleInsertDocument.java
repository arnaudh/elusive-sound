package org.elusive.ui.tools;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.PlainDocument;

import org.elusive.main.Main;

public class ToggleInsertDocument extends DefaultStyledDocument implements KeyListener {
	private boolean insertMode = true;

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_INSERT || ( /* Main.IS_MAC && */ e.getKeyCode() == KeyEvent.VK_F13) ) {
			toggleInsertMode();
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	public boolean isInsertMode() {
		return insertMode;
	}

	public void toggleInsertMode() {
		insertMode = !insertMode;
	}

	public void setInsertMode(boolean insertMode) {
		this.insertMode = insertMode;
	}

	public void insertString(int offs, String str, AttributeSet a)
			throws BadLocationException {
		super.insertString(offs, str, a);
		if (!insertMode) { //Have to remove after, not before, otherwise ther's a bug when undoing a column paste (don't ask why...)
			int docLength = getLength();
			int length = str.length();
			offs += length;
			if (offs < docLength) {
				int remainder = docLength - offs;
				length = (length > remainder) ? remainder : length;
				remove(offs, length);
			}
		}
	}
}