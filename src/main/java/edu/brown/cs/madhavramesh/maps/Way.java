package edu.brown.cs.madhavramesh.maps;

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

}

