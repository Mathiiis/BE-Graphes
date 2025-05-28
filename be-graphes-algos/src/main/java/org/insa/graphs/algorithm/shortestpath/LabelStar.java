package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.model.Node;

public class LabelStar extends Label {

    private double coutEstime;

    public LabelStar(Node sommet, Node destination, AbstractInputData data) {
        super(sommet);
        if (data.getMode() == AbstractInputData.Mode.TIME) {
            // Calcul du coût estimé en temps
            this.coutEstime = sommet.getPoint().distanceTo(destination.getPoint()) / data.getMaximumSpeed();
        } else {
            // Calcul du coût estimé en distance
            this.coutEstime = sommet.getPoint().distanceTo(destination.getPoint());
        }
    }

    @Override
    public double getTotalCost(){
        return this.getCout_realise() + this.coutEstime;
    }

    public double getCoutEstime(){
        return coutEstime;
    }

    
}