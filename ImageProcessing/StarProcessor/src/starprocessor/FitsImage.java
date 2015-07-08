/*
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * Clase destinada a manejar y encapsular imagenes FITS.
 *
 * @author @josemlp and @zerjillo Proyect Ardufocuser-INDI
 * @version: 22/03/2015/A See { * @linktourl
 * https://github.com/josemlp91/Ardufocuser-INDI}
 */
package starprocessor;

import java.util.*;
import java.io.*;
import org.eso.fits.*;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author josemlp Imagenes Astronomicas FITS
 * @see
 * <a href="http://es.wikipedia.org/wiki/FITS">http://es.wikipedia.org/wiki/FITS</a>
 *
 * Encapsula propiedades y métodos para realizar procesamiento este tipo de
 * imágenes de forma cómoda.
 *
 */
public class FitsImage {

    // Nombre del archivo que almacena la imagen.
    private String filename;

    // Binario con el archivo abierto.
    private FitsFile fitsfile;

    // Pixeles de la imagen (en formato fits)
    private FitsMatrix fitsmatrix;

    // Metadatos de la imagen.
    private ListIterator keyword;

    // Array lineal con los pixeles de la imagen.
    private int[] ImageArray;

    // Matrix bidimensional con los pixeles de la imagen.
    private int[][] ImageMatrix;

    // Número de pixeles totales.
    private int nval;

    // Numero de filas.
    private int nrow;

    // Numero de columnas.
    private int ncol;

    // Media de los valores de la imagen.
    private double mean;

    // Maximo de los valores de la imagen.
    private int max;

