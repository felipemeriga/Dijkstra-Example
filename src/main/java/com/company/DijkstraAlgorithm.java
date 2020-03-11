package com.company;

import java.util.*;

public class DijkstraAlgorithm {

    private final List<Vertex> nodes;
    private final List<Edge> edges;
    private Set<Vertex> settledNodes;
    private Set<Vertex> unSettledNodes;
    private Map<Vertex, Vertex> predecessors;
    private Map<Vertex, Integer> distance;
    private Map<Vertex, Set<Edge>> neighbourMap;

    public DijkstraAlgorithm(Graph graph) {
        // create a copy of the array so that we can operate on this array
        this.nodes = new ArrayList<Vertex>(graph.getVertexes());
        this.edges = new ArrayList<Edge>(graph.getEdges());
        this.neighbourMap = new HashMap<>();
        this.fillNeighbourMap();
    }

/*  This function fills a map where the keys are the vertix mapping a list of edges
    This map is used to prevent in every iteration to do a for loop in all edges, so the time
    complexity of those functions falls from O(n) to O(1)*/
    public void fillNeighbourMap() {
        for (Edge edge : edges) {
            neighbourMap.computeIfAbsent(edge.getSource(), k -> new HashSet<>(Collections.singletonList(edge)));
            neighbourMap.get(edge.getSource()).add(edge);
            if(edge.isBothDirections()) {
                neighbourMap.get(edge.getDestination()).add(edge);
            }
        }
    }

    public void execute(Vertex source) {
        settledNodes = new HashSet<Vertex>();
        unSettledNodes = new HashSet<Vertex>();
        distance = new HashMap<Vertex, Integer>();
        predecessors = new HashMap<Vertex, Vertex>();
        distance.put(source, 0);
        unSettledNodes.add(source);
        while (unSettledNodes.size() > 0) {
            Vertex node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            findMinimalDistances(node);
        }
    }

    private void findMinimalDistances(Vertex node) {
        List<Vertex> adjacentNodes = getNeighbors(node);
        for (Vertex target : adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node)
                    + getDistance(node, target)) {
                distance.put(target, getShortestDistance(node)
                        + getDistance(node, target));
                predecessors.put(target, node);
                unSettledNodes.add(target);
            }
        }
    }

/*    Here we use the neighbourMap in order to get only the edges who has relationship with the current node,
    reducing the time complexity*/
    private int getDistance(Vertex node, Vertex target) {
        Set<Edge> neighborEdges = neighbourMap.get(node);
        if(neighborEdges != null) {
            for (Edge edge : neighborEdges) {
                // The OR logical inside the if covers the edges that have both directions, which means that you don't
                // have a proper source or destination
                if (edge.getSource().equals(node)
                        && edge.getDestination().equals(target)
                        || (edge.getSource().equals(target) && edge.getDestination().equals(node) && edge.isBothDirections())) {
                    return edge.getWeight();
                }
            }

        }
        throw new RuntimeException("Should not happen");

    }

    /*    Here we use the neighbourMap in order to get only the edges who has relationship with the current node,
    reducing the time complexity*/
    private List<Vertex> getNeighbors(Vertex node) {
        List<Vertex> neighbors = new ArrayList<Vertex>();
        Set<Edge> neighborEdges = neighbourMap.get(node);
        if(neighborEdges != null) {
            for (Edge edge : neighborEdges) {
                if (getRelationshipBetweenEdgeNode(edge, node)) {
                    if(edge.getDestination().equals(node))
                        neighbors.add(edge.getSource());
                    else
                        neighbors.add(edge.getDestination());
                }
            }
        }
        return neighbors;
    }

    private boolean getRelationshipBetweenEdgeNode(Edge edge, Vertex node) {
        boolean bothDirectionsDestination = checkDestinationBothDirections(edge, node);
        return (((edge.getSource().equals(node) || bothDirectionsDestination))
                &&
                (!isSettled(edge.getDestination()) || (isSettled(edge.getDestination()) && bothDirectionsDestination))
        );
    }

    private boolean checkDestinationBothDirections(Edge edge, Vertex node) {
        return (edge.getDestination().equals(node) && edge.isBothDirections());
    }


    private Vertex getMinimum(Set<Vertex> vertexes) {
        Vertex minimum = null;
        for (Vertex vertex : vertexes) {
            if (minimum == null) {
                minimum = vertex;
            } else {
                if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                    minimum = vertex;
                }
            }
        }
        return minimum;
    }

    private boolean isSettled(Vertex vertex) {
        return settledNodes.contains(vertex);
    }

    private int getShortestDistance(Vertex destination) {
        Integer d = distance.get(destination);
        if (d == null) {
            return Integer.MAX_VALUE;
        } else {
            return d;
        }
    }
    /*
     * This method returns the path from the source to the selected target and
     * NULL if no path exists
     */
    public LinkedList<Vertex> getPath(Vertex target) {
        LinkedList<Vertex> path = new LinkedList<Vertex>();
        Vertex step = target;
        // check if a path exists
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        // Put it into the correct order
        Collections.reverse(path);
        return path;
    }

}
