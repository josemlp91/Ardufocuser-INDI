/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.awt.image.BufferedImage.TYPE_USHORT_GRAY;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import org.eso.fits.FitsException;
import starprocessor.FitsImage;
import static starprocessor.StarProcessor.get_all_peak;
import static starprocessor.StarProcessor.is_peak_layer;

import starprocessor.StarSet;

/**
 *
 * @author josemlp
 */
public final class GUIMain extends javax.swing.JFrame {
    
    private GUIInternal vi;
    private FitsImage fimg;
    private BufferedImage bimg;
    
    public int actual_mean_val;
    public int actual_max_val;
    
    public StarSet stars;

    /**
     * Creates new form viwes
     * @throws java.io.IOException
     * @throws org.eso.fits.FitsException
     */
    public GUIMain() throws IOException, FitsException {
        
        initComponents();
        setSize(1100, 800);
        
        fimg = new FitsImage("/home/josemlp/workspace/pruebasEnfoque/nucleo24880_042.fit");
        fimg.Matrix2BufferedImage(WIDTH);

        //img.SaveAsJPG();
        bimg = fimg.Matrix2BufferedImage(TYPE_INT_ARGB);
        
        //GUIInternal.showImage(bimg, "Imagen Astronomica");
        
        ProcesingAndDrawStar();
    }
    
    public static JDesktopPane getEscritorio() {
        return escritorio;
    }
    
