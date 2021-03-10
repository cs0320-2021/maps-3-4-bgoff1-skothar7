package edu.brown.cs.madhavramesh.graph;

import java.util.Comparator;
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;
import java.util.AbstractMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Queue;
import java.util.PriorityQueue;

/**
 * Implements graph algorithms.
 *
 * @param <N> Type parameter of nodes in graph
 * @param <E> Type parameter of edges in graph
 */
public class GraphAlgorithms<N extends GraphNode<N, E>, E extends GraphEdge<N, E>> {

  /**
   * Uses Dijkstra's algorithm to find shortest path.
   *
   * @param start starting node
   * @param end   destination node
   * @return ordered list of nodes that lead from start to destination where
   * the start is the first element in the list and destination is the last
   * element in the list
   */
  public Stack<E> dijkstra(N start, N end) {
    Map<N, E> previous = new HashMap<>();
    Map<N, Double> distances = new HashMap<>();
    Set<N> processed = new HashSet<>();
    Queue<Map.Entry<E, Double>> unprocessed = new PriorityQueue<>(
            Comparator.comparingDouble(Map.Entry::getValue));

    // Adds all edges connected to start node to priority queue
    for (E edge : start.getOutEdges()) {
      Double totalCost = edge.weight();
      distances.put(edge.to(), totalCost);
      unprocessed.add(new AbstractMap.SimpleEntry(edge, totalCost));
    }

    // Initializes visited set with start node
    distances.put(start, 0.0);
    processed.add(start);

    while (!unprocessed.isEmpty()) {
      // Removes minimum edge from priority queue
      Map.Entry<E, Double> min = unprocessed.poll();
      E minEdge = min.getKey();
      Double minValue = min.getValue();

      previous.put(minEdge.to(), minEdge);

      // Checks if node that minimum edge points to equals end node
      if (minEdge.to().equals(end)) {
        break;
      }
      // Checks if node that minimum edge points to has already been processed
      if (processed.contains(minEdge.to())) {
        continue;
      }
      // Adds node that minimum edge points to processed set
      processed.add(minEdge.to());

      for (E edge : minEdge.to().getOutEdges()) {
        if (!processed.contains(edge.to())) {
          Double newCost = minValue + edge.weight();

          // Checks if distance calculated is less than current distance to node
          if (!distances.containsKey(edge.to()) || newCost < distances.get(edge.to())) {
            distances.put(edge.to(), newCost);
            unprocessed.add(new AbstractMap.SimpleEntry(edge, newCost));
          }
        }
      }
    }

    return calculatePath(previous, end);
  }

  /**
   * Uses A* algorithm to find shortest path.
   *
   * @param start starting node
   * @param end   destination node
   * @param p     Heuristic function that estimates cost of the cheapest path from node
   * @return ordered stack of nodes that lead from start to destination where
   * the start is the first element in the list and destination is the last
   * element in the stack
   */
  public Stack<E> aStar(N start, N end, AStarHeuristic<N, E> p) {
    Map<N, E> previous = new HashMap<>();
    Map<N, Double> distances = new HashMap<>();
    Set<N> processed = new HashSet<>();
    Queue<Map.Entry<E, Double>> unprocessed = new PriorityQueue<>(
            Comparator.comparingDouble(Map.Entry::getValue));

    // Adds all edges connected to start node to priority queue
    for (E edge : start.getOutEdges()) {
      Double totalCost = edge.weight() + p.computeCost(edge.to(), end);
      distances.put(edge.to(), totalCost);
      unprocessed.add(new AbstractMap.SimpleEntry(edge, totalCost));
    }

    // Initializes visited set with start node
    distances.put(start, 0.0);
    processed.add(start);

    while (!unprocessed.isEmpty()) {
      // Removes minimum edge from priority queue
      Map.Entry<E, Double> min = unprocessed.poll();
      E minEdge = min.getKey();
      Double minValue = min.getValue();

      previous.put(minEdge.to(), minEdge);

      // Checks if node that minimum edge points to equals end node
      if (minEdge.to().equals(end)) {
        break;
      }
      // Checks if node that minimum edge points to has already been processed
      if (processed.contains(minEdge.to())) {
        continue;
      }
      // Adds node that minimum edge points to processed set
      processed.add(minEdge.to());

      for (E edge : minEdge.to().getOutEdges()) {
        if (!processed.contains(edge.to())) {
          Double newCost = minValue + edge.weight() + p.computeCost(edge.to(), end);

          // Checks if distance calculated is less than current distance to node
          if (!distances.containsKey(edge.to()) || newCost < distances.get(edge.to())) {
            distances.put(edge.to(), newCost);
            unprocessed.add(new AbstractMap.SimpleEntry(edge, newCost));
          }
        }
      }
    }

    return calculatePath(previous, end);
  }

  private Stack<E> calculatePath(Map<N, E> previous, N end) {
    Stack<E> path = new Stack<>();
    N cur = end;

    while (previous.containsKey(cur)) {
      E lastEdgeToNode = previous.get(cur);
      path.push(lastEdgeToNode);
      cur = lastEdgeToNode.from();
    }

    return path;
  }
}
