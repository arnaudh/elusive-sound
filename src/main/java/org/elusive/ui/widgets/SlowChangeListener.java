package org.elusive.ui.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



public abstract class SlowChangeListener implements ChangeListener {
    long lastChange = System.currentTimeMillis();
    long interval;

    public SlowChangeListener(long interval){
    	this.interval = interval;
    }

    public void stateChanged(ChangeEvent e) {
        long now = System.currentTimeMillis();
        if( now - lastChange > interval ){
        	slowStateChanged(e);
        	lastChange = now;
        }
    }
    
    public abstract void slowStateChanged(ChangeEvent e);
}