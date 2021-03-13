package edu.brown.cs.madhavramesh.maps;

import edu.brown.cs.madhavramesh.graph.GraphEdge;
import edu.brown.cs.madhavramesh.graph.GraphNode;
import edu.brown.cs.madhavramesh.points.HasCoordinates;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Class that represents a way.
 */
public class Way {

  private String wayID;
  private double weight;
  private MapNode target;

  /**
   * Way constructor that sets class variables to passed in varialbes.
   * @param wayID wayID
   * @param target target node
   * @param weight weight
   */
  public Way(String wayID, MapNode target, double weight) {
    this.wayID = wayID;
    this.weight = weight;
    this.target = target;
  }

  /**
   * getter for weight variable.
   * @return double representing weight
   */
  public double getWeight() {
    return weight;
  }

  /**
   * setter for weight.
   * @param weight double that represents what the weight will be set to
   */
  public void setWeight(double weight) {
    this.weight = weight;
  }

  /**
   * getter for target MapNode.
   * @return the mapNode that is the target
   */
  public MapNode getTarget() {
    return target;
  }

  /**
   * setter for target.
   * @param target mapNode to set the target field to
   */
  public void setTarget(MapNode target) {
    this.target = target;
  }

  /**
   * getter for way ID.
   * @return string representing way ID
   */
  public String getWayID() {
    return wayID;
  }

  /**
   * custom equals function to check if two ways are the same.
   * @param obj the way
   * @return a boolean representing if the two objects are equal
   */
  @Override
  public boolean equals(Object obj) {

    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Way)) {
      return false;
    }
    Way toCompare = (Way) obj;
    return this.getWayID().equals(toCompare.getWayID());
  }

  @Override
  public int hashCode() {
    return this.wayID.hashCode();
  }

//  @Override
//  public GraphNode from() {
//    return null;
//  }
//
//  @Override
//  public GraphNode to() {
//    return this.target;
//  }
//
//  @Override
//  public double weight() {
//    return this.weight;
//  }

//  /**
//   * Compares this object with the specified object for order.  Returns a
//   * negative integer, zero, or a positive integer as this object is less
//   * than, equal to, or greater than the specified object.
//   *
//   * <p>The implementor must ensure
//   * {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))}
//   * for all {@code x} and {@code y}.  (This
//   * implies that {@code x.compareTo(y)} must throw an exception iff
//   * {@code y.compareTo(x)} throws an exception.)
//   *
//   * <p>The implementor must also ensure that the relation is transitive:
//   * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
//   * {@code x.compareTo(z) > 0}.
//   *
//   * <p>Finally, the implementor must ensure that {@code x.compareTo(y)==0}
//   * implies that {@code sgn(x.compareTo(z)) == sgn(y.compareTo(z))}, for
//   * all {@code z}.
//   *
//   * <p>It is strongly recommended, but <i>not</i> strictly required that
//   * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
//   * class that implements the {@code Comparable} interface and violates
//   * this condition should clearly indicate this fact.  The recommended
//   * language is "Note: this class has a natural ordering that is
//   * inconsistent with equals."
//   *
//   * <p>In the foregoing description, the notation
//   * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
//   * <i>signum</i> function, which is defined to return one of {@code -1},
//   * {@code 0}, or {@code 1} according to whether the value of
//   * <i>expression</i> is negative, zero, or positive, respectively.
//   *
//   * @param o the object to be compared.
//   * @return a negative integer, zero, or a positive integer as this object
//   * is less than, equal to, or greater than the specified object.
//   * @throws NullPointerException if the specified object is null
//   * @throws ClassCastException   if the specified object's type prevents it
//   *                              from being compared to this object.
//   */
//  @Override
//  public int compareTo(Object o) {
//    return 0;
//  }
}


