package org.elusive.ui.grille;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.elusive.main.historique.ActionHistoriqueMultiple;
import org.elusive.main.historique.action.AjouteBloc;
import org.elusive.main.historique.action.AjouteEffet;
import org.elusive.main.performance.MemoryStatus;
import org.elusive.sound.analyse.Analyser;
import org.elusive.sound.blocs.Bloc;
import org.elusive.sound.blocs.BlocFichier;
import org.elusive.sound.blocs.BlocPositionne;
import org.elusive.sound.effets.Effet;
import org.elusive.sound.tempo.ui.TempoPanel;
import org.elusive.sound.tools.SoundTools;
import org.elusive.ui.action.MyAction;
import org.elusive.ui.action.shortcut.MyKeyStrokes;
import org.elusive.ui.fenetre.ShowableMenuFrame;
import org.elusive.ui.tools.file.FileOpenDialog;
import org.elusive.ui.tools.file.FileSaveDialog;

public class GridMenuBar extends JMenuBar {

	private Grid grid;
	
	//actions
	private MyAction cutAction;
	private MyAction copyAction;
	private MyAction pasteAction;
	private MyAction selectAllAction;
	private MyAction deleteAction;
	private MyAction selectionToFileAction;
	private MyAction editTempoSettingsAction;
	
	public GridMenuBar(Grid grid_) {
		this.grid = grid_;
		
		initActions();

		JMenu menu;

		// *********************** FILE ************************** //
		menu = new JMenu("File");
		grid.getOpenSaveManager().addToMenu(menu);

		this.add(menu);

		// *********************** EDIT ************************** //
		menu = new JMenu("Edit");
		menu.add(grid.getHistorique().getUndoAction());
		menu.add(grid.getHistorique().getRedoAction());

		menu.addSeparator();
		menu.add(cutAction.createMenuItem());
		menu.add(copyAction.createMenuItem());
		menu.add(pasteAction.createMenuItem());

		menu.addSeparator();
		menu.add(selectAllAction.createMenuItem());
		menu.add(deleteAction.createMenuItem());

		this.add(menu);

		// *********************** BLOCS ************************** //
		this.add(getMenuBlocs());

		// *********************** EFFETS ************************** //

		this.add(getMenuEffets());

		// *********************** SELECTION ************************** //
		menu = new JMenu("Selection");
		menu.add(grid.getClipboard().getCopyAction().createMenuItem());
		menu.add(grid.getClipboard().getPasteAction().createMenuItem());
		menu.add(selectionToFileAction);
		this.add(menu);

		// *********************** ANALYSE ************************** //

		this.add(getMenuAnalyse());

		// *********************** Affichage ************************** //
		menu = new JMenu("View");
		// ********** HISTORIQUE ********** //
		menu.add(grid.getHistorique().getShowAction());
		// ********** MEMORY ********** //
		ShowableMenuFrame memory = new ShowableMenuFrame("Memory");
		Thread th = new Thread(new MemoryStatus(memory));
		th.start();
		menu.add(memory.getShowAction());
		// ********** TEMPO ********** //
		menu.add(editTempoSettingsAction.createMenuItem());

		this.add(menu);

	}
	
	private void initActions(){
		cutAction = new MyAction("Cut", MyKeyStrokes.CUT_KEY_STROKE, "Cut blocs", null) {
			@Override
			public void actionPerformed(ActionEvent e) {
				grid.copy();
				grid.deletedSelectedBlocs();
			}
		};
		copyAction = new MyAction("Copy", MyKeyStrokes.COPY_KEY_STROKE, "Copy blocs", null) {
			@Override
			public void actionPerformed(ActionEvent e) {
				grid.copy();
			}
		};
		pasteAction = new MyAction("Paste", MyKeyStrokes.PASTE_KEY_STROKE, "Paste blocs", null) {
			@Override
			public void actionPerformed(ActionEvent e) {
				grid.paste();
			}
		};
		selectAllAction = new MyAction("Select all", MyKeyStrokes.SELECT_ALL_KEY_STROKE, "Select all blocs", null) {
			@Override
			public void actionPerformed(ActionEvent e) {
				grid.selectAll();
			}
		};
		deleteAction = new MyAction("Delete", MyKeyStrokes.DELETE_KEY_STROKE, "Delete selected blocs", null) {
			@Override
			public void actionPerformed(ActionEvent e) {
				grid.deletedSelectedBlocs();
			}
		};
		selectionToFileAction = new MyAction("Save to file", null, "Save selection to file", null) {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileSaveDialog dial = new FileSaveDialog("Save selection to file", grid.fenetre, null, FileOpenDialog.getSupportedAudioFileExtensions());
				File file = dial.getSelectedFile();
				if( file != null ){
					SoundTools.writeDataToFile(grid.getSelectedData(), file);
				}
			}
		};
		editTempoSettingsAction = new MyAction("Show tempo settings", null, "Show tempo settings", null) {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame("Tempo");
				frame.add(new TempoPanel(grid.getTempo()));
				frame.pack();
				frame.setAlwaysOnTop(true);
				frame.setVisible(true);
			}
		};
	}
	

	public JMenu getMenuBlocs() {
		JMenu menu = new JMenu("Bloc");

		for (final Class<? extends Bloc> c : Bloc.liste) {
			JMenuItem item = new JMenuItem(c.getSimpleName());
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						Bloc b = c.newInstance();
						if( b instanceof BlocFichier){
							FileOpenDialog dial = new FileOpenDialog("Open", grid.getPanel(), null, FileOpenDialog.getSupportedAudioFileExtensions());
							File f = dial.getSelectedFile();
							if( f == null ){
								return;
							}
							((BlocFichier) b).setFichier(f);
						}
						AjouteBloc action = new AjouteBloc(grid, b);
						grid.getHistorique().execute(action);
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			});
			menu.add(item);
		}

		return menu;
	}

	public JMenu getMenuEffets() {
		JMenu menu = new JMenu("Effet");

		for (final Class<? extends Effet> c : Effet.liste) {
			JMenuItem item = new JMenuItem(c.getSimpleName());
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (!grid.getBlocsSelectionnes().isEmpty()) {
						try {
							Effet eff = c.newInstance();
							ActionHistoriqueMultiple action = new ActionHistoriqueMultiple("Ajoute Effet");
							for (BlocPositionne bp : grid.getBlocsSelectionnes()) {
								action.addAction(new AjouteEffet(grid, eff, bp));
							}
							grid.getHistorique().execute(action);

						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			});
			menu.add(item);
		}

		return menu;

	}

	public JMenu getMenuAnalyse() {
		JMenu menu = new JMenu("Analyse");

		for (final Class<? extends Analyser> c : Bloc.listeAnalysers) {
			JMenuItem item = new JMenuItem(c.getSimpleName());
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					float[] data = grid.getClipboard().getData();
					if (data != null) {
						try {
							Analyser b = c.newInstance();
							b.setGrille(grid);
							b.analyse(data);
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			});
			menu.add(item);

		}

		return menu;

	}
}
