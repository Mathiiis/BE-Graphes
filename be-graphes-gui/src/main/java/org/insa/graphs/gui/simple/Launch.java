package org.insa.graphs.gui.simple;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.components.BasicDrawing;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;

import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.Label;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.gui.drawing.Drawing.AlphaMode;

public class Launch {

    /**
     * Create a new Drawing inside a JFrame an return it.
     *
     * @return The created drawing.
     * @throws Exception if something wrong happens when creating the graph.
     */
    public static Drawing createDrawing() throws Exception {
        BasicDrawing basicDrawing = new BasicDrawing();
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("BE Graphes - Launch");
                frame.setLayout(new BorderLayout());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.setSize(new Dimension(800, 600));
                frame.setContentPane(basicDrawing);
                frame.validate();
            }
        });
        return basicDrawing;
    }

    static Graph createGraph(final String mapName) throws java.io.IOException {
        
        final Graph graph;
        
        // create path reader
        try (final GraphReader reader = new BinaryGraphReader(new DataInputStream(
                new BufferedInputStream(new FileInputStream(mapName))))) {

            graph = reader.read();
        }
        return graph;
    }

    static Drawing drawGraph(Graph graph){
        // draw the graph
        final Drawing drawingMap;
        try {
            drawingMap = createDrawing();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create drawing", e);
        }
        drawingMap.drawGraph(graph);
        return drawingMap;
    }

    static void testPath(Graph graph, Drawing drawMap, final String pathName) throws java.io.IOException {
        // create path reader
        try (final PathReader pathReader = new BinaryPathReader(
            new DataInputStream(new BufferedInputStream(
            new FileInputStream(pathName)))) ) {

            final Path path = pathReader.readPath(graph);
            drawMap.drawPath(path);
        }
    }

    static void testDijkstra(Graph graph, Drawing drawingMap, int originID, int destID){
        final Node origin;
        final Node destination;

        origin = graph.get(originID);
        destination = graph.get(destID);

        // mettre les points
        drawingMap.drawMarker(origin.getPoint(), Color.GREEN, Color.GREEN, AlphaMode.OPAQUE);
        drawingMap.drawMarker(destination.getPoint(), Color.RED, Color.RED, AlphaMode.OPAQUE);

        // recuperer le data pour le parcours dijkstra
        ShortestPathData data = new ShortestPathData(graph, origin, destination, ArcInspectorFactory.getAllFilters().get(0));

        // Test Dijkstra
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
        ShortestPathSolution solution = dijkstra.run();
        System.out.println("Shortest path : " + solution.toString());

        drawingMap.drawPath(solution.getPath(), Color.CYAN);
    }

    static void testAStar(Graph graph, Drawing drawingMap, int originID, int destID){
        final Node origin;
        final Node destination;

        origin = graph.get(originID);
        destination = graph.get(destID);

        // mettre les points
        drawingMap.drawMarker(origin.getPoint(), Color.GREEN, Color.GREEN, AlphaMode.OPAQUE);
        drawingMap.drawMarker(destination.getPoint(), Color.RED, Color.RED, AlphaMode.OPAQUE);

        // recuperer le data pour le parcours dijkstra
        ShortestPathData data = new ShortestPathData(graph, origin, destination, ArcInspectorFactory.getAllFilters().get(0));

        // Test A*
        AStarAlgorithm astar = new AStarAlgorithm(data);
        ShortestPathSolution solution = astar.run();
        System.out.println("Shortest path : " + solution.toString());

        drawingMap.drawPath(solution.getPath(), Color.BLACK);
    }

    public static void main(String[] args) throws Exception {

        // visit these directory to see the list of available files on commetud.
        // map routière : insa
        final String mapNameRoutiere = "../Maps/belgium.mapgr";
        //final String mapNameNonRoutiere = "../Maps/carre.mapgr";
        final String pathName = "../Maps/path_be_173101_302442.path";

        // origin
        final int originID = 173101;

        // destination
        final int destID = 302442;

        final Graph graph = createGraph(mapNameRoutiere);
        final Drawing drawGraph = drawGraph(graph);

        testPath(graph, drawGraph, pathName);
        testDijkstra(graph, drawGraph, originID, destID);
        testAStar(graph, drawGraph, originID, destID);


        // drawingMapNonRoutiere.drawGraph(graphRoutiere2);
        // Initialize origin and destination before using them
        // origin = graphRoutiere2.get(originID);
        // destination = graphRoutiere2.get(destID);
        // drawingMapRoutiere.drawMarker(origin.getPoint(), Color.GREEN, Color.GREEN, AlphaMode.OPAQUE);
        // drawingMapRoutiere.drawMarker(destination.getPoint(), Color.RED, Color.RED, AlphaMode.OPAQUE);
        // drawingMapNonRoutiere.drawGraph(graphNonRoutiere);

        // ShortestPathData data = new ShortestPathData(graphRoutiere2, origin, destination, ArcInspectorFactory.getAllFilters().get(0));

        // try (final PathReader pathReader = new BinaryPathReader(
        // new DataInputStream(new BufferedInputStream(
        // new FileInputStream(pathName)))) ) {

        // pathMapRoutiere = pathReader.readPath(graphRoutiere);
        // drawingMapRoutiere.drawPath(pathMapRoutiere);

        // } catch (Exception e) {
        // System.out.println("Chemin inexistant dans : " + mapNameRoutiere);
        // }

        // // Test chemin inexistant
        // try (final PathReader pathReader = new BinaryPathReader(
        // new DataInputStream(new BufferedInputStream(
        // new FileInputStream(pathName)))) ) {

        // pathMapNonRoutiere = pathReader.readPath(graphNonRoutiere);
        // drawingMapNonRoutiere.drawPath(pathMapNonRoutiere);
        // } catch (Exception e) {
        // System.out.println("Chemin : " + pathName + " est inexistant dans : " +
        // mapNameNonRoutiere + "\n");
        // }

        // Test Dijkstra
        // DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
        // ShortestPathSolution solution = dijkstra.run();
        // System.out.println("Shortest path : " + solution.toString());

        // drawingMapRoutiere.drawPath(solution.getPath());
    }
}
