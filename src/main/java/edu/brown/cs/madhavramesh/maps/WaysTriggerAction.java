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
        List<String> targetIDs = waysWithinBoundSQL(lat1, lon1, lat2, lon2);
        for (String i : targetIDs) {
          result.append(i).append("\n");
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

  public ArrayList<String> waysWithinBoundSQL(Double lat2, Double lon2, Double lat1, Double lon1)
          throws SQLException {
    Connection conn = Maps.getConnection();
    Statement stat = conn.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys=ON;");
    PreparedStatement prep = conn.prepareStatement(
            "SELECT * FROM way INNER JOIN node as src on way.start = src.id"
        + "INNER JOIN node as dest on way.end = dest.id "
                + "WHERE (((src.latitude >= ?) AND (src.longitude <= ?)"
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
    ArrayList<String> ways = new ArrayList<>();
    while (rs.next()) {
      ways.add(rs.getString(1));
    }
    Collections.sort(ways);

    return ways;
  }
}
