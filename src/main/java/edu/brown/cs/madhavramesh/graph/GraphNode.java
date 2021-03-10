package edu.brown.cs.madhavramesh.graph;

import java.util.Collection;

/**
 * Node in a Graph.
 *
 * @param <N> Type of Node in Graph
 * @param <E> Type of Edge in Graph
 */
public interface GraphNode<N extends GraphNode<N, E>, E extends GraphEdge<N, E>> {
  Collection<E> getOutEdges();
}
