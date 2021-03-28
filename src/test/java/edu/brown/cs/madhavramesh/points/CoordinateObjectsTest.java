package edu.brown.cs.madhavramesh.points;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CoordinateObjectsTest {
  private List<Point> points;

  @Before
  public void setUp() {
    Point p1 = new Point(0.0, 0.0, 0.0);
    Point p2 = new Point(-0.3, 2.4, 0.7);
    Point p3 = new Point(4.6, 1.8, -12.3);
    Point p4 = new Point(12.3, 2.3, -2.12);

    points = Arrays.asList(p1, p2, p3, p4);
  }

  @After
  public void tearDown() {
    points = null;
  }

  @Test
  public void testConstruction() {
    setUp();
    CoordinateObjects<Point> co = new CoordinateObjects<>(points);

    assertEquals(co.getPoints(), points);
    tearDown();
  }

  @Test
  public void addPoint() {
    setUp();
    CoordinateObjects<Point> co = new CoordinateObjects<>(points);

    assertEquals(co.getPoints().size(), 4);
    co.addPoint(new Point(-3.2, 3.5, 1.2));
    assertEquals(co.getPoints().size(), 5);
  }

  @Test
  public void testKNearestNeighborsNaive() {
    setUp();
    CoordinateObjects<Point> co = new CoordinateObjects<>(points);

    List<Point> emptyNeighbors = co.kNearestNeighborsNaive(0, points.get(1));
    List<Point> oneNeighbor =
        co.kNearestNeighborsNaive(1, points.get(1));
    List<Point> twoNeighbor =
        co.kNearestNeighborsNaive(2, points.get(2));
    List<Point> threeNeighbor =
        co.kNearestNeighborsNaive(3, points.get(1));
    List<Point> pointNotInList =
        co.kNearestNeighborsNaive(2, new Point(-4.2, -2.1, 0.2));
    List<Point> tooManyNeighbors =
        co.kNearestNeighborsNaive(10, points.get(3));

    assertThrows(IllegalArgumentException.class,
        () -> {
          co.kNearestNeighborsNaive(-10, points.get(0));
        });
    assertEquals(emptyNeighbors, new ArrayList<>());
    assertEquals(oneNeighbor, Arrays.asList(points.get(1)));
    assertEquals(twoNeighbor, Arrays.asList(points.get(2), points.get(3)));
    assertEquals(threeNeighbor, Arrays.asList(points.get(1), points.get(0), points.get(3)));
    assertEquals(pointNotInList, Arrays.asList(points.get(0), points.get(1)));
    assertEquals(tooManyNeighbors,
        Arrays.asList(points.get(3), points.get(0), points.get(2), points.get(1)));
    tearDown();
  }

  @Test
  public void testNearestNeighborsInRadiusNaive() {
    setUp();
    CoordinateObjects<Point> co = new CoordinateObjects<>(points);

    List<Point> noNeighborZeroRadius =
        co.nearestNeighborsInRadiusNaive(0, new Point(1.0, 1.0, 1.0));
    List<Point> oneNeighborZeroRadius = co.nearestNeighborsInRadiusNaive(0, points.get(1));
    List<Point> boundaryInclusive =
        co.nearestNeighborsInRadiusNaive(1, new Point(-1.0, 0.0, 0.0));
    List<Point> twoNeighbors =
        co.nearestNeighborsInRadiusNaive(5, new Point(1.0, 1.2, 0.8));
    List<Point> allNeighbors =
        co.nearestNeighborsInRadiusNaive(50, points.get(0));

    assertThrows(IllegalArgumentException.class,
        () -> {
          co.nearestNeighborsInRadiusNaive(-10, points.get(0));
        });
    assertEquals(noNeighborZeroRadius, new ArrayList<>());
    assertEquals(oneNeighborZeroRadius, Arrays.asList(points.get(1)));
    assertEquals(boundaryInclusive, Arrays.asList(points.get(0)));
    assertEquals(twoNeighbors, Arrays.asList(points.get(0), points.get(1)));
    assertEquals(allNeighbors,
        Arrays.asList(points.get(0), points.get(1), points.get(3), points.get(2)));
    tearDown();
  }
}
