package org.elusive.ui.volume;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.elusive.ui.action.MyAction;
import org.elusive.ui.grille.Grid;
import org.elusive.ui.resources.Icons;

public abstract class VolumeController {

	private float volumeMax = 1;
	private float lastVolumeValue = 0;

	// static
	private static int SLIDER_MAX = 1000;
	
	// Actions
	private MyAction volumeOffAction;
	private MyAction volumeOnAction;
	
	// UI
	private JButton volumeButton = null;
	private List<JSlider> sliders;
	
	/**
	 * 
	 * @param orientation Orientation of the slider (JSlider.HORIZONTAL or VERTICAL)
	 */
	public VolumeController(float volumeMax_) {
		this.volumeMax = volumeMax_;
		volumeOffAction = new MyAction("Turn volume off", null, "Turn volume off", null) {
			@Override
			public void actionPerformed(ActionEvent e) {
				turnVolumeOff();
			}
		};
		volumeOnAction = new MyAction("Turn volume on", null, "Turn volume on", Icons.VOLUME_OFF_ICON) {
			@Override
			public void actionPerformed(ActionEvent e) {
				turnVolumeOn();
			}
		};

		sliders = new ArrayList<JSlider>();
		volumeButton = volumeOffAction.createSimpleIconButton();
		updateButton();
	}

	public void setVolume(float vol){
		// Check range
		if( vol > volumeMax ){
			vol = volumeMax;
		}
		if( vol < 0 ){
			vol = 0;
		}
		// Change value
		volumeChanged(vol);
		
		// Update UIs
		// sliders
		for( JSlider slider : sliders ){
			updateSlider(slider);
		}
		updateButton();
	}

	public void turnVolumeOff() {
		if (getVolume() > 0) {
			lastVolumeValue = getVolume();
			setVolume(0);
			volumeOnAction.attachToButton(volumeButton);
		}
	}

	public void turnVolumeOn() {
		if (getVolume() == 0) {
			setVolume(lastVolumeValue);
			volumeOffAction.attachToButton(volumeButton);
		}
	}
	
	public JButton getVolumeButton(){
		return volumeButton;
	}

	public JSlider createVolumeSlider(int orientation) {
		final JSlider volumeSlider = new JSlider(orientation, 0, SLIDER_MAX, (int) (getVolume()/volumeMax * SLIDER_MAX));
		volumeSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				float vol = volumeSlider.getValue() / (float) SLIDER_MAX * volumeMax;
				// update volume
				volumeChanged(vol);
				// update UI
				updateSlidersExcept(volumeSlider);
				updateButton();
			}
		});
		sliders.add(volumeSlider);
		return volumeSlider;
	}
	
	private void updateSlider(JSlider slider) {
		slider.setValue((int) (getVolume()/volumeMax * SLIDER_MAX));
	}
	
	private void updateButton(){
		// Action
		if (getVolume() != 0) {
			if( volumeButton.getAction().equals(volumeOnAction) ){
				volumeOffAction.attachToButton(volumeButton);
			}
		}
		// Icon
		float value = getVolume();
		if( value == 0){
			volumeButton.setIcon(Icons.VOLUME_OFF_ICON);
		}else if( value < volumeMax/3){
			volumeButton.setIcon(Icons.VOLUME_ICON_1);
		}else if( value < volumeMax*2/3){
			volumeButton.setIcon(Icons.VOLUME_ICON_2);
		}else{
			volumeButton.setIcon(Icons.VOLUME_ICON_3);
		}
	}
	
	private void updateSlidersExcept(JSlider s){
		for( JSlider slider : sliders ){
			if( slider != s ){
				updateSlider(slider);
			}
		}
	}
	
	public abstract void volumeChanged(float volume);
	public abstract float getVolume();
	
}
