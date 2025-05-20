package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;

public class LabelStar extends Label {

    private double coutEstime;

    public LabelStar(Node sommet, Node destination) {
        super(sommet);
        this.coutEstime = sommet.getPoint().distanceTo(destination.getPoint());
    }

    @Override
    public double getTotalCost(){
        return this.getCout_realise() + this.coutEstime;
    }

    public double getCoutEstime(){
        return coutEstime;
    }

    
}