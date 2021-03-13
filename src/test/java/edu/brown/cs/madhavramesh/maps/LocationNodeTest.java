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
    MapNode node1 = new MapNode("/n/4", 41.8203, -71.4003);
    assertTrue(node1.getStringID().equals("/n/4"));
    assertTrue(node1.getCoordinates().get(0)== 41.8203);
    assertTrue(node1.getCoordinates().get(1) == -71.4003);
    assertFalse(node1.equals("node"));
    assertFalse(node1.equals(null));
    assertTrue(node1.equals(node1));
  }

//  @Test
//  public void getEdgesTest() {
//    m.execute(new String[] {"data/maps/smallMaps.sqlite3"}, true);
//    MapNode node1 = new MapNode("/n/4", 41.8203, -71.4003);
//    assertTrue(node1.getWays().size() == 1);
//    assertTrue(((ArrayList<Way>) node1.getWays()).get(0).getWayID().equals("/w/6"));
//  }
//
//  @Test
//  public void toStringTest() {
//    m.execute(new String[] {"data/maps/smallMaps.sqlite3"}, true);
//    MapNode node1 = new MapNode("/n/4", 41.8203, -71.4003);
//    assertTrue(node1.toString().equals("/n/4"));
//  }
}
