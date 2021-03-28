package edu.brown.cs.madhavramesh.maps;

import edu.brown.cs.madhavramesh.stars.TriggerAction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Executes ways command and finds all ways inside
 * a given bounding box.
 */
public class WaysTriggerAction implements TriggerAction {

  private final int argsCount = 4;

  /**
   * Returns command matching this TriggerAction.
   *
   * @return Command
   */
  @Override
  public String command() {
    return "ways";
  }

  /**
   * Returns number of arguments that execute can take in.
   *
   * @return Possible numbers of arguments
   */
  @Override
  public int[] getNumParameters() {
    return new int[]{argsCount};
  }

  /**
   * Searches for all ways that fit inside of the defined bounding box.
   *
   * @param args   Arguments used to find relevant ways
   * @param isREPL True if result is meant to be printed in terminal, false if meant
   *               to be printed in GUI
   * @return String representing result
   */
  @Override
  public String execute(String[] args, boolean isREPL) {
    StringBuilder result = new StringBuilder();
    try {
      double lat1 = Double.parseDouble(args[0].trim());
      double lon1 = Double.parseDouble(args[1].trim());
      double lat2 = Double.parseDouble(args[2].trim());
      double lon2 = Double.parseDouble(args[3].trim());
      if (lat1 >= lat2 && lon1 <= lon2) {
        if (isREPL) {
          List<String> targetIDs = waysWithinBoundSQLREPL(lat1, lon1, lat2, lon2);
          for (String i : targetIDs) {
            result.append(i).append("\n");
          }
        } else {
          List<String> targetIDs = waysWithinBoundSQLGUI(lat1, lon1, lat2, lon2);
          int i = 1;
          result.append(targetIDs.get(0));
          for (i = 1; i < targetIDs.size(); i++) {
            result.append("\n").append(targetIDs.get(i));
          }
        }
      } else {
        System.err.println("ERROR: Point 1 was not northwest of Point 2");
      }
      return result.toString();
    } catch (NumberFormatException e) {
      System.err.println("ERROR: Arguments provided after ways were not doubles");
    } catch (NullPointerException e) {
      System.err.println("ERROR: Data must be loaded first");
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
    } catch (Exception e) {
      System.err.println("ERROR: Could not run ways command");
    } finally {
      return result.toString();
    }
  }

  public List<String> waysWithinBoundSQLGUI(Double lat2, Double lon2, Double lat1, Double lon1)
          throws SQLException {
    Connection conn = Maps.getConnection();
    Statement stat = conn.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys=ON;");
    PreparedStatement prep = conn.prepareStatement(
            "SELECT way.type, src.latitude, src.longitude, dest.latitude, dest.longitude "
        + "FROM way JOIN node as src "
        + "JOIN node as dest WHERE way.end = dest.id "
                + "AND way.start = src.id AND (((src.latitude >= ?) AND (src.longitude <= ?)"
                    + "AND (src.latitude <= ?) AND (src.longitude >= ?)) OR"
        + "((dest.latitude >= ?) AND (dest.longitude <= ?)"
        + "AND (dest.latitude <= ?) AND (dest.longitude >= ?)));");
    prep.setDouble(1, lat1);
    prep.setDouble(2, lon1);
    prep.setDouble(3, lat2);
    prep.setDouble(4, lon2);
    prep.setDouble(5, lat1);
    prep.setDouble(6, lon1);
    prep.setDouble(7, lat2);
    prep.setDouble(8, lon2);
    ResultSet rs = prep.executeQuery();
    List<String> ways = new ArrayList<>();
    StringBuilder current;
    while (rs.next()) {
      current = new StringBuilder();
      current.append(rs.getString(1)).append(",");
      current.append(rs.getString(2)).append(",");
      current.append(rs.getString(3)).append(",");
      current.append(rs.getString(4)).append(",");
      current.append(rs.getString(5));
      ways.add(current.toString());
    }

    return ways;
  }

  public List<String> waysWithinBoundSQLREPL(Double lat2, Double lon2, Double lat1, Double lon1)
      throws SQLException {
    Connection conn = Maps.getConnection();
    Statement stat = conn.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys=ON;");
    PreparedStatement prep = conn.prepareStatement(
        "SELECT DISTINCT way.id FROM way JOIN node "
            + "WHERE (way.start = node.id OR way.end = node.id) "
            + "AND ((node.latitude >= ?) AND (node.longitude <= ?)"
            + "AND (node.latitude <= ?) AND (node.longitude >= ?));");
    prep.setDouble(1, lat1);
    prep.setDouble(2, lon1);
    prep.setDouble(3, lat2);
    prep.setDouble(4, lon2);
    ResultSet rs = prep.executeQuery();
    List<String> ways = new ArrayList<>();
    while (rs.next()) {
      ways.add(rs.getString(1));
    }
    Collections.sort(ways);

    return ways;
  }
}
