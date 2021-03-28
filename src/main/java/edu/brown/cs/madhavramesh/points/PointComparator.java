package edu.brown.cs.madhavramesh.points;

import java.util.Comparator;

/** Orders objects with a position based on their distance from a reference position. */
public class PointComparator implements Comparator<HasCoordinates> {
  private HasCoordinates referencePoint;

  /**
   * @param referencePoint Reference point
   */
  public PointComparator(HasCoordinates referencePoint) {
    this.referencePoint = referencePoint;
  }

  /** Compares distances of two objects from reference position.
   * @param p1 First object
   * @param p2 Second object
   * @return 0 if p1 is equal to p2; 1 if p1 is greater than p2;
   * -1 if p1 is less than p2
   */
  @Override
  public int compare(HasCoordinates p1, HasCoordinates p2) {
    return Double.compare(p1.dist(referencePoint), p2.dist(referencePoint));
  }
}
