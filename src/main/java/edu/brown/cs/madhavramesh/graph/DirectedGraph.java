package edu.brown.cs.madhavramesh.graph;

import edu.brown.cs.madhavramesh.maps.MapNode;
import edu.brown.cs.madhavramesh.maps.Way;

import java.util.HashSet;
import java.util.Set;

public class DirectedGraph {

  private Set<MapNode> verticesSet;

  public DirectedGraph() {
    this.verticesSet = new HashSet<>();
  }

  /**
   * adds vertex the set of vertices.
   * @param vertex MapNode that is being added
   */
  public void addVertex(MapNode vertex) {
    verticesSet.add(vertex);
  }

  /**
   * Implements A* optimizations. Calculates haversine distance and adds new way into way list.
   * @param wayID way id
   * @param start start node
   * @param end end node
   * @param finalDest final destination node
   */
  public void addEdge(String wayID, MapNode start, MapNode end, MapNode finalDest) {

    double hCost = MapCalculations
        .haversine(end.getCoordinates().get(0), end.getCoordinates().get(1), finalDest.getCoordinates().get(0),
            finalDest.getCoordinates().get(1));

    double haversineDistance = MapCalculations.haversine(start.getCoordinates().get(0),
        start.getCoordinates().get(1), end.getCoordinates().get(0), end.getCoordinates().get(1));
    start.addWay(new Way(wayID, end, haversineDistance + hCost));
  }

  /**
   * getter for vertices set.
   * @return the verticesSet
   */
  public Set<MapNode> getVerticesSet() {
    return verticesSet;
  }

  /**
   * setter for vertices set.
   * @param verticesSet the vertices set we are setting
   */
  public void setVerticesSet(Set<MapNode> verticesSet) {
    this.verticesSet = verticesSet;
  }

  public int size() {
    return this.verticesSet.size();
  }
}