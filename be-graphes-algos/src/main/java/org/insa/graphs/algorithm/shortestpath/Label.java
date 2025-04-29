package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;

public class Label {

    private final Node sommetCourant;
    private boolean marque;
    private int coutRealise;
    private Node pere;

    public Label(Node sommet) {
        this.sommetCourant = sommet;
        this.marque = false;
        this.coutRealise = 0;

    }

    // Getters
    public Node getSommet_courant() {
        return sommetCourant;
    }

    public boolean isMarque() {
        return marque;
    }

    public int getCout_realise() {
        return coutRealise;
    }

    public Node getPere() {
        return pere;
    }

    public int getCost() {
        return coutRealise;
    }
}