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
 * Clase destinada a procesar imagenes Astronomicas, orientada a la extracción y
 * enfoque de estrellas.
 *
 * @author @josemlp and @zerjillo Proyect Ardufocuser-INDI
 * @version: 22/03/2015/A See {
 * @linktourl https://github.com/josemlp91/Ardufocuser-INDI}
 */
package starprocessor;

import java.util.*;
import java.io.*;
import org.eso.fits.*;


/**
 * 
 * @author josemlp
 * Procesador de estrellas para imagenes FITS
 * 
 * Métodos especificos de procesamiento de estrellas (localización, calculo distancias, luminocidad etc)
 * sobre imagenes FITS.
 * 
 */
public class StarProcessor {


    /**
     * Busca estrellas, mediante una heuristica basada en puntos elevados en un
     * conjuntos de puntos vecinos.
     *
     * @param fittsImage Imagen donde realizar la busqueda.
     * @param margin margen prudencial donde buscar picos estrellas.
     * @return conjunto de estrellas encontradas.
     *
     */
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

        for (int i = 0 + margin; i < (col - margin); i++) {
            for (int j = 0 + margin; j < (row - margin) ; j++) {
                star = IsPeak(i, j, umbralMin, fitsImage);

                if (star.isValid()) {
                    //System.out.println(star.toString());
                    stars.add(star);
                }

            }
        }

        return stars;

    }

    /**
     * Compruba si un pixel cumple la condicioón de sobresalir sobre sus
     * adyacentes y sobre un umbral mínimo inical.
     *
     * @param coorx Coordenada x.
     * @param coory Coordenada y .
     * @param minUmbral Umbral minimo.
     * @return Estrella en caso de cumplir las condiciones descritas.
     *
     */
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
