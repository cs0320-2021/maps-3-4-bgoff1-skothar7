package edu.brown.cs.madhavramesh.kdtree;

import edu.brown.cs.madhavramesh.points.Point;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class KDTreeTest {
  private List<Point> points;

  @Before
  public void setUp() {
    Point p1 = new Point(-10.5, 0.3);
    Point p2 = new Point(-3.2, -5.1);
    Point p3 = new Point(-1.3, 2.4);
    Point p4 = new Point(-1.0, 0.0);
    Point p5 = new Point(0.0, 0.0);
    Point p6 = new Point(0.7, 6.3);
    Point p7 = new Point(5.5, 3.2);
    points = Arrays.asList(p1, p2, p3, p4, p5, p6, p7);
  }

  @After
  public void tearDown() {
    points = null;
  }

  @Test
  public void testCreateNullKDTree() {
    setUp();
    KDTree<Point> empty = new KDTree(new ArrayList<>());

    assertTrue(empty.getNode().isEmpty());
    assertEquals(empty.getNumDimensions(), 0);
    tearDown();
  }

  @Test
  public void testCreateOneNodeKDTree() {
    setUp();
    KDTree<Point> oneElem = new KDTree<>(new ArrayList<>(points.subList(0, 1)));
    assertEquals(oneElem.getNode().get(), points.get(0));
    assertEquals(oneElem.getNumDimensions(), 2);
    tearDown();
  }

  @Test
  public void testCreateTwoNodeKDTree() {
    setUp();
    KDTree<Point> twoElem = new KDTree<>(new ArrayList(points.subList(0, 2)));
    assertEquals(twoElem.getNode().get(), points.get(1));
    assertEquals(twoElem.getLeft().get().getNode().get(), points.get(0));
    assertEquals(twoElem.getNumDimensions(), 2);
    tearDown();
  }

  @Test
  public void testCreateThreeNodeKDTree() {
    KDTree<Point> threeElem = new KDTree<>(new ArrayList<>(points.subList(0, 3)));
    assertEquals(threeElem.getNode().get(), points.get(1));
    assertEquals(threeElem.getLeft().get().getNode().get(), points.get(0));
    assertEquals(threeElem.getRight().get().getNode().get(), points.get(2));
    tearDown();
  }

  @Test
  public void testCreateBigKDTree() {
    setUp();
    KDTree<Point> sevenElem = new KDTree<>(new ArrayList<>(points));
    List<Point> repeatedMedianList =
        Arrays.asList(points.get(1), points.get(2), points.get(3),
            points.get(2), points.get(2), points.get(2), points.get(2));
    KDTree<Point> repeatedMedianTree = new KDTree<>(new ArrayList<>(repeatedMedianList));
    assertEquals(sevenElem.getNode().get(), points.get(3));
    assertEquals(sevenElem.getLeft().get().getNode().get(), points.get(0));
    assertEquals(((KDTree) sevenElem.getLeft().get().getLeft().get()).getNode().get(), points.get(1));
    assertEquals(((KDTree) sevenElem.getLeft().get().getRight().get()).getNode().get(), points.get(2));
    assertEquals(sevenElem.getRight().get().getNode().get(), points.get(6));
    assertEquals(((KDTree) sevenElem.getRight().get().getLeft().get()).getNode().get(), points.get(4));
    assertEquals(((KDTree) sevenElem.getRight().get().getRight().get()).getNode().get(), points.get(5));

    assertEquals(repeatedMedianTree.getNode().get(), points.get(2));
    assertEquals(repeatedMedianTree.getLeft().get().getNode().get(), points.get(1));
    assertEquals(((KDTree) repeatedMedianTree.getRight().get().getLeft().get()).getNode().get(), points.get(3));
    assertEquals(repeatedMedianTree.getRight().get().getNode().get(), points.get(2));
    assertEquals(((KDTree) repeatedMedianTree.getRight().get().getRight().get()).getNode().get(), points.get(2));
    assertEquals(((KDTree) ((KDTree) repeatedMedianTree.getRight().get().getRight().get()).getRight().get()).getNode().get(), points.get(2));
    assertEquals(((KDTree) ((KDTree) ((KDTree) repeatedMedianTree.getRight().get().getRight().get()).getRight().get()).getRight().get()).getNode().get(), points.get(2));
    tearDown();
  }

  @Test
  public void testNeighbors() {
    setUp();
    final int numTimes = 1000;
    KDTreeModelBasedTesting mbt = new KDTreeModelBasedTesting();

    assertTrue(mbt.runMBTMultipleTimes(numTimes, true));

    tearDown();
  }

  @Test
  public void testRadius() {
    setUp();
    final int numTimes = 1000;
    KDTreeModelBasedTesting mbt = new KDTreeModelBasedTesting();

    assertTrue(mbt.runMBTMultipleTimes(numTimes, false));

    tearDown();
  }
}