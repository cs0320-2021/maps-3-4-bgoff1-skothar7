package edu.brown.cs.madhavramesh.stars;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertTrue;

public class NaiveNeighborsTriggerActionTest {

  private NaiveNeighborsTriggerAction n = new NaiveNeighborsTriggerAction();
  private StarApplications sa;

  /**
   ** Tests calling 0 stars when 0 stars are loaded.
   */
  @Test
  public void testNoStars() throws IOException {
    sa = new StarApplications("data/stars/one-star.csv");
    String output = n.execute(new String[] {"0", "0", "0", "0"}, true);
    assertTrue(output.equals(""));
  }

  /**
   ** Tests command name returned properly.
   */
  @Test
  public void testName() {
    assertTrue(n.command().equals("naive_neighbors"));
  }
}
