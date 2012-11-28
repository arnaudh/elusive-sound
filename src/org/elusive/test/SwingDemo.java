package org.elusive.test;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics; 
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseMotionAdapter;

public class SwingDemo {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                createAndShowGUI(); 
            }
        });
    }

    private static void createAndShowGUI() {
        JFrame f = new JFrame("Swing Demo");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        f.add(new MyPanel());
        f.pack();
        f.setVisible(true);
    } 
}

class MyPanel extends JPanel {

    private int squareX = 50;
    private int squareY = 50;
    private int squareW = 20;
    private int squareH = 20;

    public MyPanel() {

        setBorder(BorderFactory.createLineBorder(Color.black));

        addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                moveSquare(e.getX(),e.getY());
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                moveSquare(e.getX(),e.getY());
            }
        });

    }

    private void moveSquare(final int x, final int y){
                final int OFFSET = 1000;
                if ((squareX!=x) || (squareY!=y)) {
                	System.out.println("MyPanel.moveSquare("+System.currentTimeMillis()+") repaint 1");
                    repaint(squareX,squareY,squareW+OFFSET,squareH+OFFSET);
                    squareX=x;
                    squareY=y;
                	System.out.println("MyPanel.moveSquare("+System.currentTimeMillis()+") repaint 2");
                    repaint(squareX,squareY,squareW+OFFSET,squareH+OFFSET);
                } 
    }

    public Dimension getPreferredSize() {
        return new Dimension(250,200);
    }
    
    protected void paintComponent(Graphics g) {
    	System.out.println("MyPanel.paintComponent("+System.currentTimeMillis()+")");
        super.paintComponent(g);       
        g.drawString("This is my custom Panel!",10,20);
        g.setColor(Color.RED);
        g.fillRect(squareX,squareY,squareW,squareH);
        g.setColor(Color.BLACK);
        g.drawRect(squareX,squareY,squareW,squareH);
    }  
}