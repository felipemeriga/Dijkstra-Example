package com.company;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestDijkstraAlgorithm {

    private List<Vertex> nodes;
    private List<Edge> edges;

    @Test
    public void testExcute() {
        nodes = new ArrayList<Vertex>();
        edges = new ArrayList<Edge>();
        for (int i = 0; i < 12; i++) {
            Vertex location = new Vertex("Node_" + i, "Node_" + i);
            nodes.add(location);
        }

        addLane("Edge_0", 0, 1, 85, false);
        addLane("Edge_1", 0, 2, 217, false);
        addLane("Edge_2", 0, 4, 173, false);
        addLane("Edge_3", 2, 6, 186, false);
        addLane("Edge_4", 2, 7, 103, false);
        addLane("Edge_5", 3, 7, 183, false);
        addLane("Edge_6", 5, 8, 250, false);
        addLane("Edge_7", 8, 9, 84, false);
        addLane("Edge_8", 7, 9, 167, false);
        addLane("Edge_9", 4, 9, 502, false);
        addLane("Edge_10", 9, 10, 40, false);
        addLane("Edge_11", 1, 10, 600, false);
        addLane("Edge_12", 11, 0, 15, false);
        addLane("Edge_12", 11, 10, 15, false);

        // Lets check from location Loc_1 to Loc_10
        Graph graph = new Graph(nodes, edges);
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.execute(nodes.get(0));
        LinkedList<Vertex> path = dijkstra.getPath(nodes.get(10));

        assertNotNull(path);
        assertTrue(path.size() > 0);

        for (Vertex vertex : path) {
            System.out.println(vertex);
        }
    }

    private void addLane(String laneId, int sourceLocNo, int destLocNo,
                         int duration, boolean bothDirections) {
        Edge lane = new Edge(laneId,nodes.get(sourceLocNo), nodes.get(destLocNo), duration, bothDirections);
        edges.add(lane);
    }
}
