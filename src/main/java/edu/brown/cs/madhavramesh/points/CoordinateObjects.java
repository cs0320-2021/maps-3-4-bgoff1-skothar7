package edu.brown.cs.madhavramesh.points;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/** List of objects with a position.
 * @param <T> Type of object in list
 */
public class CoordinateObjects<T extends HasCoordinates> {
  private List<T> points;

  /** Construct a CoordinateObjects containing the objects from another List.
   * @param points List whose objects should be stored in this CoordinateObjects
   */
  public CoordinateObjects(List<T> points) {
    this.points = new ArrayList<>(points);
  }

  /** Finds a nonnegative, integral number of neighbors next to a
   * certain object.
   * @param numToFind Number of neighbors to find
   * @param searchPos Object whose position is the target position
   * @return List of points that are neighbors
   */
  public List<T> kNearestNeighborsNaive(int numToFind, HasCoordinates searchPos)
      throws IllegalArgumentException {
    if (numToFind < 0) {
      throw new IllegalArgumentException("ERROR: Number of nearest "
          + "neighbors to find must be a positive integer");
    }
    List<T> pointsCopy = new ArrayList<>(points);
    Collections.shuffle(pointsCopy);
    Collections.sort(pointsCopy, new PointComparator(searchPos));

    if (numToFind >= pointsCopy.size()) {
      return pointsCopy;
    }

    return pointsCopy.subList(0, numToFind);
  }

  /** Finds all neighbors within a certain radius of a point.
   * @param r Radius
   * @param searchPos Object whose position is the target position
   * @return List of points that are neighbors
   */
  public List<T> nearestNeighborsInRadiusNaive(double r, HasCoordinates searchPos)
    throws IllegalArgumentException {
    if (r < 0) {
      throw new IllegalArgumentException("ERROR: Radius must be "
      + "a positive integer");
    }
    List<T> pointsCopy = new ArrayList<>(points);
    Collections.shuffle(pointsCopy);

    pointsCopy.removeIf(p -> searchPos.dist(p) > r);

    Collections.sort(pointsCopy, new PointComparator(searchPos));

    return pointsCopy;
  }

  /** Adds an object with a position to existing List of points.
   * @param p Point to add
   */
  public void addPoint(T p) {
    points.add(p);
  }

  public List<T> getPoints() {
    return new ArrayList<>(points);
  }
}
