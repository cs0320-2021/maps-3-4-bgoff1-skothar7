package edu.brown.cs.madhavramesh.maps;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LocationNodeTest {

  private MapTriggerAction m = new MapTriggerAction();

  @Test
  public void createNodeTest() {
    m.execute(new String[] {"data/maps/smallMaps.sqlite3"}, true);
    LocationNode node1 = new LocationNode("/n/4", 41.8203, -71.4003);
    assertTrue(node1.getiD().equals("/n/4"));
    assertTrue(node1.getLatitude() == 41.8203);
    assertTrue(node1.getLongitude() == -71.4003);
    assertFalse(node1.equals("node"));
    assertFalse(node1.equals(null));
    assertTrue(node1.equals(node1));
  }

  @Test
  public void getEdgesTest() {
    m.execute(new String[] {"data/maps/smallMaps.sqlite3"}, true);
    LocationNode node1 = new LocationNode("/n/4", 41.8203, -71.4003);
    assertTrue(node1.getOutEdges().size() == 1);
    assertTrue(((ArrayList<Way>) node1.getOutEdges()).get(0).getiD().equals("/w/6"));
  }

  @Test
  public void toStringTest() {
    m.execute(new String[] {"data/maps/smallMaps.sqlite3"}, true);
    LocationNode node1 = new LocationNode("/n/4", 41.8203, -71.4003);
    assertTrue(node1.toString().equals("/n/4"));
  }
}
