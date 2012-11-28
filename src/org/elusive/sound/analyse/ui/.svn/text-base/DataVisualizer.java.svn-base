package org.elusive.sound.analyse.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BoundedRangeModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DataVisualizer extends JPanel {


	private float[] data;

	// Paramètres d'affichage
	private float amplitudeMax;
	private int pixelHover = -1; //pixels
	private int curseur = -1; //offset
	private int nbFramesParPixel = 2; //scale

	private JSlider echelle;
	private JPanel dessin;
	private JScrollBar scroll;

	private static final Color BACKGROUND_COLOR = new Color(150, 200, 250);
	private final static int[] correspondanceEchelleFrames = { -20, -14, -10, -6, -4, -2, -1, 1, 2, 4, 10, 20, 40, 100, 200, 400, 1000, 2000, 4000, 10000, 20000 };

	public DataVisualizer(float[] data) {
		super(new GridBagLayout());
		setData(data);

		initEchelle();
		initDessin();
		initScroll();

		// ********** LAYOUT ********* //		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.fill = gbc.HORIZONTAL;
		this.add(echelle, gbc);

		gbc.gridy = 1;
		gbc.weighty = 1;
		gbc.fill = gbc.BOTH;
		this.add(dessin, gbc);

		gbc.gridy = 2;
		gbc.weighty = 0;
		gbc.fill = gbc.HORIZONTAL;
		this.add(scroll, gbc);

	}

	private void setData(float[] data) {
		this.data = data;
		// calcul du max
		for (int i = 0; i < data.length; i++) {
			float val = Math.abs(data[i]);
			if (val > amplitudeMax) {
				amplitudeMax = val;
			}
		}
	}

	private void initEchelle() {
		echelle = new JSlider(JSlider.HORIZONTAL, 0, correspondanceEchelleFrames.length - 1, 7);
		echelle.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				nbFramesParPixel = correspondanceEchelleFrames[echelle.getValue()];
				updateScroll();
				dessin.repaint();
			}
		});
	}

	private void initDessin() {
		dessin = new JPanel() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g2 = (Graphics2D) g;

				//Background
				g2.setColor(BACKGROUND_COLOR);
				g2.fillRect(1, 1, getWidth() - 2, getHeight() - 2);

				g2.setColor(Color.black);

				// Paramètres d'affichage
				int premierPix = 0;
				int dernierPix = getWidth();

				int numFrame = getOffset();
				if (nbFramesParPixel >= 1) { //Vue éloignée
					for (int px = premierPix; px < dernierPix; px++) {
						numFrame += nbFramesParPixel;

						int derniereFrame = Math.min( numFrame + nbFramesParPixel, data.length-1);

						//Calcul des frames min et max
						float max = 0;
						float min = 0;
						int fmax = numFrame;
						int fmin = numFrame;
						for (int f = numFrame; f < derniereFrame; f++) {
							float v = data[f];
							if (v > max) {
								max = v;
								fmax = f;
							}
							if (v < min) {
								min = v;
								fmin = f;
							}
						}

						//Si on est là où la souris est
						if (px == pixelHover) {
							g2.setColor(Color.red);
							String str;
							int x;
							float y;
							if (max > -min) {
								x = fmax;
								y = max;
							} else {
								x = fmin;
								y = min;
							}
							str = "[" + (x / (double) data.length * 44100) + "] = " + y;
							g2.drawString(str, 0, 15);
						} else {
							g2.setColor(Color.black);
						}
						g2.drawLine(px, amplitude2Pix(min), px, amplitude2Pix(max));
					}
				} else { //Cas rapproché, on voit chaque data
					//Zero horizontal
					g2.setColor(Color.gray);
					g2.drawLine(premierPix, amplitude2Pix(0), dernierPix, amplitude2Pix(0));
					g2.setColor(Color.black);
					
					
					int pasPixel = -nbFramesParPixel;
					int valPrecedente = 0;
					for (int px = premierPix; px < dernierPix; px += pasPixel, numFrame ++) {
						float v = data[numFrame];
						float max = v;
						int val = amplitude2Pix(max);
						g2.drawLine(px, val + 1, px, val - 1);
						if (numFrame % 10 == 0) {
							// g2.drawString(""+numFrame, px, milieu-val-2);
						}
						if (px > premierPix) { //si ce n'est pas la première valeur affichée
							g2.drawLine(px - pasPixel, valPrecedente, px, val); //on trace le lien avec la valeur précédente
						}
						valPrecedente = val;
					}

				}

				// affichage du curseur
				g2.setColor(Color.gray);
				int x = offset2pixel(curseur);
				System.out.println("curseur = "+curseur+", x = "+x);
				g2.drawLine(x, 0, x, getHeight());
			}

		};

		dessin.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				pixelHover = e.getX();
				dessin.repaint();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}
		});

		dessin.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				curseur = pixel2offset(e.getX());
				dessin.repaint();
			}
		});

	}

	private void initScroll() {
		scroll = new JScrollBar(JScrollBar.HORIZONTAL);
		scroll.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				dessin.repaint();
			}
		});

		updateScroll();
	}

	public void updateScroll() {
		// il faut placer le curseur au centre, si possible.
		int off = curseur;

		int value = 0;
		int extent = 1;
		int min = 0;
		int max = data.length;

		int unitIncrement = 10;

		if (nbFramesParPixel > 0) {
			max /= nbFramesParPixel;
			off /= nbFramesParPixel;
		} else {
			max *= -nbFramesParPixel;
			off *= -nbFramesParPixel;
			unitIncrement = -nbFramesParPixel;
		}

		if (dessin != null) {
			value = off - dessin.getWidth() / 2;
			if (nbFramesParPixel < 0) {
				// il faut s'assurer qu'on commence sur une frame
				value -= value % (-nbFramesParPixel);
			}
			if (value < 0) {
				value = 0;
			}
		}

		scroll.setUnitIncrement(unitIncrement);
		scroll.setBlockIncrement(10 * unitIncrement);
		int ancienneVal = scroll.getValue();
		scroll.setValues(value, extent, min, max - extent);
		if (ancienneVal == value) {
			// il faut appeler un repaint car l'adjustment listener n'est pas
			// appelé
			// (cas où l'échelle passe de -1 à 1 et vice versa)
			repaint();
		}
	}

	public int offset2pixel(int offset) {
		int ret = 0;
		int val = offset - this.getOffset();
		if (this.nbFramesParPixel > 0) {
			ret =  val / this.nbFramesParPixel;
		} else {
			ret = val * (-this.nbFramesParPixel);
		}
		System.out.println("nbFr="+nbFramesParPixel+", offset = "+offset+", ret = "+ret);
		return ret;
	}

	public int pixel2offset(int pixel) {
		int val = pixel;
		if (this.nbFramesParPixel > 0) {
			val *= this.nbFramesParPixel;
		} else {
			val /= (-this.nbFramesParPixel);
		}
		return this.getOffset() + val;
	}

	public int amplitude2Pix(double a) {
		return (int) ((amplitudeMax - a) / amplitudeMax * (this.getHeight() / 2));
	}

	public double pix2amplitude(int pix) {
		return amplitudeMax * (1 - (pix) / (double) (this.getHeight() / 2));
	}

	public int getOffset() {
		int val = scroll.getValue();
		if (nbFramesParPixel > 0) {
			val *= nbFramesParPixel;
		} else {
			val /= (-nbFramesParPixel);
		}
		return val;
	}

	public static JFrame createSimpleFrameVisualiser(String title, float[] data) {
		DataVisualizer v = new DataVisualizer(data);

		JFrame frame = new JFrame(title);
		frame.getContentPane().add(v);
		frame.setMinimumSize(new Dimension(500, 400));
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		return frame;
	}

}
