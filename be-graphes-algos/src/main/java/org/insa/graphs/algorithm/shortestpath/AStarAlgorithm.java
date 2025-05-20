package org.insa.graphs.algorithm.shortestpath;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data, int natureCout) {
        super(data,natureCout,1);
    }

    public AStarAlgorithm(ShortestPathData data){
        super(data, 0, 1);
    }

}
