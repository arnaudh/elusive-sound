package org.elusive.ui.grille;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ActionMap;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.KeyStroke;

import org.elusive.main.historique.ActionHistoriqueMultiple;
import org.elusive.main.historique.action.AjouteBloc;
import org.elusive.main.historique.action.AjouteEffet;
import org.elusive.main.historique.action.DeplaceBloc;
import org.elusive.main.historique.action.ResizeBloc;
import org.elusive.main.historique.action.SupprimeBloc;
import org.elusive.main.performance.MemoryStatus;
import org.elusive.main.persistence.xml.XMLException;
import org.elusive.main.persistence.xml.XmlTools;
import org.elusive.main.tools.IOtools;
import org.elusive.sound.analyse.Analyser;
import org.elusive.sound.blocs.Bloc;
import org.elusive.sound.blocs.BlocFichier;
import org.elusive.sound.blocs.BlocPositionne;
import org.elusive.sound.blocs.frequenceur.Frequencable;
import org.elusive.sound.blocs.frequenceur.Frequenceur;
import org.elusive.sound.effets.Effet;
import org.elusive.sound.genetics.GeneticExplorer;
import org.elusive.sound.genetics.Geneticable;
import org.elusive.sound.genetics.ui.GeneticExplorerPanel;
import org.elusive.sound.genetics.ui.GeneticableAction;
import org.elusive.sound.rythm.Rythm;
import org.elusive.sound.tools.SoundTools;
import org.elusive.ui.action.MyAction;
import org.elusive.ui.action.shortcut.MyKeyStrokes;
import org.elusive.ui.config.Colors;
import org.elusive.ui.events.MouseEventTools;
import org.elusive.ui.fenetre.ElusivePanel;
import org.elusive.ui.fenetre.ShowableMenuFrame;
import org.elusive.ui.grille.line.GridLine;
import org.elusive.ui.tools.PopupListener;
import org.elusive.ui.tools.file.FileOpenDialog;

public class GridPanel extends ElusivePanel {

