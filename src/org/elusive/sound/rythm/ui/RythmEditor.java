package org.elusive.sound.rythm.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import org.elusive.sound.rythm.Hit;
import org.elusive.sound.rythm.Rythm;
import org.elusive.sound.tools.TimeFormat;
import org.elusive.ui.action.MyAction;
import org.elusive.ui.fenetre.ElusivePanel;
import org.elusive.ui.grille.Grid;
import org.elusive.ui.texteditors.JTextPaneNoWrap;
import org.elusive.ui.tools.TextComponentTools;
import org.elusive.ui.tools.ToggleInsertDocument;
import org.elusive.ui.tools.formatters.MyFormatters;

public abstract class RythmEditor extends ElusivePanel {

	private Rythm rythm;
	private TimeFormat format = TimeFormat.TEMPO;
	private int fractionOfRythmForTemporise = 4;

	//UI
	private JComboBox formatComboBox;
	private JTextPane textcomp;
	private JTextPane cumSumTextpane;
	private JButton rythmRecorderButton;
	
	//actions
	private MyAction temporiseRythmAction;
	
	public RythmEditor(Rythm rythm_) {
		super("Rythm Editor");
		this.rythm = rythm_;
		
		// Text Component
		textcomp = new JTextPaneNoWrap();
		textcomp.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int k = e.getKeyCode();
				if( k != KeyEvent.VK_LEFT && k != KeyEvent.VK_RIGHT && k != KeyEvent.VK_UP && k != KeyEvent.VK_DOWN ){ 
					updateRythmFromTextPane();
					updateTextComponents(false, true);
				}
			}
		});
		TextComponentTools.addUndoRedo(textcomp);
		JScrollPane scrollPane = new JScrollPane(textcomp);
		// Cumulative Sum Text Pane
		cumSumTextpane = new JTextPaneNoWrap();
		JScrollPane cumSumScrollPane = new JScrollPane(cumSumTextpane, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		cumSumScrollPane.getVerticalScrollBar().setModel(scrollPane.getVerticalScrollBar().getModel());
		
		updateTextComponents(true, true);
		
		// Time format Combo Box
		formatComboBox = new JComboBox(TimeFormat.getStrings());
		formatComboBox.setSelectedItem(format.toString());
		formatComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				format = TimeFormat.values()[formatComboBox.getSelectedIndex()];
				updateTextComponentAndRythm();
			}
		});
		
		// Rythm recorder button 
		rythmRecorderButton = new RythmRecorder(new RythmRecorderAdapter() {
			@Override
			public void addedHit(Hit hit) {
				
				RythmEditor.this.rythm.addHit(hit);
				updateTextComponentAndRythm();
				rythmChanged(rythm);
			}
		});
		
		initActions();
		
		// Layout
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		this.add(formatComboBox, gbc);
		gbc.gridy++;
		gbc.weightx = 0.7;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 1;
		this.add(scrollPane,gbc);
		gbc.gridx ++;
		gbc.weightx = 0.3;
		this.add(cumSumScrollPane, gbc);
		gbc.gridx --;
		gbc.gridy++;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridwidth = 2;
		this.add(rythmRecorderButton, gbc);
		gbc.gridy ++;
		this.add(temporiseRythmAction.createSimpleTextButton(), gbc);
		
		
	}
	

	private void initActions(){
		temporiseRythmAction = new MyAction("Temporise rythm", null, "make the rythm in sync with the tempo", null, null) {
			@Override
			public void actionPerformed(ActionEvent e) {
				temporiseRythm();
			}
		};
	}
	private void temporiseRythm(){
		for(Hit hit : rythm){
			double seconds = hit.getOffset() / (double)Grid.FRAMES_PER_SECOND;
			double tempo = MyFormatters.roundHalfUp(seconds * Grid.getTempo().getBpm() / 60, 3);
			double theTempo =  MyFormatters.roundHalfUp(tempo * fractionOfRythmForTemporise, 0) / fractionOfRythmForTemporise;
			int theOffset = (int) (theTempo * Grid.FRAMES_PER_SECOND / Grid.getTempo().getBpm() * 60);
			hit.setOffset(theOffset);
			updateTextComponentAndRythm();
		}
	}
	
	private void updateRythmFromTextPane(){
		Rythm rythm = new Rythm();
		//Rythm.fromString(str) ...
		//depends on format
		String text = textcomp.getText();
		String[] lines = text.split("\n");
		for(String line : lines){
//			System.out.println("line : "+line);
			double doubleValue = 0;
			try{
				doubleValue = Double.parseDouble(line);
			}catch (NumberFormatException e) {
				continue;
			}
//			System.out.println("double : "+doubleValue);
			int offset = 0;
			switch (format) {
			case SECONDS:
				offset = (int) (doubleValue * Grid.FRAMES_PER_SECOND);
				break;
			case TEMPO:
				offset = (int) (doubleValue * Grid.FRAMES_PER_SECOND / Grid.getTempo().getBpm() * 60);
				break;
			case FRAMES:
			default:
				offset = (int) doubleValue;
				break;
			}
//			System.out.println("offset : "+offset);
			rythm.addHit(new Hit(offset, 10000));
			
		}
		this.rythm = rythm;
		rythmChanged(rythm);
	}

	private void updateTextComponents(boolean left, boolean right) {
		StringBuilder sb = new StringBuilder();
		StringBuilder sbCum = new StringBuilder();
		double cumSum = 0;
		
		int line = 0;
		int caretPosition = 0;
		
		for(Hit hit : rythm){
			int offset = hit.getOffset();
			double seconds = offset / (double)Grid.FRAMES_PER_SECOND;
			String strOffset = "";
			String strCumSum = "";
			switch (format) {
			case SECONDS:
				double val = MyFormatters.roundHalfUp(seconds, 3);
				strOffset = MyFormatters.SECONDS_FORMATTER.format(val);
				cumSum += val;
				strCumSum = MyFormatters.SECONDS_FORMATTER.format(cumSum);
				break;
			case TEMPO:
				double tempo = MyFormatters.roundHalfUp(seconds * Grid.getTempo().getBpm() / 60, 3);
//				System.out.println("offset="+offset+", "+(seconds * Grid.getTempo().getBpm() / 60)+", "+tempo);
				strOffset = MyFormatters.TEMPO_FORMATTER.format(tempo);
				cumSum += tempo;
				strCumSum = MyFormatters.TEMPO_FORMATTER.format(cumSum);
				break;
			case FRAMES:
			default:
				strOffset = Integer.toString(offset);
				cumSum += offset;
				strCumSum = Integer.toString((int) cumSum);
				break;
			}
			sb.append(strOffset);
			sb.append('\n');
			sbCum.append(strCumSum);
			sbCum.append('\n');
		}
		if( left ) textcomp.setText(sb.toString());		
		if( right ) cumSumTextpane.setText(sbCum.toString());		
	}
	private void updateTextComponentAndRythm(){
		updateTextComponents(true, true);
		updateRythmFromTextPane();
	}
	
	protected abstract void rythmChanged(Rythm rythm);
	

}
