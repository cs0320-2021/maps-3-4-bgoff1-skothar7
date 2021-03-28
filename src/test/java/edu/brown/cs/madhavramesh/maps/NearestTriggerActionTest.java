package edu.brown.cs.madhavramesh.maps;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class NearestTriggerActionTest {
  private NearestTriggerAction n = new NearestTriggerAction();
  private MapTriggerAction m = new MapTriggerAction();

  @Test
  public void commandTest() {
    String output = n.command();
    assertTrue(output.equals("nearest"));
  }

  @Test
  public void parametersTest() {
    int[] output = n.getNumParameters();
    assertTrue(output[0] == 2);
    assertTrue(output.length == 1);
  }

  @Test
  public void executionTest() {
    m.execute(new String[] {"data/maps/smallMaps.sqlite3"}, true);
    String output;
    output = n.execute(new String[] {"41.8201", "-71.4004"}, true);
    System.out.println("nearestTest l28: "+output);
    assertTrue(output.equals("/n/3\n"));

    output = n.execute(new String[] {"41.8203", "-71.40033"}, true);
    System.out.println("nearestTest l32: "+output);
    assertTrue(output.equals("/n/4\n"));
  }

  @Test
  public void farAwayTest() {
    m.execute(new String[] {"data/maps/smallMaps.sqlite3"}, true);
    String output = n.execute(new String[] {"0", "0"}, true);
    System.out.println(output);
    assertTrue(output.equals("/n/0\n"));
  }

  @Test
  public void equalDistanceTest() {
    m.execute(new String[] {"data/maps/smallMaps.sqlite3"}, true);
    String output = n.execute(new String[] {"41.82015", "-71.4"}, true);
    System.out.println(output);
    assertTrue(output.equals("/n/0\n") || output.equals("/n/1\n"));
  }

  @Test
  public void executionErrorTest() {
    m.execute(new String[] {"data/maps/smallMaps.sqlite3"}, true);
    String output = n.execute(new String[] {"41.8201", "-71.4004", "41.8199", "-71.4002"}, true);
    assertTrue(output.equals(""));
    output = n.execute(new String[] {"-71.4jj004", "41.8199"}, true);
    assertTrue(output.equals(""));
  }
}
