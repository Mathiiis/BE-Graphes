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

    public static void main(String[] args) throws Exception {

        // visit these directory to see the list of available files on commetud.
        // map routière : insa
        final String mapNameRoutiere = "../Maps/insa.mapgr";
        final String mapNameNonRoutiere = "../Maps/carre.mapgr";
        final String pathName = "../Maps/path_fr31insa_rangueil_r2.path";

        final int originID = 552;
        final Node origin;

        final int destID = 526;
        final Node destination;

        final Graph graphRoutiere;
        final Graph graphNonRoutiere;
        final Path pathMapRoutiere;
        final Path pathMapNonRoutiere;

        // create a graph reader
        try (final GraphReader reader = new BinaryGraphReader(new DataInputStream(
                new BufferedInputStream(new FileInputStream(mapNameRoutiere))))) {

            graphRoutiere = reader.read();
        }

        try (final GraphReader reader = new BinaryGraphReader(new DataInputStream(
                new BufferedInputStream(new FileInputStream(mapNameNonRoutiere))))) {

            graphNonRoutiere = reader.read();
        }

        // create the drawing
        final Drawing drawingMapRoutiere = createDrawing();
        // final Drawing drawingMapNonRoutiere = createDrawing();

        drawingMapRoutiere.drawGraph(graphRoutiere);
        // Initialize origin and destination before using them
        origin = graphRoutiere.get(originID);
        destination = graphRoutiere.get(destID);
        drawingMapRoutiere.drawMarker(origin.getPoint(), Color.GREEN, Color.GREEN, AlphaMode.OPAQUE);
        drawingMapRoutiere.drawMarker(destination.getPoint(), Color.RED, Color.RED, AlphaMode.OPAQUE);
        // drawingMapNonRoutiere.drawGraph(graphNonRoutiere);

        ShortestPathData data = new ShortestPathData(graphRoutiere, origin, destination, ArcInspectorFactory.getAllFilters().get(0));

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
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
        ShortestPathSolution solution = dijkstra.run();
        System.out.println("Shortest path : " + solution.toString());

        //pathName = solution.getPath();

        try (final PathReader pathReader = new BinaryPathReader(
            new DataInputStream(new BufferedInputStream(
            new FileInputStream(pathName)))) ) {

        pathMapRoutiere = pathReader.readPath(graphRoutiere);
        drawingMapRoutiere.drawPath(pathMapRoutiere);
        }

    }
}
