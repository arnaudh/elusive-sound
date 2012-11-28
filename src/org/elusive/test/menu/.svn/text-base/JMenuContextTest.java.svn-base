package org.elusive.test.menu;

import java.awt.Component;
import java.awt.MenuItem;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JPopupMenu.Separator;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

//ICIIIIIIIIII
//SALOPERIE DE MERDE
// getItemCount() ET getMenuComponentsCount() comptent les SEPARATORS de merde (et évidemment getItem(x) ne compte pas les SEPARATORS)

//MORALE
// utilise getMenuComponentsCount() et getMenuComponent(x) ET fais gaffe aux separators

public class JMenuContextTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		JFrame frame = new JFrame("Frame");

		JMenuBar menuBar = new JMenuBar();
		JMenu menu;
		JMenuItem item;

		// MyPanelMenu pan1 = new MyPanelMenu("PAN1");
		//
		//
		// pan1.a1.action.setEnabled(false);
		//
		// // MENU
		// menu = new JMenu("File");
		//
		// item = new JMenuItem("New");
		// menu.add(item);
		//
		// menu.add(pan1.a1.toJMenuItem());
		// menu.add(pan1.a2.toJMenuItem());
		//
		//
		// System.out.println("keystroke = " +
		// pan1.a1.toJMenuItem().getAction().isEnabled());
		//
		// // menuBar.add(menu);
		//
		// JMenu m = pan1.menu.toJMenu();
		// System.out.println(m.getText());
		// menuBar.add(m);

		PanelPur pur = new PanelPur();

		Set<KeyStroke> accelerators = getAccelerators(pur.menu);

		// menuBar.add((JMenu) copy(pur.menu, new HashSet<KeyStroke>()) );

		// Set<KeyStroke> forbiddenAccelerators = new HashSet<KeyStroke>();
		// int nMenuBar = menuBar.getMenuCount();
		//
		// for(int m = 0; m < nMenuBar; m++ ){
		// JMenu me = menuBar.getMenu(m);
		// forbiddenAccelerators.addAll(getAccelerators(me));
		// }

		JMenu meme = (JMenu) copy(pur.menu, accelerators);
		merge(menuBar, pur.menu);
		System.out.println(menuBarToString(menuBar));
		merge(menuBar, pur.menu);
		System.out.println(menuBarToString(menuBar));
		merge(menuBar, pur.menu);
		System.out.println(menuBarToString(menuBar));
		
		JMenu memenu = (JMenu)copy(menuBar.getMenu(0), getAccelerators(menuBar.getMenu(0)));
		memenu.setText("Edit");
		merge(menuBar, memenu);
		System.out.println(menuBarToString(menuBar));


		// FRAME
		frame.setJMenuBar(menuBar);
		// frame.add(pan1);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static JMenuItem copy(JMenuItem menu, Set<KeyStroke> forbiddenAccelerators) {
		JMenuItem ret;
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
			KeyStroke acc = menu.getAccelerator();
			if (acc != null && !forbiddenAccelerators.contains(acc)) {
				forbiddenAccelerators.add(acc);
				ret.setAccelerator(menu.getAccelerator());
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
			sb.append("=====");
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

}