    public void ProcesingAndDrawStar() throws FitsException {
        
        actual_mean_val = (int) fimg.getMean();
        actual_max_val = (int) fimg.getMax();
        
        meanVal.setText("" + actual_mean_val);
        maxVal.setText("" + actual_max_val);

        ///Tuning calculos, mecanizar con los calculos.
        int umbralMin = (int) actual_mean_val * (Integer) min_factor_spinner.getValue();
        int umbralMax = (int) (actual_max_val - (actual_max_val * (Integer) max_factor_spinner.getValue() / 10));
        
        
        // Creamos un primer conjunto.
        stars = new StarSet(fimg);
        
        System.out.println("Estrellas iniciales");
        System.out.println(stars.size_aceptadas());
        stars.filter_star_by_initial_umbral(umbralMin, umbralMax);
        System.out.println("Filtro umbrales");
        System.out.println(stars.size_aceptadas());
       
        System.out.println(fimg.getNcol());
        System.out.println(fimg.getNrow());
        System.out.println("sssssssss");
        
        
        // Ejemplo 
        is_peak_layer(949,775,  fimg, 5);
        


        //stars.print_star_set();
        
        
        
        
        /*
        stars = get_all_peak(fimg, 50);
        stars.filter_star_by_min_distance(slider_distancia.getValue());
       
        
        int a = stars.size_aceptadas();
        int t = stars.size();
        
        stars.setFrame(slider_distancia.getValue()/2);
        
        
        int m = t - a;
        totales.setText("" + t);
        aceptadas.setText("" + a);
        
        rechazadas.setText("" + m);
        
        GUIInternal.lienzo.starload = true;
        GUIInternal.lienzo.setStarSet(stars);
        
        */
        
        //repaint();
        //pack();
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        barra_tool = new javax.swing.JToolBar();
        jToolBar1 = new javax.swing.JToolBar();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        meanVal = new javax.swing.JTextField();
        maxVal = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        totales = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        aceptadas = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        rechazadas = new javax.swing.JTextField();
        escritorio = new javax.swing.JDesktopPane();
        main_panel_tool = new javax.swing.JPanel();
        barra_botton = new javax.swing.JToolBar();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        SpinnerModel model = new SpinnerNumberModel(1, 1, 4, 1);
        min_factor_spinner = new javax.swing.JSpinner(model);
        SpinnerModel model1 = new SpinnerNumberModel(1, 1, 9, 1);
        max_factor_spinner = new javax.swing.JSpinner(model1);
        jPanel2 = new javax.swing.JPanel();
        slider_distancia = new javax.swing.JSlider();
        slider_val = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jSpinner3 = new javax.swing.JSpinner();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        barra_menu = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        deleteMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        contentsMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1024, 800));
        getContentPane().setLayout(new java.awt.BorderLayout());

        barra_tool.setFloatable(false);
        barra_tool.setRequestFocusEnabled(false);
        getContentPane().add(barra_tool, java.awt.BorderLayout.NORTH);

        jToolBar1.setRollover(true);
        jToolBar1.setPreferredSize(new java.awt.Dimension(18, 30));
        getContentPane().add(jToolBar1, java.awt.BorderLayout.NORTH);

        jPanel4.setOpaque(false);
        jPanel4.setPreferredSize(new java.awt.Dimension(190, 319));

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Resultados:"));
        jPanel5.setPreferredSize(new java.awt.Dimension(126, 307));

        jLabel3.setText("Media:");

        jLabel4.setText("Maxima:");

        meanVal.setEditable(false);
        meanVal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                meanValActionPerformed(evt);
            }
        });

        maxVal.setEditable(false);
        maxVal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maxValActionPerformed(evt);
            }
        });

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Estrellas"));

        jLabel5.setText("Totales");

        totales.setEditable(false);
        totales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalesActionPerformed(evt);
            }
        });

        jLabel6.setText("Aceptadas");

        aceptadas.setEditable(false);
        aceptadas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptadasActionPerformed(evt);
            }
        });

        jLabel7.setText("Rechazadas");

        rechazadas.setEditable(false);
        rechazadas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rechazadasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(totales, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(aceptadas, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(rechazadas, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 21, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(aceptadas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rechazadas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1)
                .addGap(128, 128, 128))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(meanVal, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(maxVal, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(meanVal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maxVal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 328, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel4, java.awt.BorderLayout.LINE_END);

        escritorio.setPreferredSize(new java.awt.Dimension(885, 519));

        javax.swing.GroupLayout escritorioLayout = new javax.swing.GroupLayout(escritorio);
        escritorio.setLayout(escritorioLayout);
        escritorioLayout.setHorizontalGroup(
            escritorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 685, Short.MAX_VALUE)
        );
        escritorioLayout.setVerticalGroup(
            escritorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 306, Short.MAX_VALUE)
        );

        getContentPane().add(escritorio, java.awt.BorderLayout.CENTER);

        main_panel_tool.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        barra_botton.setFloatable(false);
        barra_botton.setRollover(true);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Umbrales"));
        jPanel1.setToolTipText("Umbrales");
        jPanel1.setName(""); // NOI18N

        jLabel1.setText("Factor minimo:");

        jLabel2.setText("Factor maximo:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(min_factor_spinner, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(max_factor_spinner, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(min_factor_spinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(max_factor_spinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Distancia"));
        jPanel2.setToolTipText("Distancia");
        jPanel2.setName(""); // NOI18N

        slider_distancia.setPaintLabels(true);
        slider_distancia.setPaintTicks(true);
        slider_distancia.setSnapToTicks(true);
        slider_distancia.setValue(1);
        slider_distancia.setValueIsAdjusting(true);
        slider_distancia.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                slider_distanciaStateChanged(evt);
            }
        });

        slider_val.setEditable(false);
        slider_val.setText("0");
        slider_val.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                slider_valActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(slider_distancia, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(slider_val, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(slider_distancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(slider_val, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Otros filtros"));
        jPanel3.setToolTipText("Distancia");
        jPanel3.setName(""); // NOI18N

        jCheckBox1.setText("Estrellas lumnosas");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(105, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox1)))
        );

        jButton1.setText("Aplicar filtros");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout main_panel_toolLayout = new javax.swing.GroupLayout(main_panel_tool);
        main_panel_tool.setLayout(main_panel_toolLayout);
        main_panel_toolLayout.setHorizontalGroup(
            main_panel_toolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(main_panel_toolLayout.createSequentialGroup()
                .addComponent(barra_botton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(230, 230, 230))
            .addGroup(main_panel_toolLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(main_panel_toolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(main_panel_toolLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(main_panel_toolLayout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        main_panel_toolLayout.setVerticalGroup(
            main_panel_toolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, main_panel_toolLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(main_panel_toolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(main_panel_toolLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(barra_botton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(main_panel_tool, java.awt.BorderLayout.PAGE_END);

        fileMenu.setMnemonic('f');
        fileMenu.setText("File");

        openMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        openMenuItem.setMnemonic('o');
        openMenuItem.setText("Open");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openMenuItem);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("New");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem1);

        saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        saveMenuItem.setMnemonic('s');
        saveMenuItem.setText("Save");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F12, 0));
        saveAsMenuItem.setMnemonic('a');
        saveAsMenuItem.setText("Save As ...");
        saveAsMenuItem.setDisplayedMnemonicIndex(5);
        saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveAsMenuItem);

        exitMenuItem.setMnemonic('x');
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        barra_menu.add(fileMenu);

        editMenu.setMnemonic('e');
        editMenu.setText("Edit");

        deleteMenuItem.setMnemonic('d');
        deleteMenuItem.setText("Ver Barra de estado");
        deleteMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(deleteMenuItem);

        barra_menu.add(editMenu);

        helpMenu.setMnemonic('h');
        helpMenu.setText("Help");

        contentsMenuItem.setMnemonic('c');
        contentsMenuItem.setText("Contents");
        helpMenu.add(contentsMenuItem);

        aboutMenuItem.setMnemonic('a');
        aboutMenuItem.setText("About");
        helpMenu.add(aboutMenuItem);

        barra_menu.add(helpMenu);

        setJMenuBar(barra_menu);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
        JFileChooser dlg = new JFileChooser();
        int resp = dlg.showOpenDialog(this);
        if (resp == JFileChooser.APPROVE_OPTION) {
            File f = dlg.getSelectedFile();
            
            fimg = new FitsImage(f.getAbsolutePath());
            fimg.Matrix2BufferedImage(WIDTH);

            //img.SaveAsJPG();
            BufferedImage bimg = fimg.Matrix2BufferedImage(TYPE_USHORT_GRAY);
            GUIInternal.showImage(bimg, "Imagen Astronomicaaa");
            meanVal.setText("" + (int) fimg.getMean());
            maxVal.setText("" + (int) fimg.getMax());
            
            repaint();
            
        }
    }//GEN-LAST:event_openMenuItemActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        /*
         VentanaInterna vi = new VentanaInterna();
         Escritorio.add(vi);
         vi.setLocation(posX, posY);
         posX+=50;posY+=50;
         vi.setVisible(true);

         */
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        JFileChooser dlg = new JFileChooser();
        int resp = dlg.showOpenDialog(this);
        if (resp == JFileChooser.APPROVE_OPTION) {
            File f = dlg.getSelectedFile();
            //Código
        }        // TODO add your handling code here:
    }//GEN-LAST:event_saveMenuItemActionPerformed

    private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuItemActionPerformed
        JFileChooser dlg = new JFileChooser();
        int resp = dlg.showOpenDialog(this);
        if (resp == JFileChooser.APPROVE_OPTION) {
            File f = dlg.getSelectedFile();
            //Código
        }        // TODO add your handling code here:
    }//GEN-LAST:event_saveAsMenuItemActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void deleteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteMenuItemActionPerformed
        /*  if (biestable1) { barraEstado.setVisible(true); biestable1=false;}
         else { barraEstado.setVisible(false); biestable1=true;}*/
    }//GEN-LAST:event_deleteMenuItemActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void maxValActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maxValActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_maxValActionPerformed

    private void meanValActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_meanValActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_meanValActionPerformed

    private void totalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalesActionPerformed

    private void aceptadasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aceptadasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_aceptadasActionPerformed

    private void rechazadasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rechazadasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rechazadasActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            ProcesingAndDrawStar();        // TODO add your handling code here:
        } catch (FitsException ex) {
            Logger.getLogger(GUIMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void slider_valActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_slider_valActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_slider_valActionPerformed

    private void slider_distanciaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_slider_distanciaStateChanged
        slider_val.setText("" + slider_distancia.getValue());       // TODO add your handling code here:
    }//GEN-LAST:event_slider_distanciaStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws IOException, FitsException {

       // FitsImage img = new FitsImage("/home/josemlp/pruebasEnfoque/nucleo24910_111.fit");
        // img.showKeyword();
        //img.printImageMatrix();
           /*
       

         double means = img.getMean();
         int max = img.getMax();

         System.out.println(max);

         int umbralMin = (int) means * 2;
         int umbralMax = max - (max * 20) / 100;

         StarSet stars = new StarSet();
         stars = getAllPeak(img, 50);

         stars.filterStarByInitialUmbral(umbralMin, umbralMax);
         stars.filterStarByMinDistance(10);

         stars.printStarSet();
         */

        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUIMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUIMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUIMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUIMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new GUIMain().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(GUIMain.class.getName()).log(Level.SEVERE, null, ex);
                } catch (FitsException ex) {
                    Logger.getLogger(GUIMain.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    public final GUIInternal newWindows(int width, int height) {
        final GUIInternal vi = new GUIInternal();
        escritorio.add(vi);
        vi.setSize(width, height);
        vi.setTitle("Lienzo ");
        vi.setVisible(true);
        setVentanaInterna(vi);
        
        return vi;
    }
    
    public void setVentanaInterna(GUIInternal vi) {
        this.vi = vi;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JTextField aceptadas;
    private javax.swing.JToolBar barra_botton;
    private javax.swing.JMenuBar barra_menu;
    private javax.swing.JToolBar barra_tool;
    private javax.swing.JMenuItem contentsMenuItem;
    private javax.swing.JMenuItem deleteMenuItem;
    private javax.swing.JMenu editMenu;
    private static javax.swing.JDesktopPane escritorio;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel main_panel_tool;
    private javax.swing.JTextField maxVal;
    private javax.swing.JSpinner max_factor_spinner;
    private javax.swing.JTextField meanVal;
    private javax.swing.JSpinner min_factor_spinner;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JTextField rechazadas;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JSlider slider_distancia;
    private javax.swing.JTextField slider_val;
    private javax.swing.JTextField totales;
    // End of variables declaration//GEN-END:variables
}
