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
 * @version: 22/05/2015/A See {
 * @linktourl https://github.com/josemlp91/Ardufocuser-INDI}
 */
package starprocessor;

import common.StarFilterStatus;
import static common.StarFilterStatus.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Star {

    private int fram_dim;

    // Coordenadas del plano 2D donde se localizan la estrella.
    private Point2D.Double coord;
    private Rectangle2D.Double frame;
    private int[][] SubmatrixImage;

    // Nivel de luminosidad maxima que alcanza la estrella.
    private float maxlux;

    // Campo que indica si la estrella es válida para realizar el enfoque. 
    private Boolean flagFocus;

    // Campo que indica el paso del procesamiento donde la estrella ha sido descartada para usarse en el enfoque.
    //Valores válidos:
    //  0 : Estrella no inicializada.
    //  1 : No pasa filtro  .... (x)
    //  2 : No pasa filtro  .... (y)
    //  3 : No pasa filtro  .....(z)
    private StarFilterStatus status;

    // Campo de texto auxiliar donde podemos asignarle un comentario
    // o el nombre propio de la estrella si es conocido.
    private String message;

    // Distancia a la estrella más próxima.
    private float distanceToNext;

    /**
     * Constructor por defecto que inicializa la estructura.
     */
    public Star() {
        this.coord = new Point2D.Double();
        this.maxlux = 0;
        this.flagFocus = false;
        this.status = FILTER_NOT_VALID;
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
        this.coord = new Point2D.Double();

        this.coord.setLocation(coordx, coordy);
        calculate_star_frame();
        this.maxlux = maxlux;
        this.flagFocus = flagFocus;
        this.status = FILTER_NOT_VALID;
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
    public void inicialize_star(int coordx, int coordy, float maxlux, Boolean flagFocus) {

        this.coord.setLocation(coordx, coordy);
        calculate_star_frame();
        this.maxlux = maxlux;
        this.flagFocus = flagFocus;
        this.status = FILTER_ALL_PASS;
    }

    public void setSubMatrixImage(int[][] img) {
        this.SubmatrixImage = img;

    }

    // Asigna la dimension del marco de la estrella.
    public void setFrameDim(int dim) {
        this.fram_dim = dim;
    }

    // Dada la dimension genera el marco de la estrella.
    public void calculate_star_frame() {
        this.frame = new Rectangle2D.Double();
        this.frame.setFrameFromCenter(this.coord.getX(), this.coord.getY(), this.coord.getX() + this.fram_dim, this.coord.getY() + this.fram_dim);
    }

    // Obtener el marco.
    public Rectangle2D.Double get_strar_frame() {
        return this.frame;
    }

    //////////////
    /* GETTERS */
    /////////////
    public double getCoordx() {
        return coord.getX();

    }

    public double getCoordy() {
        return coord.getY();

    }

    public int getFrameDim() {
        return this.fram_dim;
    }

    public Point2D.Double getCoord() {

        return this.coord;
    }

    public float getMaxlux() {
        return maxlux;
    }

    public StarFilterStatus getStatus() {
        return status;
    }

    //////////////
    /* SETTERS */
    /////////////
    public void setCoord(double coordx, double coordy) {

        this.coord.setLocation(coordx, coordy);
        calculate_star_frame();
    }

    public void setMaxlux(float maxlux) {
        this.maxlux = maxlux;
    }

    public void setStatus(StarFilterStatus s) {
        this.status = s;
    }

    /**
     * Indica si la estrella es seleccionada para aplicar rutinas de enfoque.
     * @return Booleano si la estrella es útil para enfoque.
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
     * Calcula distancia euclidea a otra estrella, dadas sus coordenadas
     *
     * @param Star estrella a calculara la distancia.
     * @return distancia
     */
    public double calculate_distance_star(Star s) {
        double d = this.coord.distance(s.coord);
        return d;

    }

    public void calculateSubMatrixImage() {

    }

    /**
     * Escribe por pantalla la estructura estrella en un formato legible. USE:
     * Debug.
     */
    @Override
    public String toString() {
        return "Star{" + "coordx=" + coord.getX() + ", coordy=" + coord.getY() + ", maxlux=" + maxlux + '}';
    }

}
