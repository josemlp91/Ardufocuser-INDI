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

import static common.StarFilterStatus.FILTER_BY_DISTANCE;
import static common.StarFilterStatus.FILTER_BY_LESS_UMBRAL;
import static common.StarFilterStatus.FILTER_BY_MAGIN;
import static common.StarFilterStatus.FILTER_BY_MORE_UMBRAL;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

/**
 * Clase destinada a modelar un conjunto de estrellas presentes en la misma
 * imagen.
 *
 * @author @josemlp and @zerjillo Proyect Ardufocuser-INDI
 * @version: 22/05/2015/A See {
 * @linktourl https://github.com/josemlp91/Ardufocuser-INDI}
 */
public class StarSet {

    // Array de objetos Estrell
    private List<Star> stars;

    // Constructor por defecto.
    public StarSet() {
        stars = new ArrayList();
    }

    public int size_aceptadas() {
        int count = 0;
        for (int i = 0; i < this.size() - 1; i++) {

            if (this.get(i).isValid()) {
                count++;
            }

        }
        return count;
    }

    public void setFrame(int dim) {
        for (Star s : stars) {
            
            s.setFrameDim(dim);
            s.CalculateStarFrame();
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
    /**
     * Constructor general.
     *
     * @param misdis distancia mínima entre estrellas.
     * @param coordy coordenada en el eje de ordenadas.
     * @param maxlux valor de luminocidad máximo.
     * @param flagFocus indica si la estrella es interesante para ejecutar
     * rutinas de enfoque.
     */
    public void filterStarByMinDistance(float mindis) {

        Star s, s1;
        float dis;
        float ndis;
        Boolean isnear;
        ArrayList<Integer> starnear = new ArrayList<Integer>();
        // Por cada estrella del conjunto.
        for (int i = 0; i < stars.size() - 1; i++) {

            // Seleccionamos el resto de estrellas del conjunto 
            starnear = IsStarTooNear(i, mindis);

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

    private ArrayList<Integer> IsStarTooNear(int i, float mindis) {
        Star s, s1;
        ArrayList<Integer> starnear = new ArrayList<Integer>();

        float dis;

        s = stars.get(i);
        starnear.add(0, i);
        for (int j = 0; (j < (stars.size() - 1)) && (i != j); j++) {
            s1 = stars.get(j);
            starnear.add(1, j);
            dis = (float) s.calculateDistanceStar(s1);
            if (dis < mindis) {
                return starnear;
            }

        }
        starnear.clear();
        return starnear;

    }

    public void filterStarByInitialUmbral(int umbralMinimo, int umbralMaximo) {

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

    public void filterStarByMargin(int margin, int dimx, int dimy) {

        for (Star star : this.stars) {
            if ((star.getCoordx() < margin) || (star.getCoordx() > dimx - margin)
                    || (star.getCoordy() < margin) || (star.getCoordy() > dimy - margin)) {
                star.unableStar();
                star.setStatus(FILTER_BY_MAGIN);
            } //Pasa el tercer  filto por tanto status=3;

        }
    }

    public void printStarSet() {

        for (Star star : this.stars) {
            if (star.isValid()) {
                System.out.println(star.toString());
            }

        }

    }

}
