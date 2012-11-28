package org.elusive.sound.play;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import javax.sound.sampled.AudioFormat;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.elusive.main.Main;
import org.elusive.main.performance.Chrono;
import org.elusive.sound.blocs.Bloc;
import org.elusive.sound.blocs.BlocPositionne;
import org.elusive.sound.blocs.BlocTotal;
import org.elusive.sound.effets.Effet;
import org.elusive.sound.normalise.Normalise;
import org.elusive.sound.tools.MyFormats;
import org.elusive.ui.fenetre.Controls;
import org.elusive.ui.grille.Grid;
import org.elusive.ui.grille.Selection;

public class GridPlayer implements Runnable {

	private static final int BUFFER_SIZE = 100;
	private Grid grille;
	private float[] data;
	// private final BlocPositionne blocPositionne;
	int b = 0;
	private boolean enLecture = false;
	private ArrayList<Integer> intervalles;

	// format
	AudioFormat format = MyFormats.out();

	public GridPlayer(Grid grille) {
		this.grille = grille;
		data = new float[grille.frameMax];
		// Bloc bloc = new BlocTotal(data);
		// blocPositionne = new BlocPositionne(bloc, 0, 0, new
		// ArrayList<Effet>());
	}

	public void ajouteIntervalleLoad(int a, int b) {
		// ajoute à intervalles au bon endroit, si besoin est
	}

	public void load() {
		// TODO : load les intervalles à loader (debuts, fins)
		load(0, grille.frameMax);
	}

	public void load(int debutLoad, int finLoad) {
		// System.out.println("load("+debutLoad+", "+finLoad+")");
		// découpe l'intervalle en plein d'intervalles dans lesquels les blocs
		// sont fixes
		ArrayList<Integer> pointsParticuliers = new ArrayList<Integer>();
		pointsParticuliers.add(debutLoad);
		pointsParticuliers.add(finLoad);
		for (BlocPositionne bp : grille.getBlocs()) {
			// System.out.println("ajoute "+bp.getDebut()+" et "+bp.getFin());
			ajoutePoint(bp.getDebut(), debutLoad, finLoad, pointsParticuliers);
			ajoutePoint(bp.getFin(), debutLoad, finLoad, pointsParticuliers);
		}
		Collections.sort(pointsParticuliers);

		// Pour chaque intervalle ainsi créé
		for (int i = 0; i < pointsParticuliers.size() - 1; i++) {
			int deb = pointsParticuliers.get(i);
			int fin = pointsParticuliers.get(i + 1);
			assert (deb >= 0);
			loadBlocFixes(deb, fin);
		}

		//TODO PUT BACK
//		Normalise.normalise(data, debutLoad, finLoad);

	}

	public void ajoutePoint(int val, int debutLoad, int finLoad,
			ArrayList<Integer> pointsParticuliers) {
		if (val >= debutLoad && val <= finLoad
				&& !pointsParticuliers.contains(val)) {
			pointsParticuliers.add(val);
		}
	}

	private float maxTOTAL = 0;
	private DataPlayer dp;

	private void loadBlocFixes(final int debutLoad, final int finLoad) {
		// System.out.println(" loadBlocFixes("+debutLoad+", "+finLoad+")");
		// blocs concernés
		ArrayList<BlocPositionne> blocs = new ArrayList<BlocPositionne>();
		for (BlocPositionne bp : grille.getBlocs()) {
			if (bp.getDebut() < finLoad && bp.getFin() > debutLoad) {
				blocs.add(bp);
			}
		}
		int nbBlocs = blocs.size();

		if (nbBlocs == 0) {
			for (b = debutLoad; b < finLoad; b++) {
				data[b] = 0; // TODO ArrayIndexOutOfBounds
				// pour refaire :
				// bloc en 0, certaine longueur
				// play, jusqu'à plus loin que la fin du bloc
				// pause
				// sélectionne de droite à gauche le bloc jusqu'en 0
				// play <-
			}
			return;
		}

		// - décalages <- décalage des blocs;
		int[] decalage = new int[nbBlocs];
		float[][] datas = new float[nbBlocs][];
		float[] volumes = new float[nbBlocs];

		// Attention, les data récupérées ici peuvent être plus petites que le
		// finLoad (cf ArrayOutOfBounds ci-dessous)
		for (int i = 0; i < nbBlocs; i++) {
			decalage[i] = -blocs.get(i).getDebut();
			datas[i] = blocs.get(i).getData();
			volumes[i] = blocs.get(i).getVolume();
		}

		// *
		int i = 0;
		try {
			for (b = debutLoad; b < finLoad; b++) {
				float somme = 0;
				float max = 0;
					for (i = 0; i < nbBlocs; i++) {
						float val = datas[i][decalage[i] + b] * volumes[i];
						if (Math.abs(val) > max) {
							max = val;
						}
						if (Math.abs(val) > maxTOTAL) {
							maxTOTAL = val;
						}
						somme += val;
				}

				data[b] = somme;

			}
		} catch (ArrayIndexOutOfBoundsException ex) {
			// possible dans le cas ou un bloc s'est reactualisé après l'appel
			// de cette méthode, on étouffe l'exception
			 System.err.println("Array Out of Bounds (PAS DE PANIQUE) : debutLoad="+debutLoad+", finLoad="+finLoad+", b="+b+", i="+i+", datas.length=["+datas.length+","+datas[0].length+"], decalage[i]="+decalage[i]);
			return;
		}
		// */
	}

	public void play() {
		enLecture = true;
		dp = new DataPlayer(format);

		int deb = this.grille.curseur;
		int fin = data.length;

		boolean loop = false;

		do {
			Selection selection = grille.getPanelGrille().getSelection();
			if (selection != null) { // LOOP
				loop = true;
				this.grille.curseur = selection.getDebut();
				deb = this.grille.curseur;
				fin = selection.getFin();
			}

			load(deb, fin);

			while (enLecture && grille.curseur < fin) {
				int debut = grille.curseur;
				int taille = Math.min(BUFFER_SIZE, (fin - debut));

				float[] dataToPlay = new float[taille];
				for (int j = 0; j < taille; j++) {
					dataToPlay[j] = (Controls.getControls().getVolume() * data[debut + j]);
				}
				dp.play(dataToPlay, 0, taille);

				grille.curseur += taille;
				this.grille.repaint();
			}

		} while (loop && enLecture);

	}

	public void pause() {
		enLecture = false;
		if (dp != null) {
			dp.stop();
		}
	}

	public void run() {
		play();
	}

	public boolean isPlaying() {
		return enLecture;
	}

	public float[] getData() {
		return data;
	}

}
