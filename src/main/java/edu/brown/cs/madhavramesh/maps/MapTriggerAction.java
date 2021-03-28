package edu.brown.cs.madhavramesh.maps;

import edu.brown.cs.madhavramesh.graph.DirectedGraph;
import edu.brown.cs.madhavramesh.kdtree.KDTree;
import edu.brown.cs.madhavramesh.stars.TriggerAction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** Executes map command and loads database as kd-tree. */
public class MapTriggerAction implements TriggerAction {

  private List<MapNode> nodes = new ArrayList<>();
  private List<Way> ways = new ArrayList<>();
  private final int argsCount = 1;

  /**
   * Returns command matching this TriggerAction.
   *
   * @return Command
   */
  public String command() {
    return "map";
  }

  /**
   * Returns number of arguments that execute can take in.
   *
   * @return Possible numbers of arguments
   */
  public int[] getNumParameters() {
    return new int[]{argsCount};
  }

  /**
   * Reads SQL file and loads the traversable nodes into data.
   *
   * @param args Arguments used to read SQL file
   * @param isREPL True if result is meant to be printed in terminal, false if meant
   *               to be printed in GUI
   * @return String result of Map command
   */
  public String execute(String[] args, boolean isREPL) {
    StringBuilder result = new StringBuilder();
    try {
      Maps.setConnection(args[0]);

      Class.forName("org.sqlite.JDBC");
      String urlToDB = "jdbc:sqlite:" + args[0];
      Connection conn = DriverManager.getConnection(urlToDB);
      Statement stat = conn.createStatement();
      stat.executeUpdate("PRAGMA foreign_keys=ON;");
      PreparedStatement prep = conn.prepareStatement(
              "SELECT DISTINCT node.id, node.latitude, node.longitude FROM way JOIN node "
                      + "WHERE (way.start = node.id OR way.end = node.id) AND way.type !='' "
                      + "AND way.type !='unclassified';");
      ResultSet rs = prep.executeQuery();
      MapNode currentNode;
      nodes.clear();
      DirectedGraph dg = new DirectedGraph();

      HashMap<String, MapNode> idToNodeMap = new HashMap();
      while (rs.next()) {
        String iD = rs.getString(1);
        Double lat = rs.getDouble(2);
        Double lon = rs.getDouble(3);
        currentNode = new MapNode(iD, lat, lon);
        nodes.add(currentNode);
        dg.addVertex(currentNode);
        idToNodeMap.put(currentNode.getStringID(), currentNode);
      }
      Maps.setNodesTree(new KDTree<MapNode>(nodes));
      Maps.setIdToNodeMap(idToNodeMap);
      Maps.setDg(dg);
      rs.close();
      prep.close();

      result.append("map set to " + args[0] + "\n");
    } catch (SQLException e) {
      System.err.println("ERROR: Could not connect to SQL database or could not read data");
    } catch (ClassNotFoundException e) {
      System.err.println("ERROR: Problem with SQL setup or commands");
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("ERROR: Could not run map command");
    } finally {
      return result.toString();
    }
  }
}
