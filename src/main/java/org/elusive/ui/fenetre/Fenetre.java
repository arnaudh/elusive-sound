package org.elusive.ui.fenetre;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import org.elusive.sound.blocs.BlocPositionne;
import org.elusive.sound.play.DataPlayer;
import org.elusive.sound.play.GridPlayer;
import org.elusive.ui.config.Colors;
import org.elusive.ui.fenetre.panelsmanager.TheBlocPanelsManager;
import org.elusive.ui.grille.Grid;

public class Fenetre extends JFrame {

	public Controls controles = null;
	private JSplitPane splitPane;
	public Grid grille = null;
	private TheBlocPanelsManager panelsManager;
	
	public GridPlayer grillePlayer = null;
	public DataPlayer dataPlayer = null;
	
	public Fenetre() {
		super("Elusive Sound");

		// Barre de controles
		controles = new Controls(this);
		panelsManager = new TheBlocPanelsManager(this);
	}

	public void setGrille(Grid g) {
		grille = g;
		grillePlayer = new GridPlayer(grille);
		grillePlayer.load();

		redoLayout();
	}
	
	private void redoLayout(){
		//MENU
		this.setJMenuBar(grille.getMenuBar());

		// SplitPane
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, grille.getPanel(), panelsManager.getPanel());
		splitPane.setContinuousLayout(true);
		
		//LAYOUT
		Container contentPane = new Container();
		contentPane.setLayout(new GridBagLayout());
		contentPane.setBackground(Colors.MAIN_BACKGROUND_1);
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.BOTH;
		contentPane.add(controles, gbc);

		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridy ++;
		gbc.insets = new Insets(0, 0, 0, 0);
		contentPane.add(splitPane, gbc);
		
		// contentPane.add(grillePlayer.getPanel(), gbc);
		
		this.setContentPane(contentPane);
		this.pack();
		splitPane.setDividerLocation(Integer.MAX_VALUE); //has to be done after pack()
	}

	public void showBlocControls(BlocPositionne bloc) {
		panelsManager.showBloc(bloc);
		panelsManager.getPanel().revalidate();
		panelsManager.getPanel().repaint();
	}
	
	public TheBlocPanelsManager getPanelsManager() {
		return panelsManager;
	}
	
	public JSplitPane getSplitPane(){
		return splitPane;
	}

}
