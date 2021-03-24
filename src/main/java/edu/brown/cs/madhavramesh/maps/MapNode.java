package edu.brown.cs.madhavramesh.maps;

import edu.brown.cs.madhavramesh.graph.GraphNode;
import edu.brown.cs.madhavramesh.points.HasCoordinates;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Collection;
import java.util.Collections;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

/**
 * Class that represents a MapNode which is of type KDObject to make more generic.
 */
public class MapNode implements HasCoordinates {

  private String stringID;
  private List<Double> coords;
  private Set<Way> ways;
  private MapNode parent;
  private int id;

  /**
   * MopNode constructor to instantiate class variables.
   * @param id MapNode id
   * @param lat MapNode latitude
   * @param lon MapNode longitude
   */
  public MapNode(String id, double lat, double lon) {
    this.stringID = id;
    this.coords = new ArrayList<Double>();
    this.coords.add(lat);
    this.coords.add(lon);
    this.parent = null;
    this.ways = new HashSet<>();
  }


  public MapNode copy() {
    return new MapNode(this.stringID, this.getCoordinate(0), this.getCoordinate(1));
  }
//  /**
//   * getter method for coordinates.
//   * @return list representing coordinates
//   */
//  @Override
//  public List<Double> getCoords() {
//    return coords;
//  }
//  /**
//   * getter method for id.
//   * @return integer representing id
//   */
//  @Override
//  public int getID() {
//    return id;
//  }

//  /**
//   * getter method for name.
//   * @return String representing name. For MapNode, the name is empty since there is no name
//   */
//  @Override
//  public String getName() {
//    return "";
//  }

//  public int getId() {
//    return id;
//  }
//
//  public void setId(int id) {
//    this.id = id;
//  }
  /**
   * getter method for ways.
   * @return set representing ways
   */
  public Set<Way> getWays() {
    return this.ways;
  }

  /**
   * adds a new way to the set of ways.
   * @param newWay represents a newWay to be added to the way set
   */
  public void addWay(Way newWay) {
    this.ways.add(newWay);
  }

  public void setWays(Set<Way> ways) {
    this.ways = ways;
  }

  public void setCoords(List<Double> coords) {
    this.coords = coords;
  }

  /**
   * getter method for parent of MapNode.
   * @return MapNode representing the parent of the current Node
   */
  public MapNode getParent() {
    return parent;
  }

  /**
   * getter method for stringID. used for MapNodes since ids are formatted as strings instead of
   * ints like in stars.
   * @return String representing MapNode id
   */
  public String getStringID() {
    return stringID;
  }

  public void setStringID(String stringID) {
    this.stringID = stringID;
  }

  /**
   * setter method for Parent Node.
   * @param parent representing the MapNode which is the parent of the current Node
   */
  public void setParent(MapNode parent) {
    this.parent = parent;
  }

  /**
   * custom equals method which checks of two MapNodes are the same.
   * @param obj the object we want to compare with
   * @return a boolean which represents if the two objects are equal.
   */
  @Override
  public boolean equals(Object obj) {

    if (obj == this) {
      return true;
    }
    if (!(obj instanceof MapNode)) {
      return false;
    }
    MapNode toCompare = (MapNode) obj;
    if (this.getStringID().equals(toCompare.getStringID())) {
      return true;
    }
    return false;
  }

  /**
   * gets the stringID HashCode.
   * @return integer
   */
  @Override
  public int hashCode() {
    return this.stringID.hashCode();
  }

  @Override
  public int getNumDimensions() {
    return 2;
  }

    @Override
  public double getCoordinate(int d) {
    int dimension = d % getNumDimensions();
    if (dimension == 0) {
      return this.coords.get(0);
    } else {
      return this.coords.get(1);
    }
  }

  /**
   * getter method for coordinates.
   * @return list representing coordinates
   */
  @Override
  public List<Double> getCoordinates() {
    return this.coords;
  }
}

///**
// * Specific type of GraphNode containing an ID, latitude, and longitude.
// */
//public class LocationNode implements GraphNode<LocationNode, Way>, HasCoordinates {
//
//  private final int dimensionCount = 2;
//  private final String iD;
//  private final double latitude;
//  private final double longitude;
//
//  /**
//   * Initializes node using ID, latitude, and longitude.
//   *
//   * @param i ID
//   * @param lat Latitude of node
//   * @param lon Longitude of node
//   */
//  public LocationNode(String i, double lat, double lon) {
//    iD = i;
//    latitude = lat;
//    longitude = lon;
//  }
//
//  /**
//   * @return Number of possible coordinate values node has
//   */
//  @Override
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
//  @Override
//  public double getCoordinate(int d) {
//    int dimension = d % dimensionCount;
//    if (dimension == 0) {
//      return latitude;
//    } else {
//      return longitude;
//    }
//  }
//
//  /**
//   * @return Latitude of node
//   */
//  public double getLatitude() {
//    return getCoordinate(0);
//  }
//
//  /**
//   * @return Longitude of node
//   */
//  public double getLongitude() {
//    return getCoordinate(1);
//  }
//
//  /**
//   * @return Returns a List of all the coordinates
//   */
//  @Override
//  public List<Double> getCoordinates() {
//    return Arrays.asList(this.latitude, this.longitude);
//  }
//
//  /**
//   * @return ID of node
//   */
//  public String getiD() {
//    return iD;
//  }
//
//  /**
//   * Calculates haversine distance between two Nodes.
//   *
//   * @param a LocationNode 1
//   * @param b LocationNode 2
//   * @return Distance as a decimal
//   */
//  public static double haversineDist(LocationNode a, LocationNode b) {
//    final double r = 6371;
//    double lat1 = Math.toRadians(a.getLatitude());
//    double lat2 = Math.toRadians(b.getLatitude());
//    double long1 = Math.toRadians(a.getLongitude());
//    double long2 = Math.toRadians(b.getLongitude());
//
//    double havLat = Math.pow(Math.sin((lat2 - lat1) / 2), 2);
//    double havLong = Math.pow(Math.sin((long2 - long1) / 2), 2);
//    double cosMult = Math.cos(lat1) * Math.cos(lat2);
//
//    return 2 * r * Math.asin(Math.sqrt(havLat + (cosMult * havLong)));
//  }
//
//  /**
//   * Retrieves all edges coming out of node from cache and calculates them otherwise.
//   *
//   * @return All edges coming out of node
//   */
//  @Override
//  public Collection<Way> getOutEdges() {
//    try {
//      return Maps.getFromEdgesCache(iD);
//    } catch (Exception e) {
//      return Collections.emptyList();
//    }
//  }
//
//  /**
//   * @return New hashcode made up of values for ID, latitude, and longitude
//   */
//  @Override
//  public int hashCode() {
//    return Objects.hash(iD, latitude, longitude);
//  }
//
//  /**
//   * 2 LocationNodes are equal if they have the same ID, latitude, and longitude.
//   *
//   * @param obj Other LocationNode to compare to
//   * @return true if both are equal, false otherwise
//   */
//  @Override
//  public boolean equals(Object obj) {
//    if (obj == null) {
//      return false;
//    }
//    if (!(obj instanceof LocationNode)) {
//      return false;
//    }
//    if (obj == this) {
//      return true;
//    }
//
//    LocationNode ln = (LocationNode) obj;
//    boolean equal = iD.equals(ln.getiD())
//            && Double.compare(latitude, ln.getLatitude()) == 0
//            && Double.compare(longitude, ln.getLongitude()) == 0;
//
//    return equal;
//  }
//
//  /**
//   * @return Returns the ID of node
//   */
//  @Override
//  public String toString() {
//    return iD;
//  }
//}
