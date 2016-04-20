package org.elusive.sound.flow;

public class Timestamp {
	
	private long tmstmp = System.currentTimeMillis();
	
	public Timestamp() {
	}
	
	public Timestamp(long t){
		tmstmp = t;
	}
	
	public Timestamp(Timestamp timestamp) {
		tmstmp = timestamp.getValue();
	}

	public void update(){
		tmstmp = System.currentTimeMillis();
		if( trace){
			System.out.println(name+" Timestamp.update() : "+tmstmp);
		}
	}
	
	public boolean isDifferentAndUpdate(Timestamp t){
		boolean different = tmstmp != t.tmstmp;
		tmstmp = t.tmstmp;
		if( trace){
			System.out.println(name+" Timestamp.isDifferentAndUpdate() : different = "+different+", tmp ="+tmstmp);
		}
		return different;
	}
	
	public long getValue(){
		return tmstmp;
	}

	private boolean trace = false; //TODO : remove
	private String name = null;
	public void trace(String name) {
		this.trace = true;
		this.name = name;
	}
	
	public String toString(){
		return "[t"+tmstmp+"]";
	}
	
	

}
