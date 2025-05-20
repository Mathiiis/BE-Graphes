package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;

import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.algorithm.utils.ElementNotFoundException;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.Arc;
import org.insa.graphs.algorithm.AbstractSolution.Status;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {

        // retrieve data from the input problem (getInputData() is inherited from the
        // parent class ShortestPathAlgorithm)
        final ShortestPathData data = getInputData();

        // variable that will contain the solution of the shortest path problem
        ShortestPathSolution solution = null;
        
        Graph graph = data.getGraph();
        final int nbNodes = graph.size();

        // Tableau des labels
        Label[] labels = new Label[nbNodes];

        // Tas des labels à traiter
        BinaryHeap<Label> tas = new BinaryHeap<>();

        // Initialisation
        for (Node node : graph.getNodes()) {
            labels[node.getId()] = new Label(node);
        }

        Node origin = data.getOrigin();
        labels[origin.getId()].setCoutRealise(0.0);
        tas.insert(labels[origin.getId()]);

        // Algo Dijkstra
        while (!labels[data.getDestination().getId()].isMarque()) {
            Label x = tas.deleteMin();
            x.setMarque(true);
            notifyNodeMarked(x.getSommet_courant());

            for(Arc arc : x.getSommet_courant().getSuccessors()){
                
                Label y = labels[arc.getDestination().getId()];
                if (!y.isMarque()) {
                    double newCost = x.getCout_realise() + data.getCost(arc); // PCC en distance
                    //double newCost = x.getCout_realise() + arc.getMinimumTravelTime(); // PCC en temps
                    if (newCost < y.getCout_realise()) {
                        y.setCoutRealise(newCost);
                        y.setPere(arc);

                        System.out.println("Nouveau coût optimal pour le nœud " + y + ": " + y.getCout_realise());

                        try {
                            tas.remove(y);
                        } catch (ElementNotFoundException e) {
                            notifyNodeReached(y.getSommet_courant());
                        }
                        tas.insert(y); 
                    }
                    System.out.println("New cost " + y.getSommet_courant().getId() + ": " + y.getCost() + "\n");
                }
            }
        }

        // Construction de la solution
        if (!labels[data.getDestination().getId()].isMarque()) {
            // Destination non atteignable
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        } else {
            // Création du chemin destination vers l'origine
            ArrayList<Arc> arcs = new ArrayList<>();
            Node current = data.getDestination();
            
            // Tant que l'origine n'est pas atteinte
            while (!current.equals(data.getOrigin())) {
                Label currentLabel = labels[current.getId()];
                Arc arc = currentLabel.getPere();
                arcs.add(0, arc);
                current = arc.getOrigin();
            }
            
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
        }

        // when the algorithm terminates, return the solution that has been found
        return solution;
    }

}
