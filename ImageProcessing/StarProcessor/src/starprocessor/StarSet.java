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
package starprocessor;

import static common.StarFilterStatus.FILTER_ALL_PASS;
import static common.StarFilterStatus.FILTER_BY_DISTANCE;
import static common.StarFilterStatus.FILTER_BY_LESS_UMBRAL;
import static common.StarFilterStatus.FILTER_BY_MAGIN;
import static common.StarFilterStatus.FILTER_BY_MORE_UMBRAL;
import static common.StarFilterStatus.FILTER_NOT_APPLY;
import java.util.ArrayList;
import java.util.List;
import static starprocessor.StarProcessor.is_peak_brightness;

/**
 * Clase destinada a modelar un conjunto de estrellas presentes en la misma
 * imagen.
 *
 * @author @josemlp and @zerjillo Proyect Ardufocuser-INDI
 * @version: 22/05/2015/A See {
 * @linktourl https://github.com/josemlp91/Ardufocuser-INDI}
 */
public class StarSet {

    // Array de objetos Estrella
    private List<Star> stars;
    private FitsImage fitsimage;

    
    /** Crea un conjunto inicial de estrellas dado un filtro básico.
     *  Esto no son estrellas ni nada.
     *
     * @param fitsImage
     */
    public StarSet(FitsImage fitsImage) {

        this.fitsimage = fitsImage;
        stars = new ArrayList();

        Star star = null;

        int col = fitsImage.getNcol();
        int row = fitsImage.getNrow();
        double mean = fitsImage.getMean();

        ///////////PARAMETROS/////////////////
        int umbralMin = (int) (mean) * 2;
        int radio = 3;
        int margin = 10;
        /////////////////////////////////////

        System.out.println("Umbral min inicial: " + umbralMin);
        for (int i = 0 + margin; i < (col - margin); i++) {
            for (int j = 0 + margin; j < (row - margin); j++) {
                if (is_peak_brightness(i, j, umbralMin, fitsImage, radio)) {
                    star=new Star();
                    star.inicialize_star(i, j, fitsImage.getValue(i, j), true);
                    star.setStatus(FILTER_NOT_APPLY);
                    stars.add(star);
                }
                
                
            }
        }

    }

    // Cuenta estrellas que pasan los filtros.
    public int size_aceptadas() {
        int count = 0;
        for (int i = 0; i < this.size() - 1; i++) {

            if (this.get(i).isValid()) {
                count++;
            }

        }
        return count;
    }

    // Asigna marco a todo el conjunto de estrellas.
    public void setFrame(int dim) {
        for (Star s : stars) {

            s.setFrameDim(dim);
            s.calculate_star_frame();
        }
    }

    // Calcular numero de estrellas del conjunto.
    public int size() {
        return stars.size();
    }

    // Get objeto Estrella del conjunto dado el indice.
    public Star get(int i) {
        return stars.get(i);
    }

    // Añadimos un objeto estrella al conjunto.
    public void add(Star star) {
        stars.add(star);
    }

    ///////////////////////////////////////////////////////////////////////////
    ////////////*FILTROS APLICABLES AL CONJUNTO DE ESTRELLAS */////////////////
    //////////* Interesantes para aplicar algoritmos de enfoque *//////////////
    ///////////////////////////////////////////////////////////////////////////   
    //OJO:  Los filtros se aplican a todas las estrellas del conjunto.
    // Se puede cambiar a un modo en cascada para optimizar el proceso.
    /*
     * Descarta estrella proximas unas de las otras.
     */
    public void filter_star_by_min_distance(float mindis) {

        Star s, s1;
        ArrayList<Integer> starnear = new ArrayList<Integer>();
        // Por cada estrella del conjunto.
        for (int i = 0; i < stars.size() - 1; i++) {

            // Seleccionamos el resto de estrellas del conjunto 
            starnear = is_star_too_near(i, mindis);

            // Si la distancia es menor a la distancia especificada
            if (starnear.size() > 0) {

                // Invalidamos estrllas para aplicar enfoque
                s = stars.get(starnear.get(0));
                s1 = stars.get(starnear.get(1));
                s.unableStar();
                s1.unableStar();
                // Marcamos estrallas como rechazadas por este filtro.
                s1.setStatus(FILTER_BY_DISTANCE);
                s.setStatus(FILTER_BY_DISTANCE);

            }

        }

    }
    
    

    /*
     * Comprueba si la estrella almacenada en el indice i, esta a una distancia menor de mindis
     * de la estrella actual.
     */
    private ArrayList<Integer> is_star_too_near(int i, float mindis) {
        Star s, s1;
        ArrayList<Integer> starnear = new ArrayList<Integer>();

        float dis;

        s = stars.get(i);
        starnear.add(0, i);
        for (int j = 0; (j < (stars.size() - 1)) && (i != j); j++) {
            s1 = stars.get(j);
            starnear.add(1, j);
            dis = (float) s.calculate_distance_star(s1);
            if (dis < mindis) {
                return starnear;
            }

        }
        starnear.clear();
        return starnear;

    }

    /*
     * Filtra estrella por los umbrales de luminocidad, maximo y minimo.
     */
    public void filter_star_by_initial_umbral(int umbralMinimo, int umbralMaximo) {

        for (Star star : this.stars) {
            if (star.getMaxlux() < umbralMinimo) {

                star.unableStar();
                star.setStatus(FILTER_BY_LESS_UMBRAL);
            }

            if (star.getMaxlux() > umbralMaximo) {

                star.unableStar();
                star.setStatus(FILTER_BY_MORE_UMBRAL);
            }

            //Pasa el segundo  filto por tanto status=2;
        }

    }

    /*
     * Descarta estrella que esta proximas al borde o margen de la imagen.
     */
    public void filter_star_by_margin(int margin, int dimx, int dimy) {

        for (Star star : this.stars) {
            if ((star.getCoordx() < margin) || (star.getCoordx() > dimx - margin)
                    || (star.getCoordy() < margin) || (star.getCoordy() > dimy - margin)) {
                star.unableStar();
                star.setStatus(FILTER_BY_MAGIN);
            } //Pasa el tercer  filto por tanto status=3;

        }
    }
    
    
     /*
     * Descarta estrella no cumplir la heuristica de capas.
     */
    public void filter_star_by_layer(int margin, int dimx, int dimy) {

         //Boolean is_peak_layer(int coorx, int coory, FitsImage img, int nlayer);
         
         
    }
    
 

    public void set_image_submatrix_to_stars() {

        for (Star s : stars) {
            s.setSubMatrixImage(fitsimage.getSubMatrix((int) s.get_strar_frame().getX(), (int) s.get_strar_frame().getY(), s.getFrameDim()));
        }
    }

    /*
     * Imprime el conjunto de estrellas.
     *  USE: Debug
     */
    public void print_star_set() {

        for (Star star : this.stars) {
            if (star.isValid()) {
                System.out.println(star.toString());
            }

        }

    }
    
    
    
     

}
