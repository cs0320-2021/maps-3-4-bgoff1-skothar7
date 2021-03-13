package edu.brown.cs.madhavramesh.maps;

import edu.brown.cs.madhavramesh.graph.DirectedGraph;
import edu.brown.cs.madhavramesh.graph.MapCalculations;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WayTest {

  private MapTriggerAction m = new MapTriggerAction();
  MapNode p1;
  MapNode p2;

  @Test
  public void zeroHaversineTest() {
    p1 = new MapNode("1", 0, 0);
    p2 = new MapNode("2", 0, 0);
    double distance = MapCalculations
        .haversine(p1.getCoordinates().get(0), p1.getCoordinates().get(1),
            p2.getCoordinates().get(0), p2.getCoordinates().get(1));

    assertEquals(distance, 0, 0.1);

    p1 = new MapNode("1", 15.8, -10);
    p2 = new MapNode("2", 15.8, -10);
    distance = MapCalculations
        .haversine(p1.getCoordinates().get(0), p1.getCoordinates().get(1),
            p2.getCoordinates().get(0), p2.getCoordinates().get(1));

    assertEquals(distance, 0, 0.001);

    p1 = new MapNode("1", -18, -22.55);
    p2 = new MapNode("2", -18, -22.55);
    distance = MapCalculations
        .haversine(p1.getCoordinates().get(0), p1.getCoordinates().get(1),
            p2.getCoordinates().get(0), p2.getCoordinates().get(1));

    assertEquals(distance, 0, 0.1);
  }

  @Test
  public void coterminalHaversineTest() {
    p1 = new MapNode("1", 0, 180);
    p2 = new MapNode("2", -360, 0);

    double distance = MapCalculations
        .haversine(p1.getCoordinates().get(0), p1.getCoordinates().get(1),
            p2.getCoordinates().get(0), p2.getCoordinates().get(1));

    assertEquals(distance, 20015.1150703545, 0.1);

    p1 = new MapNode("1", 180, 360);
    p2 = new MapNode("2", 540, 540);
    distance = MapCalculations
        .haversine(p1.getCoordinates().get(0), p1.getCoordinates().get(1),
            p2.getCoordinates().get(0), p2.getCoordinates().get(1));

    assertEquals(distance, 20015.1150703545, 0.1);
  }

  @Test
  public void anyHaversineTest() {
    p1 = new MapNode("1", 0, 14.3);
    p2 = new MapNode("2", -47, 16);
    double distance = MapCalculations
        .haversine(p1.getCoordinates().get(0), p1.getCoordinates().get(1),
            p2.getCoordinates().get(0), p2.getCoordinates().get(1));
    assertEquals(distance, 5228.78333321254, 0.1);
    p1 = new MapNode("1", -12, -12);
    p2 = new MapNode("2", -17, -19);
    distance = MapCalculations
        .haversine(p1.getCoordinates().get(0), p1.getCoordinates().get(1),
            p2.getCoordinates().get(0), p2.getCoordinates().get(1));
    assertEquals(distance, 936.21759113445, 0.1);
  }

//  @Test
//  public void createWayTest() throws SQLException {
//    m.execute(new String[] {"data/maps/smallMaps.sqlite3"}, true);
//    Way output = new Way("/w/0", "Chihiro Ave", "residential", "/n/0", "/n/1");
//    assertTrue(output.getWayID().equals("/w/0"));
//    assertTrue(output.getName().equals("Chihiro Ave"));
//    //assertTrue(output.getType().equals("residential"));
//    //assertTrue(output.from().getiD().equals("/n/0"));
//    assertTrue(output.getTarget().getStringID().equals("/n/1"));
//    assertTrue(output.getNumDimensions() == 4);
//    assertTrue(output.getCoordinates().size() == 4);
//    assertTrue(output.getCoordinate(0) == 41.82);
//    assertTrue(output.getCoordinate(1) == -71.4);
//    assertTrue(output.getCoordinate(2) == 41.8203);
//    assertTrue(output.getCoordinate(3) == -71.4);
//  }
}
