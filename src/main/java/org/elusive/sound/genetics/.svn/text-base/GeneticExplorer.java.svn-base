package org.elusive.sound.genetics;

import java.util.ArrayList;
import java.util.List;

import org.elusive.main.historique.state.StateHistory;
import org.elusive.main.historique.state.StateHistoryListener;
import org.elusive.sound.genetics.ui.GeneticChoiceListener;
import org.elusive.sound.genetics.ui.GeneticExplorerPanel;

public class GeneticExplorer implements GeneticChoiceListener {

	private double strength = 1;
	private int populationSize = 4;
	private Geneticable geneticableActuel;
	private List<Geneticable> population;
	
	//Historique des geneticables choisis
	private StateHistory<Geneticable> history;

	// UI
	private GeneticExplorerPanel panel;

	public GeneticExplorer() {
		//TODO revoir le mécanisme d'historique
		history = new StateHistory<Geneticable>(new StateHistoryListener<Geneticable>() {
			@Override
			public void stateHasChanged(Geneticable state) {
				init(state);
			}
		});
	}

	public void init(Geneticable gen) {
		geneticableActuel = gen;
		population = new ArrayList<Geneticable>();

		for (int i = 0; i < populationSize; i++) {
			Geneticable g = gen.mutate(strength);
			population.add(g);
		}
		if (panel != null) {
			panel.updatedGenExpPanel();
		}
	}

	public void regenerate() {
		if (geneticableActuel != null) {
			init(geneticableActuel);
		}
	}

	@Override
	public void geneticableChosen(Geneticable gen) {
		history.add(gen);
		strength *= 0.8;
		
		init(gen);
	}

	public double getStrength() {
		return strength;
	}

	public void setStrength(double strength) {
		this.strength = strength;
	}

	public List<Geneticable> getPopulation() {
		return population;
	}

	public void setPanel(GeneticExplorerPanel panel) {
		this.panel = panel;
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	public StateHistory<Geneticable> getHistory(){
		return history;
	}
}
