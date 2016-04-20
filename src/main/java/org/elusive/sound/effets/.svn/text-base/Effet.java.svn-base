package org.elusive.sound.effets;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.elusive.sound.blocs.Bloc;
import org.elusive.sound.flow.Timestamp;

import sun.security.action.GetBooleanAction;




public abstract class Effet {
	private Bloc bloc;
	public abstract float[] applyTo(float[] data);
	public Icon getIcon(){
		String path = "images/icon/"+this.getClass().getSimpleName()+".png";
		ImageIcon icon = new ImageIcon(path);
		return icon;
	}
	
	public String getName(){
		return this.getClass().getSimpleName();
	}
	
	private transient JPanel panel;
	protected abstract JPanel createPanel();	
	public JPanel getPanel(){
		if( panel == null ){
			panel = createPanel();
		}
		return panel;
	}
	
	public static final List<Class<? extends Effet>> liste;
	static{
		liste = new ArrayList<Class<? extends Effet>>();
		liste.add(Inverse.class);
		liste.add(Enveloppance.class);
		liste.add(FiltrePasseBas.class);
		liste.add(FiltrePasseHaut.class);
		liste.add(SmoothOperator.class);
		liste.add(SlowMo.class);
	}


	private transient Timestamp timestamp = new Timestamp();
	protected void updated(){
//		timestamp.update();
		if( bloc != null){
			bloc.update();
		}
	}
	public Timestamp getTimestamp(){
		if( timestamp == null ) timestamp = new Timestamp();
		return timestamp;
	}
	public Object readResolve(){
		timestamp = new Timestamp();
		return this;
	}
	
	public void setBloc(Bloc bloc){
		this.bloc = bloc;
	}
	
}
