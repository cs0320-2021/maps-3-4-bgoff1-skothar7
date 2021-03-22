package edu.brown.cs.madhavramesh.graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;
import java.util.AbstractMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Queue;
import java.util.PriorityQueue;
import edu.brown.cs.madhavramesh.maps.MapNode;
import edu.brown.cs.madhavramesh.maps.Maps;
import edu.brown.cs.madhavramesh.maps.RouteTriggerAction;
import edu.brown.cs.madhavramesh.maps.Way;

public class Dijkstra {
  private MapNode start;
  private MapNode end;
  private List<MapNode> pathList;
  private HashMap<MapNode, Double> distances;
  private PriorityQueue<MapNode> pq;
  private DirectedGraph dg;
  private HashMap<String, String> targetIDtoWayID;

  /**
   * Dijkstra constructor which instantiates relevant class variables.
   *
   * @param dg       directed graph
   * @param start    start node
   * @param end      end node
   */
  public Dijkstra(DirectedGraph dg, MapNode start, MapNode end) {

    this.start = start;
    this.end = end;
    this.pathList = new ArrayList<>();
    this.distances = new HashMap<>();
    if (distances == null) {
      System.out.println("41 ");
    }
    this.dg = dg;
    this.targetIDtoWayID = new HashMap<>();

    //Initialize distances map
    for (MapNode node : dg.getVerticesSet()) {
      distances.put(node, Double.POSITIVE_INFINITY);
    }
    distances.put(start, 0.0);
    distances.put(end, Double.POSITIVE_INFINITY);

    //initialize pq;
    //pq that orders Nodes on their current distance value
    //sort by ascending order
    pq = new PriorityQueue<MapNode>(new DijkstraPqComparator(distances));

    //populate the pq
    //calculate corresponding distance as you go

    for (MapNode node : distances.keySet()) {
      pq.add(node);
    }

    if (pq.isEmpty()) {
      System.out.println("ERROR: pq was empty");
      return;
    }
    MapNode currentVertex = pq.poll();
    while (/*dg.getVerticesSet().size() > 0 && */!pq.isEmpty()) {
      //---------CACHING----------------
      Set<Way> cachedWays = null;
      try {
        cachedWays = RouteTriggerAction.getCache().get(currentVertex.getStringID());


        for (Way way : cachedWays) {

          //System.out.println("76 "+way.getWeight());
          //System.out.println("77 "+distances.get(currentVertex));
          if (distances == null) {
            System.out.println("78 ");
          }
          //System.out.println("79 "+distances.get(way.getTarget()));
          if (distances.get(way.getTarget()) > distances.get(currentVertex) + way.getWeight()) {
            distances.put(way.getTarget(), distances.get(currentVertex) + way.getWeight());
            way.getTarget().setParent(currentVertex);
            pq.add(way.getTarget());

            targetIDtoWayID.put(way.getTarget().getStringID(), way.getWayID());
          }
        }

      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Node has no ways");
        System.out.println(currentVertex.getStringID());

      }

      //dg.getVerticesSet().remove(currentVertex);

      currentVertex = pq.poll(); //update vertex
      assert currentVertex != null;

    }

  }

  /**
   * Produces the list of vertices comprising the shortest path from the
   * source to the destination.
   */
  public void findShortestPath() {

    List<String> wayIdsToPrint = new ArrayList<>();

    //String stringBuilder = "";

    if (distances.get(end) == Double.POSITIVE_INFINITY) {
      System.out.println(start.getStringID().trim() + " -/- " + end.getStringID().trim());
    } else {
      MapNode thisVertex = end;

      if (end.getStringID().equals(start.getStringID())) {
        pathList.add(start);
        System.out.println(end.getStringID() + " you're already there!");
      } else {

        while (thisVertex != null && !thisVertex.getStringID().equals(start.getStringID())) {
          pathList.add(thisVertex);
          wayIdsToPrint.add(targetIDtoWayID.get(thisVertex.getStringID()));
          thisVertex = thisVertex.getParent();
        }
        pathList.add(start);
        if (pathList.size() >= 2) {
          wayIdsToPrint.add(targetIDtoWayID.get(pathList.get(pathList.size() - 2).getStringID()));
        }
      }
    }

    for (int i = pathList.size() - 1; i > 0; i--) {
      System.out.println(pathList.get(i).getStringID() + " -> "
          + pathList.get(i - 1).getStringID() + " : " + wayIdsToPrint.get(i - 1));
    }


  }

