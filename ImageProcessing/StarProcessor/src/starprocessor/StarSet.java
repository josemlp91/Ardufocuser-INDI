/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package starprocessor;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author josemlp
 */
public class StarSet {

    private List<Star> stars;

    public StarSet() {
        stars = new ArrayList();

    }

    public int size() {
        return stars.size();
    }

    public void printStarSet() {

        for (Star star : this.stars) {
            if (star.isValid()) {
                System.out.println(star.toString());
            }

        }

    }

    public void add(Star star) {

        stars.add(star);
    }

    public void filterStarByMinDistance(float mindis) {

        Star s, s1;
        float dis;
        float ndis;

        for (int i = 0; i < stars.size() - 1; i++) {
            s = stars.get(i);

            for (int j = 0; j < stars.size() - 1; j++) {
                if (i == j) {
                    j++;
                }
                s1 = stars.get(j);
                dis = s.calculateDistanceStar(s1);
                //System.out.println(dis);

                if (dis < mindis) {
                    s.unableStar();
                    s1.unableStar();
                }

            }

        }

    }

    public void filterStarByInitialUmbral(int umbralMinimo, int umbralMaximo) {

        for (Star star : this.stars) {
            if ((star.getMaxlux() < umbralMinimo) || (star.getMaxlux() > umbralMaximo)) {
                star.unableStar();
            }
        }

    }

    public void filterStarByMargin(int margin, int dimx, int dimy) {

        for (Star star : this.stars) {
            if ((star.getCoordx() < margin) || (star.getCoordx() > dimx - margin)
                    || (star.getCoordy() < margin) || (star.getCoordy() > dimy - margin)) {
                star.unableStar();
            }
        }
    }

}
