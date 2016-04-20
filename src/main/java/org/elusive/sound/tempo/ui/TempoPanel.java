package org.elusive.sound.tempo.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Hashtable;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.elusive.sound.play.ui.PlayPanel;
import org.elusive.sound.play.ui.PlayPauseButton;
import org.elusive.sound.tempo.Tempo;
import org.elusive.ui.tools.formatters.MyFormatters;


public class TempoPanel extends JPanel{

	private Tempo tempo;
	
	//UI
	private JLabel label;
	private JSlider slider;
	private PlayPauseButton playPanel;
	
	//static
	final double TEMPO_STEP = 0.1;
    final int tick = 20;
    final int min = 1;
    final int max = 200;
	
	public TempoPanel(Tempo tempo_) {
		super();
		this.tempo = tempo_;
		label = new JLabel(bpmToLabel(tempo.getBpm()));
		slider = createTempoSlider();
		playPanel = new PlayPauseButton(tempo.getMetronome());
		
		// Layout
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		this.add(playPanel, gbc);
		gbc.gridx ++;
		this.add(label, gbc);
		gbc.gridx ++;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.VERTICAL;
		this.add(slider, gbc);
	}
	
	private String bpmToLabel(double bpm){
		return MyFormatters.BPM_FORMATTER.format(bpm)+" bpm";
	}
	
	private JSlider createTempoSlider(){
		final JSlider slider = new JSlider();
		slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setMinimum((int)(min/TEMPO_STEP));
        slider.setMaximum((int)(max/TEMPO_STEP));
        slider.setValue((int)(tempo.getBpm()/TEMPO_STEP));
		slider.setOrientation(JSlider.VERTICAL);
        Hashtable<Integer, JLabel> ht = new Hashtable<Integer, JLabel>();
        for (float i = min; i <= max; i+=tick)
        {
          JLabel l = new JLabel(""+i);
          ht.put(new Integer((int)Math.rint(i/TEMPO_STEP)), l);
        }
        slider.setLabelTable(ht);
        slider.setMajorTickSpacing((int)(tick/TEMPO_STEP));
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				tempo.setBpm(slider.getValue()*TEMPO_STEP);
				label.setText(bpmToLabel(tempo.getBpm()));
			}
		});
		return slider;
	}
	
	
}
