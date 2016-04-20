package org.elusive.ui.menus;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.JPopupMenu.Separator;

import com.sun.media.sound.Toolkit;

public class MenuTools {
	
	public static JMenuBar mergeMenuBars(JMenuBar bar, JMenuBar bar2){
		JMenuBar ret = new JMenuBar();
//		System.out.println("MenuTools.mergeMenuBars("+menuBarToOneLineString(bar)+" AND "+menuBarToOneLineString(bar2)+")");
		if( bar != null ){
			for(int i = 0, nMenu = bar.getMenuCount(); i < nMenu; i++){
				merge(ret, bar.getMenu(i));
			}
		}
		if( bar2 != null ){
			for(int i = 0, nMenu = bar2.getMenuCount(); i < nMenu; i++){
				merge(ret, bar2.getMenu(i));
			}
		}
		return ret;
	}
	
	
	
	public static JMenuItem copy(JMenuItem menu, Set<KeyStroke> forbiddenAccelerators) {
		JMenuItem ret;
		if( forbiddenAccelerators == null ){
			forbiddenAccelerators = new HashSet<KeyStroke>();
		}
		if (menu instanceof JMenu) {
			ret = new JMenu(menu.getText());
			for (Object o : ((JMenu) menu).getMenuComponents()) {
				if (o instanceof JMenuItem) {
					JMenuItem menuItem = (JMenuItem) o;
					ret.add(copy(menuItem, forbiddenAccelerators));
				} else if (o instanceof JSeparator) {
					((JMenu) ret).addSeparator();
				}
			}
		} else {
			// Item
			if (menu instanceof JCheckBoxMenuItem) {
				ret = new JCheckBoxMenuItem(menu.getText());
				bindItemStateChanged(ret, menu);
			} else {
				ret = new JMenuItem(menu.getText());
				Action action = menu.getAction();
				if (action != null) {
					ret.setAction(action);
				}
			}
			//copy actionlisteners
			for(ActionListener lis : menu.getActionListeners() ){
				ret.addActionListener(lis);
			}
			KeyStroke acc = menu.getAccelerator();
			if (acc != null && !forbiddenAccelerators.contains(acc)) {
				forbiddenAccelerators.add(acc);
				ret.setAccelerator(acc);
			}
		}
		return ret;
	}

	public static void merge(JMenuBar menuBar, JMenu menu) {
		Set<KeyStroke> forbiddenAccelerators = new HashSet<KeyStroke>();
		int nMenuBar = menuBar.getMenuCount();
		for (int m = 0; m < nMenuBar; m++) {
			JMenu me = menuBar.getMenu(m);
			forbiddenAccelerators.addAll(getAccelerators(me));
		}
		String text = menu.getText();
		for (int m = 0; m < nMenuBar; m++) {
			JMenu me = menuBar.getMenu(m);
			if (me.getText().equals(text)) {
				// Merge needed
				boolean seperatorAdded = false;

				int nItem = menu.getMenuComponentCount();
				for (int i = 0; i < nItem; i++) {
					Component comp = menu.getMenuComponent(i);
					if( comp instanceof Separator){
						continue;
					}					
					JMenuItem item = (JMenuItem) comp;
					
					boolean inserted = false;

					int nnItem = me.getMenuComponentCount();
					for (int ii = 0; ii < nnItem; ii++) {
						Component compp = me.getMenuComponent(ii);
						if( compp instanceof Separator ){
							continue;
						}
						JMenuItem iitem = (JMenuItem) compp;
						if (iitem.getText().equals(item.getText())) {
							// TODO merge needed OU PAS
							System.out.println("//TODO merge needed for " + item.getText());
							// inserted = true;
							break;
						}
					}

					if (!inserted) {
						// if first, add seperator
						if (!seperatorAdded) {
							me.addSeparator();
							seperatorAdded = true;
						}
						JMenuItem meme = copy(item, forbiddenAccelerators);
						me.add(meme);
					}
				}
				// Merged
				return;
			}
		}
		// Not merged -> just add a copy
		menuBar.add(copy(menu, forbiddenAccelerators));
	}

	public static Set<KeyStroke> getAccelerators(JMenuItem item) {
		Set<KeyStroke> ret = new HashSet<KeyStroke>();
		if (item instanceof JMenu) {
			JMenu menu = ((JMenu) item);
			int nItem = menu.getMenuComponentCount();
			for (int i = 0; i < nItem; i++) {
				Component comp = menu.getMenuComponent(i);
				if( comp instanceof Separator){
					continue;
				}					
				JMenuItem it = (JMenuItem) comp;
				ret.addAll(getAccelerators(it));
			}
		} else {
			if (item.getAccelerator() != null) {
				ret.add(item.getAccelerator());
			}
		}
		return ret;
	}