///**
// * Specific type of GraphEdge containing an ID, name, and type.
// */
//public class Way implements GraphEdge<MapNode, Way>, HasCoordinates {
//
//  private final int dimensionCount = 4;
//  private String iD;
//  private String name;
//  private String type;
//  private MapNode start;
//  private MapNode end;
//  private double weight;
//
//  /**
//   * Initializes edge by finding starting node and ending node using
//   * SQL queries to search a database.
//   *
//   * @param i ID
//   * @param n Name
//   * @param t Type
//   * @param s String corresponding to starting node ID
//   * @param e String corresponding to ending node ID
//   * @throws SQLException if not found
//   */
//  public Way(String i, String n, String t, String s, String e)
//          throws SQLException {
//    iD = i;
//    name = n;
//    type = t;
//
//    try {
//      Connection conn = Maps.getConnection();
//      Statement stat = conn.createStatement();
//      stat.executeUpdate("PRAGMA foreign_keys=ON;");
//      PreparedStatement prep = conn.prepareStatement(
//              "SELECT DISTINCT * FROM 'node' WHERE (node.id =?);");
//      prep.setString(1, s);
//      ResultSet startNode = prep.executeQuery();
//
//      String nodeiD = startNode.getString(1);
//      double nodeLat = Double.parseDouble(startNode.getString(2));
//      double nodeLong = Double.parseDouble(startNode.getString(3));
//
//      startNode.close();
//
//      start = new LocationNode(nodeiD, nodeLat, nodeLong);
//
//      prep.setString(1, e);
//      ResultSet endNode = prep.executeQuery();
//
//      nodeiD = endNode.getString(1);
//      nodeLat = Double.parseDouble(endNode.getString(2));
//      nodeLong = Double.parseDouble(endNode.getString(3));
//
//      endNode.close();
//      prep.close();
//      stat.close();
//
//      end = new LocationNode(nodeiD, nodeLat, nodeLong);
//
//      weight = LocationNode.haversineDist(start, end);
//    } catch (SQLException err) {
//      throw new SQLException("ERROR: SQL Exception");
//    }
//  }
//
//  /**
//   * @return Start node of edge
//   */
//  @Override
//  public LocationNode from() {
//    return start;
//  }
//
//  /**
//   * @return End node of edge
//   */
//  @Override
//  public LocationNode to() {
//    return end;
//  }
//
//  /**
//   * @return Haversine distance from start to end node of edge
//   */
//  @Override
//  public double weight() {
//    return weight;
//  }
//
//  /**
//   * @return ID of edge
//   */
//  public String getiD() {
//    return iD;
//  }
//
//  /**
//   * @return Name of edge
//   */
//  public String getName() {
//    return name;
//  }
//
//  /**
//   * @return Type of edge
//   */
//  public String getType() {
//    return type;
//  }
//
//  /**
//   * @return Number of possible coordinate values edge has
//   */
//  public int getNumDimensions() {
//    return dimensionCount;
//  }
//
//  /**
//   * Finds the value of the coordinate corresponding to the given axis.
//   *
//   * @param d Axis of coordinate
//   * @return Value corresponding to given coordinate
//   */
//  public double getCoordinate(int d) {
//    int dimension = d % dimensionCount;
//    double dimensionValue = 0;
//    switch (dimension) {
//      case 0:
//        dimensionValue = this.start.getLatitude();
//        break;
//      case 1:
//        dimensionValue = this.start.getLongitude();
//        break;
//      case 2:
//        dimensionValue = this.end.getLatitude();
//        break;
//      case 3:
//        dimensionValue = this.end.getLongitude();
//        break;
//      default:
//        break;
//    }
//    return dimensionValue;
//  }
//
//  /**
//   * @return Returns a List of all the coordinates
//   */
//  @Override
//  public List<Double> getCoordinates() {
//    List<Double> coords = Arrays.asList(new Double[]{this.start.getLatitude(),
//            this.start.getLongitude(), this.end.getLatitude(), this.end.getLongitude()});
//    return coords;
//  }
//
//  /**
//   * Compares two GraphEdges and returns which one is greater.
//   *
//   * @param o GraphEdge to compare to
//   * @return -1 if this GraphEdge is less than one passed in; 1 if this
//   * GraphEdge is greater than one passed in; 0 if both are equal
//   */
//  @Override
//  public int compareTo(Way o) {
//    return this.iD.compareTo(o.getiD());
//  }
//}
