package edu.brown.cs.madhavramesh.points;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PointComparatorTest {
  @Test
  public void testEqual() {
    HasCoordinates p1 = new Point(0.0, 10.3, -5.3);
    HasCoordinates p2 = new Point(p1);
    HasCoordinates reference = new Point(0.0, 0.0, 0.0);
    PointComparator c = new PointComparator(reference);

    assertEquals(c.compare(p1, p2), 0);
  }

  @Test
  public void testGreaterThan() {
    HasCoordinates p1 = new Point(1.0, 2.5, -4.0, 5.2, 1.3);
    HasCoordinates p2 = new Point(3.2, 0.2, -2.3, 1.2, 0.3);
    HasCoordinates reference = new Point(1.2, 0.3, -0.6, 1.2, 1.2);
    PointComparator c = new PointComparator(reference);

    assertEquals(c.compare(p1, p2), 1);
  }

  @Test
  public void testLessThan() {
    HasCoordinates p1 = new Point(1.0, 5.7);
    HasCoordinates p2 = new Point(8.23, 9.1);
    HasCoordinates reference = new Point(-3.1, 2.45);
    PointComparator c = new PointComparator(reference);

    assertEquals(c.compare(p1, p2), -1);
  }
}
