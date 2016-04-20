package org.elusive.ui.grille;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Graduations extends JPanel {
	
	private Grid grille;
	private Dimension minDim = new Dimension(0, 30);
	
	public Graduations(Grid grille){
		this.grille = grille;
		this.setPreferredSize(minDim);
		//this.setMinimumSize(minDim);
	}
	
	@Override
	public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2 = (Graphics2D)g;
		
        g2.setColor(Color.black);
        
        //int premiereFrame = grille.pix2frame(0);
        
        
        //affichage tu temps au premier offset
		g2.setColor(new Color(200, 200, 200));
        g2.fill(getVisibleRect());

		g2.setColor(Color.black);
        double sec = (double)grille.getOffset()/44100;
        //afficheTemps(g2, sec, 1);
        afficheOffset(g2, grille.getOffset());
        afficheOffset(g2, grille.getPanelGrille().pixel2offset(getWidth()) );

		g2.setColor(Color.red);
        afficheOffset(g2, grille.curseur);
        
	}
	
	public void afficheOffset(Graphics2D g2, int offset){
		double sec = (double)offset / 44100;
		int pix = grille.getPanelGrille().offset2pixel(offset);
		if( pix == 0 ){
			pix = 1;
		}
		if( pix + 40 > this.getWidth()){
			pix = this.getWidth() - 40;
		}
		afficheTemps(g2, sec, pix);
	}
	
	public void afficheTemps(Graphics2D g2, double sec, int x){
        String str = Double.toString(sec);
        if( str.length() > 6 ){
        	str = str.substring(0, 5);
        }
        str += " s";
        g2.drawString(str,	x, 11);
		
	}
}
