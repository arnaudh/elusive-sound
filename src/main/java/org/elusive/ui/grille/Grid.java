package org.elusive.ui.grille;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import org.elusive.main.historique.ActionHistorique;
import org.elusive.main.historique.ActionHistoriqueMultiple;
import org.elusive.main.historique.History;
import org.elusive.main.historique.HistoryListener;
import org.elusive.main.historique.action.AjouteBloc;
import org.elusive.main.historique.action.ColleBlocs;
import org.elusive.main.historique.action.SupprimeBloc;
import org.elusive.main.persistence.ElusiveProperties;
import org.elusive.main.persistence.ResourceManager;
import org.elusive.main.persistence.xml.XMLException;
import org.elusive.main.persistence.xml.XmlTools;
import org.elusive.main.tools.IOtools;
import org.elusive.sound.blocs.Bloc;
import org.elusive.sound.blocs.BlocFichier;
import org.elusive.sound.blocs.BlocPositionne;
import org.elusive.sound.effets.Effet;
import org.elusive.sound.rythm.Rythm;
import org.elusive.sound.tempo.Tempo;
import org.elusive.ui.action.MyAction;
import org.elusive.ui.fenetre.Controls;
import org.elusive.ui.fenetre.Fenetre;
import org.elusive.ui.grille.line.GridLine;
import org.elusive.ui.tools.file.FileDrop;
import org.elusive.ui.tools.file.OpenSaveManager;

public class Grid implements HistoryListener {

	// static
	public static final int FRAMES_PER_SECOND = 44100;
	public static final File SAVE_DIRECTORY = new File("sauvegardes");
	public static final int DEFAULT_LINE_NUMBER = 20;
	public static final int frameMax = FRAMES_PER_SECOND * 600; // morceau de 10min

	public Fenetre fenetre;
	public Controls controls;

	private History history;
	private OpenSaveManager openSaveManager;
	private Clipboard clipboard;

	private ArrayList<BlocPositionne> blocs = null;
	private ArrayList<BlocPositionne> blocsSelectionnes = null;
	private ArrayList<String> blocsCopies = null;
	
	// Lines
	private List<GridLine> lines;

	private Rythm rythm = new Rythm();
	private static Tempo tempo = new Tempo(126.0);

	public int longueurLigne = FRAMES_PER_SECOND * 20; // nbFrames
	public JPanel panel = null; // le tout
	public Graduations graduations = null;
	private GridPanel panelGrille = null;
	private JScrollBar scrollH = null;
	private JScrollBar scrollV = null;

	public int curseur = 0;
	private Selection selection = null;


	
	//menu
	private GridMenuBar menuBar;

	public Grid(Fenetre fenetre, File sauvegarde) {
		grilleDepot = this;
		this.fenetre = fenetre;
		this.controls = fenetre.controles;
		reset(null);

		// Historique
		history = new History(true);
		history.addHistoriqueListener(this);

		// OpenSaveManger
		openSaveManager = new OpenSaveManager(getPanelGrille(), SAVE_DIRECTORY, "project", ".elu") {
			@Override
			public String save() {
				ElusiveProperties.put(ElusiveProperties.KEY_PROJECT, openSaveManager.getSavedFile().getAbsolutePath());
				return Grid.this.save();
			}

			@Override
			public boolean open(String fileContent) {
				try {
					Grid.this.open(fileContent);
				} catch (XMLException e) {
					JOptionPane.showMessageDialog(Grid.this.getPanelGrille(), "Il s'est produit une erreur lors de la lecture du fichier XML (cf console).\n\n\n" + e.getMessage(), "Erreur",
							JOptionPane.ERROR_MESSAGE);
					System.err.println(e);
					return false;
				}
				ElusiveProperties.put(ElusiveProperties.KEY_PROJECT, openSaveManager.getSavedFile().getAbsolutePath());
				history.clear();
				return true;
			}
		};
		openSaveManager.open(sauvegarde);

		//Clipboard
		clipboard = new Clipboard(this);
		
		//UI
		initUI();

		// Drag and Drop
		new FileDrop(this.getPanelGrille(), new FileDrop.Listener() {
			public void filesDropped(File[] files) {
				for (File file : files) {
					try {
						File ressourceFile = IOtools.copyFileInDirectory(file, getResourcesDir());

						// Create bloc from ressource file
						Bloc bloc = new BlocFichier(ressourceFile);
						AjouteBloc action = new AjouteBloc(Grid.this, bloc);
						history.execute(action);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		// blocs.add(new BlocPositionne(new KarplusStrong(), 1, 0));
		// blocs.add(new BlocPositionne(new WhiteNoise(), 2, 0));
		// blocs.add(new BlocPositionne(new FMSynthetizer(), 3, 0));
		// blocs.add(new BlocPositionne(new AdditiveSynth(), 4, 0));

		menuBar = new GridMenuBar(this);
	}

	private void initUI() {
		panel = new JPanel(new GridBagLayout());

		scrollH = new JScrollBar(JScrollBar.HORIZONTAL);
		scrollH.setFocusable(true);
		updateScrollH(0);

		int max = GridPanel.LINE_HEIGHT * lines.size();
		scrollV = new JScrollBar(JScrollBar.VERTICAL, 0, max / 5, 0, max);

		scrollH.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				panel.repaint();
			}
		});
		scrollV.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				panel.repaint();
			}
		});

