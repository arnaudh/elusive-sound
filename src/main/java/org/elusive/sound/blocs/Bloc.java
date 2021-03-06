package org.elusive.sound.blocs;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.elusive.main.Main;
import org.elusive.main.listeners.ProgressListener;
import org.elusive.sound.analyse.Analyser;
import org.elusive.sound.analyse.FFTanalizer;
import org.elusive.sound.analyse.PartitionExtractor;
import org.elusive.sound.blocs.additive.AdditiveSynth;
import org.elusive.sound.blocs.additive.FrequenceAmplitude;
import org.elusive.sound.blocs.additive.sequencer.AdditiveSynthSequencer;
import org.elusive.sound.blocs.fm.FMSynthetizer;
import org.elusive.sound.blocs.frequenceur.Frequencable;
import org.elusive.sound.blocs.frequenceur.Frequenceur;
import org.elusive.sound.blocs.karplusstrong.KarplusStrong;
import org.elusive.sound.blocs.noise.WhiteNoise;
import org.elusive.sound.blocs.voice.MacSay;
import org.elusive.sound.blocs.wave.WaveShaper;
import org.elusive.sound.flow.BlocUpdatedListener;
import org.elusive.sound.flow.Timestamp;
import org.elusive.sound.flow.Updatable;
import org.elusive.sound.genetics.GeneticExplorer;
import org.elusive.sound.genetics.Geneticable;
import org.elusive.sound.normalise.Normalise;
import org.elusive.sound.play.BufferedData;
import org.elusive.sound.play.PlayableAudio;
import org.elusive.sound.rythm.Rythm;
import org.elusive.ui.config.Colors;
import org.elusive.ui.fenetre.Controls;
import org.elusive.ui.fenetre.ElusivePanel;
import org.elusive.ui.grille.Grid;
import org.elusive.ui.resources.Icons;

public abstract class Bloc implements Geneticable, Updatable, ProgressListener {

	protected int start = 0;
	protected int end = 44100;

	private transient ElusivePanel elusivePanel;
	protected transient Exception errorGeneratingData = null;

	public abstract float[] generateData();

	protected abstract ElusivePanel createElusivePanel();

	public final ElusivePanel getElusivePanel() {
		if (elusivePanel == null) {
			elusivePanel = createElusivePanel();
		}
		return elusivePanel;
	}

	public void setSize(int start, int end) {
		this.start = start;
		this.end = end;
		update();
	}

	public void setSizeNoUpdate(int start, int end) {
		this.start = start;
		this.end = end;
	}

	public int resize(int s, int e) {
		setSize(start + s, end + e);
		return s;
	}

	public final Color getOutsideColor() {
		if (errorGeneratingData != null) {
			return Colors.COLOR_ERROR;
		}
		Color color = null;
		Class<? extends Bloc> c = this.getClass();
		if (c.equals(BlocFichier.class)) {
			color = Colors.COLOR_BLOC_FICHIER;
		} else if (c.equals(FMSynthetizer.class)) {
			color = Colors.COLOR_SYNTHETISEUR_FM;
		} else if (c.equals(BlocTotal.class)) {
			color = Colors.COLOR_BLOC_TOTAL;
		} else if (c.equals(Frequenceur.class)) {
			color = Colors.COLOR_FREQUENCEUR;
		} else if (c.equals(AdditiveSynth.class)){
			color = Colors.COLOR_ADDITIVE_SYNTH;
		} else if (c.equals(AdditiveSynthSequencer.class)){
			color = Colors.COLOR_ADDITIVE_SYNTH_SEQUENCER;
		} else {
			color = Color.white;
		}
		return color;
	}

	public List<Rythm> getRythms() {
		return new ArrayList<Rythm>();
	}

	
	private transient Timestamp timestamp = new Timestamp();
	private transient List<BlocUpdatedListener> listeners = new ArrayList<BlocUpdatedListener>();

	public void update() {
//		 System.out.println("Bloc.updated() : "+this);
		timestamp.update();
		for( BlocUpdatedListener lis : listeners ){
			lis.blocUpdated(this);
		}
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public Object readResolve() {
		timestamp = new Timestamp();
		// timestamp.trace();
		listeners = new ArrayList<BlocUpdatedListener>();
		return this;
	}
	public void addBlocUpdatedListener(BlocUpdatedListener listener){
		listeners.add(listener);
	}

	public float[] generateDataNoNormalise() {
		return generateData();
	}

	@Override
	public void progressChanged(double progress) {
		// TODO Auto-generated method stub
	}
	
	
	

	public static final List<Class<? extends Bloc>> liste;
	static {
		liste = new ArrayList<Class<? extends Bloc>>();
		liste.add(BlocFichier.class);
		liste.add(FMSynthetizer.class);
		liste.add(AdditiveSynth.class);
		liste.add(AdditiveSynthSequencer.class);
		liste.add(KarplusStrong.class);
		liste.add(WaveShaper.class);
		liste.add(Frequenceur.class);
		liste.add(WhiteNoise.class);
		if( Main.isMacOSX() ){
			liste.add(MacSay.class);
		}
	}

	public static final List<Class<? extends Analyser>> listeAnalysers;
	static {
		listeAnalysers = new ArrayList<Class<? extends Analyser>>();
		listeAnalysers.add(FFTanalizer.class);
		listeAnalysers.add(PartitionExtractor.class);
	}

	public static final List<Class<? extends Frequencable>> listeFrequencables;
	static {
		listeFrequencables = new ArrayList<Class<? extends Frequencable>>();
		listeFrequencables.add(AdditiveSynth.class);
		listeFrequencables.add(AdditiveSynthSequencer.class);
		listeFrequencables.add(FMSynthetizer.class);
		listeFrequencables.add(KarplusStrong.class);
		listeFrequencables.add(WaveShaper.class);
	}
	

	/////////////////////////// GENETICABLE /////////////////////////////
	// OVERRIDE FOR GENETICS :)
	
//	@Override
//	public Geneticable mutate(double strength) {
//		return this;
//	}

	@Override
	public Geneticable combineWith(Geneticable gen) {
		return this;
	}

	private JPanel previewPanel = null;
	@Override
	public JPanel getPreviewPanel() {
		if( previewPanel == null ){
			previewPanel = new JPanel();			
			final PlayableAudio playable = new PlayableAudio(new BufferedData() {
				
				float[] data;
				int size = 44100;
				int offset = 0;
				
				@Override
				public void initData() {
					data = Bloc.this.generateData();
				}
				@Override
				public float[] getNextData() {
					float[] data;
					if( Bloc.this instanceof BlocGenerateur ){
						data = ((BlocGenerateur) Bloc.this).generateNextData(size);
					}else{
						int n = Math.min(size, this.data.length - offset);
						data = new float[n];
						for(int i = 0; i < n; i++){
							data[i] = this.data[offset + i];
						}
						offset += n;
						if( offset >= this.data.length ){
							offset = 0;
						}
					}
					for(int i = 0, l=data.length; i<l; i++){
						data[i] *= Controls.getControls().getVolume();
					}
					return data;
				}
			});
			final JButton playPanel = new JButton(Icons.PLAY_ICON);
			playPanel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					super.mouseEntered(e);
					new Thread(playable).start();
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					super.mouseExited(e);
					playable.stop();
				}
			});
			
			previewPanel.add(getPreviewPanelWithoutPlayButton());
			previewPanel.add(playPanel);
		
		}
		return previewPanel;
	}
	
	public JPanel getPreviewPanelWithoutPlayButton(){
		return Bloc.this.getElusivePanel();
	}

}
