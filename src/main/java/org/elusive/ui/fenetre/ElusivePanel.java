package org.elusive.ui.fenetre;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import org.elusive.main.historique.History;
import org.elusive.main.historique.HistoryListener;
import org.elusive.ui.action.MyAction;
import org.elusive.ui.config.Colors;

public class ElusivePanel extends JPanel implements HistoryListener {

	private static List<ElusivePanel> allElusivePanels = new ArrayList<ElusivePanel>();

	// private JMenuBar menuBar;
	protected String title;
	protected ElusivePanel elusiveParent;
	protected JFrame frame;
	
	protected History history;
	protected List<MyAction> actions;

	
	public ElusivePanel(final String title) {
		super(new BorderLayout());
		this.title = title;
		this.setHistory(new History());
		getHistory().addHistoriqueListener(this);
		this.actions = new ArrayList<MyAction>();
		allElusivePanels.add(this);
//		this.addFocusListener(this);
		
		this.setBackground(Colors.ELUSIVE_PANEL_BACKGROUND);
	}

	protected JPanel createButtonBarFromActions(){
		JPanel bar = new JPanel();
		bar.setBackground(Colors.ELUSIVE_PANEL_BACKGROUND);
		for( MyAction action : actions ){
			bar.add(action.createSimpleIconButton());
		}
		return bar;
	}
	protected JMenuBar createMenuBarFromActions(){
		JMenuBar bar = new JMenuBar();
		List<JMenu> menus = new ArrayList<JMenu>();
		actionFor:for( MyAction action : actions ){
			for( JMenu menu : menus ){
				if( menu.getText().equals( action.getCategory().toMenuString() )){
					menu.add(action);
					continue actionFor;
				}
			}
			String menuName = action.getCategory().toMenuString();
			JMenu menu = new JMenu(menuName);
			menu.add(action);
			menus.add(menu);
		}
		for( JMenu menu : menus ){
			bar.add(menu);
		}
		return bar;
	}

	public String getTitle() {
		return title;
	}

	public void setParent(ElusivePanel parent) {
		this.elusiveParent = parent;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	// Override if neccessary
	@Override
	public void afterAction() {
		repaint();
	}

	public History getHistory() {
		return history;
	}

	public void setHistory(History history) {
		this.history = history;
	}


}