	public static void bindItemStateChanged(final JMenuItem i1, final JMenuItem i2) {
		ItemListener listener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean selected = e.getStateChange() == ItemEvent.SELECTED ? true : false;
				i1.setSelected(selected);
				i2.setSelected(selected);
			}
		};
		i1.addItemListener(listener);
		i2.addItemListener(listener);
	}

	public static String menuBarToString(JMenuBar menuBar) {
		StringBuilder sb = new StringBuilder("=====BAR=====\n");
		int n = menuBar.getMenuCount();
		for (int i = 0; i < n; i++) {
			JMenu menu = menuBar.getMenu(i);
			sb.append(menuToString(menu));
			sb.append("=====\n");
		}
		return sb.toString();
	}
	
	public static String menuBarToOneLineString(JMenuBar menuBar){
		if( menuBar == null ) return "null";
		StringBuilder sb = new StringBuilder();
		int n = menuBar.getMenuCount();
		for (int i = 0; i < n; i++) {
			if( i > 0 ){
				sb.append(", ");
			}
			JMenu menu = menuBar.getMenu(i);
			sb.append(menu.getText());
		}
		return sb.toString();
		
	}

	public static String menuToString(JMenuItem menuItem) {
		return menuToString(menuItem, 0);
	}

	public static String menuToString(JMenuItem menuItem, int indentation) {
		StringBuilder sb = new StringBuilder();

		String strIndent = strIndent(indentation);

		sb.append(strIndent + menuItem.getText());

		if (menuItem instanceof JCheckBoxMenuItem) {
			sb.append(" [");
			if (((JCheckBoxMenuItem) menuItem).getState()) {
				sb.append("X");
			}
			sb.append("]");
		}

		if (menuItem.getAccelerator() != null) {
			sb.append(" {" + menuItem.getAccelerator() + "}");
		}
		if (!menuItem.isEnabled()) {
			sb.append(" (disabled)");
		}
		sb.append("\n");

		if (menuItem instanceof JMenu) {
			JMenu menu = (JMenu) menuItem;
			for (Object o : menu.getMenuComponents()) {
				if (o instanceof JMenuItem) {
					JMenuItem menu2 = (JMenuItem) o;
					String str = menuToString(menu2, indentation + 3);
					sb.append(str);
				} else if (o instanceof JSeparator) {
					sb.append(strIndent + "------\n");
				}
			}
		}
		return sb.toString();
	}

	public static String strIndent(int indentation) {
		StringBuilder sbIndent = new StringBuilder();
		for (int i = 0; i < indentation; i++) {
			sbIndent.append(' ');
		}
		String strIndent = sbIndent.toString();
		return strIndent;
	}
	
	//Seperates the menuBar's menus with seperators, all showing in one list
	public static JPopupMenu menuBarToPopupMenu( JMenuBar menuBar ){
		JPopupMenu popup = new JPopupMenu();
		int nMenu = menuBar.getMenuCount();
		for (int i = 0; i < nMenu; i++) {
			if (i > 0) {
				popup.addSeparator();
			}
			JMenu menu = menuBar.getMenu(i);
//			for (Object o : menu.getMenuComponents()) {
//				if (o instanceof JMenuItem) {
//					popup.add(copy((JMenuItem) o, null));
//				}else{
//					System.err.println("MenuTooListener.addMenuBarToPopupMenu() OBJECT NOT INSTANCE OF JMenuItem : "+o);
//				}
//			}
			popup.add(copy(menu, null));
		}
		return popup;
	}
	
	
	
	
	public static void main(String[] args) {
		test();
	}
	
	public static void test() {
		JMenuBar bar1 = new JMenuBar();
		JMenu menu;
		JMenu menu2;
		JMenuItem item;
		
		menu = new JMenu("File");
		menu2 = new JMenu("Open");
		menu2.add(new JMenuItem("File"));
		menu2.add(new JMenuItem("Folder"));
		menu2.add(new JMenuItem("Other..."));
		menu.add(menu2);
		menu.addSeparator();
		menu.add(new JCheckBoxMenuItem("check"));
		bar1.add(menu);
		menu = new JMenu("Edit");
		item = new JMenuItem("Undo");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.add(item);
		item = new JMenuItem("Redo");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.add(item);
		bar1.add(menu);
		
		JMenuBar bar2 = mergeMenuBars(bar1, bar1);

//		System.out.println(menuBarToString(bar1));
//		System.out.println(menuBarToString(bar2)); 		
		
		testBarOnFrame(bar2);
	}
	
	public static void testBarOnFrame(JMenuBar bar){
		JFrame frame = new JFrame();
		frame.setJMenuBar(bar);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
