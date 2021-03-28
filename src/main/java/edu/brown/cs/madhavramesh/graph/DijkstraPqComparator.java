package edu.brown.cs.madhavramesh.graph;

import edu.brown.cs.madhavramesh.maps.MapNode;

import java.util.Comparator;
import java.util.HashMap;

/**
 * Class to sort MapNodes in Dijstra algorithm.
 * Sorts nodes in ascending order.
 */
public class DijkstraPqComparator implements Comparator<MapNode> {

  private HashMap<MapNode, Double> distances;

  /**
   * Constructor for custom comparator.
   * @param distances hashmap that maps MapNodes and the distances.
   */
  public DijkstraPqComparator(HashMap<MapNode, Double> distances) {
    this.distances = distances;
  }

  /**
   * Custom compare method that compares MapNodes distances. Used in Dijkstra/A* algorithm
   * @param p a MapNode
   * @param q a MapNode
   * @return integer that represents how the MapNodes should be sorted
   */
  @Override
  public int compare(MapNode p, MapNode q) {
    if (distances.get(p) == distances.get(q)) {
      return 0;
    } else if (distances.get(p) > distances.get(q)) {
      return 1;
    } else {
      return -1;
    }
  }
}