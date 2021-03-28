package edu.brown.cs.madhavramesh.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Set;
import java.util.PriorityQueue;
import edu.brown.cs.madhavramesh.maps.MapNode;
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
    pq = new PriorityQueue<>(new DijkstraPqComparator(distances));

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
    while (!pq.isEmpty()) {
      //---------CACHING----------------
      Set<Way> cachedWays = null;
      try {
        cachedWays = RouteTriggerAction.getCache().get(currentVertex.getStringID());


        for (Way way : cachedWays) {
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
      System.out.println("End is unreachable.");
      return new String[0];
    } else {

      if (end.getStringID().equals(start.getStringID())) {
        System.out.println("Start node same as end node");
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