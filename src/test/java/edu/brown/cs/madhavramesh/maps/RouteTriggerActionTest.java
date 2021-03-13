package edu.brown.cs.madhavramesh.maps;

import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public class RouteTriggerActionTest {
  private RouteTriggerAction r = new RouteTriggerAction();
  private MapTriggerAction m = new MapTriggerAction();

  @Test
  public void commandTest() {
    assertTrue(r.command().equals("route"));
  }

  @Test
  public void parametersTest() {
    assertTrue(r.getNumParameters().length == 1);
    assertTrue(r.getNumParameters()[0] == 4);
  }

  @Test
  public void multiplePathsTest() {
    m.execute(new String[] {"data/maps/smallMaps.sqlite3"}, true);
    String output = r.execute(new String[] {"41.82", "-71.40", "41.8206", "-71.4003"}, true);
    System.out.println(output);
    assertTrue(output.equals("/n/0 -> /n/1 : /w/0" + "\n" +
        "/n/1 -> /n/2 : /w/1" + "\n" +
        "/n/2 -> /n/5 : /w/4" + "\n"));
  }

  @Test
  public void noPathTest() {
    m.execute(new String[] {"data/maps/smallMaps.sqlite3"}, true);

    String output = r.execute(new String[] {"41.82", "-71.40", "41.82", "-71.40"}, true);
    System.out.println(output);
    //assertTrue(output.equals("/n/0 you're already there!" + "\n"));
    assertTrue(output.equals("/n/0 -/- /n/0" + "\n"));

    output = r.execute(new String[] {"41.8206", "-71.4003", "41.82", "-71.4"}, true);
    System.out.println(output);
    assertTrue(output.equals("/n/5 -/- /n/0" + "\n"));
  }

  @Test
  public void getsCorrectNodesTest() throws SQLException {
    m.execute(new String[] {"data/maps/smallMaps.sqlite3"}, true);
    MapNode[] output = r.checkArgsAreIntsOrStrings(new String[] {"41.82", "-71.40", "41.82", "-71.40"});
    System.out.println(output[0].getStringID());
    System.out.println(output[1].getStringID());
    assertTrue(output[0].getStringID().equals("/n/0"));
    assertTrue(output[1].getStringID().equals("/n/0"));
    output = new MapNode[]{};
    output = r.checkArgsAreIntsOrStrings(new String[] {"Chihiro Ave", "Radish Spirit Blvd", "Kamaji Pl", "Yubaba St"});
    assertTrue(output[0].getStringID().equals("/n/0"));
    assertTrue(output[1].getStringID().equals("/n/5"));
  }
}
