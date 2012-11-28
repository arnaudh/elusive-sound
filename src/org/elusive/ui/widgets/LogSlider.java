package org.elusive.ui.widgets;



public class LogSlider extends JFloatLogSlider {

	public LogSlider(int position, double min, double max, double init) {
		
		
		super(position, min, max, init, 0.1);
	}

    public int setLogValue(int value){
  	  setValue((int)(Math.log10(value)*SCALE)); 
  	  return logValue();
    }
    
	public int logValue(){
		return (int)Math.pow(10f, this.getFloatValue());
	}

}