		graduations = new Graduations(this);
		setPanelGrille(new GridPanel(this));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.weightx = 1.;
		gbc.weighty = 0;
		panel.add(graduations, gbc);
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		panel.add(getPanelGrille(), gbc);
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.gridx = 1;
		panel.add(scrollV, gbc);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(scrollH, gbc);
		getPanelGrille().setBorder(BorderFactory.createLineBorder(Color.black));
		panel.setBorder(BorderFactory.createLineBorder(Color.black));
		// panel.setMinimumSize(dim);
		// panel.setPreferredSize(dim);
	}

	public JPanel getPanel() {
		return panel;
	}

	public int getSrollVValue() {
		return scrollV.getValue();
	}

	public void repaint() {
		// panel.paintImmediately(new Rectangle(panel.getSize()));
		if( panel != null ){
			panel.repaint();
		}
	}
	
	

	public List<GridLine> getLines() {
		return lines;
	}

	public ArrayList<BlocPositionne> getBlocs() {
		return blocs;
	}

	public ArrayList<BlocPositionne> getBlocsSelectionnes() {
		return blocsSelectionnes;
	}

	public boolean ajoutable(BlocPositionne bp) {
		return ajoutable(bp, blocs);
	}

	public boolean ajoutable(BlocPositionne bp, ArrayList<BlocPositionne> blocsPositionnes) {
		return ajoutable(bp.getLigne(), bp.getDebut(), bp.getFin(), blocsPositionnes);
	}

	public boolean ajoutable(int ligne, int debut, int fin, ArrayList<BlocPositionne> blocsFixes) {
		for (BlocPositionne b : blocsFixes) {
			if (b.getLigne() == ligne) {
				if (b.getDebut() < fin && b.getFin() > debut) {
					// ils se chevauchent
					return false;
				}
			}
		}
		return true;
	}

	public int getOffset() {
		int val = scrollH.getValue();
		int nbFramesParPixel = controls.getFramesPerPixel();
		if (nbFramesParPixel  > 0) {
			val *= nbFramesParPixel;
		} else {
			val /= (-nbFramesParPixel);
		}
		return val;
	}

	public void deletedSelectedBlocs() {
		if (!getBlocsSelectionnes().isEmpty()) {
			ActionHistoriqueMultiple action = new ActionHistoriqueMultiple("Supprime Blocs");
			for (BlocPositionne bp : this.getBlocsSelectionnes()) {
				action.addAction(new SupprimeBloc(this, bp));
			}
			this.history.execute(action);
		}
	}

	public void selectAll() {
		blocsSelectionnes = (ArrayList<BlocPositionne>) blocs.clone();
		repaint();
	}

	public BlocPositionne newBloc(Bloc bloc) {
		int ligne = 0;
		int offset = curseur;
		if (getPanelGrille() != null) {
			Point lastClic = getPanelGrille().getLastClic();
			if (lastClic != null) {
				ligne = getPanelGrille().ligneAt(lastClic);
				offset = getPanelGrille().pixel2offset(lastClic.x);
			}
		}
		BlocPositionne bp = new BlocPositionne(this, bloc, ligne, offset, new ArrayList<Effet>());
		boolean b = newBloc(bp);
		if (b) {
			return bp;
		} else {
			return null;
		}
	}

	// On ajoute le bloc là où il "est", sinon sur une autre ligne
	public boolean newBloc(BlocPositionne bp) {
		boolean ret = false;
		if (ajoutable(bp)) {
			getBlocs().add(bp);
			ret = true;
		} else {
			int ligne = bp.getLigne();
			for (int l = ligne + 1; l != ligne; l++) {
				if (l >= lines.size()) {
					l = 0;
				}
				bp.setLigne(l);
				if (ajoutable(bp)) {
					getBlocs().add(bp);
					ret = true;
					break;
				}
			}
		}
		return ret;
	}

	// TODO : à déplacer dans l'UI
	public void updateScrollH(int oldCursorPixelPosition) {
		// il faut placer le curseur LA OU IL ETAIT, si possible.
		int off = curseur;

		int value = 0;
		int extent = 1;
		int min = 0;
		int max = frameMax;

		int unitIncrement = 10;

		int nbFramesPerPixel = controls.getFramesPerPixel();
		if (nbFramesPerPixel  > 0) {
			max /= nbFramesPerPixel;
			off /= nbFramesPerPixel;
		} else {
			max *= -nbFramesPerPixel;
			off *= -nbFramesPerPixel;
			unitIncrement = -nbFramesPerPixel;
		}

		if (getPanelGrille() != null) {
			value = off - oldCursorPixelPosition;
			if (nbFramesPerPixel < 0) {
				// il faut s'assurer qu'on commence sur une frame
				value -= value % (-nbFramesPerPixel);
			}
			if (value < 0) {
				value = 0;
			}
		}

		scrollH.setUnitIncrement(unitIncrement);
		scrollH.setBlockIncrement(10 * unitIncrement);
		int ancienneVal = scrollH.getValue();
		scrollH.setValues(value, extent, min, max);
		if (ancienneVal == value) {
			// il faut appeler un repaint car l'adjustment listener n'est pas
			// appelé
			// (cas où l'échelle passe de -1 à 1 et vice versa)
			repaint();
		}
	}

	public void copy() {
		System.out.println(" grille.copie()");
		blocsCopies.clear();
		for (BlocPositionne bp : blocsSelectionnes) {
			String xml = XmlTools.toXML(bp);
			System.out.println("*******************************COPIED :\n" + xml);
			blocsCopies.add(xml);
		}
	}

	public void paste() {
		if (!blocsCopies.isEmpty()) {
			int ligne = getPanelGrille().ligneAt(getPanelGrille().getLastClic());
			ActionHistorique ah = null;
			try {
				ah = new ColleBlocs(this, blocsCopies, curseur, ligne);
			} catch (XMLException e) {
				e.printStackTrace();
			}
			this.history.execute(ah);
		}
	}

	public void reset(ArrayList<BlocPositionne> blocs2) {
		blocs = blocs2;
		if (blocs == null) {
			blocs = new ArrayList<BlocPositionne>();
		}
		for(BlocPositionne bp : blocs){
//			System.out.println("bp "+bp+" setGrille");
			bp.setGrille(this);
		}
		blocsSelectionnes = new ArrayList<BlocPositionne>();
		blocsCopies = new ArrayList<String>();
		
		lines = new ArrayList<GridLine>();
		for( int i = 0; i < DEFAULT_LINE_NUMBER; i++ ){
			lines.add(new GridLine(this));
		}
	}

	public void setOffset(int newOffset) {
		if (newOffset < 0) {
			newOffset = 0;
		}
		int val = newOffset;
		int nbFramesPerPixel = controls.getFramesPerPixel();
		if (nbFramesPerPixel  > 0) {
			val /= nbFramesPerPixel;
		} else {
			val *= (-nbFramesPerPixel);
		}
		scrollH.setValue(val);
		// updateScrollH();
	}

	public JScrollBar getScrollV() {
		return scrollV;
	}

	public void setScrollVValue(int newY) {
		scrollV.setValue(newY);
	}
	
	public JScrollBar getScrollH(){
		return scrollH;
	}
	
	public void setScrollHValue(int newH){
		scrollH.setValue(newH);
	}

	public static Tempo getTempo() {
		return tempo;
	}

	// Ajoute un hit au rythme solitaire, à l'endroit où se trouve le curseur
	public void hitRythm() {
		int decalage = fenetre.grillePlayer.isPlaying() ? -20000 : 0; // TODO :
																		// empirique...
		rythm.addHit(curseur + decalage);
	}

	public Rythm getRythm() {
		return rythm;
	}

	@Override
	public void afterAction() {
		repaint();
	}

	private static Grid grilleDepot;

	public static Grid getMainGrid() {
		return grilleDepot;
	}

	public void remove(BlocPositionne blocASupprimer) {
		blocs.remove(blocASupprimer);
		fenetre.getPanelsManager().removeBloc(blocASupprimer);
	}

	public void open(String fileContent) throws XMLException {
		ResourceManager.setProjectFile(openSaveManager.getSavedFile()); //do before reading XML
		ArrayList<BlocPositionne> blocs = (ArrayList<BlocPositionne>) XmlTools.fromXML(fileContent);
		this.reset(blocs);
		this.repaint();
	}

	private String save() {
		//if resource folder in temp, create proper "resources/" folder where file is saved
		if( ResourceManager.getResourcesDir().getAbsolutePath().startsWith(IOtools.getTemporaryFolder().toString())){
			ResourceManager.setProjectFile(openSaveManager.getSavedFile());
		}
		for (BlocPositionne bp : this.getBlocs()) {
			if (bp.getBloc() instanceof BlocFichier) {
				BlocFichier bf = (BlocFichier) bp.getBloc();
				if (!bf.getFichier().getAbsolutePath().startsWith(getResourcesDir().getAbsolutePath())) {
					// if file not in resources, put it in resources
					try {
						File resourceFile = IOtools.copyFileInDirectory(bf.getFichier(), getResourcesDir());
						bf.setFichier(resourceFile);
						System.out.println("Fenetre.save() - file copied to resources : " + resourceFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		String s = XmlTools.toXML(this.getBlocs());
		s = XmlTools.XML_HEADER + s;
		return s;
	}

	public OpenSaveManager getOpenSaveManager() {
		return openSaveManager;
	}

	public File getResourcesDir() {
		return ResourceManager.getResourcesDir();
//		File resources = null;
//		if (openSaveManager.getSavedFile() != null && openSaveManager.getSavedFile().getParent() != null) {
//			resources = new File(openSaveManager.getSavedFile().getParent() + File.separator + "resources");
//		} else {
//			// need intern temp directory
//			File dir = IOtools.getTemporaryFolder();
//			resources = new File(dir.getAbsolutePath() + File.separator + "Elusive");
//
//		}
//		if (!resources.isDirectory()) {
//			boolean b = resources.mkdir();
//			if (!b) {
//				System.err.println("Fenetre : couldn't mkdir the directory : " + resources);
//				return null;
//			}
//		}
//		return resources;

	}

	public History getHistorique() {
		return history;
	}

	public Selection getSelection() {
		return selection;
	}

	public void setSelection(Selection selection) {
		this.selection = selection;
	}


	public Clipboard getClipboard() {
		return clipboard;
	}

	public void setPanelGrille(GridPanel panelGrille) {
		this.panelGrille = panelGrille;
	}

	public GridPanel getPanelGrille() {
		return panelGrille;
	}

	public GridMenuBar getMenuBar() {
		return menuBar;
	}
	
	public float[] getSelectedData(){
		fenetre.grillePlayer.load(getSelection().getDebut(), getSelection().getFin());
		float[] dataFull = fenetre.grillePlayer.getData();
		float[] data = new float[getSelection().getFin() - getSelection().getDebut()];
		for (int i = 0, l = data.length; i < l; i++) {
			data[i] = dataFull[i + getSelection().getDebut()];
		}
		return data;
	}

}
