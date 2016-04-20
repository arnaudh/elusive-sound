package org.elusive.ui.action;

public enum ActionCategory {

	NONE,
	FILE,
	EDIT,
	VIEW;
	
	public String toMenuString(){
		if( this.equals(NONE) ){
			return "Other";
		}
		String s = this.toString();
		return s.substring(0, 1) + s.substring(1).toLowerCase();
	}

}
