package edu.brown.cs.madhavramesh.points;

import edu.brown.cs.madhavramesh.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Point in n-dimensional Cartesian space. */
public class Point implements HasCoordinates {
  private final int numDimensions;
  private List<Double> coordinates;

  /** Constructs an n-dimensional point.
   * @param coordinates Values corresponding to x, y, z, etc.
   */
  public Point(Double...coordinates) {
    this.coordinates = Arrays.asList(coordinates);
    this.numDimensions = this.coordinates.size();
  }

  /** Constructs an n-dimensional point.
   * @param coordinates Values corresponding to x, y, z, etc.
   */
  public Point(List<Double> coordinates) {
    this.coordinates = new ArrayList<>(coordinates);
    this.numDimensions = this.coordinates.size();
  }

  /** Constructs a point from another point.
   * @param p Point whose coordinates should be copied
   */
  public Point(HasCoordinates p) {
    coordinates = new ArrayList<>(p.getCoordinates());
    this.numDimensions = p.getNumDimensions();
  }

  @Override
  public int getNumDimensions() {
    return numDimensions;
  }

  @Override
  public double getCoordinate(int d) {
    return coordinates.get(d);
  }

  @Override
  public List<Double> getCoordinates() {
    return new ArrayList<>(coordinates);
  }


  /**
   * @return true if coordinates of this are numerically equal to coordinates of obj
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Point)) {
      return false;
    }
    if (obj == this) {
      return true;
    }

    Point p = (Point) obj;
    return p.getCoordinates().equals(coordinates);
  }

  //https://medium.com/codelog/overriding-hashcode-method-effective-java-notes-723c1fedf51c
  @Override
  public int hashCode() {
    final int hash = 17;
    int result = Constants.THIRTY_ONE;

    for (Double coordinate : coordinates) {
      long coordsLong = Double.doubleToLongBits(coordinate);
      result = hash * result + (int) (coordsLong ^ (coordsLong >>> Constants.THIRTY_TWO));
    }
    return result;
  }

  /**
   * @return coordinates of point
   */
  @Override
  public String toString() {
    StringBuilder output = new StringBuilder("(");

    for (int i = 0; i < numDimensions; i++) {
      output.append(getCoordinate(i));
      if (i == numDimensions - 1) {
        output.append(")");
      } else {
        output.append(", ");
      }
    }
    return output.toString();
  }
}
