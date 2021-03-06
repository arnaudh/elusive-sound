package org.elusive.sound.genetics.ui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;

import org.elusive.main.historique.action.AjouteBloc;
import org.elusive.main.persistence.xml.XmlTools;
import org.elusive.sound.blocs.Bloc;
import org.elusive.sound.genetics.Geneticable;
import org.elusive.ui.grille.Grid;
import org.elusive.ui.tools.file.OpenSaveManager;

public class GeneticableAction extends AbstractAction{

	private GeneticChoiceListener listener;
	private Geneticable geneticable;

	public GeneticableAction(String name, GeneticChoiceListener listener) {
		super(name);
		this.listener = listener;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		listener.geneticableChosen(geneticable);
	}

	public Geneticable getGeneticable() {
		return geneticable;
	}

	public void setGeneticable(Geneticable geneticable) {
		this.geneticable = geneticable;
	}

	
	
	public static List<GeneticableAction> getDefaultBlocList(){
		List<GeneticableAction> actions = new ArrayList<GeneticableAction>();
		actions.add(new GeneticableAction("Save As...", new GeneticChoiceListener() {
			@Override
			public void geneticableChosen(final Geneticable gen) {
				OpenSaveManager manager = new OpenSaveManager(null, null, "Genetic Bloc", ".elu") {
					@Override
					public String save() {
						return XmlTools.toXML(gen);
					}
					@Override
					public boolean open(String fileContent) {
						return false;
					}
				};
				manager.saveAsAction();
			}
		}));
		return actions;
	}

	public static List<GeneticableAction> getDefaultBlocGrilleList(final Grid grille) {
		List<GeneticableAction> actions = getDefaultBlocList();
		actions.add(new GeneticableAction("Add to grid", new GeneticChoiceListener() {
			@Override
			public void geneticableChosen(Geneticable gen) {
				Bloc bloc = (Bloc) gen;
				
				AjouteBloc ajoute = new AjouteBloc(grille, bloc);
				grille.getHistorique().execute(ajoute);
				
			}
		}));
		return actions;
	}
	
}
