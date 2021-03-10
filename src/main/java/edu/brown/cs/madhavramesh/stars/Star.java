package edu.brown.cs.madhavramesh.stars;

import edu.brown.cs.madhavramesh.points.Point;

/** Star with name and id properties in addition to
 * x, y, z coordinates.
 */
public class Star extends Point {
  private int id;
  private String name;

  /** Constructs a star from the given id, name, and coordinates.
   * @param id ID
   * @param name Name
   * @param x X coordinate
   * @param y Y coordinate
   * @param z Z coordinate
   */
  public Star(int id, String name, double x, double y, double z) {
    super(x, y, z);
    this.id = id;
    this.name = name;
  }

  public int getID() {
    return id;
  }

  public String getName() {
    return name;
  }

  public double getX() {
    return super.getCoordinate(0);
  }

  public double getY() {
    return super.getCoordinate(1);
  }

  public double getZ() {
    return super.getCoordinate(2);
  }
}
