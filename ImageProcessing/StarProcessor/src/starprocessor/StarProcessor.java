/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package starprocessor;

//import static opencv.simple.ImageUtils.applyBinaryInverted;
import java.util.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eso.fits.*;

/**
 *
 * @author josemlp
 */
public class StarProcessor {

    private static Object star;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FitsException {


        FitsImage img = new FitsImage("/home/josemlp/pruebasEnfoque/nucleo24910_111.fit");
        //img.showKeyword();
        //img.printImageMatrix();

        double means = img.getMean();
        int max = img.getMax();

        int umbralMin = (int) means * 2;
        int umbralMax = max - (max * 20) / 100;

        StarSet stars = new StarSet();
        stars = getAllPeak(img, 50);

        stars.filterStarByInitialUmbral(umbralMin, umbralMax);
        stars.filterStarByMinDistance(10);

        stars.printStarSet();
    }

  
    public static StarSet getAllPeak(FitsImage fitsImage, int margin) throws FitsException {

        StarSet stars = new StarSet();
        Star star;

        int col = fitsImage.getNcol();
        int row = fitsImage.getNrow();

        double mean = fitsImage.getMean();

        int umbralMin = (int) (mean) * 2;

        //To test
        //int umbralMin = (int) max - 100;
       
        System.out.println("Umbral min inicial: " + umbralMin);

        
        for (int i = 0 + margin; i < (row - margin); i++) {
            for (int j = 0 + margin; j < (col - margin); j++) {
                star = IsPeak(i, j, umbralMin, fitsImage);

                if (star.isValid()) {
                    //System.out.println(star.toString());
                    stars.add(star);
                }

            }
        }

        return stars;

    }

    public static Star IsPeak(int coorx, int coory, int minUmbral, FitsImage img) {

        //   0  | 1 | 2   //
        //   3  | p | 4   //
        //   5  | 6 | 7   //
        Boolean isPeak = true;
        Star star = new Star();

        //Adyacentes.
        int ady[] = new int[8];

        int p = img.getValue(coorx, coory);

        if (p > minUmbral) {

            ady[0] = img.getValue(coorx - 1, coory - 1);
            ady[1] = img.getValue(coorx, coory - 1);
            ady[2] = img.getValue(coorx + 1, coory - 1);

            ady[3] = img.getValue(coorx - 1, coory);
            ady[4] = img.getValue(coorx + 1, coory);

            ady[5] = img.getValue(coorx - 1, coory + 1);
            ady[6] = img.getValue(coorx, coory + 1);
            ady[7] = img.getValue(coorx + 1, coory + 1);

            for (int i = 0; i < 8; i++) {
                if (p < ady[i]) {
                    isPeak = false;
                    break;
                }
            }

        }

        if (isPeak) {
            if (p > minUmbral) {
                star.InicializeStar(coorx, coory, p, true);
            }
        }

        return star;
    }

   

}
