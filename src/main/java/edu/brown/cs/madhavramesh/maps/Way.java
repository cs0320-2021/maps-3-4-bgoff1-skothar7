package edu.brown.cs.madhavramesh.maps;

import edu.brown.cs.madhavramesh.graph.GraphEdge;
import edu.brown.cs.madhavramesh.points.HasCoordinates;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Specific type of GraphEdge containing an ID, name, and type.
 */
public class Way implements GraphEdge<LocationNode, Way>, HasCoordinates {

  private final int dimensionCount = 4;
  private String iD;
  private String name;
  private String type;
  private LocationNode start;
  private LocationNode end;
  private double weight;

  /**
   * Initializes edge by finding starting node and ending node using
   * SQL queries to search a database.
   *
   * @param i ID
   * @param n Name
   * @param t Type
   * @param s String corresponding to starting node ID
   * @param e String corresponding to ending node ID
   * @throws SQLException if not found
   */
  public Way(String i, String n, String t, String s, String e)
          throws SQLException {
    iD = i;
    name = n;
    type = t;

    try {
      Connection conn = Maps.getConnection();
      Statement stat = conn.createStatement();
      stat.executeUpdate("PRAGMA foreign_keys=ON;");
      PreparedStatement prep = conn.prepareStatement(
              "SELECT DISTINCT * FROM 'node' WHERE (node.id =?);");
      prep.setString(1, s);
      ResultSet startNode = prep.executeQuery();

      String nodeiD = startNode.getString(1);
      double nodeLat = Double.parseDouble(startNode.getString(2));
      double nodeLong = Double.parseDouble(startNode.getString(3));

      startNode.close();

      start = new LocationNode(nodeiD, nodeLat, nodeLong);

      prep.setString(1, e);
      ResultSet endNode = prep.executeQuery();

      nodeiD = endNode.getString(1);
      nodeLat = Double.parseDouble(endNode.getString(2));
      nodeLong = Double.parseDouble(endNode.getString(3));

      endNode.close();
      prep.close();
      stat.close();

      end = new LocationNode(nodeiD, nodeLat, nodeLong);

      weight = LocationNode.haversineDist(start, end);
    } catch (SQLException err) {
      throw new SQLException("ERROR: SQL Exception");
    }
  }

  /**
   * @return Start node of edge
   */
  @Override
  public LocationNode from() {
    return start;
  }

  /**
   * @return End node of edge
   */
  @Override
  public LocationNode to() {
    return end;
  }

  /**
   * @return Haversine distance from start to end node of edge
   */
  @Override
  public double weight() {
    return weight;
  }

  /**
   * @return ID of edge
   */
  public String getiD() {
    return iD;
  }

  /**
   * @return Name of edge
   */
  public String getName() {
    return name;
  }

  /**
   * @return Type of edge
   */
  public String getType() {
    return type;
  }

  /**
   * @return Number of possible coordinate values edge has
   */
  public int getNumDimensions() {
    return dimensionCount;
  }

  /**
   * Finds the value of the coordinate corresponding to the given axis.
   *
   * @param d Axis of coordinate
   * @return Value corresponding to given coordinate
   */
  public double getCoordinate(int d) {
    int dimension = d % dimensionCount;
    double dimensionValue = 0;
    switch (dimension) {
      case 0:
        dimensionValue = this.start.getLatitude();
        break;
      case 1:
        dimensionValue = this.start.getLongitude();
        break;
      case 2:
        dimensionValue = this.end.getLatitude();
        break;
      case 3:
        dimensionValue = this.end.getLongitude();
        break;
      default:
        break;
    }
    return dimensionValue;
  }

  /**
   * @return Returns a List of all the coordinates
   */
  @Override
  public List<Double> getCoordinates() {
    List<Double> coords = Arrays.asList(new Double[]{this.start.getLatitude(),
            this.start.getLongitude(), this.end.getLatitude(), this.end.getLongitude()});
    return coords;
  }

  /**
   * Compares two GraphEdges and returns which one is greater.
   *
   * @param o GraphEdge to compare to
   * @return -1 if this GraphEdge is less than one passed in; 1 if this
   * GraphEdge is greater than one passed in; 0 if both are equal
   */
  @Override
  public int compareTo(Way o) {
    return this.iD.compareTo(o.getiD());
  }
}
