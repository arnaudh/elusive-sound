package org.elusive.sound.enveloppes.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.elusive.main.historique.History;
import org.elusive.sound.enveloppes.Enveloppe;
import org.elusive.sound.enveloppes.PointSon;
import org.elusive.ui.events.MouseEventTools;
import org.elusive.ui.menus.MenuTools;
import org.elusive.ui.panels.HorizontalViewPanel;
import org.elusive.ui.tools.JFrameTools;
import org.elusive.ui.tools.PopupListener;

public class EnveloppePanel2 extends HorizontalViewPanel {

	private Enveloppe enveloppe;
	private History history;
	
	//temporary
	private ArrayList<PointSon> pointsSelectionnes;
	private ArrayList<PointSon> pointsPress;
	protected Point lastPress = null;
	protected int decalageX = 0;
	protected int decalageY = 0;

	protected int decalageXmin = 0;
	protected int decalageXmax = 0;
	
	//static
	private static final Color BACKGROUND_COLOR = new Color(80, 30, 80);
	private static final Color AXES_COLOR = Color.WHITE;
	private static final Color ENVELOPPE_COLOR = Color.YELLOW;
	protected Cursor curseur_main = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
	
	public static void main(String[] args) {
		Enveloppe env = Enveloppe.createEnveloppeADSR();
		JFrameTools.show(env.getPanel());
	}
	
	public EnveloppePanel2(Enveloppe enveloppe, History history) {
		super();
		this.enveloppe = enveloppe;
		this.history = history;

		this.backgroundColor = BACKGROUND_COLOR;
		this.axesColor = AXES_COLOR;
		this.horizontalUnit = "f"; //frames OR s tempo? TODO
		this.verticalUnit = "A";
		this.paintHorizontalAxis = true;
		this.paintVerticalAxis = false;
		this.minHorizontalOnset = 0;
		this.maxHorizontalOffset = 200000;
		this.horizontalOnset = minHorizontalOnset;
		this.minHorizontalUnitsPerPixel = 1; //f
		this.horizontalUnitsPerPixel = 100;
		this.verticalUnitOnset = 0; //TODO change?
		this.maxFixedVerticalOffset = 1;

		pointsSelectionnes = new ArrayList<PointSon>();
		pointsPress = new ArrayList<PointSon>();
		
		initListeners();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		for (int p = 0; p < enveloppe.getPoints().size() - 1; p++) {
			PointSon p1 = enveloppe.getPoints().get(p);
			PointSon p2 = enveloppe.getPoints().get(p + 1);
			int decalageX1 = 0;
			int decalageY1 = 0;
			int decalageX2 = 0;
			int decalageY2 = 0;
			if (pointsSelectionnes.contains(p1)) {
				g2.setColor(Color.red);
				decalageX1 += decalageX;
				decalageY1 += decalageY;
			} else {
				g2.setColor(ENVELOPPE_COLOR);
			}
			if (pointsSelectionnes.contains(p2)) {
				decalageX2 += decalageX;
				decalageY2 += decalageY;
			}
			int p1x = horizontalUnit2pix(p1.getX()) + decalageX1;
			int p1y = verticalUnit2pix(p1.getY()) + decalageY1;
			int p2x = horizontalUnit2pix(p2.getX()) + decalageX2;
			int p2y = verticalUnit2pix(p2.getY()) + decalageY2;
			g2.drawOval(p1x - 2, p1y - 2 , 5, 5);
			g2.setColor(ENVELOPPE_COLOR);
			g2.drawLine(p1x, p1y, p2x, p2y);
		}

		
	}
	

	private void initListeners() {
		this.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				PointSon p = pointAround(e.getPoint());
				if (p != null) {
					EnveloppePanel2.this.setCursor(curseur_main);
				} else {
					EnveloppePanel2.this.setCursor(Cursor.getDefaultCursor());
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				Point pt = e.getPoint();
				decalageX = pt.x - lastPress.x;
				decalageY = pt.y - lastPress.y;

//				decalageX = Math.min(decalageXmax, Math.max(decalageXmin, decalageX));
//				decalageY = (int) Math.min(pix2verticalUnit(getHeight()), Math.max(0, decalageY));

				for (int i = 0; i < pointsPress.size(); i++) {
					int x = (int) pix2horizontalUnit(horizontalUnit2pix(pointsPress.get(i).getX()) + decalageX);
					double y = pix2verticalUnit(verticalUnit2pix(pointsPress.get(i).getY()) + decalageY);
					pointsSelectionnes.get(i).setLocation(x, y);
				}

				decalageX = 0;
				decalageY = 0;

				EnveloppePanel2.this.repaint();
			}
		});

		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
