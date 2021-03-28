package edu.brown.cs.madhavramesh.kdtree;

import edu.brown.cs.madhavramesh.points.HasCoordinates;

import edu.brown.cs.madhavramesh.points.Point;
import edu.brown.cs.madhavramesh.points.PointComparator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class BoundedPriorityQueueTest {
  private BoundedPriorityQueue<HasCoordinates> pq;
  private HasCoordinates p1;
  private HasCoordinates p2;
  private HasCoordinates p3;
  private HasCoordinates p4;

  @Before
  public void setUp() {
    HasCoordinates reference = new Point(-1.0, 0.0, 1.0);
    Comparator<HasCoordinates> pc = Collections.reverseOrder(new PointComparator(reference));
    pq = new BoundedPriorityQueue<>(pc, 2);

    p1 = new Point(10.5, -10.0, 10.5);
    p2 = new Point(15.0, 23.2, -1.3);
    p3 = new Point(5.5, 0.4, 0.2);
    p4 = new Point(0.0, 0.0, 0.0);
  }

  @After
  public void tearDown() {
    pq = null;
  }

  @Test
  public void testCreateCopy() {
    setUp();
    pq.add(p1);
    pq.add(p2);
    pq.add(p3);
    pq.add(p4);
    BoundedPriorityQueue<HasCoordinates> newPQ = new BoundedPriorityQueue<>(pq);

    assertNotSame(newPQ, pq);
    assertEquals(newPQ.toList(), pq.toList());
    tearDown();
  }

  @Test
  public void testIsEmpty() {
    setUp();
    assertTrue(pq.isEmpty());
    tearDown();
  }

  @Test
  public void testAdd() {
    setUp();
    pq.add(p1);
    assertEquals(pq.getQueue().size(), 1);
    assertEquals(pq.getQueue().peek(), p1);

    pq.add(p2);
    assertEquals(pq.getQueue().size(), 2);
    assertEquals(pq.getQueue().peek(), p2);

    pq.add(p3);
    assertEquals(pq.getQueue().size(), 2);
    assertEquals(pq.getQueue().peek(), p1);

    pq.add(p4);
    assertEquals(pq.getQueue().size(), 2);
    assertEquals(pq.getQueue().peek(), p3);
    tearDown();
  }

  @Test
  public void testToList() {
    setUp();
    pq.add(p1);
    pq.add(p2);
    pq.add(p3);
    pq.add(p4);
    List<HasCoordinates> l = Arrays.asList(p3, p4);
    assertEquals(pq.toList(), l);
    tearDown();
  }
}
