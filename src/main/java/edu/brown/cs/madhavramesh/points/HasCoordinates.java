package edu.brown.cs.madhavramesh.points;

import java.util.List;

/** Implemented by any object that has a position associated with it. */
public interface HasCoordinates {
  int getNumDimensions();
  double getCoordinate(int d);
  List<Double> getCoordinates();
  default double dist(HasCoordinates obj) {
    double sumSquares = 0;
    for (int i = 0; i < getNumDimensions(); i++) {
      try {
        sumSquares += Math.pow(getCoordinate(i) - obj.getCoordinate(i), 2);
      } catch (IndexOutOfBoundsException e) {
        continue;
      }
    }
    return Math.sqrt(sumSquares);
  }
}
