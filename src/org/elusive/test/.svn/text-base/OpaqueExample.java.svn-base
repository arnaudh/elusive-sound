package org.elusive.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class OpaqueExample extends JFrame {

    private JLayeredPane layers;
    private JPanel up, down;
    private JButton toggleOpaque;

    public OpaqueExample() {
        layers = new JLayeredPane();

        down = new JPanel();
        down.setBackground(Color.GREEN);
        down.setBounds(100, 100, 200, 200);
        layers.add(down, new Integer(1));

        up = new JPanel() {
            public void paintComponent(Graphics og) {
                super.paintComponent(og);

                Graphics2D g = (Graphics2D)og;
                GradientPaint gradient = new GradientPaint(0, 0, Color.BLUE, 10, 0, 
                          Color.WHITE, true );

                Polygon poly = new Polygon();
                poly.addPoint(10, 10);
                poly.addPoint(100, 50);
                poly.addPoint(190, 10);
                poly.addPoint(150, 100);
                poly.addPoint(190, 190);
                poly.addPoint(100, 150);
                poly.addPoint(10, 190);
                poly.addPoint(50, 100);
                poly.addPoint(10, 10);

                g.setPaint(gradient);
                g.fill(poly);

                g.setPaint(Color.BLACK);
                g.draw(poly);
           }
        };
        up.setBackground(Color.RED);
        up.setBounds(150, 150, 200, 200);
        layers.add(up, new Integer(2));

        getContentPane().add(layers, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        toggleOpaque = new JButton("Toggle Opaque");
        toggleOpaque.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                up.setOpaque(!up.isOpaque());
                layers.repaint();
            }
        });
        buttonPanel.add(toggleOpaque);

        getContentPane().add(buttonPanel, BorderLayout.EAST);
    } 

    public static void main(String[] args) {
        JFrame f = new OpaqueExample();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(500, 500);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}