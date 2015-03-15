/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package starprocessor;

//import static opencv.simple.ImageUtils.applyBinaryInverted;
import java.util.*;
import java.io.*;
import java.lang.Math.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eso.fits.*;

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

        List<Star> stars = new ArrayList();
        stars = getAllPeak(m, 10, 50);
        stars=filterStarByMinDistance(stars, 5);
        printStarList(stars);

    }

    public static void printStarList(List<Star> stars) {

        for (Star star : stars) {
            if (star.isValid()) {
                System.out.println(star.toString());
            }

        }

    }

    public static List<Star> filterStarByMinDistance(List<Star> stars, float mindis) {

        Star s, s1;
        float dis;

        for (int i = 0; i < stars.size()-1; i++) {
            s = stars.get(i);

            for (int j = 0; j < stars.size()-1; j++) {
                if (i == j) {
                    j++;
                }
                s1 = stars.get(j);
                dis = calculateDistanceStar(s, s1);
                //System.out.println(dis);

                if (dis < mindis) {
                    s.unableStar();
                    s1.unableStar();
                }

            }

        }

        return stars;

    }

    public static float calculateDistanceStar(Star s1, Star s2) {

        int h = Math.abs(s1.getCoordy() - s2.getCoordy());
        int l = Math.abs(s1.getCoordx() - s2.getCoordx());
        float d = (int) Math.sqrt(Math.pow(h, 2) + Math.pow(l, 2));

        return d;

    }

    public static float getValue(int x, int y, FitsMatrix dm) throws FitsException {

        float p[] = new float[1];
        int n[] = getDimension(dm);
        float val;
        int dim = n[0];

        dm.getFloatValues(((y) * (dim)) + x, 1, p);
        val = p[0];
        return val;

    }

    public static List getAllPeak(FitsMatrix dm, int factorPlusMean, int margin) throws FitsException {

        List<Star> stars = new ArrayList();
        Star star;

        int n[] = getDimension(dm);

        int col = n[0];
        int row = n[1];

        float mean = getMean(dm);
        float max = getMaximun(dm);

        int umbralMin = (int) (mean) * factorPlusMean;
        int umbralMax = (int) (max - ((max * 10) / 100));
        
        System.out.println("Umbral min: " + umbralMin);
        System.out.println("Umbral max: " + umbralMax);
        //To test
        //int umbralMin = (int) max - 100;
        //int umbralMax = (int) max + 100;

        for (int i = 0 + margin; i < (col - margin); i++) {
            for (int j = 0 + margin; j < (row - margin); j++) {
                star = IsPeak(i, j, col, dm, umbralMin, umbralMax);

                if (star.isValid()) {
                    //System.out.println(star.toString());
                    stars.add(star);
                }

            }
        }

        return stars;

    }

    public static FitsFile openFitsFile(String FileName) {

        FitsFile file = null;
        try {
            file = new FitsFile(FileName);

        } catch (IOException ex) {
            Logger.getLogger(StarProcessor.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (FitsException ex) {
            Logger.getLogger(StarProcessor.class
                    .getName()).log(Level.SEVERE, null, ex);
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

    public static Star IsPeak(int coorx, int coory, int dim, FitsMatrix dm, float umbralMinimo, float umbralMaximo) throws FitsException {

        //   0  | 1 | 2   //
        //   3  | p | 4   //
        //   5  | 6 | 7   //
        float p[] = new float[1];
        Boolean isPeak = true;
        Star star = new Star();
        float lux;

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

        lux = p[0];

        for (int i = 0; i < 8; i++) {
            if (lux < ady[i][0]) {
                isPeak = false;
                break;
            }
        }

        if (isPeak) {
            if ((lux > umbralMinimo) && (lux < umbralMaximo)) {
                star.InicializeStar(coorx, coory, lux, true);
            }
        }

        return star;
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
