package org.insa.graphs.algorithm;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.junit.BeforeClass;
import org.junit.Test;

public class ShortestPathAlgorithmTest {

    private static Graph graphRoutiere, graphNonRoutiere, graphGuadeloupe;

    @BeforeClass
    public static void initAll() throws Exception {
        // Charger les cartes
        try (GraphReader reader = new BinaryGraphReader(new DataInputStream(
                new BufferedInputStream(new FileInputStream("../haute-garonne.mapgr"))))) {
            graphRoutiere = reader.read();
        }
        try (GraphReader reader = new BinaryGraphReader(new DataInputStream(
                new BufferedInputStream(new FileInputStream("../carre.mapgr"))))) {
            graphNonRoutiere = reader.read();
        }
        try (GraphReader reader = new BinaryGraphReader(new DataInputStream(
                new BufferedInputStream(new FileInputStream("../Guadeloupe.mapgr"))))) {
            graphGuadeloupe = reader.read();
        }
    }

    @Test
    public void testCheminInexistant() {
        // Origine et destination non connectées
        ShortestPathData data = new ShortestPathData(graphGuadeloupe, 
                graphGuadeloupe.get(31612), graphGuadeloupe.get(34555), 
                ArcInspectorFactory.getAllFilters().get(0));
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
        ShortestPathSolution solution = dijkstra.run();

        assertEquals(Status.INFEASIBLE, solution.getStatus());
    }

    @Test
    public void testCheminLongueurNulle() {
        // Origine et destination identiques
        ShortestPathData data = new ShortestPathData(graphRoutiere, 
                graphRoutiere.get(0), graphRoutiere.get(0), 
                ArcInspectorFactory.getAllFilters().get(0));
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
        ShortestPathSolution solution = dijkstra.run();

        assertEquals(Status.OPTIMAL, solution.getStatus());
        assertEquals(0, solution.getPath().getLength(), 1e-6);
    }

    @Test
    public void testTrajetCourt() {
        // Deux nœuds proches
        ShortestPathData data = new ShortestPathData(graphRoutiere, 
                graphRoutiere.get(0), graphRoutiere.get(10), 
                ArcInspectorFactory.getAllFilters().get(0));
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
        ShortestPathSolution solution = dijkstra.run();

        assertEquals(Status.OPTIMAL, solution.getStatus());
        assertTrue(solution.getPath().isValid());
    }

    @Test
    public void testComparaisonBellmanFord() {
        // Comparer Dijkstra et Bellman-Ford
        ShortestPathData data = new ShortestPathData(graphRoutiere, 
                graphRoutiere.get(0), graphRoutiere.get(10), 
                ArcInspectorFactory.getAllFilters().get(0));
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
        BellmanFordAlgorithm bellmanFord = new BellmanFordAlgorithm(data);

        ShortestPathSolution solutionDijkstra = dijkstra.run();
        ShortestPathSolution solutionBellmanFord = bellmanFord.run();

        assertEquals(solutionDijkstra.getPath().getLength(), 
                     solutionBellmanFord.getPath().getLength(), 1e-6);
    }

    @Test
    public void testAStar() {
        // Test de l'algorithme A* et comparaison avec la solution de Dijkstra
        ShortestPathData data = new ShortestPathData(graphRoutiere, 
                graphRoutiere.get(0), graphRoutiere.get(10), 
                ArcInspectorFactory.getAllFilters().get(0));
        AStarAlgorithm aStar = new AStarAlgorithm(data);
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
        ShortestPathSolution solutionAStar = aStar.run();
        ShortestPathSolution solutionDijkstra = dijkstra.run();

        assertEquals(Status.OPTIMAL, solutionAStar.getStatus());
        assertTrue(solutionAStar.getPath().isValid());
        assertEquals(solutionDijkstra.getPath().getLength(), 
                     solutionAStar.getPath().getLength(), 1e-6);

    }
}