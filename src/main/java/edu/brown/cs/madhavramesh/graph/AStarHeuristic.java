package edu.brown.cs.madhavramesh.graph;

/**
 * Heuristic that estimates cost of the
 * cheapest path from a node.
 *
 * @param <N> Type of Node in graph
 * @param <E> Type of Edge in graph
 */
public interface AStarHeuristic<N extends GraphNode<N, E>, E extends GraphEdge<N, E>> {
  double computeCost(N a, N b);
}
