package org.elusive.sound.genetics;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.elusive.sound.blocs.Bloc;
import org.elusive.sound.blocs.BlocGenerateur;
import org.elusive.sound.play.BufferedData;
import org.elusive.sound.play.PlayableAudio;
import org.elusive.ui.resources.Icons;

public abstract class GeneticableSound implements Geneticable {

	protected Bloc bloc;

	public GeneticableSound(Bloc bloc) {
		super();
		this.bloc = bloc;
	}

	@Override
	public JPanel getPreviewPanel() {

		JPanel panel = new JPanel();
		
		final PlayableAudio playable = new PlayableAudio(new BufferedData() {
			
			@Override
			public void initData() {
			}
			@Override
			public float[] getNextData() {
				float[] data;
				if( bloc instanceof BlocGenerateur ){
					data = ((BlocGenerateur) bloc).generateNextData(1000);
				}else{
					data = bloc.generateData();
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
		
		
		panel.add(bloc.getElusivePanel());
		panel.add(playPanel);
		
		
		return panel;
	}


}
