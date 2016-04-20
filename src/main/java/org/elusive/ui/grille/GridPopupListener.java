package org.elusive.ui.grille;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.elusive.main.historique.ActionHistoriqueMultiple;
import org.elusive.main.historique.action.AjouteBloc;
import org.elusive.main.historique.action.SupprimeBloc;
import org.elusive.main.persistence.xml.XMLException;
import org.elusive.main.persistence.xml.XmlTools;
import org.elusive.sound.blocs.BlocPositionne;
import org.elusive.sound.blocs.frequenceur.Frequencable;
import org.elusive.sound.blocs.frequenceur.Frequenceur;
import org.elusive.sound.genetics.GeneticExplorer;
import org.elusive.sound.genetics.Geneticable;
import org.elusive.sound.genetics.ui.GeneticExplorerPanel;
import org.elusive.sound.genetics.ui.GeneticableAction;
import org.elusive.ui.tools.PopupListener;

public class GridPopupListener extends PopupListener {
	
	private Grid grid;
	
	public GridPopupListener(Grid grid) {
		super();
		this.grid = grid;
	}

	@Override
	public JPopupMenu showMenu(MouseEvent e) {
			JPopupMenu popup = new JPopupMenu();
			final BlocPositionne blocPos = grid.getPanelGrille().blocAt(e.getPoint());
			if (blocPos != null) {
				JMenuItem menuItem;

				popup.add(grid.getMenuBar().getMenuEffets());

				menuItem = new JMenuItem("Supprimer");
				menuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						grid.deletedSelectedBlocs();
					}
				});
				popup.add(menuItem);

				if (blocPos.getBloc() instanceof Frequencable) {
					menuItem = new JMenuItem("Frequenceur");
					menuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							try {
								Frequencable frequencable = (Frequencable) XmlTools.fromXML(XmlTools.toXML(blocPos.getBloc()));
								Frequenceur fre = new Frequenceur(frequencable);
								BlocPositionne bpFre = new BlocPositionne(grid, fre, blocPos.getLigne(), blocPos.getDebut());
								ActionHistoriqueMultiple ah = new ActionHistoriqueMultiple("Frequenceur");
								ah.addAction(new SupprimeBloc(grid, blocPos));
								ah.addAction(new AjouteBloc(grid, bpFre));
								grid.getHistorique().execute(ah);
							} catch (XMLException e) {
								e.printStackTrace();
							}
						}
					});
					popup.add(menuItem);
				}

				if (blocPos.getBloc() instanceof Geneticable) {
					menuItem = new JMenuItem("Genetics Explorer");
					menuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							GeneticExplorer gen = new GeneticExplorer();
							gen.init(blocPos.getBloc());
							GeneticExplorerPanel panel = new GeneticExplorerPanel(gen, GeneticableAction.getDefaultBlocGrilleList(grid));
							JFrame frame = new JFrame("Genetic Explorer");
							frame.getContentPane().add(panel);
							frame.pack();
							frame.setLocationRelativeTo(null);
							frame.setVisible(true);
						}
					});
					popup.add(menuItem);
				}

			} else {
				popup.add(grid.getMenuBar().getMenuBlocs());

			}
			return popup;
		}

}
