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
 * Clase destinada a modelar los objetos Estrellas.
 *
 * @author @josemlp and @zerjillo Proyect Ardufocuser-INDI
 * @version: 22/03/2015/A See {
 * @linktourl https://github.com/josemlp91/Ardufocuser-INDI}
 */
package starprocessor;

public class Star {

    private int coordx;
    private int coordy;
    private float maxlux;
    private float distanceToNext;
    private Boolean flagFocus;

    /**
     * Constructor por defecto que inicializa la estructura.
     *
     */
    public Star() {
        this.coordx = 0;
        this.coordy = 0;
        this.maxlux = 0;
        this.flagFocus = false;

    }

    /**
     * Constructor general.
     *
     * @param coordx coordenada en el eje de abscisas
     * @param coordy coordenada en el eje de ordenadas.
     * @param maxlux valor de luminocidad máximo.
     * @param flagFocus flag que indica si la estrella es útil para ejecutar
     * rutinas de enfoque.
     */
    public Star(int coordx, int coordy, float maxlux, Boolean flagFocus) {
        this.coordx = coordx;
        this.coordy = coordy;
        this.maxlux = maxlux;
        this.flagFocus = flagFocus;
    }

    /**
     * Inicializa la estructura.
     *
     * @param coordx coordenada en el eje de abscisas
     * @param coordy coordenada en el eje de ordenadas.
     * @param maxlux valor de luminocidad máximo.
     * @param flagFocus flag que indica si la estrella es útil para ejecutar
     * rutinas de enfoque.
     */
    public void InicializeStar(int coordx, int coordy, float maxlux, Boolean flagFocus) {
        this.coordx = coordx;
        this.coordy = coordy;
        this.maxlux = maxlux;
        this.flagFocus = flagFocus;
    }

    public int getCoordx() {
        return coordx;
    }

    public int getCoordy() {
        return coordy;
    }

    public float getMaxlux() {
        return maxlux;
    }

    public void setCoordx(int coordx) {
        this.coordx = coordx;
    }

    public void setCoordy(int coordy) {
        this.coordy = coordy;
    }

    public void setMaxlux(float maxlux) {
        this.maxlux = maxlux;
    }

    /**
     * Indica si la estrella es útil para aplicar rutinas de enfoque.
     *
     * @return Booleano si la estrella es útil para enfoque.
     *
     */
    public Boolean isValid() {
        return flagFocus;
    }

    /**
     * Invalida estrella para aplicar rutinas de enfoque.
     */
    public void unableStar() {
        flagFocus = false;
    }

    /**
     * Calcula distancia euclidea a otra estrella.
     *
     * @param Star estrella a calculara la distancia.
     * @return distancia
     */
    public float calculateDistanceStar(Star s) {

        int h = Math.abs(this.getCoordy() - s.getCoordy());
        int l = Math.abs(this.getCoordx() - s.getCoordx());
        float d = (int) Math.sqrt(Math.pow(h, 2) + Math.pow(l, 2));

        return d;

    }

    /**
     * Escribe por pantalla la estructura estrella en un formato legible.
     */
    @Override
    public String toString() {
        return "Star{" + "coordx=" + coordx + ", coordy=" + coordy + ", maxlux=" + maxlux + '}';
    }

}
