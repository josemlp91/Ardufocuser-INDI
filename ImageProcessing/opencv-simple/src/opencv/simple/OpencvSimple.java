/*
 * Copyright (C) 2015  @Ardufocuser-INDI 
 * José Miguel López.  @josemlp91

 * Clase destinada al procesaminto básico de imagenes astronomicas,
 * haciendo uso de JavaCv. (opencv)

 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package opencv.simple;

import com.googlecode.javacv.cpp.*;
import static com.googlecode.javacv.cpp.avutil.M_PI;
import static com.googlecode.javacv.cpp.opencv_core.*;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 *
 * @author josemlp
 */
public class OpencvSimple {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        CvMat img = cvLoadImageM("/home/josemlp/NetBeansProjects/opencv-simple/img/potw1346a.tif");
        cvShowImage("Telescopio", img);

        double fwhm = fwmh(img);
        System.out.println(fwhm);

        cvWaitKey();

    }

    /**
     * Computes the luminosity of an rgb value by one standard formula.
     *
     * @param r	red value (0-255)
     * @param g	green value (0-255)
     * @param b	blue value (0-255)
     * @return	luminosity (0-255)
     */
    private static double luminosity(double r, double g, double b) {
        return 0.299 * r + 0.587 * g + 0.114 * b;
    }

    /*
     * p is a three- or four-component array, list, or tuple:
     *
     *    y =  [p3 +] p0/(p1*sqrt(2pi)) * exp(-(x-p2)**2 / (2*p1**2))

     *    p[0] -- Area of the gaussian
     *    p[1] -- one-sigma dispersion
     *    p[2] -- central offset (mean location)
     *    p[3] -- optional constant, vertical offset

     *    NOTE: FWHM = 2*sqrt(2*ln(2)) * p1  ~ 2.3548*p1
     */
    private static double fwmh(CvMat img) {

        double npx = img.rows() * img.cols();
        double mean = 0;
        double x2_sum = 0;
        double fwhm_value = 0;

        //Calculamos 
        for (int i = 0; i < img.rows(); i++) {
            for (int j = 0; j < img.cols(); j++) {
                double gray = luminosity(img.get(i, j, 2), img.get(i, j, 1), img.get(i, j, 0));
                mean += gray;
            }
        }

        mean /= npx;
        for (int i = 0; i < img.rows(); i++) {
            for (int j = 0; j < img.cols(); j++) {
                double gray = luminosity(img.get(i, j, 2), img.get(i, j, 1), img.get(i, j, 0));
                x2_sum += pow((double) gray - mean, 2);
            }
        }

        x2_sum /= (double) npx;

        //FWHM = 2*sqrt(2*ln(2)) * p ~ 2.3548*p1
        fwhm_value = sqrt(x2_sum) * 2.3548;
        fwhm_value = sqrt(fwhm_value / M_PI) * 2;

        return fwhm_value;

    }

    private opencv_imgproc.CvHistogram generateHistogram(IplImage imagen) {

        IplImage mascara = new IplImage(null);
        int dimensiones = 1;
        int numBins = 256;
        float minRange = 0.0f;
        float maxRange = 255.0f;

        int[] tamanios = {numBins};
        int tipoHistograma = opencv_imgproc.CV_HIST_ARRAY;
        float[][] rango = {{minRange, maxRange}};
        opencv_imgproc.CvHistogram hist
                = opencv_imgproc.cvCreateHist(dimensiones, tamanios, tipoHistograma, rango, 1);

        int accumulate = 0;
        IplImage[] arregloImagen = {imagen.clone()};
        opencv_imgproc.cvCalcHist(arregloImagen, hist, accumulate, mascara);

        return hist;

    }

    private IplImage DrawHistogram(CvHistogram hist, IplImage image) {//draw histogram
        int scaleX = 1;
        int scaleY = 1;
        int i;
        float[] max_value = {0};
        int[] int_value = {0};
        cvGetMinMaxHistValue(hist, max_value, max_value, int_value, int_value);//get min and max value for histogram

        IplImage imgHist = cvCreateImage(cvSize(256, image.height()), IPL_DEPTH_8U, 1);//create image to store histogram
        cvZero(imgHist);
        CvPoint pts = new CvPoint(5);

        for (i = 0; i < 256; i++) {//draw the histogram 
            float value = opencv_legacy.cvQueryHistValue_1D(hist, i);
            float nextValue = opencv_legacy.cvQueryHistValue_1D(hist, i + 1);

            pts.position(0).x(i * scaleX).y(image.height() * scaleY);
            pts.position(1).x(i * scaleX + scaleX).y(image.height() * scaleY);
            pts.position(2).x(i * scaleX + scaleX).y((int) ((image.height() - nextValue * image.height() / max_value[0]) * scaleY));
            pts.position(3).x(i * scaleX).y((int) ((image.height() - value * image.height() / max_value[0]) * scaleY));
            pts.position(4).x(i * scaleX).y(image.height() * scaleY);
            cvFillConvexPoly(imgHist, pts.position(0), 5, CvScalar.RED, CV_AA, 0);
        }
        return imgHist;
    }

}
