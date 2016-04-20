package org.elusive.sound.melody.tabs;

import java.util.ArrayList;

public class Couplet extends ArrayList<Tranche> {

	//offset of the first string (high E) in the text tab
	private int offset = 0;

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	
}
