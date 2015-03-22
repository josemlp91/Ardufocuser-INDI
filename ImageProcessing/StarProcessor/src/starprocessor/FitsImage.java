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
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class FitsImage {

    private String filename;
    private FitsFile fitsfile;
    private FitsMatrix fitsmatrix;
    private ListIterator keyword;

    private int[] ImageArray;
    private int[][] ImageMatrix;

    private int nval;
    private int nrow;
    private int ncol;

    private double mean;
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

            file = new FitsFile(filename);
            this.fitsfile = file;

            FitsHDUnit hdu = fitsfile.getHDUnit(0);
            FitsHeader hdr = hdu.getHeader();
            keyword = hdr.getKeywords();

            this.fitsmatrix = (FitsMatrix) hdu.getData();
            this.filename = filename;
            int naxis[] = fitsmatrix.getNaxis();
            this.nval = fitsmatrix.getNoValues();

            if (0 < nval) {
                this.ncol = naxis[0];
                this.nrow = nval / ncol;
            }

            this.ImageArray = new int[this.nval];
            this.ImageMatrix = new int[nrow][ncol];

            try {
                this.fitsmatrix.getIntValues(0, this.nval, ImageArray);
            } catch (FitsException ex) {

            }

            int off, npix;
            off = npix = 0;
            int val;

            for (int nr = 0; nr < nrow; nr++) {
                int data[] = new int[ncol];
                try {
                    this.fitsmatrix.getIntValues(off, this.ncol, data);
                    for (int n = 0; n < ncol; n++) {
                        val = data[n];
                        if (val > max) {
                            this.max = val;
                        }

                        this.mean += val;
                        npix++;
                    }

                } catch (FitsException e) {
                }

                ImageMatrix[nr] = data;
                off += ncol;

            }

            int transpose[][] = new int[this.ncol][this.nrow];

            for (int c = 0; c < this.nrow; c++) {
                for (int d = 0; d < this.ncol; d++) {
                    transpose[d][c] = ImageMatrix[c][d];
                }
            }

            this.ImageMatrix = transpose;

            this.mean = this.mean / npix;

        } catch (IOException ex) {
            Logger.getLogger(StarProcessor.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (FitsException ex) {
            Logger.getLogger(StarProcessor.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getFilename() {
        return filename;
    }

    public FitsFile getFitsfile() {
        return fitsfile;
    }

    public FitsMatrix getFitsmatrix() {
        return fitsmatrix;
    }

    public ListIterator getKeyword() {
        return keyword;
    }

    public int[] getImageArray() {
        return ImageArray;
    }

    public int[][] getImageMatrix() {
        return ImageMatrix;
    }

    public int getNval() {
        return nval;
    }

    public int getNrow() {
        return nrow;
    }

    public int getNcol() {
        return ncol;
    }

    public double getMean() {
        return mean;
    }

    public int getMax() {
        return max;
    }

    public int getValue(int x, int y) {
        return ImageMatrix[x][y];
    }

    /**
     * Imprime por pantalla la representación de la matrix.
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
     * Genera archivo png iamgen.
     */
    public void SaveAsJPG() throws IOException {

        BufferedImage bufferedImage = Matrix2BufferedImage(BufferedImage.TYPE_USHORT_GRAY);
        ImageIO.write(bufferedImage, "png", new File("output.png"));

    }

    public BufferedImage Matrix2BufferedImage(int type) {
        BufferedImage bufferedImage = new BufferedImage(this.ncol, this.nrow, type);
        for (int i = 0; i < this.ncol; i++) {
            for (int j = 0; j < this.nrow; j++) {

                int pixel = ImageMatrix[i][j];
                bufferedImage.setRGB(i, j, pixel);

            }
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

    public void verbose() {

        System.out.println("Max value: " + this.max);
        System.out.println("Mean value: " + this.mean);
        System.out.println("Cols number: " + this.ncol);
        System.out.println("Rows number: " + this.nrow);
        System.out.println("Pixels number: " + this.nval);

    }

}
