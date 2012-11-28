package org.elusive.ui.events;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import org.elusive.main.Main;

public class MouseEventTools {


	public static boolean isControlOrMetaDown(MouseEvent e){
		return Main.isMacOSX() ? isMetaDown(e) : e.isControlDown();		
	}
	public static boolean isAltOrMetaDown(MouseEvent e){
		return Main.isMacOSX() ? isMetaDown(e) : e.isAltDown();		
	}

	private static boolean isMetaDown(MouseEvent e) {
		return (e.getModifiersEx() & InputEvent.META_DOWN_MASK) == InputEvent.META_DOWN_MASK;
	}
	
	
}
