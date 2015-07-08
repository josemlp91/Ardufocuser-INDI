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

import static common.StarFilterStatus.FILTER_ALL_PASS;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 *
 * Métodos especificos de procesamiento de estrellas (localización, calculo
 * distancias, luminocidad etc) sobre imagenes FITS.
 *
 * Para optimizar y librar el procedimiento de gran carga computacional, vamos
 * aplicando filtro desde un grano grueso a un grano mucho más fino
 *
 */
public class StarProcessor {

    /**
     * Busca estrellas, mediante una heuristica definida.
     *
     *
     *
     * @param fitsImage
     * @param margin margen prudencial donde buscar picos estrellas.
     * @return conjunto de estrellas encontradas.
     *
     */
    public static StarSet get_all_peak(FitsImage fitsImage, int margin) {

        StarSet stars = new StarSet(fitsImage);
        Star star;

        int col = fitsImage.getNcol();
        int row = fitsImage.getNrow();

        double mean = fitsImage.getMean();

        int umbralMin = (int) (mean) * 2;
        stars=find_star_by_brightness(fitsImage,  margin);

       
        return stars;

    }

    /**
     * Busca estrellas, mediante una heuristica basada en puntos elevados en un
     * conjuntos de puntos vecinos.
     *
     * @param fitsImage
     * @param margin margen prudencial donde buscar picos estrellas.
     * @return conjunto de estrellas encontradas.
     *
     */
    public static StarSet find_star_by_brightness(FitsImage fitsImage, int margin) {

        StarSet stars = new StarSet(fitsImage);
        Star star = null;

        int col = fitsImage.getNcol();
        int row = fitsImage.getNrow();
        double mean = fitsImage.getMean();

        ///////////PARAMETROS/////////////////
        int umbralMin = (int) (mean) * 2;
        int radio = 3;
        /////////////////////////////////////

        System.out.println("Umbral min inicial: " + umbralMin);
        for (int i = 0 + margin; i < (col - margin); i++) {
            for (int j = 0 + margin; j < (row - margin); j++) {
                if (is_peak_brightness(i, j, umbralMin, fitsImage, radio)) {
                    star.inicialize_star(i, j, fitsImage.getValue(i, j), true);
                }
                star.setStatus(FILTER_ALL_PASS);
                stars.add(star);
            }
        }

        return stars;

    }

    /**
     * Filtro de grano grueso.
     *
     * Compruba si un pixel cumple la condicioón de sobresalir sobre los (radio)
     * de sus adyacentes y sobre un umbral mínimo inical.
     *
     * @param coorx Coordenada x.
     * @param coory Coordenada y.
     * @param radio radio de los adyacentes.
     * @param img
     * @param minUmbral Umbral minimo.
     * @return Boolean true en caso de cumplir las condiciones descritas.
     *
     */
    public static Boolean is_peak_brightness(int coorx, int coory, int minUmbral, FitsImage img, int radio) {

        Boolean isPeak = true;
        int p = img.getValue(coorx, coory);

        for (int x = coorx - radio; x <= coorx + radio; x++) {
            for (int y = coory - radio; y <= coory + radio; y++) {
                if ((x != coorx) && (y != coory)) {
                    if ((p < img.getValue(x, y)) || (img.getValue(x, y) < minUmbral)) {
                        isPeak = false;
                        break;

                    }

                }

            }

        }

        return isPeak;
    }

    /**
     * Filtro de grano fino.
     *
     * Compruba si la media de la luminosidad de una capa de la estrella
     * sobresale de su capa adyacente externa, con una varianza x.
     *
     * @param coorx Coordenada x.
     * @param coory Coordenada y.
     * @param img
     * @param nlayer numero da capas hasta donde profundizar..
     * @return Estrella en caso de cumplir las condiciones descritas.
     *
     */
    public static Boolean is_peak_layer(int coorx, int coory, FitsImage img, int nlayer) {
        HashMap<Integer, List<Integer>> d = get_brightness_by_layer(coorx, coory, img, nlayer);

        
        
        
        //// TODO ////
        // Calcular media de cada layer
        // Calcular varianza (como la media pero de los cuadrados de los valores)
        // Desviación estándar raiz cuadrada de la varianza.
        System.out.println(d);

        /////////////////////
        Star a = new Star();
        return true;

    }

