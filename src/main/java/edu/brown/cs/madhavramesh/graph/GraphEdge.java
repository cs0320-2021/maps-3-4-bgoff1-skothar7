package edu.brown.cs.madhavramesh.graph;

/** Edge in a Graph.
 *
 * @param <N> Type of Node in Graph
 * @param <E> Type of Edge in Graph
 */
public interface GraphEdge<N extends GraphNode<N, E>, E extends GraphEdge<N, E>>
        extends Comparable<E> {
  N from();
  N to();
  double weight();
}
