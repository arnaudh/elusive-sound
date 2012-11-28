package org.elusive.ui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.math.BigDecimal;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

/**
 * This panel must :
 *  - provide a scrollbar to move along horizontal space
 *  
 *  - scale to layout ?
 * @author arnaudhenry
 *
 */
public class HorizontalViewPanel extends JPanel{
	
	// Parameters
	protected String horizontalUnit = "s";
	protected String verticalUnit = "A";
	protected boolean paintHorizontalAxis = true;
	protected boolean paintVerticalAxis = true;
	protected boolean paintGrid = false;
	
	protected Color backgroundColor = Color.BLACK;
	protected Color axesColor = Color.WHITE;
	
	protected double minHorizontalOnset = -100;
	protected double maxHorizontalOffset = 1000;
	protected double minHorizontalUnitsPerPixel = 0.0001;
	
	// set to a value if vertical value is bounded
	protected double maxFixedVerticalOffset = Double.NaN;
	
	// UI
	protected JScrollBar horizontalScrollbar;
	
	// varying parameters
	protected double horizontalUnitsPerPixel = minHorizontalUnitsPerPixel;
	protected double horizontalOnset = minHorizontalOnset;
	protected double verticalUnitsPerPixel = 0.1;
	protected double verticalUnitOnset = -10;

	// static
	private static final int SPEED_HORIZONTAL_SCROLL = 1;
	private static final double SPEED_ZOOM = 0.01;
	private static final double MIN_HORIZONTAL_SCALE_SPACING = 30; //pixels
	
	public HorizontalViewPanel() {
		super();
		
		this.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int n = e.getWheelRotation();
				if( e.isShiftDown() ){
					//horizontal shift
					double newOnset = pix2horizontalUnit(SPEED_HORIZONTAL_SCROLL*n);
					setHorizontalOnset(newOnset);
				}else{
					if( e.isAltDown() ){
						//zoom
						double newPerPix = horizontalUnitsPerPixel * (1 + n*SPEED_ZOOM);
						setHorizontalUnitsPerPixel(newPerPix, e);
					}
				}
			}
		});
		
	}


	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		//TODO calculate resize...
		if( !Double.isNaN(maxFixedVerticalOffset) ){
			// fixed vertical
			this.verticalUnitsPerPixel = (maxFixedVerticalOffset - verticalUnitOnset) / getHeight();
		}
		
		// background fill
		g2.setBackground(backgroundColor);
		g2.setColor(backgroundColor);
		g2.fill(this.getVisibleRect());

		// paint axes
		g2.setColor(axesColor);
		if( paintHorizontalAxis ){
			int h = verticalUnit2pix(0);
			g2.drawLine(0, h, getWidth(), h);
		}
		if( paintVerticalAxis ){
			int p = horizontalUnit2pix(0);
			g2.drawLine(p, getHeight(), p, 0);
		}
		
		double minUnits = horizontalUnitsPerPixel * MIN_HORIZONTAL_SCALE_SPACING;
		double e = Math.floor( Math.log10(minUnits) ) + 1;
		double l = Math.exp( e * Math.log(10));
//		System.out.println("minUnits = "+minUnits+", e="+e+", l = "+l);
		double u = Math.ceil(horizontalOnset/l)*l;
		while( true ){
			int p = horizontalUnit2pix(u);
			if( p > getWidth() ){
				break;
			}
//			System.out.println("u="+u+", p="+p);
			g2.drawLine(p, getHeight(), p, getHeight()-12);
			g2.drawString(horizontalUnit2String(u, e), p+1, getHeight()-1);
			u += l;
		}
		
	}

	protected String horizontalUnit2String(double u, double e){
		int scale = (int) (e<0?-e:0);
		BigDecimal b = new BigDecimal(u).setScale(scale, BigDecimal.ROUND_HALF_UP);
		return b.toString();
	}
	
	protected int horizontalUnit2pix(double u){
		return (int) ((u - horizontalOnset) / horizontalUnitsPerPixel);
	}
	
	protected double pix2horizontalUnit(int pix){
		return pix*horizontalUnitsPerPixel + horizontalOnset;
	}
	
	protected int verticalUnit2pix(double u){
		return (int) ( getHeight() - (u - verticalUnitOnset) / verticalUnitsPerPixel);
	}
	
	protected double pix2verticalUnit(int pix){
		return (getHeight() - pix)*verticalUnitsPerPixel + verticalUnitOnset;
	}
	
	public void setHorizontalOnset(double val){
		double maxOnset = maxHorizontalOffset - getWidth()*horizontalUnitsPerPixel;
		if( val > maxOnset ){
			val = maxOnset;
		}
		if( val < minHorizontalOnset ){
			val = minHorizontalOnset;
		}
		horizontalOnset = val;
		repaint();
		//TODO update scrollbar
	}

	public void setHorizontalUnitsPerPixel(double val, MouseEvent e){
		if( val < minHorizontalUnitsPerPixel ){
			val = minHorizontalUnitsPerPixel;
		}
		//maximum imposed by the maxHorizontalOffset
		if( getWidth() * val > maxHorizontalOffset - minHorizontalOnset){
			return;
		}
		
		// newOnset + e.getX() * newUnitsPerPix = onset + e.getX() * unitsPerPix
		double newOnset = horizontalOnset + e.getX() * (horizontalUnitsPerPixel - val);
		horizontalUnitsPerPixel = val;
		setHorizontalOnset(newOnset);
	}
}
