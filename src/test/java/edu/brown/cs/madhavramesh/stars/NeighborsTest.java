package edu.brown.cs.madhavramesh.stars;

import edu.brown.cs.madhavramesh.Constants;
import edu.brown.cs.madhavramesh.points.Point;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

import static edu.brown.cs.madhavramesh.stars.NeighborsTriggerAction.neighbors;
import static edu.brown.cs.madhavramesh.stars.NeighborsTriggerAction.resetCurrentFringe;
import static edu.brown.cs.madhavramesh.stars.NeighborsTriggerAction.setCurrentNearest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NeighborsTest {

  private NeighborsTriggerAction n = new NeighborsTriggerAction();
  private StarApplications sa;

  public NeighborsTest() {
  }

  /**
   * Resets the NaiveNeighbors object.
   */
  @Before
  public void reset() {
    resetCurrentFringe();
  }

  /**
   ** Tests calling 0 stars when 0 stars are loaded.
   */
  @Test
  public void testNoStars() throws IOException {
    reset();
    sa = new StarApplications("data/stars/one-star.csv");
    setCurrentNearest(new PriorityQueue<>(Comparator.comparingDouble(
        s -> -1 * s.dist(new Point(0.0, 0.0, 0.0)))));
    Star[] output = neighbors(sa.getStarsAsTree(), 0, new Point(0.0, 0.0, 0.0));
    assertTrue(output.length == 0);
  }

  /**
   ** Tests calling all loaded stars.
   */
  @Test
  public void testAllStars() throws IOException {
    reset();
    sa = new StarApplications("data/stars/one-star.csv");
    setCurrentNearest(new PriorityQueue<>(Comparator.comparingDouble(
        s -> -1 * s.dist(new Point(0.0, 0.0, 0.0)))));
    Star[] output = neighbors(sa.getStarsAsTree(), 1, new Point(0.0, 0.0, 0.0));
    assertTrue(output.length == 1);
    assertTrue(((Star) Array.get(output, 0)).getID() == 1);
    sa = new StarApplications("data/stars/ten-star.csv");
    reset();
    setCurrentNearest(new PriorityQueue<>(Comparator.comparingDouble(
        s -> -1 * s.dist(new Point(0.0, 0.0, 0.0)))));
    output = neighbors(sa.getStarsAsTree(), Constants.NINE, new Point(0.0, 0.0, 0.0));
    assertTrue(output.length == Constants.NINE);
    reset();
    sa = new StarApplications("data/stars/stardata.csv");
    setCurrentNearest(new PriorityQueue<>(Comparator.comparingDouble(
        s -> -1 * s.dist(new Point(Constants.FIVE_TWO, (double) Constants.NEG_ONE, (double) Constants.EIGHT)))));
    output = neighbors(sa.getStarsAsTree(), Constants.ONEONENINESIXONESEVEN,
        new Point(Constants.FIVE_TWO, (double) Constants.NEG_ONE, (double) Constants.EIGHT));
    assertTrue(output.length == Constants.ONEONENINESIXONESEVEN);
    reset();
    sa = new StarApplications("data/stars/stardata.csv");
    setCurrentNearest(new PriorityQueue<>(Comparator.comparingDouble(
        s -> -1 * s.dist(new Point(Constants.FIVE_TWO, (double) Constants.NEG_ONE, (double) Constants.EIGHT)))));
    output = neighbors(sa.getStarsAsTree(), Constants.ONEONENINESIXONESEVEN,
        new Point(Constants.FIVE_TWO, (double) Constants.NEG_ONE, (double) Constants.EIGHT));
    assertEquals(output.length, Constants.ONEONENINESIXONESEVEN);
  }

  /**
   ** Tests calling more than the number of loaded stars.
   */
  @Test
  public void testOverloadStars() throws IOException {
    reset();
    sa = new StarApplications("data/stars/one-star.csv");
    try {
      setCurrentNearest(new PriorityQueue<>(Comparator.comparingDouble(
          s -> -1 * s.dist(new Point(0.0, 0.0, 0.0)))));
      neighbors(sa.getStarsAsTree(), 2, new Point(0.0, 0.0, 0.0));
    } catch (Exception e) {
      assertTrue(true);
    }
    try {
      reset();
      setCurrentNearest(new PriorityQueue<>(Comparator.comparingDouble(s ->
          -1 * s.dist(new Point((double) Constants.FIVE, Constants.NEG_TWO_TWOFOUR, Constants.TEN_ZEROFOUR)))));
      neighbors(sa.getStarsAsTree(), Constants.ONE, new Point((double) Constants.FIVE, Constants.NEG_TWO_TWOFOUR,
          Constants.TEN_ZEROFOUR));
    } catch (Exception e) {
      assertTrue(true);
    }
    sa = new StarApplications("data/stars/stardata.csv");
    try {
      reset();
      setCurrentNearest(new PriorityQueue<>(Comparator.comparingDouble(
          s -> -1 * s.dist(new Point(0.0, 0.0, 0.0)))));
      neighbors(sa.getStarsAsTree(), Constants.ONEONENINESIXONESEVEN + 1, new Point(0.0, 0.0, 0.0));
    } catch (Exception e) {
      assertTrue(true);
    }
    try {
      reset();
      setCurrentNearest(new PriorityQueue<>(Comparator.comparingDouble(
          s -> -1 * s.dist(new Point(0.0, 0.0, 0.0)))));
      neighbors(sa.getStarsAsTree(), Constants.ONEONENINESIXONESEVEN, new Point(0.0, 0.0, 0.0));
    } catch (Exception e) {
      assertTrue(true);
    }
  }

  /**
   ** Tests command name returned properly.
   */
  @Test
  public void testName() {
    reset();
    assertTrue(n.command().equals("neighbors"));
  }

  /**
   ** Tests proper return on large number of stars.
   */
  @Test
  public void testLotsOfStars() throws IOException {
    reset();
    setCurrentNearest(new PriorityQueue<>(Comparator.comparingDouble(
        s -> -1 * s.dist(new Point((double) Constants.NEG_ONE, (double) Constants.NEG_ONE, (double) Constants.NEG_ONE)))));
    sa = new StarApplications("data/stars/ten-star.csv");
    Star[] output = neighbors(sa.getStarsAsTree(), Constants.FIVE, new Point((double) Constants.NEG_ONE,
        (double) Constants.NEG_ONE, (double) Constants.NEG_ONE));
    assertTrue(output.length == 5);
    assertTrue(Arrays.stream(output).map(s -> Integer.toString(s.getID())).anyMatch("0"::equals));
    assertTrue(Arrays.stream(output).map(s -> Integer.toString(s.getID())).anyMatch("70667"::equals));
    assertTrue(Arrays.stream(output).map(s -> Integer.toString(s.getID())).anyMatch("71454"::equals));
    assertTrue(Arrays.stream(output).map(s -> Integer.toString(s.getID())).anyMatch("71457"::equals));
    assertTrue(Arrays.stream(output).map(s -> Integer.toString(s.getID())).anyMatch("87666"::equals));
    reset();
    setCurrentNearest(new PriorityQueue<>(Comparator.comparingDouble(
        s -> -1 * s.dist(new Point(Constants.PROXIMA_CENTAURI_X,
            Constants.PROXIMA_CENTAURI_Y, Constants.PROXIMA_CENTAURI_Z)))));
    output = neighbors(sa.getStarsAsTree(), 5, new Point(Constants.PROXIMA_CENTAURI_X,
        Constants.PROXIMA_CENTAURI_Y, Constants.PROXIMA_CENTAURI_Z));
    assertTrue(output.length == 5);
    assertTrue(Arrays.stream(output).map(s -> Integer.toString(s.getID())).anyMatch("0"::equals));
    assertTrue(Arrays.stream(output).map(s -> Integer.toString(s.getID())).anyMatch("70667"::equals));
    assertTrue(Arrays.stream(output).map(s -> Integer.toString(s.getID())).anyMatch("71454"::equals));
    assertTrue(Arrays.stream(output).map(s -> Integer.toString(s.getID())).anyMatch("71457"::equals));
    assertTrue(Arrays.stream(output).map(s -> Integer.toString(s.getID())).anyMatch("87666"::equals));
  }

  /**
   ** Tests stars that tie for distance.
   */
  @Test
  public void testTieStars() throws IOException {
    reset();
    setCurrentNearest(new PriorityQueue<>(Comparator.comparingDouble(
        s -> -1 * s.dist(new Point(0.0, 0.0, 0.0)))));
    sa = new StarApplications("data/stars/tie-star.csv");
    Star[] output = neighbors(sa.getStarsAsTree(), 5, new Point(0.0, 0.0, 0.0));
    assertTrue(output[0].dist(new Point(0.0, 0.0, 0.0)) == 0);
    assertTrue(output[1].dist(new Point(0.0, 0.0, 0.0)) == 0);
    assertTrue(output[2].dist(new Point(0.0, 0.0, 0.0)) == 0);
    assertTrue(output[3].dist(new Point(0.0, 0.0, 0.0)) == 1);
    assertTrue(output[4].dist(new Point(0.0, 0.0, 0.0)) == 1);
    reset();
  }
}
