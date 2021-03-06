package org.elusive.sound.blocs.frequenceur;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import org.elusive.main.listeners.BlocSelectedListener;
import org.elusive.sound.blocs.Bloc;
import org.elusive.sound.blocs.additive.AdditiveSynth;
import org.elusive.sound.blocs.fm.FMSynthetizer;
import org.elusive.sound.enveloppes.Enveloppe;
import org.elusive.sound.flow.Timestamp;
import org.elusive.sound.genetics.Geneticable;
import org.elusive.sound.melody.partition.Partition;
import org.elusive.sound.melody.tabs.TabPartition;
import org.elusive.sound.normalise.Normalise;
import org.elusive.sound.rythm.Rythm;
import org.elusive.ui.config.Colors;
import org.elusive.ui.fenetre.ElusivePanel;


public class Frequenceur extends Bloc implements BlocSelectedListener {

	private Frequencable frequencable;
	private Enveloppe frappe;
	private Partition partition;

	private transient JPanel panelFrequencable;
	private transient ElusivePanel panel;
	
	public Frequenceur() {
		this(new AdditiveSynth());
	}
	
	public Frequenceur(Frequencable frequencable) {
		this(frequencable, Enveloppe.createEnveloppeADSR(), new TabPartition());
	}
	
	public Frequenceur(Frequencable frequencable, Enveloppe frappe,
			Partition partition) {
		super();
		this.frequencable = frequencable;
		this.frappe = frappe;
		setPartition(partition);
	}

	@Override
	public float[] generateData() {
		if( !partition.getNotes().isEmpty() ){
			NotePlacee note = partition.getNotes().get(partition.getNotes().size()-1);
			end = note.getStart() +note.getLength();
		}
		float[] data = new float[end - start];
		int offset = 0;

		if( frequencable instanceof AdditiveSynth ){
			((AdditiveSynth) frequencable ).reinitPhases();
		}
		
//		System.out.println("FOOOOR");
		for (NotePlacee note : partition.getNotes()) {
			if (note.getStart() > data.length) {
				break;
			}
			double frequence = note.getFrequence();
//			int size = note.getDuree();
			int size = frappe.getLength();

			offset = note.getStart();
//			System.out.println("offset: "+offset);
			Bloc blocFrequencable = ((Bloc) frequencable);
			
			frequencable.setFrequenceNoUpdate(frequence);
			blocFrequencable.setSizeNoUpdate(0, size);

			float[] datum = blocFrequencable.generateDataNoNormalise();
			frappe.applyTo(datum, 0, 0);
			int max = Math.min(size, data.length - offset);
			
			for (int i = 0; i < max; i++) {
				double val = datum[i];
				data[offset] += val;
				offset++;
			}
//			System.out.println("offset-end: "+offset);
		}
		Normalise.normalise(data);
		return data;
	}
	
	@Override
	public Timestamp getTimestamp() {
		long tmp = super.getTimestamp().getValue();
		tmp = Math.max(tmp, frappe.getTimestamp().getValue());
		tmp = Math.max(tmp, ((Bloc)frequencable).getTimestamp().getValue());
		tmp = Math.max(tmp, partition.getTimestamp().getValue());
		return new Timestamp(tmp);
	}

	public Frequencable getFrequencable() {
		return frequencable;
	}

	public void setFrequencable(Frequencable frequencable) {
		this.frequencable = frequencable;
		update();
	}

	@Override
	protected ElusivePanel createElusivePanel() {
		panel = new ElusivePanel("Frequenceur");
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		JPanel panelGauche = null;
		if (frequencable instanceof Bloc) {
			panelGauche = new JPanel(new BorderLayout());
			
			FrequencableManager manager = new FrequencableManager(this);
			manager.addFrequencable(frequencable);
			Bloc bloc = ((Bloc) frequencable);
			panelFrequencable = new JPanel(new BorderLayout());
				
			//Layout
			panelGauche.add(manager, BorderLayout.NORTH);
			panelFrequencable.add(bloc.getElusivePanel());		
			panelGauche.add(panelFrequencable);			
			
			//Color
			panelFrequencable.setBackground(Colors.ELUSIVE_PANEL_BACKGROUND);
			manager.setBackground(Colors.ELUSIVE_PANEL_BACKGROUND);
			
			//Menu
			bloc.getElusivePanel().setParent(panel);
		}
		
		ElusivePanel partitionPanel = partition.createElusivePanel();
		
		//Layout
		tabbedPane.addTab("Sound", panelGauche);
		tabbedPane.addTab("Enveloppe", frappe.getPanel());
		tabbedPane.addTab("Melody", partitionPanel);
		
		//Color
		panelGauche.setBackground(Colors.ELUSIVE_PANEL_BACKGROUND);
		tabbedPane.setBackground(Colors.ELUSIVE_PANEL_BACKGROUND);
		
		//Menu
		partitionPanel.setParent(panel);
		
		panel.add(tabbedPane);

		return panel;
	}

	public Partition getPartition() {
		return partition;
	}

	public void setPartition(Partition partition) {
		this.partition = partition;
		this.partition.setUpdateListener(this);
		update();
	}

	@Override
	public List<Rythm> getRythms() {
		List<Rythm> rythms = super.getRythms();
		if( partition instanceof TabPartition){
			rythms.add(((TabPartition) partition).getRythm());			
		}
		return rythms;
	}

	@Override
	public void blocSelected(Bloc bloc) {
		if( bloc instanceof Frequencable ){
			frequencable = (Frequencable)bloc;
			panelFrequencable.removeAll();
			panelFrequencable.add(bloc.getElusivePanel());
			panel.revalidate();
			panel.repaint();
			update();
		}else{
			System.err.println("Frequenceur : a reçu un bloc non frequencable");
		}		
	}
	
	///////////////////// GENETICS ////////////////////////

	private boolean mutateFrequencable = true;
	private boolean mutatePartition = true;
	
	@Override
	public Geneticable mutate(double strength) {
		//TODO : if frequencable mutate only
		Frequenceur fre = new Frequenceur();
		fre.setSize(start, end);
		fre.setPartition(partition);
		if( mutatePartition ){
			fre.setPartition((Partition) partition.mutate(strength));
		}else{
			fre.setPartition((Partition) partition.mutate(0));
		}
		if( mutateFrequencable ){
			fre.setFrequencable((Frequencable) ((Bloc)frequencable).mutate(strength));
		}else{
			fre.setFrequencable((Frequencable) ((Bloc)frequencable).mutate(0));
		}
		return fre;
	}
	@Override
	public JPanel getPreviewPanelWithoutPlayButton() {
		//TODO : if frequencable mutate only
		return  getElusivePanel();
	}


}
