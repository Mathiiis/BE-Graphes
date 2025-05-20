package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;
import org.insa.graphs.model.Arc;

public class Label implements Comparable<Label> {

    private final Node sommetCourant;
    private boolean marque;
    private Double coutRealise;
    private Arc pere;

    public Label(Node sommet) {
        this.sommetCourant = sommet;
        this.marque = false;
        this.coutRealise = Double.POSITIVE_INFINITY;

    }

    // Setters
    public void setCoutRealise(Double coutRealise) {
        this.coutRealise = coutRealise;
    }

    public void setMarque(boolean marque) {
        this.marque = marque;
    }

    public void setPere(Arc pere) {
        this.pere = pere;
    }

    // Getters
    public Node getSommet_courant() {
        return sommetCourant;
    }

    public boolean isMarque() {
        return marque;
    }

    public double getCout_realise() {
        return coutRealise;
    }

    public Arc getPere() {
        return pere;
    }

    public double getCost() {
        return coutRealise;
    }

    public double getTotalCost(){
        return coutRealise;
    }

    @Override
    public int compareTo(Label other) {
        int cmp = Double.compare(this.getTotalCost(), other.getTotalCost());
        if (cmp == 0) {
            // Si égalité, comparer les coûts estimés
            if (this instanceof LabelStar && other instanceof LabelStar) {
                return Double.compare(((LabelStar) this).getCoutEstime(), ((LabelStar) other).getCoutEstime());
            }
        }
    return cmp;
    }
}