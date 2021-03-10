package edu.brown.cs.madhavramesh.points;

import edu.brown.cs.madhavramesh.stars.Star;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class PointTest {
  private final double delta = 0.00001;

  @Test
  public void testVarargsConstruction() {
    HasCoordinates p1 = new Point(0.0);
    HasCoordinates p2 = new Point(-20.0, 3.0, -0.3);
    HasCoordinates p3 = new Point(10.32, -30.123, 0.3, 32.0, 3.0, 2.0, 0.234);

    assertEquals(p1.getCoordinate(0), 0, delta);
    assertEquals(p2.getCoordinate(0), -20.0, delta);
    assertEquals(p2.getCoordinate(2), -0.3, delta);
    assertEquals(p3.getCoordinate(0), 10.32, delta);
    assertEquals(p3.getCoordinate(3), 32.0, delta);
    assertEquals(p3.getCoordinate(6), 0.234, delta);
  }

  @Test
  public void testListConstruction() {
    List<Double> l1 = Arrays.asList(0.0);
    List<Double> l2 = Arrays.asList(-212.32, 0.8302, 10.23);
    List<Double> l3 = Arrays.asList(2.3, -129.34, 0.281, 65.2, 1013.0, 34.23, 9283.2);
    HasCoordinates p1 = new Point(l1);
    HasCoordinates p2 = new Point(l2);
    HasCoordinates p3 = new Point(l3);

    assertEquals(p1.getCoordinate(0), 0, delta);
    assertEquals(p2.getCoordinate(0), -212.32, delta);
    assertEquals(p2.getCoordinate(2), 10.23, delta);
    assertEquals(p3.getCoordinate(0), 2.3, delta);
    assertEquals(p3.getCoordinate(3), 65.2, delta);
    assertEquals(p3.getCoordinate(6), 9283.2, delta);
  }

  @Test
  public void testConstructionFromPoint() {
    HasCoordinates p1 = new Point(39.2);
    HasCoordinates p2 = new Point(-129.12, 0.12, 23.20);
    HasCoordinates p3 = new Point(-12.329, 349.0, 20.238, -12.23, 0.129);
    HasCoordinates p4 = new Point(p1);
    HasCoordinates p5 = new Point(p2);
    HasCoordinates p6 = new Point(p3);

    assertEquals(p1, p4);
    assertEquals(p2, p5);
    assertEquals(p3, p6);
  }

  @Test
  public void testEquals() {
    HasCoordinates p1 = new Point(0.42, -0.12);
    HasCoordinates p2 = new Point(0.42, -0.12);
    HasCoordinates p3 = new Point(0.42, -0.12);
    HasCoordinates p4 = new Point(0.281, -0.329);

    assertTrue(p1.equals(p1));
    assertTrue(p1.equals(p2) && p2.equals(p1));
    assertTrue(p1.equals(p2) && p2.equals(p3) && p1.equals(p3));
    assertFalse(p1.equals(p4));
    assertFalse(p1.equals(new Star(1, "name", 0, 0, 0)));
    assertFalse(p1.equals(null));
  }

  @Test
  public void testHashCode() {
    HasCoordinates p1 = new Point(0.0, 7.8);
    HasCoordinates p2 = new Point(0.0, 7.8);
    HasCoordinates p3 = new Point(-5.0, 7.8);

    assertEquals(p1.hashCode(), p1.hashCode());
    assertNotSame(p1, p2);
    assertEquals(p1.hashCode(), p2.hashCode());
    assertNotEquals(p1.hashCode(), p3.hashCode());
  }
}
