/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;
import common.StarFilterStatus;
import static common.StarFilterStatus.FILTER_NOT_VALID;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javafx.util.Pair;
import starprocessor.Star;
import starprocessor.StarSet;

/**
 *
 * @author josemlp
 */
public class GUILienzo extends javax.swing.JPanel {

    private BufferedImage img;
    private BufferedImage imgDest;

    static Stroke strok = new BasicStroke(2);

    ArrayList<Pair<StarFilterStatus, Shape>> vShape = new ArrayList<Pair<StarFilterStatus, Shape>>();

    static StarSet stars = new StarSet();
    public Boolean starload;

    /**
     * Creates new form Lienzo
     */
    public GUILienzo() {
        initComponents();

        this.starload = true;

    }

    public void setStarSet(StarSet newstars) {

        System.out.println(newstars.size());
        this.stars = newstars;
    }

    @Override
    public void paint(Graphics g) {

        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        if (imgDest != null) {
            g2d.drawImage(imgDest, 0, 0, this);
        }

        if (this.starload) {
            loadStars();
        }

        //Definimos el contexto.
        
        g2d.setStroke(strok);

        //Bucle que pinta cada una de las figuras del array.
        for (Pair<StarFilterStatus, Shape> s : vShape) {

            switch (s.getKey()) {
                case FILTER_NOT_VALID:
                    g2d.setPaint(Color.RED);
                    break;
                case FILTER_NOT_APPLY:
                    g2d.setPaint(Color.BLUE);
                    break;
                case FILTER_ALL_PASS:
                    g2d.setPaint(Color.GREEN);
                    break;
                case FILTER_BY_DISTANCE:
                    g2d.setPaint(Color.YELLOW);
                    break;

                case FILTER_BY_LESS_UMBRAL:
                    g2d.setPaint(Color.PINK);
                    break;

                case FILTER_BY_MORE_UMBRAL:
                    g2d.setPaint(Color.RED);
                    break;

                case FILTER_BY_MAGIN:
                    g2d.setPaint(Color.ORANGE);
                    break;

            }

            g2d.fill(s.getValue());
            g2d.draw(s.getValue());
        }

    }

    private void loadStars() {

        for (int i = 0; i < stars.size(); i++) {

            Star s = stars.get(i);
            Point2D.Double p = new Point2D.Double(s.getCoordx(), s.getCoordy());
            Line2D.Double punto = new Line2D.Double(p, p);

            Pair pp = new Pair(s.getStatus(), punto);
            vShape.add(pp);
            this.starload = false;

        }

    }

    public void setImageOriginal(BufferedImage img) {
        if (img != null) {
            setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
            this.img = img;
            setImageActual(img);
        }
    }

    void setImageActual(BufferedImage imgDest) {
        setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
        this.imgDest = imgDest;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
