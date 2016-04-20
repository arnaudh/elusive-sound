package org.elusive.ui.tools.formatters;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MyFormatters {
	
	public static DecimalFormat BPM_FORMATTER = new DecimalFormat("0.0");
	public static DecimalFormat SECONDS_FORMATTER = new DecimalFormat("0.#######");
	public static DecimalFormat TEMPO_FORMATTER = new DecimalFormat("0.#######");
	
	public static double roundHalfUp(double val, int scale){
		return BigDecimal.valueOf(val).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
}