	// UI Constants
	protected static final BasicStroke STROKE_BLOC_CONTOUR = new BasicStroke(1);
	protected static final BasicStroke STROKE_SELECTED_BLOC_CONTOUR = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 20, 8 }, 0);
	protected static final int BLOC_ROUND_RECT_ARC_SIZE = 10;
	protected static final int PIXEL_TOLERANCE = 4;
	protected static final Cursor RESIZE_CURSOR = new Cursor(Cursor.E_RESIZE_CURSOR);
	protected static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
	
	private Grid grid;

	protected DragAction dragAction = DragAction.NONE;

	protected Point lastPress = null;
	protected Point lastClic = new Point(0, 0);
	protected Point lastDrag = null;
	protected BlocPositionne blocLastPress;
	protected int offsetGlobalLastPress;
	protected int yLastPress;
	// déplacements
	protected int decalageLigne = 0;
	protected int decalageOffset = 0;
	protected int decalageResize = 0;

	// affichage
	protected int curseur_fictif = -1;
	public static int LINE_HEIGHT= 50; // pixels

	public GridPanel(final Grid grid_) {
		super("Grille Panel");
		this.grid = grid_;
		this.frame = grid.fenetre;

		this.setBackground(Colors.GRID_BACKGROUND_COLOR);

		this.addMouseListener(new GridMouseListener(this));
		this.addMouseMotionListener(new GridMouseMoveListener(this));

		this.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.isAltDown()) {
					int curseur = grid.curseur;
					grid.curseur = GridPanel.this.pixel2offset(e.getX());
					grid.fenetre.controles.changeZoom(-e.getWheelRotation());
					grid.curseur = curseur;
					repaint();
				} else if( e.isShiftDown() ){ //corresponds to horizontal scrolling on Mac
					JScrollBar scrollH = grid.getScrollH();
					int value = scrollH.getValue();
					value += 4 * e.getWheelRotation();
					scrollH.setValue(value);
					
				} else {
					JScrollBar scrollV = grid.getScrollV();
					int value = scrollV.getValue();
					value += 2 * e.getWheelRotation();
					scrollV.setValue(value);
				}
			}
		});

		this.addMouseListener(new PopupListener() {
			@Override
			public JPopupMenu showMenu(MouseEvent e) {
				lastClic = e.getPoint();
				return null;
			}
		});
		this.addMouseListener(new GridPopupListener(grid));

		initActionMap();
		initInputMap();
	}
	
	private void initActionMap(){
		ActionMap actionMap = this.getActionMap();
		actionMap.put(MyKeyStrokes.VOLUME_DOWN_BLOC, new MyAction("Volume down bloc", null, "Volume down bloc", null) {
			@Override
			public void actionPerformed(ActionEvent e) {
				volumeDownBLoc();
			}
		});
		actionMap.put(MyKeyStrokes.VOLUME_UP_BLOC, new MyAction("Volume up bloc", null, "Volume up bloc", null) {
			@Override
			public void actionPerformed(ActionEvent e) {
				volumeUpBLoc();
			}
		});
		actionMap.put(MyKeyStrokes.VOLUME_ON_BLOC, new MyAction("Volume on bloc", null, "Volume on bloc", null) {
			@Override
			public void actionPerformed(ActionEvent e) {
				volumeOnBLoc();
			}
		});
		actionMap.put(MyKeyStrokes.VOLUME_OFF_BLOC, new MyAction("Volume off bloc", null, "Volume off bloc", null) {
			@Override
			public void actionPerformed(ActionEvent e) {
				volumeOffBLoc();
			}
		});
	}
	
	private void initInputMap(){
		this.getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(MyKeyStrokes.VOLUME_DOWN_BLOC, MyKeyStrokes.VOLUME_DOWN_BLOC);
		this.getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(MyKeyStrokes.VOLUME_UP_BLOC, MyKeyStrokes.VOLUME_UP_BLOC);
		this.getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(MyKeyStrokes.VOLUME_ON_BLOC, MyKeyStrokes.VOLUME_ON_BLOC);
		this.getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(MyKeyStrokes.VOLUME_OFF_BLOC, MyKeyStrokes.VOLUME_OFF_BLOC);
	}

	private void volumeDownBLoc(){
		for(BlocPositionne bp : grid.getBlocsSelectionnes()){
			bp.volumeDown();
		}
		repaint();
	}
	private void volumeUpBLoc(){
		for(BlocPositionne bp : grid.getBlocsSelectionnes()){
			bp.volumeUp();
		}
		repaint();
	}
	private void volumeOnBLoc(){
		for(BlocPositionne bp : grid.getBlocsSelectionnes()){
			bp.volumeOn();
		}
		repaint();
	}
	private void volumeOffBLoc(){
		for(BlocPositionne bp : grid.getBlocsSelectionnes()){
			bp.volumeOff();
		}
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		int hauteurGraduations = -grid.getSrollVValue();

		g2.setColor(Colors.GRID_LINES);
		
		int h = hauteurGraduations;
		for( GridLine line : grid.getLines() ){
			h += LINE_HEIGHT;
			if( line.isOn() ){
				g2.setColor(Colors.GRID_LINES);
			}else{
				g2.setColor(Color.GRAY);
			}
			g2.drawLine(0, h, this.getWidth(), h);
			g2.drawLine(0, h + LINE_HEIGHT, this.getWidth(), h + LINE_HEIGHT);
		}

		for (BlocPositionne bp : grid.getBlocs()) {
			drawBloc(bp, g2, hauteurGraduations);
		}

		// affiche le rythme solitaire
		drawRythm(grid.getRythm(), 0, -1, g2, hauteurGraduations);

		// affiche la sélection
		if (grid.getSelection() != null) {
			g2.setColor(Colors.SELECTION);

			int px1 = offset2pixel(grid.getSelection().getDebut());
			int px2 = offset2pixel(grid.getSelection().getFin());
			Rectangle rect = new Rectangle(px1, 0, px2 - px1, getHeight());
			g2.fill(rect);
		}

		// affiche le curseur
		g2.setColor(Colors.CURSEUR);
		int px = offset2pixel(grid.curseur);
		g2.drawLine(px, 0, px, getHeight());
		if (curseur_fictif >= 0) {
			// affiche le curseur fictif
			g2.setColor(Colors.CURSEUR_FICTIF);
			px = offset2pixel(curseur_fictif);
			g2.drawLine(px, 0, px, getHeight());
		}

	}

	public int offset2pixel(int offset) {
		int val = offset - grid.getOffset();
		int nbFramesPerPixel = grid.controls.getFramesPerPixel();
		if (nbFramesPerPixel > 0) {
			return val / nbFramesPerPixel;
		} else {
			return val * (-nbFramesPerPixel);
		}
	}

	public int pixel2offset(int pixel) {
		int val = pixel;
		int nbFramesPerPixel = grid.controls.getFramesPerPixel();
		if (nbFramesPerPixel > 0) {
			val *= nbFramesPerPixel;
		} else {
			val /= (-nbFramesPerPixel);
		}
		return grid.getOffset() + val;
	}

	public int ligneAt(Point p) {
		int hauteurGraduations = grid.getSrollVValue();
		int y = p.y + hauteurGraduations;
		int n = y / LINE_HEIGHT;
		// if (n < 0) {
		// n = 0;
		// }
		// if (n > grid.nbLignes - 1) {
		// n = grille.nbLignes - 1;
		// }
		return n;
	}

	public BlocPositionne blocAt(Point p) {
		int ligne = ligneAt(p);
		int x = p.x;
		for (BlocPositionne bp : grid.getBlocs()) {
			if (bp.getLigne() == ligne && offset2pixel(bp.getDebut()) <= x && offset2pixel(bp.getFin()) >= x) {
				return bp;
			}
		}
		return null;
	}

	public void drawBloc(BlocPositionne bp, Graphics2D g2, int hauteurGraduations) {
		Bloc b = bp.getBloc();
		float[] data = bp.getData();
		

		Color blocColor = b.getOutsideColor();

		int debutBloc = bp.getDebut();
		int finBloc = bp.getFin();
		int ligne = bp.getLigne();

		Stroke strokeContour = null;
		BlocDrawing type = BlocDrawing.FIXE;
		if (grid.getBlocsSelectionnes().contains(bp)) {
			strokeContour = STROKE_SELECTED_BLOC_CONTOUR;
			switch (dragAction) {
			case BLOC_MOVE:
				ligne += decalageLigne;
				debutBloc += decalageOffset;
				finBloc += decalageOffset;
				type = BlocDrawing.DEPLACEMENT;
				break;
			case BLOC_RESIZE_LEFT:
				type = BlocDrawing.RESIZE_GAUCHE;
				break;
			case BLOC_RESIZE_RIGHT:
				type = BlocDrawing.RESIZE_DROIT;
				break;
			default:
				type = BlocDrawing.SELECTION;
				break;
			}
		} else {
			strokeContour = STROKE_BLOC_CONTOUR;
		}
		int h = hauteurGraduations + LINE_HEIGHT * ligne;
		int milieu = h + LINE_HEIGHT / 2;

		int offsetMin = grid.getOffset();
		int offsetMax = offsetMin;

		int nbFramesPerPixel = grid.controls.getFramesPerPixel();
		if (nbFramesPerPixel > 0) {
			offsetMax += this.getWidth() * nbFramesPerPixel;
		} else {
			offsetMax += this.getWidth() / (-nbFramesPerPixel);
		}

		if (debutBloc < offsetMax && finBloc > offsetMin) {
			int premierPix = 0;
			int numFrame = offsetMin - debutBloc;
			int dernierPix = this.getWidth();
			if (debutBloc > offsetMin) {
				premierPix = (debutBloc - offsetMin);
				if (nbFramesPerPixel > 0) {
					premierPix /= nbFramesPerPixel;
				} else {
					premierPix *= -nbFramesPerPixel;
				}
				numFrame = 0;
			}
			if (finBloc < offsetMax) {
				int qte = (finBloc - offsetMin);
				if (nbFramesPerPixel > 0) {
					qte /= nbFramesPerPixel;
				} else {
					qte *= -nbFramesPerPixel;
				}
				dernierPix = qte;
			}
			// fill in with bloc color
			g2.setColor(blocColor);
			GradientPaint gradient = new GradientPaint(0, h, Colors.OUTSIDE_BLOC_COLOR, 0, h + LINE_HEIGHT / 2, blocColor, true);
			g2.setPaint(gradient);
//			Rectangle rect = new Rectangle(premierPix, h + 1, dernierPix - premierPix, hauteurLigne - 1);
//			g2.fill(rect);
			g2.fillRoundRect(premierPix, h, dernierPix-premierPix, LINE_HEIGHT, BLOC_ROUND_RECT_ARC_SIZE, BLOC_ROUND_RECT_ARC_SIZE);

			// The sound wave
			g2.setColor(Colors.COLOR_DATA);
			g2.setStroke(new BasicStroke());
			if (nbFramesPerPixel >= 1) {
				float[] minFrames = bp.getMinFrames(nbFramesPerPixel);
				float[] maxFrames = bp.getMaxFrames(nbFramesPerPixel);
				int realPx = numFrame / nbFramesPerPixel;
				for (int px = premierPix; px < dernierPix; px++) {
					double min = minFrames[realPx + px - premierPix]; // TODO :
																	// ArrayIndexOutOfBounds
					double max = maxFrames[realPx + px - premierPix];
					int valMin = (int) (min * LINE_HEIGHT / 2);
					int valMax = (int) (max * LINE_HEIGHT / 2);
					g2.drawLine(px, milieu - valMin, px, milieu - valMax);
				}
			} else {
				g2.drawLine(premierPix, milieu, dernierPix, milieu);
				int pasPixel = -nbFramesPerPixel;
				int valPrecedente = 0;
				int n = (dernierPix - premierPix) / pasPixel;
				for (int px = premierPix; px < dernierPix; px += pasPixel, numFrame += 1) {
					// byte v = (byte)getShort(data[f*nbBytesParFrame+1],
					// data[f*nbBytesParFrame]);
					double v = data[numFrame];
					double max = v;
					int val = (int) (max * LINE_HEIGHT / 2);
					g2.drawLine(px, milieu - val + 1, px, milieu - val - 1);
					if (numFrame % 10 == 0) {
						// g2.drawString(""+numFrame, px, milieu-val-2);
					}
					if (px > premierPix) {
						g2.drawLine(px - pasPixel, milieu - valPrecedente, px, milieu - val);
					}
					valPrecedente = val;
				}

			}
			if (type == BlocDrawing.RESIZE_DROIT || type == BlocDrawing.RESIZE_GAUCHE) {
				g2.setColor(blocColor);
				int pix = 0;
				if (type == BlocDrawing.RESIZE_DROIT) {
					pix = dernierPix;
				} else {
					pix = premierPix;
				}
				int ultimePix = offset2pixel(pixel2offset(pix) + decalageResize);
				// System.out.println("pix2off("+pix+") = "+pixel2offset(pix));
				// System.out.println(" *** ultimePix = offset2pixel( pix2off("+pix+") + "+decalageResize+") = "+ultimePix);
				if (ultimePix < pix) {
					int temp = ultimePix;
					ultimePix = pix;
					pix = temp;
				}
				for (int px = pix + 1; px < ultimePix; px++) {
					g2.drawLine(px, h + 1, px, h + LINE_HEIGHT - 1);
				}
				/*
				 * g2.setColor(Color.black); g2.drawLine(ultimePix, h + 1,
				 * ultimePix, h + hauteurLigne - 1); g2.drawLine(pix, h + 1,
				 * pix, h + hauteurLigne - 1);
				 */

			}
			
			// The contour
			g2.setStroke(strokeContour);
			g2.setColor(Colors.COLOR_ROUND_RECT);
			g2.drawRoundRect(premierPix, h, dernierPix-premierPix-1, LINE_HEIGHT, BLOC_ROUND_RECT_ARC_SIZE, BLOC_ROUND_RECT_ARC_SIZE);

			g2.setStroke(new BasicStroke());
		}

		// Affiche les rythmes de ce bloc
		List<Rythm> rythmes = b.getRythms();
		for (Rythm rythm : rythmes) {
			drawRythm(rythm, debutBloc, ligne, g2, hauteurGraduations);
		}

	}

	public void drawRythm(Rythm rythm, int offset, int ligne, Graphics2D g2, int hauteurGraduations) {
		g2.setColor(Colors.OUTSIDE_COLOR_RYTHM);

		int minH = 0;
		int maxH = hauteurGraduations;
		if (ligne < 0) {
			// Toute la hauteur du panel
			minH = 0;
			maxH = grid.getLines().size() * LINE_HEIGHT;
		} else {
			// Il faut calculer les bornes en pixel
			minH = hauteurGraduations + LINE_HEIGHT * ligne;
			maxH = minH + LINE_HEIGHT;
		}

		int off = offset;
		for (int i = 0, l = rythm.size(); i < l; i++) {
			off += rythm.get(i).getOffset();
			// System.out.println("off(hits["+i+"]) = "+off);
			int pix = offset2pixel(off);
			if (pix > this.getWidth()) {
				return;
			}
			if (pix > 0) {
				g2.drawLine(pix, minH, pix, maxH);
			}
		}

	}

	public Point getLastClic() {
		return lastClic;
	}

	public ArrayList<BlocPositionne> getBlocsFixes() {
		ArrayList<BlocPositionne> blocsFixes = new ArrayList<BlocPositionne>();
		for (BlocPositionne bp : grid.getBlocs()) {
			if (!grid.getBlocsSelectionnes().contains(bp)) {
				blocsFixes.add(bp);
			}
		}
		return blocsFixes;
	}

	public Selection getSelection() {
		return grid.getSelection();
	}
	
	public Grid getGrid(){
		return grid;
	}


	public void calculeCurseurFictif(MouseEvent e) {
		// affiche le curseur fictif sur un "point particulier" de la grille
		// (debut ou fin de bloc le plus proche)
		int souris = pixel2offset(e.getX());
		// Recherche des points particuliers
		ArrayList<Integer> pointsParticuliers = new ArrayList<Integer>();
		pointsParticuliers.add(0);
		for (BlocPositionne bp_ : grid.getBlocs()) {
			pointsParticuliers.add(bp_.getDebut());
			pointsParticuliers.add(bp_.getFin());
		}
		// On récupère le plus proche
		int distMin = Integer.MAX_VALUE;
		for (Integer i : pointsParticuliers) {
			if (Math.abs(i - souris) < distMin) {
				distMin = Math.abs(i - souris);
				curseur_fictif = i;
			}
		}
	}



}