    /**
     * Constructor, genera y iniclializa imagen dado la ruta del archivo.
     *
     * @param filename Ruta del archivo con la imagen en formato fits Flexible
     * Image Transport System. See {
     * @linktourl http://es.wikipedia.org/wiki/FITS}
     *
     */
    public FitsImage(String filename) {

        FitsFile file = null;

        try {

            // Abrimos archivo fits 
            file = new FitsFile(filename);

            this.fitsfile = file;

            // Get HDUnit in FitsFile by its position. 
            // If the position is less that 0, the first HDU is returned while the last is given for positions beyond the actual number.
            FitsHDUnit hdu = fitsfile.getHDUnit(0);

            // Cabeceras
            FitsHeader hdr = hdu.getHeader();
            keyword = hdr.getKeywords();

            this.fitsmatrix = (FitsMatrix) hdu.getData();
            this.filename = filename;

            // Numero de dimensiones 
            int naxis[] = fitsmatrix.getNaxis();

            // Numero total de pixeles.
            this.nval = fitsmatrix.getNoValues();
            try {
                if (0 < nval) {
                    // Calculamos numero de filas y de columnas.
                    this.ncol = naxis[0];
                    this.nrow = nval / ncol;
                }
            } catch (SecurityException ex) {
                Logger.getLogger(StarProcessor.class
                        .getName()).log(Level.SEVERE, "Imagen no valida.", ex);

                System.out.println("Imagen no valida.");

            }

            // Iniciamos arrays.
            this.ImageArray = new int[this.nval];
            this.ImageMatrix = new int[ncol][nrow];

            try {
                this.fitsmatrix.getIntValues(0, this.nval, ImageArray);
            } catch (FitsException ex) {
                Logger.getLogger(StarProcessor.class
                        .getName()).log(Level.SEVERE, "No es posible acceder a los pixeles de la imagen.", ex);

            }

            int off, npix;
            off = npix = 0;
            int val;

            // Recorremos matrix de pixeles (filas x columnas), 
            // alamcenando en ImageArray, ImageMatrix 
            // haciendo calculos de média y máximo.
            for (int nr = 0; nr < nrow; nr++) {

                // En data almacenamos las filas.
                int data[] = new int[ncol];
                try {

                    // Seleccionamos la fila dada por el desplazamiento y almacenamos en data.
                    this.fitsmatrix.getIntValues(off, this.ncol, data);

                    ////Calculos intermedios media, maximo. 
                    for (int n = 0; n < ncol; n++) {
                        val = data[n];
                        //  System.out.print(val + "\t");
                        // Calculo del maximo
                        if (val > max) {
                            //   System.out.print(val + "\t");
                            this.max = val;
                        }
                        // Calculo de la media.
                        this.mean += val;
                        npix++;
                    }

                } catch (FitsException e) {
                    Logger.getLogger(StarProcessor.class
                            .getName()).log(Level.SEVERE, "No es posible acceder a los pixeles de la imagen.", e);

                }

                // Almacenamos en la fila la fila extraida.
                ImageMatrix[nr] = data;
                //    System.out.println("");
                // Incrementamos el desplazamiento para que apunte a la siguiente fila. 
                off += ncol;
            }

            // Calculo de media.
            this.mean = this.mean / npix;

            // Transponemos la matrix no se porque !!! pero lo tengo que hacer
            // DEPURAR BUG DURO.
            //this.ImageMatrix = traspose();
        } catch (IOException ex) {
            Logger.getLogger(StarProcessor.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (FitsException ex) {
            Logger.getLogger(StarProcessor.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    /*
     * Informa del normbre del archivo 
     * @return nombre archivo 
     */
    public String getFilename() {
        return filename;
    }

    /*
     * Acedemos al binario que contiene los datos de la imagen. 
     * @return fits file.
     */
    public FitsFile getFitsfile() {
        return fitsfile;
    }

    /*
     * Acedemos a la matrix de la imagen 
     * @return matrix imagen
     */
    public FitsMatrix getFitsmatrix() {
        return fitsmatrix;
    }

    /*
     * Acedemos a las cabeceras de la imagen
     * @return metadatos 
     */
    public ListIterator getKeyword() {
        return keyword;
    }

    /*
     * Acedemos array lineal con pixels de la imagen.
     * @return array[] 
     */
    public int[] getImageArray() {
        return ImageArray;
    }

    /*
     * Acedemos matrix bidimensional  con pixels de la imagen.
     * @return array[][]
     */
    public int[][] getImageMatrix() {
        return ImageMatrix;
    }

    /*
     * Numero de pixeles de la imagen.
     */
    public int getNval() {
        return nval;
    }

    /*
     * Numero de filas de la imagen.
     */
    public int getNrow() {
        return nrow;
    }

    /*
     * Numero de columnas de la imagen.
     */
    public int getNcol() {
        return ncol;
    }

    /*
     * Media
     */
    public double getMean() {
        return mean;
    }

    /*
     * Maximo valor
     */
    public int getMax() {
        return max;
    }

    /*
     * Valor del pixel x, y
     */
    public int getValue(int x, int y) {
        return ImageMatrix[x][y];
    }

    /**
     * Imprime por pantalla metadatos adjuntos en la cabecera.
     */
    public void showKeyword() {

        ListIterator itr = this.keyword;
        while (itr.hasNext()) {
            FitsKeyword kw = (FitsKeyword) itr.next();
            System.out.print("     " + kw.getName());
            switch (kw.getType()) {
                case FitsKeyword.COMMENT:
                    System.out.print("(C) " + kw.getComment());
                    break;
                case FitsKeyword.STRING:
                    System.out.print("(S)= '" + kw.getString() + "'");
                    break;
                case FitsKeyword.BOOLEAN:
                    System.out.print("(B)= " + kw.getBool());
                    break;
                case FitsKeyword.INTEGER:
                    System.out.print("(I)= " + kw.getInt());
                    break;
                case FitsKeyword.REAL:
                    System.out.print("(R)= " + kw.getReal());
                    break;
                case FitsKeyword.DATE:
                    System.out.print("(D)= " + kw.getString());
                    break;
                default:
            }
            if (0 < kw.getComment().length()
                    && (kw.getType() != FitsKeyword.COMMENT)) {
                System.out.print(" / " + kw.getComment());
            }
            System.out.println();
        }

    }

    /**
     * Genera archivo png imagen.
     */
    public void SaveAsJPG() throws IOException {

        BufferedImage bufferedImage = Matrix2BufferedImage(BufferedImage.TYPE_USHORT_GRAY);
        ImageIO.write(bufferedImage, "png", new File("output.png"));

    }

    /*
     * Convierte matrix de pixeles en un buffered Image.
     */
    public BufferedImage Matrix2BufferedImage(int type) {
        BufferedImage bufferedImage = new BufferedImage(this.nrow, this.ncol, BufferedImage.TYPE_INT_ARGB);
        double gamma = 0.5;
        //Bug.
        for (int i = 0; i < this.nrow; i++) {
            for (int j = 0; j < this.ncol; j++) {

                int pixel = ImageMatrix[i][j];

                double aux = ((double) pixel - 0) / ((double) (65536 - 0));
                aux = Math.pow(aux, gamma);
                int mod = (int) (aux * 255);
             //   if ((mod < 0) || (mod > 255)) 

              //  System.out.print("-" + pixel + " " + mod + "-\t");
                // }
                //System.out.println(pixel);
                bufferedImage.setRGB(i, j, 0xff000000 | ((mod & 0xff) << 16) | ((mod & 0xff) << 8) | (mod & 0xff));

            }
            //  System.out.println("");
        }

        return bufferedImage;
    }

    public static BufferedImage read(byte[] bytes) {
        try {
            return ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int[][] getSubMatrix(int cornerX, int cornerY, int dim) {

           System.out.println("aaaaaaaaaa");
        System.out.println(this.getValue(cornerX, cornerY));
        System.out.println("aaaaaaaaaaaa");
        
        int[][] SubMatrix = new int[dim][dim];

        int i = 0, j = 0;
        
       
        for (int ii = cornerX; ((ii < this.getNrow()) && (ii < (dim + cornerX))); ii++) {
            j = 0;
           
               System.out.println("------------");
            System.out.println(this.getNrow());
            System.out.println(cornerY);
            for (int jj = cornerY; ((jj < this.getNcol()) && (jj < (dim + cornerY))); jj++) {
                System.out.println("eeentrrraa");
                SubMatrix[i][j] = this.getValue(ii, jj);
                System.out.println(this.getValue(ii, jj));
                j++;

            }
            i++;
        }

        return SubMatrix;

    }

    /*
     * Imprime por pantalla información interesante de la imagen
     * Metodo interesante para depurar.
     * Use: Debug.
     */
    public void verbose() {

        System.out.println("Max value: " + this.max);
        System.out.println("Mean value: " + this.mean);
        System.out.println("Cols number: " + this.ncol);
        System.out.println("Rows number: " + this.nrow);
        System.out.println("Pixels number: " + this.nval);

    }

    /**
     * Imprime por pantalla la representación de la matrix. Use : Debug.
     */
    public void printImageMatrix() {

        for (int i = 0; i < this.nrow; i++) {
            for (int j = 0; j < this.ncol; j++) {

                System.out.print(this.ImageMatrix[i][j] + "  ");
            }
            System.out.println();
        }

    }

    /**
     * Transpone la matrix de la imagen, cambiando filas por columnas.
     */
    public int[][] traspose() {

        int transpose_matrix[][] = new int[this.ncol][this.nrow];

        for (int c = 0; c < this.nrow; c++) {
            for (int d = 0; d < this.ncol; d++) {
                transpose_matrix[d][c] = ImageMatrix[c][d];
            }
        }

        return transpose_matrix;

    }

}