  /**
   * Produces the list of vertices comprising the shortest path from the
   * source to the destination.
   */
  public synchronized String[] findShortestPathGUI() {
    System.out.println("yes");
    List<String> wayIdsToPrint = new ArrayList<>();

    //String stringBuilder = "";

    if (distances.get(end) == Double.POSITIVE_INFINITY) {
      return new String[0];
    } else {

      if (end.getStringID().equals(start.getStringID())) {
        return new String[0];
      } else {
        MapNode thisVertex = end;
        while (thisVertex != null && !thisVertex.getStringID().equals(start.getStringID())) {
          pathList.add(thisVertex);
          thisVertex = thisVertex.getParent();
        }
        pathList.add(start);
      }
    }

    for (int i = pathList.size() - 1; i > 0; i--) {
      wayIdsToPrint.add("path,"+pathList.get(i).getCoordinate(0)+","+pathList.get(i).getCoordinate(1) + ","
          + pathList.get(i - 1).getCoordinate(0) + ","+ pathList.get(i - 1).getCoordinate(1));
    }
    return wayIdsToPrint.toArray(new String[wayIdsToPrint.size()]);

  }

  /**
   * getter for start node.
   *
   * @return start node
   */
  public MapNode getStart() {
    return start;
  }

  /**
   * setter for start node.
   *
   * @param start start node to set start to
   */
  public void setStart(MapNode start) {
    this.start = start;
  }

  /**
   * getter for end node.
   *
   * @return end node
   */
  public MapNode getEnd() {
    return end;
  }

  /**
   * setter for end node.
   *
   * @param end end node to set end to
   */
  public void setEnd(MapNode end) {
    this.end = end;
  }



}


