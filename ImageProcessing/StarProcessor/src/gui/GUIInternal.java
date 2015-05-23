/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.image.BufferedImage;

/**
 *
 * @author josemlp
 */
public class GUIInternal extends javax.swing.JInternalFrame {

    
    //Propiedades de la ventana interna
    
    int means;
    int max;
    
    int nstar;
    int nstar_avalibles;
    int nstar_not_avalibles;
    
    
    //Filtros aplicados sobre esta ventana.
    int fac_min;
    int fac_max;
    
    int distancia_entre;
    int n_estrellas_max;
    
   
    /**
     * Creates new form VentanaInterna
    */
    public GUIInternal() {
        initComponents();
        
    }
    
    public void loseFocus(){
    
    
    }
    
     public static void showImage(BufferedImage img) {
        showImage(img, "Imagen");
    }

    public static void showImage(BufferedImage img, String title) {
        GUIInternal vi = new GUIInternal();
        
        if (img != null) {
            
            
            vi.getLienzo().setImageOriginal(img);
            GUIMain.getEscritorio().add(vi);
            vi.setVisible(true);

            double width, height;
            if (GUIMain.getEscritorio().getWidth() < img.getWidth()) {
                width = GUIMain.getEscritorio().getWidth();
            } else {
                width = img.getWidth();
            }
            if (GUIMain.getEscritorio().getHeight() < img.getHeight()) {
                height = GUIMain.getEscritorio().getHeight();
            } else {
                height = img.getHeight();
            }

            vi.setSize((int) width, (int) height);
        }
        vi.setTitle(title);

    }
    
    
    public  GUILienzo getLienzo() {
        return lienzo;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        lienzo = new gui.GUILienzo();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setAutoscrolls(true);

        javax.swing.GroupLayout lienzoLayout = new javax.swing.GroupLayout(lienzo);
        lienzo.setLayout(lienzoLayout);
        lienzoLayout.setHorizontalGroup(
            lienzoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 394, Short.MAX_VALUE)
        );
        lienzoLayout.setVerticalGroup(
            lienzoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 274, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(lienzo);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    public static gui.GUILienzo lienzo;
    // End of variables declaration//GEN-END:variables
}
