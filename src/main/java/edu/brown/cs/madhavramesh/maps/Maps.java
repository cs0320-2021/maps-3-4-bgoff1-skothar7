
package edu.brown.cs.madhavramesh.maps;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import edu.brown.cs.madhavramesh.checkins.CheckinThread;
import edu.brown.cs.madhavramesh.graph.DirectedGraph;
import edu.brown.cs.madhavramesh.kdtree.KDTree;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public final class Maps {
  private Maps() {

  }

  private static Connection conn;
  private static KDTree<MapNode> nodesTree;
  private static DirectedGraph dg;
  private static HashMap idToNodeMap;
  //private static final LoadingCache<String, Collection<Way>> EDGESCACHE = createEdgesCache();

  /**
   * Returns KDTree of nodes associated with the currently loaded SQL database.
   *
   * @return KDTree representation of nodes table in currently loaded SQL database.
   */
  public static KDTree<MapNode> getNodesTree() {
    return nodesTree;
  }

  /**
   * Returns connection object to the currently loaded SQL database.
   *
   * @return connection to current SQL database
   */
  public static Connection getConnection() {
    return conn;
  }

  /**
   * Sets KDTree of nodes associated with the currently loaded SQL database.
   *
   * @param nodesTree representation of nodes table in currently loaded SQL database.
   */
  public static void setNodesTree(KDTree<MapNode> nodesTree) {
    Maps.nodesTree = nodesTree;
  }

  public static void addVertexToDg(MapNode mapNode){
    dg.addVertex(mapNode);
  }

  public static DirectedGraph getDg() {
    //DirectedGraph returnGraph = new DirectedGraph();
    //Set<MapNode> returnSet = new HashSet<>();
    //for (MapNode value : dg.getVerticesSet()) {
    //  returnSet.add(value);
    //}
    //returnGraph.setVerticesSet(new HashSet<>(returnSet));
    return dg;
  }

  public static void addIdToNode(String id, MapNode mapNode){
    idToNodeMap.put(id, mapNode);
  }

  public static HashMap<String, MapNode> getIdToNodeMap() {
    return idToNodeMap;
  }

  public static void setDg(DirectedGraph dg) {
    Maps.dg = dg;
  }

  public static void setIdToNodeMap(HashMap idToNodeMap) {
    Maps.idToNodeMap = idToNodeMap;
  }

  /**
   * Sets connection to the SQL database at the inputted path.
   *
   * @param url path to the currently connected SQL database
   * @throws SQLException           if not found
   * @throws ClassNotFoundException if not found
   */
  public static void setConnection(String url)
          throws SQLException, ClassNotFoundException {
    try {
      Class.forName("org.sqlite.JDBC");
      String urlToDB = "jdbc:sqlite:" + url;
      conn = DriverManager.getConnection(urlToDB);
    } catch (ClassNotFoundException e) {
      throw new ClassNotFoundException("ERROR: Class not found");
    }
  }

//  /**
//   * Gets the edges coming out of a node by retrieving them from cache if
//   * found or calculating them otherwise.
//   *
//   * @param id ID of node whose edges need to be retrieved
//   * @return Edges coming out of provided node
//   * @throws ExecutionException Unable to retrieve value from cached
//   */
//  public static Collection<Way> getFromEdgesCache(String id)
//          throws ExecutionException {
//    return EDGESCACHE.get(id);
//  }

//  /**
//   * Creates a LoadingCache for edges coming out of a Node where the size of the cache
//   * is limited to 5000 elements and elements expire after 1 minute.
//   *
//   * @return CacheLoader containing a newly created Cache
//   */
//  private static LoadingCache<String, Collection<Way>> createEdgesCache() {
//    final int maxCacheSize = 100000;
//    return CacheBuilder.newBuilder()
//            .maximumSize(maxCacheSize)
//            .expireAfterWrite(5, TimeUnit.MINUTES)
//            .build(new CacheLoader<>() {
//              @Override
//              public Collection<Way> load(String id) throws Exception {
//                Statement stat = conn.createStatement();
//                stat.executeUpdate("PRAGMA foreign_keys=ON;");
//
//                PreparedStatement prep = conn.prepareStatement(
//                        "SELECT DISTINCT * FROM 'way' WHERE "
//                                + "(way.start =? AND way.type !='' "
//                            + "AND way.type !='unclassified');");
//                prep.setString(1, id);
//                ResultSet rs = prep.executeQuery();
//
//                Collection<Way> newEdges = new ArrayList<>();
//                while (rs.next()) {
//                  String wayiD = rs.getString(1);
//                  String wayName = rs.getString(2);
//                  String wayType = rs.getString(3);
//                  String wayStart = rs.getString(4);
//                  String wayEnd = rs.getString(5);
//                  newEdges.add(new Way(wayiD, wayName, wayType, wayStart, wayEnd));
//                }
//
//                rs.close();
//                prep.close();
//                stat.close();
//
//                return newEdges;
//              }
//            });
//  }
}
