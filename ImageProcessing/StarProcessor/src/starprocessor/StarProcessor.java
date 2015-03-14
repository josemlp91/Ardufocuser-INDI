/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package starprocessor;

import com.googlecode.javacv.cpp.*;
import static com.googlecode.javacv.cpp.avutil.M_PI;
import static com.googlecode.javacv.cpp.opencv_core.*;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
//import static opencv.simple.ImageUtils.applyBinaryInverted;

import java.util.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eso.fits.*;
import org.eso.fits.Fits;
import org.eso.fits.FitsException;
import org.eso.fits.FitsFile;
import org.eso.fits.FitsMatrix;

import java.util.Vector;

/**
 *
 * @author josemlp
 */
public class StarProcessor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FitsException {

        FitsFile file = openFitsFile("/home/josemlp/pruebasEnfoque/nucleo24800_000.fit");
        FitsMatrix m = getMatrix(file);
        int maxcount = 1000;
        float peaks[][] = new float[maxcount][2];
        peaks = getAllPeak(m, 50, 50, 1000);

        for (int i = 0; i < maxcount && peaks[i][0]>0; i++) {
            System.out.println(peaks[i][0]);
            System.out.println(peaks[i][1]);
            System.out.println(peaks[i][2]);
            System.out.println("-------------------");
        }

    }

    public static float[][] getAllPeak(FitsMatrix dm, int factorPlusMean, int margin, int maxcount) throws FitsException {

        float peaks[][] = new float[maxcount][3];

        int n[] = getDimension(dm);
        float mean = getMean(dm);
        float max = getMaximun(dm);

        int count = 0;
        int umbralMin = (int) (mean) * factorPlusMean;
        int umbralMax = (int) (max - ((max * 5) / 100));

        for (int i = 0; i < (n[0] - margin); i++) {
            for (int j = 0; j < (n[1] - margin); j++) {
                float t = IsPeak(i, j, n[1], dm, umbralMin, umbralMax);

                if ((t > 0) && (count < maxcount)) {
                    peaks[count][0] = t;
                    peaks[count][1] = i;
                    peaks[count][2] = j;
                    count++;

                }

            }
        }

        return peaks;

    }

    public static FitsFile openFitsFile(String FileName) {

        FitsFile file = null;
        try {
            file = new FitsFile(FileName);

        } catch (IOException ex) {
            Logger.getLogger(StarProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FitsException ex) {
            Logger.getLogger(StarProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }

        return file;
    }

    public static void getAllKeywords(FitsFile file) {

        FitsHDUnit hdu = file.getHDUnit(0);
        FitsHeader hdr = hdu.getHeader();

        int noKw = hdr.getNoKeywords();
        int type = hdr.getType();
        int size = (int) hdr.getDataSize();
        System.out.println(" : >" + hdr.getName()
                + "< of type >" + Fits.getType(type)
                + "< with " + noKw + " keywords"
                + " and " + size + " bytes of data");

        ListIterator itr = hdr.getKeywords();
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

    public static FitsMatrix getMatrix(FitsFile file) {

        FitsHDUnit hdu = file.getHDUnit(0);
        FitsMatrix dm = (FitsMatrix) hdu.getData();
        return dm;
    }

    public static int[] getDimension(FitsMatrix dm) {

        int dimension[] = new int[2];
        int naxis[] = dm.getNaxis();
        int nval = dm.getNoValues();
        double mean = 0.0;
        if (0 < nval) {
            int ncol = naxis[0];
            int nrow = nval / ncol;

            dimension[0] = ncol;
            dimension[1] = nrow;
        }

        return dimension;

    }

    public static float IsPeak(int coorx, int coory, int dim, FitsMatrix dm, float umbralMinimo, float umbralMaximo) throws FitsException {

        //float umbralMinimo, float umbralMaximo) throws FitsException {
        //   0  | 1 | 2   //
        //   3  | p | 4   //
        //   5  | 6 | 7   //
        float p[] = new float[1];

        float peak = 0;

        //Adyacentes.
        float ady[][] = new float[8][1];

        dm.getFloatValues(((coory) * (dim)) + coorx, 1, p);

        dm.getFloatValues((coorx - 1) + ((coory - 1) * dim), 1, ady[0]);
        dm.getFloatValues((coorx) + ((coory - 1) * dim), 1, ady[1]);
        dm.getFloatValues((coorx + 1) + ((coory - 1) * dim), 1, ady[2]);

        dm.getFloatValues((coorx - 1) + ((coory) * dim), 1, ady[3]);
        dm.getFloatValues((coorx + 1) + ((coory) * dim), 1, ady[4]);

        dm.getFloatValues((coorx - 1) + ((coory + 1) * dim), 1, ady[5]);
        dm.getFloatValues((coorx) + ((coory + 1) * dim), 1, ady[6]);
        dm.getFloatValues((coorx + 1) + ((coory + 1) * dim), 1, ady[7]);

        for (int i = 0; i < 8; i++) {
            if (p[0] < ady[i][0]) {
                peak = -1;
                break;
            }
        }

        if (peak != -1) {

            if ((p[0] > umbralMinimo) && (p[0] < umbralMaximo)) {
                peak = p[0];

            } else {
                peak = 0;
            }
        }

        return peak;

    }

    public static float getMaximun(FitsMatrix dm) {
        int naxis[] = dm.getNaxis();
        int off, npix;
        int nval = dm.getNoValues();
        double max = 0.0;
        if (0 < nval) {
            int ncol = naxis[0];
            int nrow = nval / ncol;

            float data[] = new float[ncol];
            double val;
            off = npix = 0;

            for (int nr = 0; nr < nrow; nr++) {
                try {
                    dm.getFloatValues(off, ncol, data);
                    for (int n = 0; n < ncol; n++) {
                        val = data[n];

                        if (val > max) {
                            max = val;
                        }

                        npix++;
                    }
                } catch (FitsException e) {
                }

                off += ncol;

            }
        }
        return (float) max;

    }

    public static float getMean(FitsMatrix dm) {

        int naxis[] = dm.getNaxis();
        int off, npix;
        int nval = dm.getNoValues();
        double mean = 0.0;
        if (0 < nval) {
            int ncol = naxis[0];
            int nrow = nval / ncol;
            int i = 0;
            float data[] = new float[ncol];

            double val;

            off = npix = 0;

            for (int nr = 0; nr < nrow; nr++) {
                try {

                    dm.getFloatValues(off, ncol, data);
                    for (int n = 0; n < ncol; n++) {
                        val = data[n];
                        mean += val;
                        npix++;
                    }
                } catch (FitsException e) {
                }

                off += ncol;

            }
            mean = mean / npix;

        }
        return (float) mean;

    }

}
