package org.elusive.ui.widgets;

import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JSlider;



public class JFloatLogSlider extends JSlider
    {
      protected final int SCALE = 20000;
      public JFloatLogSlider(int position, double min, double max, double init, double tick)
      {
      		//System.out.println(SoundTools.log(min)+" , "+SoundTools.log(max)+","+SoundTools.log(init));
        this.setPaintLabels(false);
        //this.setPaintTicks(true);

        min = Math.log10(min);
        max = Math.log10(max);
        init = Math.log10(init);

        setMinimum((int)(min*SCALE));
        setMaximum((int)(max*SCALE));
        this.setValue((int)(init*SCALE));
		this.setOrientation(position);
		
		/*
        Hashtable<Integer, JLabel> ht = new Hashtable<Integer, JLabel>();
        for (float i = min; i <= max; i+=tick)
        {
          JLabel l = new JLabel(""+i);
          ht.put(new Integer((int)Math.rint(i*SCALE)), l);
        }
        this.setLabelTable(ht);
        
        this.setMajorTickSpacing(20);
        this.setMinorTickSpacing(1);
        this.setPaintTicks(false);
        */
      }
      
      
      public float getFloatValue() {
    	  return (float)getValue()/(float)SCALE; 
      }
 }
