package edu.brown.cs.madhavramesh.maps;

import edu.brown.cs.madhavramesh.points.HasCoordinates;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
