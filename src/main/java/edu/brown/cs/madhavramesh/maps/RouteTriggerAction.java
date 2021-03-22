package edu.brown.cs.madhavramesh.maps;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import edu.brown.cs.madhavramesh.graph.Dijkstra;
import edu.brown.cs.madhavramesh.graph.DirectedGraph;
import edu.brown.cs.madhavramesh.kdtree.KDTree;
import edu.brown.cs.madhavramesh.stars.TriggerAction;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/** Calculates shortest route from one node to another in a map. */
public class RouteTriggerAction implements TriggerAction {
  private final int argsCount = 4;
  private String ultimateEndID;
  private static LoadingCache<String, Set<Way>> cache;

  /**
   * Command user types to execute this action.
   *
   * @return Command
   */
  @Override
  public String command() {
    return "route";
  }

  /**
   * Uses dijkstra's algorithm to calculate the shortest path from
   * the traversable node closest to the first set of latitude and longitudes
   * to the traversable node closest to the second set of latitude and longitude.
   *
   * @param args   Arguments given by user indicating latitudes and longitudes to use.
   *               Can be either doubles or strings
   * @param isREPL True if output will be printed in REPL, false if output will be
   *               printed in GUI
   * @return String in format "nodeID1 → nodeID2 : wayID..." corresponding to the
   * shortest path from a start node to destination node. If no path exists, returns
   * "nodeID1 -/- nodeID2"
   */
  @Override
  public String execute(String[] args, boolean isREPL) {
    String result = "";
    CacheLoader<String, Set<Way>> loader;
    loader = new CacheLoader<String, Set<Way>>() {
      @Override
      public Set<Way> load(String nodeID) {
        return queryWays(nodeID);
      }
    };

    this.cache = CacheBuilder.newBuilder().build(loader);
    try {
      MapNode[] startAndEnd = checkArgsAreIntsOrStrings(args);
      MapNode start = startAndEnd[0];
      MapNode end = startAndEnd[1];

      result = callDijkstra(start, end, isREPL);
      //assert result.length() > 1;

//      GraphAlgorithms<MapNode, Way> ga = new GraphAlgorithms<>();
//      Stack<Way> shortestPath = ga.aStar(start, end, new HaversineHeuristic());
//
//      if (shortestPath.isEmpty()) {
//        result.append(start.getStringID() + " -/- " + end.getStringID() + "\n");
//      }
//
//      while (!shortestPath.isEmpty()) {
//        Way curEdge = shortestPath.pop();
//        result.append(curEdge.from().getiD() + " -> " + curEdge.to().getiD() + " : "
//            + curEdge.getiD() + "\n");
//      }

    } catch (NumberFormatException e) {
      System.err.println("ERROR: Arguments provided after route were not decimals or strings");
    } catch (NullPointerException e) {
      e.printStackTrace();
      System.err.println("ERROR: Map data from database must be loaded first");
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("ERROR: Could not run routes command");
    } finally {
      return result;
    }
  }