//				for (PointSon p : pointsSelectionnes) {
//					p.setLocation((int) (p.getX() + pix2horizontalUnit(decalageX)), pix2verticalUnit(verticalUnit2pix(p.getY()) + decalageY));
//				}

				decalageX = 0;
				decalageY = 0;
				EnveloppePanel2.this.repaint();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				Point pt = e.getPoint();
				PointSon p = pointAround(pt);
				lastPress = pt;
				if (e.getButton() == MouseEvent.BUTTON1) {
					switch (e.getClickCount()) {
					case 2:
						PointSon nouveau = new PointSon((int) pix2horizontalUnit(pt.x), pix2verticalUnit(pt.y));
						enveloppe.add(nouveau);
						pointsSelectionnes.clear();
						pointsSelectionnes.add(nouveau);
						break;
					default:
						if (p != null) {
							if (!MouseEventTools.isControlOrMetaDown(e) && !pointsSelectionnes.contains(p)) {
								pointsSelectionnes.clear();
							}
							if (MouseEventTools.isControlOrMetaDown(e) && pointsSelectionnes.contains(p)) {
								pointsSelectionnes.remove(p);
							}
							if (!pointsSelectionnes.contains(p)) {
								pointsSelectionnes.add(p);
							}
						} else {
							pointsSelectionnes.clear();
						}
					}

					// calcul des d�calages min et max

					int minX = Integer.MAX_VALUE;
					int maxX = Integer.MAX_VALUE;
					double minY = Double.MAX_VALUE;
					double maxY = 0;
					pointsPress.clear();
					for (PointSon pp : pointsSelectionnes) {
						// System.out.println("pp = " + pp);
						// System.out.println("index : " + points.indexOf(pp));
						int blocGauche = getNextPointNonSelectionne(pp, false);
						int blocDroit = getNextPointNonSelectionne(pp, true);

						minX = Math.min(minX, pp.getX() - blocGauche);
						maxX = Math.min(maxX, blocDroit - pp.getX());
						minY = Math.min(minY, pp.getY());
						maxY = Math.max(maxY, pp.getY());
						pointsPress.add(new PointSon(pp));
					}

					 System.out.println("minX : " + minX + ", maxX : " +
					 maxX);
					// System.out.println("maxY : " + maxY + ", minY : " +
					// minY);

					decalageXmin = -horizontalUnit2pix(minX);
					decalageXmax = horizontalUnit2pix(maxX);

					 System.out.println("decalageXmin : " + decalageXmin
					 + ", decalageXmax : " + decalageXmax);

					EnveloppePanel2.this.repaint();
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				Point pt = e.getPoint();
				PointSon p = pointAround(pt);
				if (p != null) {
					if (!MouseEventTools.isControlOrMetaDown(e)) {
						pointsSelectionnes.clear();
						pointsSelectionnes.add(p);
					}
				} else if (e.getButton() != 3) {
					pointsSelectionnes.clear();
				}
			}
		});

		this.addMouseListener(new PopupListener() {
			@Override
			public JPopupMenu showMenu(MouseEvent e) {
				JMenuBar menuBar = new JMenuBar();
				JMenuItem item;

				JMenu menu = new JMenu("Edit");

				// ***************** SELECT ALL ******************* //
				item = new JMenuItem("Select all");
				item.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						pointsSelectionnes.clear();
						for (PointSon p : enveloppe.getPoints() ) {
							pointsSelectionnes.add(p);
						}
						EnveloppePanel2.this.repaint();
					}
				});
				menu.add(item);

				// ***************** DELETE SELECTED POINTS *******************
				// //
				item = new JMenuItem("Delete selected points");
				item.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						for (PointSon p : pointsSelectionnes) {
							enveloppe.getPoints().remove(p);
						}
						pointsSelectionnes.clear();
						EnveloppePanel2.this.repaint();
					}
				});
				menu.add(item);

				menuBar.add(menu);
				return MenuTools.menuBarToPopupMenu(menuBar);
			}
		});
	}
	

	public PointSon pointAround(Point pt) {
		for (PointSon p : enveloppe.getPoints()) {
			if (Math.abs(horizontalUnit2pix(p.getX()) - pt.x) < 5 && Math.abs(verticalUnit2pix(p.getY()) - pt.y) < 5) {
				return p;
			}
		}
		return null;
	}

	private int getNextPointNonSelectionne(PointSon ps, boolean droite) {
		int pas = droite ? 1 : -1;
		for (int ind = enveloppe.getPoints().indexOf(ps) + pas; ind > 0 && ind < enveloppe.getPoints().size(); ind += pas) {
			PointSon p = enveloppe.getPoints().get(ind);
			if (!pointsSelectionnes.contains(p)) {
				return p.getX();
			}
		}

		return droite ? (int)maxHorizontalOffset : 0;
	}
	
	
}
