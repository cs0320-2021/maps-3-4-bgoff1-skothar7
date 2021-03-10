package edu.brown.cs.madhavramesh.maps;

import edu.brown.cs.madhavramesh.graph.AStarHeuristic;

/** Defines an estimated cost between one Node and another
 * to use with A* algorithm.
 */
public class HaversineHeuristic implements AStarHeuristic<LocationNode, Way> {
  @Override
  public double computeCost(LocationNode a, LocationNode b) {
    return LocationNode.haversineDist(a, b);
  }
}
