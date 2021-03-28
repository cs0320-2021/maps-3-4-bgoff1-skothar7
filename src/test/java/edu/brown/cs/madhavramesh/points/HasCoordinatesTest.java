package edu.brown.cs.madhavramesh.points;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HasCoordinatesTest {
  private final double delta = 0.00001;

  @Test
  public void testDist() {
    HasCoordinates p1 = new Point(0.0, 10.3, -5.3);
    HasCoordinates p2 = new Point(p1);
    HasCoordinates p3 = new Point(0.0, 0.0, 0.0);
    HasCoordinates p4 = new Point(-10.2, -5.4, -2.2);

    assertEquals(p1.dist(p1), 0, delta);
    assertEquals(p1.dist(p2), p2.dist(p1), delta);
    assertEquals(p1.dist(p3), p1.dist(p2) + p2.dist(p3), delta);

    assertEquals(p1.dist(p2), 0, delta);
    assertEquals(p1.dist(p3), 11.583609, delta);
    assertEquals(p1.dist(p4), 18.977355, delta);
  }
}
