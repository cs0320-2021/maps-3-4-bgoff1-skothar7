package edu.brown.cs.madhavramesh.maps;

import org.junit.Test;
import java.sql.SQLException;
import java.util.List;
import static org.junit.Assert.assertTrue;

public class WaysTriggerActionTest {

private WaysTriggerAction w = new WaysTriggerAction();
private MapTriggerAction m = new MapTriggerAction();

  @Test
  public void allWaysTest() throws SQLException {
    m.execute(new String[] {"data/maps/smallMaps.sqlite3"}, true);
    List<String> output = w.waysWithinBoundSQLREPL(42.0, -72.0, 41.8, -71.3);
    assertTrue(output.size() == 7);
    assertTrue(output.contains("/w/0"));
    assertTrue(output.contains("/w/1"));
    assertTrue(output.contains("/w/2"));
    assertTrue(output.contains("/w/3"));
    assertTrue(output.contains("/w/4"));
    assertTrue(output.contains("/w/5"));
    assertTrue(output.contains("/w/6"));
  }

  @Test
  public void noWaysTest() throws SQLException {
    m.execute(new String[] {"data/maps/smallMaps.sqlite3"}, true);
    List<String> output = w.waysWithinBoundSQLREPL(60.0, -85.0, 50.0, -80.0);
    assertTrue(output.size() == 0);
  }

  @Test
  public void pointWaysBoxTest() throws SQLException {
    m.execute(new String[] {"data/maps/smallMaps.sqlite3"}, true);
    List<String> output = w.waysWithinBoundSQLREPL(41.82, -71.4, 41.82, -71.4);
    assertTrue(output.size() == 2);
    assertTrue(output.contains("/w/0"));
    assertTrue(output.contains("/w/2"));
  }

  @Test
  public void smallWaysBoxTest() throws SQLException {
    m.execute(new String[] {"data/maps/smallMaps.sqlite3"}, true);
    List<String> output = w.waysWithinBoundSQLREPL(41.8201, -71.4004, 41.8199, -71.4002);
    assertTrue(output.size() == 2);
    assertTrue(output.contains("/w/2"));
    assertTrue(output.contains("/w/5"));
  }

  @Test
  public void commandTest() {
    String output = w.command();
    assertTrue(output.equals("ways"));
  }

  @Test
  public void parametersTest() {
    int[] output = w.getNumParameters();
    assertTrue(output[0] == 4);
    assertTrue(output.length == 1);
  }

  @Test
  public void executionTest() {
    m.execute(new String[] {"data/maps/smallMaps.sqlite3"}, true);
    String output = w.execute(new String[] {"41.8201", "-71.4004", "41.8199", "-71.4002"}, true);
    assertTrue(output.equals("/w/2\n/w/5\n"));
  }

  @Test
  public void executionErrorTest() {
    m.execute(new String[] {"data/maps/smallMaps.sqlite3"}, true);
    String output = w.execute(new String[] {"41.820aa1", "-71.4004", "41.8199", "-71.4002"}, true);
    assertTrue(output.equals(""));
    output = w.execute(new String[] {"-71.4004", "41.8199", "-71.4002"}, true);
    assertTrue(output.equals(""));
  }
}
