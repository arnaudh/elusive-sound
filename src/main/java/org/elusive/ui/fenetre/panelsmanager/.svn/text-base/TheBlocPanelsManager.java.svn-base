package org.elusive.ui.fenetre.panelsmanager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.elusive.sound.blocs.Bloc;
import org.elusive.sound.blocs.BlocPositionne;
import org.elusive.sound.blocs.additive.AdditiveSynth;
import org.elusive.ui.config.Colors;
import org.elusive.ui.fenetre.Fenetre;
import org.elusive.ui.tools.JFrameTools;

/**
 * Class which organises the panels of BlocPositionne
 * you can - add or "show" a bloc
 *         - remove a bloc from it
 * All manipulations (dock in/dock out, delete panel) are handled internally
 * @author arnaudhenry
 *
 */
public class TheBlocPanelsManager {

	private Fenetre fenetre;
	protected List<TheFullElusivePanel> fullPanels;
	
	//UI
	private JPanel panel;
	
	//static
	private final static int DEFAULT_PANEL_HEIGHT = 400;
	
	public TheBlocPanelsManager(Fenetre fenetre) {
		this.fenetre = fenetre;
		fullPanels = new ArrayList<TheFullElusivePanel>();
		panel = new JPanel(new GridLayout());
		panel.setBackground(Colors.MAIN_BACKGROUND_1);
	}
	
	public void updatePanel(){
		panel.removeAll();
		for( TheFullElusivePanel full : fullPanels ){
			if( !full.isDockedOut() ){
				panel.add(full);
			}
		}
		if( panel.getComponentCount() == 0 ){
			//minimize
			fenetre.getSplitPane().setDividerLocation(Integer.MAX_VALUE);
		}else{
			fenetre.getSplitPane().setDividerLocation(DEFAULT_PANEL_HEIGHT);
		}
		panel.revalidate();
		panel.repaint();
	}
	
	public void showBloc( BlocPositionne bloc ){
		//maybe already contained, but doesn't matter
		TheFullElusivePanel full = getFullPanelFor(bloc);
		if( full == null ){
			full = new TheFullElusivePanel(this, bloc);
			fullPanels.add(full);
		}else{
			
		}
		updatePanel();
		if( full.isDockedOut() ){
			full.showDockedOut();
		}else{
			//TODO make panelManager show this panel ?
		}
	}

	public void removeBloc( BlocPositionne bloc ){
		TheFullElusivePanel full = getFullPanelFor(bloc);
		if( full!=null ){
			full.close();
		}
	}
	
	private TheFullElusivePanel getFullPanelFor( BlocPositionne bloc ){
		for( TheFullElusivePanel full : fullPanels ){
			if( full.getBloc().equals(bloc)){
				return full;
			}
		}
		return null;
	}
	
	
	public JPanel getPanel(){
		return panel;
	}

//	protected void dockOut(BlocPositionne bloc) {
//		TheFullElusivePanel full = getFullPanelFor(bloc);
//		
////		JFrame frame = new JFrame(bloc.getElusivePanel().getTitle());
////		frame.setLocationRelativeTo(null);
////		frame.getContentPane().add(full);
////		frame.pack();
////		frame.setVisible(true);		
//		
//		full.setFrame(frame);
//		updatePanel();
//	}
//	
//	protected void dockIn(BlocPositionne bloc){
//		TheFullElusivePanel full = getFullPanelFor(bloc);
//		full.getFrame().setVisible(false);
//		full.setFrame(null);
//		updatePanel();
//	}
}