    /**
     *
     * @param coorx Coordenada x, del punto de interes. (punto central de la
     * estrella.)
     * @param coory Coordenada y, del punto de interes. (punto central de la
     * estrella.)
     * @param img Matriz principal de la imagen.
     * @param nlayer Numero de capas a procesar.
     * @return
     */
    public static HashMap<Integer, List<Integer>> get_brightness_by_layer(int coorx, int coory, FitsImage img, int nlayer) {

        //calular distancias a los pixeles hasta llegar a n capas.
        //renorna un array.
        int[][] sub = new int[nlayer][nlayer];
        ArrayList<Double> distances = new ArrayList<Double>();
        
        System.out.println("xxxxxxxxxxxxxxxxxxxxx");
        System.out.println(img.getValue(coorx, coory));

        
        
        ////////////////////////////////////////////////////////////////////////////////
        // Extraemos submatrix con el cuadro de la estrella.
        ////////////////////////////////////////////////////////////////////////////////
        sub = img.getSubMatrix((int) ( coory - nlayer / 2), ( coorx - nlayer / 2), nlayer);

        // Calculamos coordenadas del centro, sabiendo que deben estar en la mitad de los dos ejes.
        int newcordx = (int) nlayer / 2;
        int newcordy = (int) nlayer / 2;

         System.out.println("xxxxxxxxxxxxxxxxxxxxx");
        System.out.println(sub[newcordx][newcordy]);
         System.out.println("xxxxxxxxxxxxxxxxxxxxx");
        
        
        // Almacenaremos las luminocidades organizadas por capas.
        HashMap<Integer, List<Integer>> layer_lux = new HashMap<Integer, List<Integer>>();

        // Precalculamos la distancia de los puntos de cada capa, 
        distances = precalculate_distance_to_layer_pixels(nlayer, sub);

        // Debe coincidir los valores de las coordenadas reales en la matrix original,
        // y las coordenadas en la matrix convertida.
        int position_into_dintancias = 0;

        /// Recorremos submatriz creada anteriormente.
        for (int i = 0; i < nlayer; i++) {
            for (int j = 0; j < nlayer; j++) {
                // Para el pixel actual calculamos la distancia euclidea respecto al punto central.
                double d = pixel_distance(newcordx, newcordy, i, j);
                position_into_dintancias = distances.indexOf(d);

                // Si la distancia calculada coincide con alguna de las precomputadas anteriormente.
                if (position_into_dintancias != -1) {

                    System.out.print(position_into_dintancias + " ");
                    System.out.println(sub[i][j]);

                    // Nos creamos un HashMap donde la clave es la capa de la estrella y
                    // el valor es un array con las luminocidades de los pixeles de dicha capa.
                    if (layer_lux.containsKey(position_into_dintancias)) {
                        layer_lux.get(position_into_dintancias).add(sub[i][j]);

                    } else {
                        List<Integer> positionList = new ArrayList<>();
                        positionList.add(sub[i][j]);
                        layer_lux.put(position_into_dintancias, positionList);

                    }

                }

            }
        }
        // Retornamos el HashMap
        //System.out.println(layer_lux);
        return layer_lux;

    }

    /**
     * Calcula la distancia euclidea de dos pixeles.
     *
     * @param p1x coordenada x del pixel 1
     * @param p1y coordenada y del pixel 1
     * @param p2x coordenada x del pixel 2
     * @param p2y coordenada y del pixel 2
     * @return dis distancia entre los dos pixeles.
     *
     */
    public static double pixel_distance(int p1x, int p1y, int p2x, int p2y) {

        int cat1 = Math.abs(p1x - p2x);
        int cat2 = Math.abs(p1y - p2y);
        double dis = Math.sqrt((Math.pow(cat1, 2) + Math.pow(cat2, 2)));
        return dis;
    }

    /**
     * Hacemos un precalculo de la distancia de las capas que forman la
     * estrella. Basandonos en la distancia euclidea, de cada uno de los puntos
     * del cuadro de la imagen.
     *
     * @param nlayer numero de capas con las que trabajamos.
     * @param img matriz de la imagen.
     * @return dis, array ordenado con las posibles distancias a los pixeles de
     * la imagen.
     *
     */
    public static ArrayList<Double> precalculate_distance_to_layer_pixels(int nlayer, int[][] img) {

        ArrayList<Double> dis = new ArrayList<Double>();

        Double d = 0.0;
        int centercordx = (int) nlayer / 2;
        int centercordy = (int) nlayer / 2;

        for (int i = 0; i <= nlayer; i++) {
            for (int j = 0; j <= nlayer; j++) {
                d = pixel_distance(centercordx, centercordy, i, j);
                if (dis.indexOf(d) == -1) {
                    dis.add(d);
                }
            }
        }
        // Ordenamos el array de distancias.
        Collections.sort(dis);
        System.out.println(dis);
        return dis;
    }

    /**
     * Funcion deprecated, primera version.
     *
     * Filtro de grano grueso.
     *
     * Compruba si un pixel cumple la condicioón de sobresalir sobre sus
     * adyacentes directos.
     *
     * @param coorx Coordenada x.
     * @param coory Coordenada y.
     * @param minUmbral Umbral minimo.
     * @param img
     * @return Estrella en caso de cumplir las condiciones descritas.
     *
     */
    public static Boolean is_peak_brightness_old(int coorx, int coory, int minUmbral, FitsImage img) {

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
        return isPeak;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////TODO////////////////////////////////////////////////////////////
    //calculate means of stars
    public double CalculateMeansOfSquare(FitsImage fitsImage, int coordX, int coordY, int dim) {
        int[][] SubMatrix = fitsImage.getSubMatrix(coordX, coordY, dim);

        /**
         * TODO
         */
        return (int) 0.1;
    }

    //calculate fwhm of stars
    public double CalculateFWHMOfSquare() {
        return (int) 0.1;
    }

}