///**
// * Implements graph algorithms.
// *
// * @param <N> Type parameter of nodes in graph
// * @param <E> Type parameter of edges in graph
// */
//public class GraphAlgorithms<N extends GraphNode<N, E>, E extends GraphEdge<N, E>> {
//
//  /**
//   * Uses Dijkstra's algorithm to find shortest path.
//   *
//   * @param start starting node
//   * @param end   destination node
//   * @return ordered list of nodes that lead from start to destination where
//   * the start is the first element in the list and destination is the last
//   * element in the list
//   */
//  public Stack<E> dijkstra(N start, N end) {
//    Map<N, E> previous = new HashMap<>();
//    Map<N, Double> distances = new HashMap<>();
//    Set<N> processed = new HashSet<>();
//    Queue<Map.Entry<E, Double>> unprocessed = new PriorityQueue<>(
//            Comparator.comparingDouble(Map.Entry::getValue));
//
//    // Adds all edges connected to start node to priority queue
//    for (E edge : start.getOutEdges()) {
//      Double totalCost = edge.weight();
//      distances.put(edge.getTarget(), totalCost);
//      unprocessed.add(new AbstractMap.SimpleEntry(edge, totalCost));
//    }
//
//    // Initializes visited set with start node
//    distances.put(start, 0.0);
//    processed.add(start);
//
//    while (!unprocessed.isEmpty()) {
//      // Removes minimum edge from priority queue
//      Map.Entry<E, Double> min = unprocessed.poll();
//      E minEdge = min.getKey();
//      Double minValue = min.getValue();
//
//      previous.put(minEdge.getTarget(), minEdge);
//
//      // Checks if node that minimum edge points to equals end node
//      if (minEdge.getTarget().equals(end)) {
//        break;
//      }
//      // Checks if node that minimum edge points to has already been processed
//      if (processed.contains(minEdge.getTarget())) {
//        continue;
//      }
//      // Adds node that minimum edge points to processed set
//      processed.add(minEdge.getTarget());
//
//      for (E edge : minEdge.getTarget().getOutEdges()) {
//        if (!processed.contains(edge.getTarget())) {
//          Double newCost = minValue + edge.weight();
//
//          // Checks if distance calculated is less than current distance to node
//          if (!distances.containsKey(edge.getTarget()) || newCost < distances.get(edge.getTarget())) {
//            distances.put(edge.getTarget(), newCost);
//            unprocessed.add(new AbstractMap.SimpleEntry(edge, newCost));
//          }
//        }
//      }
//    }
//
//    return calculatePath(previous, end);
//  }
//
//  /**
//   * Uses A* algorithm to find shortest path.
//   *
//   * @param start starting node
//   * @param end   destination node
//   * @param p     Heuristic function that estimates cost of the cheapest path from node
//   * @return ordered stack of nodes that lead from start to destination where
//   * the start is the first element in the list and destination is the last
//   * element in the stack
//   */
//  public Stack<E> aStar(N start, N end, AStarHeuristic<N, E> p) {
//    Map<N, E> previous = new HashMap<>();
//    Map<N, Double> distances = new HashMap<>();
//    Set<N> processed = new HashSet<>();
//    Queue<Map.Entry<E, Double>> unprocessed = new PriorityQueue<>(
//            Comparator.comparingDouble(Map.Entry::getValue));
//
//    // Adds all edges connected to start node to priority queue
//    for (E edge : start.getOutEdges()) {
//      Double totalCost = edge.weight() + p.computeCost(edge.getTarget(), end);
//      distances.put(edge.getTarget(), totalCost);
//      unprocessed.add(new AbstractMap.SimpleEntry(edge, totalCost));
//    }
//
//    // Initializes visited set with start node
//    distances.put(start, 0.0);
//    processed.add(start);
//
//    while (!unprocessed.isEmpty()) {
//      // Removes minimum edge from priority queue
//      Map.Entry<E, Double> min = unprocessed.poll();
//      E minEdge = min.getKey();
//      Double minValue = min.getValue();
//
//      previous.put(minEdge.getTarget(), minEdge);
//
//      // Checks if node that minimum edge points to equals end node
//      if (minEdge.getTarget().equals(end)) {
//        break;
//      }
//      // Checks if node that minimum edge points to has already been processed
//      if (processed.contains(minEdge.getTarget())) {
//        continue;
//      }
//      // Adds node that minimum edge points to processed set
//      processed.add(minEdge.getTarget());
//
//      for (E edge : minEdge.getTarget().getOutEdges()) {
//        if (!processed.contains(edge.getTarget())) {
//          Double newCost = minValue + edge.weight() + p.computeCost(edge.getTarget(), end);
//
//          // Checks if distance calculated is less than current distance to node
//          if (!distances.containsKey(edge.getTarget()) || newCost < distances.get(edge.getTarget())) {
//            distances.put(edge.getTarget(), newCost);
//            unprocessed.add(new AbstractMap.SimpleEntry(edge, newCost));
//          }
//        }
//      }
//    }
//
//    return calculatePath(previous, end);
//  }
//
//  private Stack<E> calculatePath(Map<N, E> previous, N end) {
//    Stack<E> path = new Stack<>();
//    N cur = end;
//
//    while (previous.containsKey(cur)) {
//      E lastEdgeToNode = previous.get(cur);
//      path.push(lastEdgeToNode);
//      cur = lastEdgeToNode.from();
//    }
//
//    return path;
//  }
//}
