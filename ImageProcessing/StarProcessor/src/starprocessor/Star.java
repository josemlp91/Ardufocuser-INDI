/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package starprocessor;

/**
 *
 * @author josemlp
 */
public class Star {

    private int coordx;
    private int coordy;
    private float maxlux;
    private float distanceToNext;
    private Boolean flagFocus;

    public Star() {
        this.coordx = 0;
        this.coordy = 0;
        this.maxlux = 0;
        this.flagFocus = false;

    }

    public Star(int coordx, int coordy, float maxlux, Boolean flagFocus) {
        this.coordx = coordx;
        this.coordy = coordy;
        this.maxlux = maxlux;
        this.flagFocus = flagFocus;
    }

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

    public Boolean isValid() {
        return flagFocus;
    }

    public void unableStar() {
        flagFocus = false;
    }

    @Override
    public String toString() {
        return "Star{" + "coordx=" + coordx + ", coordy=" + coordy + ", maxlux=" + maxlux + '}';
    }

}