  /**
   * Checks if arguments are integers or strings and returns appropriate starting and ending node.
   *
   * @param args Arguments given by user indicating latitudes and longitudes to use.
   *             Can be either doubles or strings
   * @return Array of MapNode where first element in array is the start node and
   * second element is the ending node
   * @throws SQLException             Error executing query on database
   * @throws IllegalArgumentException Strings are given as arguments and no node ids in database
   *                                  correspond to the given Strings
   */
  public MapNode[] checkArgsAreIntsOrStrings(String[] args)
      throws SQLException, IllegalArgumentException {
    try {
      double lat1d = Double.parseDouble(args[0]);
      double long1d = Double.parseDouble(args[1]);
      double lat2d = Double.parseDouble(args[2]);
      double long2d = Double.parseDouble(args[3]);

      KDTree<MapNode> currentNodes = Maps.getNodesTree();

      MapNode start = NearestTriggerAction.closestNode(lat1d, long1d, currentNodes);
      MapNode end = NearestTriggerAction.closestNode(lat2d, long2d, currentNodes);
      System.out.println("yo "+lat1d+" "+long1d+" "+lat2d+" "+long2d);
      return new MapNode[]{start, end};
    } catch (NumberFormatException e) {
      String street1 = args[0].replaceAll("\"", "");
      String crossStreet1 = args[1].replaceAll("\"", "");
      String street2 = args[2].replaceAll("\"", "");
      String crossStreet2 = args[3].replaceAll("\"", "");

      Connection conn = Maps.getConnection();
      Statement stat = conn.createStatement();
      stat.executeUpdate("PRAGMA foreign_keys=ON;");

      PreparedStatement prep = conn.prepareStatement(
          "SELECT DISTINCT node.id, node.latitude, node.longitude FROM node "
              + "INNER JOIN way AS w1 ON node.id = w1.start "
              + "INNER JOIN way AS w2 ON (w1.start = w2.start OR w1.start = w2.end) "
              + "AND w1.name =? AND w2.name =?"
              + "UNION SELECT DISTINCT node.id, node.latitude, node.longitude FROM node "
              + "INNER JOIN way AS w3 ON node.id = w3.end "
              + "INNER JOIN way AS w4 ON (w3.end = w4.start OR w3.end = w4.end) "
              + "AND w3.name =? AND w4.name =?");
      prep.setString(1, street1);
      prep.setString(2, crossStreet1);
      prep.setString(3, street1);
      prep.setString(4, crossStreet1);
      ResultSet rs1 = prep.executeQuery();

      String id1s = null;
      Double lat1s = null;
      Double long1s = null;
      while (rs1.next()) {
        id1s = rs1.getString(1);
        lat1s = rs1.getDouble(2);
        long1s = rs1.getDouble(3);
      }

      rs1.close();

      prep.setString(1, street2);
      prep.setString(2, crossStreet2);
      prep.setString(3, street2);
      prep.setString(4, crossStreet2);
      ResultSet rs2 = prep.executeQuery();

      String id2s = null;
      Double lat2s = null;
      Double long2s = null;
      while (rs2.next()) {
        id2s = rs2.getString(1);
        lat2s = rs2.getDouble(2);
        long2s = rs2.getDouble(3);
      }

      rs2.close();
      prep.close();
      stat.close();

      if (id1s != null && lat1s != null && long1s != null
          && id2s != null && lat2s != null && long2s != null) {
        MapNode start = new MapNode(id1s, lat1s, long1s);
        MapNode end = new MapNode(id2s, lat2s, long2s);

        MapNode[] startAndEnd = new MapNode[]{start, end};

        return startAndEnd;
      } else {
        throw new IllegalArgumentException("ERROR: No nodes exist at intersections of streets");
      }
    }
  }

  /**
   * Number of arguments user is allowed to type in for route.
   *
   * @return Array of possible numbers of arguments
   */
  @Override
  public int[] getNumParameters() {
    return new int[]{argsCount};
  }

  public String callDijkstra(MapNode start, MapNode end, boolean isRepl) {

    ultimateEndID = end.getStringID();

    System.out.println("dg size: "+Maps.getDg().size());
    Dijkstra dijkstra = new Dijkstra(Maps.getDg(), start, end);


// Run what is supposed to output something
    if (isRepl) {
      // Start capturing
      java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
      System.setOut(new java.io.PrintStream(out));
      dijkstra.findShortestPath();
      // Stop capturing
      System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
      // Use captured content
      String content = out.toString();
      assert content!= null;
      return content;
      //buffer.reset();
    } else {
      System.out.println("routetriggeraction:231 " + String.join(";", dijkstra.findShortestPathGUI()));
      return String.join(";", dijkstra.findShortestPathGUI());
    }



  }

  /**
   * Method that queries way data and coordinates given a node.
   *
   * @param nodeID
   */
  private Set<Way> queryWays(String nodeID) {
    //conn = MapLoader.getConn();
    Connection conn = Maps.getConnection();

    PreparedStatement prep = null;
    try {

      prep = conn.prepareStatement(
          "SELECT way.id, way.name, node.id, node.latitude, node.longitude FROM way JOIN node "
              + "WHERE way.type != '' AND way.type!= 'unclassified' AND way.start = " + "\"" + nodeID
              + "\"" + " AND node.id = way.end");

      ResultSet rs = prep.executeQuery();

      while (rs.next()) {
        //coords = new ArrayList<>();
        String wayID = rs.getString(1); // way ID
        String name = rs.getString(2);  // name
        String endNodeId = rs.getString(3);  // end node ID

        Maps.getDg().addEdge(wayID, Maps.getIdToNodeMap().get(nodeID), Maps.getIdToNodeMap().get(endNodeId),
            Maps.getIdToNodeMap().get(ultimateEndID));
      }
      return Maps.getIdToNodeMap().get(nodeID).getWays();

    } catch (SQLException throwables) {
      return new HashSet<Way>();
    }
  }

  public static LoadingCache<String, Set<Way>> getCache() {
    return RouteTriggerAction.cache;
  }

}
