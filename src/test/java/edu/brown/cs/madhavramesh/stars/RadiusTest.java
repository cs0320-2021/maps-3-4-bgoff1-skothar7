package edu.brown.cs.madhavramesh.stars;

import edu.brown.cs.madhavramesh.Constants;
import edu.brown.cs.madhavramesh.points.Point;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

import static edu.brown.cs.madhavramesh.stars.RadiusTriggerAction.radius;
import static org.junit.Assert.assertTrue;

public class RadiusTest {

  private RadiusTriggerAction r = new RadiusTriggerAction();
  private StarApplications sa;


  /**
   * Resets the Radius object.
   */
  @Before
  public void reset() {
    r = new RadiusTriggerAction();
  }

  /**
   ** Tests calling radius 0.
   */
  @Test
  public void testZeroRadius() throws IOException {
    reset();
    sa = new StarApplications("data/stars/ten-star.csv");
    Star[] output = radius(sa.getStarsAsTree(), 0, new Point(0.0, 0.0, 0.0));
    assertTrue(output.length == 1);
    output = radius(sa.getStarsAsTree(), 0, "Sol");
    assertTrue(output.length == 0);
  }

  /**
   ** Tests no stars in radius.
   */
  @Test
  public void testNoneInRadius() throws IOException {
    reset();
    sa = new StarApplications("data/stars/stardata.csv");
    Star[] output = radius(sa.getStarsAsTree(), Constants.ALMOST_ZERO, new Point((double) Constants.ONEONENINESIXONESEVEN,
        (double) Constants.ONEONENINESIXONESEVEN, (double) Constants.ONEONENINESIXONESEVEN));
    assertTrue(output.length == 0);
    output = radius(sa.getStarsAsTree(), Constants.ALMOST_ZERO, "Cyntha");
    assertTrue(output.length == 0);
  }

  /**
   ** Tests calling all loaded stars.
   */
  @Test
  public void testAllStars() throws IOException {
    reset();
    sa = new StarApplications("data/stars/one-star.csv");
    Star[] output = radius(sa.getStarsAsTree(), Constants.THIRTY, new Point(0.0, 0.0, 0.0));
    assertTrue(output.length == 1);
    assertTrue(((Star) Array.get(output, 0)).getID() == 1);
    output = radius(sa.getStarsAsTree(), Constants.THIRTY, "Lonely Star");
    assertTrue(output.length == 0);
  }

  /**
   ** Tests command name returned properly.
   */
  @Test
  public void testName() {
    reset();
    assertTrue(r.command().equals("radius"));
  }

  /**
   ** Tests proper return on large number of stars.
   */
  @Test
  public void testStars() throws IOException {
    reset();
    sa = new StarApplications("data/stars/ten-star.csv");
    Star[] output = radius(sa.getStarsAsTree(), 5, new Point(-1.0, -1.0, -1.0));
    assertTrue(output.length == 6);
    assertTrue(Arrays.stream(output).map(s -> Integer.toString(s.getID())).anyMatch("0"::equals));
    assertTrue(Arrays.stream(output).map(s -> Integer.toString(s.getID())).anyMatch("70667"::equals));
    assertTrue(Arrays.stream(output).map(s -> Integer.toString(s.getID())).anyMatch("71454"::equals));
    assertTrue(Arrays.stream(output).map(s -> Integer.toString(s.getID())).anyMatch("71457"::equals));
    assertTrue(Arrays.stream(output).map(s -> Integer.toString(s.getID())).anyMatch("87666"::equals));
    assertTrue(Arrays.stream(output).map(s -> Integer.toString(s.getID())).anyMatch("118721"::equals));
  }

  /**
   ** Tests calling more stars than number of loaded stars.
   */
  @Test
  public void testOverloading() throws IOException {
    reset();
    sa = new StarApplications("data/stars/one-star.csv");
    try {
      radius(sa.getStarsAsTree(), Constants.TEN, new Point(0.0, 0.0, 0.0));
    } catch (Exception e) {
      assertTrue(true);
    }
  }

  /**
   ** Tests star on edge of radius to make sure it is included.
   */
  @Test
  public void testFringeStar() throws IOException {
    reset();
    sa = new StarApplications("data/stars/ten-star.csv");
    Star[] output = radius(sa.getStarsAsTree(), Constants.FIVE, new Point(0.0, 3.0, 4.0));
    assertTrue(output[1].getName().equals("Sol"));
  }
}
