package org.elusive.ui.config;

import java.awt.Toolkit;
import java.awt.event.InputEvent;

import javax.swing.KeyStroke;

public interface ConstantesUI {

	public static KeyStroke KEY_STROKE_OPEN = KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	public static KeyStroke KEY_STROKE_SAVE = KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	public static KeyStroke KEY_STROKE_SAVE_AS = KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + InputEvent.SHIFT_MASK);

}
