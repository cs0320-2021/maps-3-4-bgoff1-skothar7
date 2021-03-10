package edu.brown.cs.madhavramesh.stars;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class StarApplicationsTest {
  @Test
  public void testConstruction()
    throws FileNotFoundException, IllegalArgumentException, IOException {
    StarApplications threeStar = new StarApplications("data/stars/three-star.csv");
    StarApplications tenStar = new StarApplications("data/stars/ten-star.csv");
    StarApplications starData = new StarApplications("data/stars/stardata.csv");

    List<List<String>> threeStarCSV =
        Arrays.asList(
            Arrays.asList("1", "Star One", "1", "0", "0"),
            Arrays.asList("2", "Star Two", "2", "0", "0"),
            Arrays.asList("3", "Star Three", "3", "0", "0"));

    assertThrows(IllegalArgumentException.class,
        () -> {
          new StarApplications("data/stars/star-data-incorrect-header.csv");
        });
    assertThrows(IllegalArgumentException.class,
        () -> {
          new StarApplications("data/stars/star-data-incorrect-col-number.csv");
        });
    assertEquals(threeStar.getCSV().size(), 3);
    assertEquals(threeStar.getCSV().getData(), threeStarCSV);
    assertEquals(tenStar.getCSV().size(), 10);
    assertEquals(starData.getCSV().size(), 119617);
  }
}
