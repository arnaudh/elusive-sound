package org.elusive.ui.action.shortcut;

import java.awt.Event;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

public class MyKeyStrokes {
	
	public static final KeyStroke NEW_KEY_STROKE = KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	public static final KeyStroke OPEN_KEY_STROKE = KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	public static final KeyStroke SAVE_KEY_STROKE = KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	public static final KeyStroke SAVE_AS_KEY_STROKE = KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + KeyEvent.SHIFT_DOWN_MASK);

	public static final KeyStroke UNDO_KEY_STROKE = KeyStroke.getKeyStroke('Z', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	public static final KeyStroke REDO_KEY_STROKE = KeyStroke.getKeyStroke('Z', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + KeyEvent.SHIFT_DOWN_MASK);
	
	public static final KeyStroke CUT_KEY_STROKE = KeyStroke.getKeyStroke('X', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	public static final KeyStroke COPY_KEY_STROKE = KeyStroke.getKeyStroke('C', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	public static final KeyStroke PASTE_KEY_STROKE = KeyStroke.getKeyStroke('V', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	public static final KeyStroke SELECT_ALL_KEY_STROKE = KeyStroke.getKeyStroke('A', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	public static final KeyStroke DELETE_KEY_STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
	
	public static final KeyStroke VOLUME_UP_BLOC = KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.ALT_DOWN_MASK);
	public static final KeyStroke VOLUME_DOWN_BLOC = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.ALT_DOWN_MASK);
	public static final KeyStroke VOLUME_ON_BLOC = KeyStroke.getKeyStroke(KeyEvent.VK_UP, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + KeyEvent.ALT_DOWN_MASK);
	public static final KeyStroke VOLUME_OFF_BLOC = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + KeyEvent.ALT_DOWN_MASK);

	//Selection
	public static final KeyStroke SELECTION_COPY_KEY_STROKE = KeyStroke.getKeyStroke('C', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + KeyEvent.SHIFT_DOWN_MASK);
	public static final KeyStroke SELECTION_PASTE_KEY_STROKE = KeyStroke.getKeyStroke('V', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + KeyEvent.SHIFT_DOWN_MASK);
	
	
	// Tab Partition Editor
	public static final KeyStroke INSERT_CHORD_STROKE = KeyStroke.getKeyStroke('K', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + Event.SHIFT_MASK);
	public static final KeyStroke COLUMN_COPY_STROKE = KeyStroke.getKeyStroke('C', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + Event.SHIFT_MASK);
	public static final KeyStroke COLUMN_CUT_STROKE = KeyStroke.getKeyStroke('X', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + Event.SHIFT_MASK);
	public static final KeyStroke COLUMN_PASTE_STROKE = KeyStroke.getKeyStroke('V', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + Event.SHIFT_MASK);
}
