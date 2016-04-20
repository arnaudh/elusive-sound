package org.elusive.sound.genetics;

import javax.swing.JPanel;

public interface Geneticable {

	public Geneticable mutate( double strength ); //mutate(0) should return exact copy
	public Geneticable combineWith( Geneticable gen );
	public JPanel getPreviewPanel();
	
	
}
