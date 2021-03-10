package edu.brown.cs.madhavramesh.repl;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class CSVParserTest {
  private final List<String> correctHeader =
      Arrays.asList("StarID", "ProperName", "X", "Y", "Z");

  @Test()
  public void testCSVParserNoErrorsConstruction()
      throws FileNotFoundException, IOException {
    CSVParser empty = new CSVParser("data/stars/no-stars.csv");
    List<List<String>> emptyParsed = Arrays.asList(correctHeader);

    CSVParser singleRow = new CSVParser("data/stars/one-star.csv");
    List<String> singleRow1 =
        Arrays.asList("1", "Lonely Star", "5", "-2.24", "10.04");
    List<List<String>> singleRowParsed =
        Arrays.asList(correctHeader, singleRow1);

    CSVParser threeRows = new CSVParser("data/stars/three-star.csv");
    List<String> threeRowParsed1 =
        Arrays.asList("1", "Star One", "1", "0", "0");
    List<String> threeRowParsed2 =
        Arrays.asList("2", "Star Two", "2", "0", "0");
    List<String> threeRowParsed3 =
        Arrays.asList("3", "Star Three", "3", "0", "0");
    List<List<String>> threeRowParsed =
        Arrays.asList(correctHeader, threeRowParsed1, threeRowParsed2, threeRowParsed3);

    assertEquals(empty.getData(), emptyParsed);
    assertEquals(singleRow.getData(), singleRowParsed);
    assertEquals(threeRows.getData(), threeRowParsed);
  }

  @Test
  public void testCSVParserErrorConstruction() {
    assertThrows(FileNotFoundException.class,
        () -> {
          new CSVParser("data/stars/no-file.csv");
        });
  }

  @Test
  public void testCSVParserMalformedCSVConstruction()
      throws FileNotFoundException, IOException {
    CSVParser periods = new CSVParser("data/stars/star-data-periods-separator.csv");
    List<String> periodsParsed1 = Arrays.asList("1.\"NAME\".0.0.0");
    List<String> periodsParsed2 = Arrays.asList("2.\"second\".3.3.2");
    List<List<String>> periodsParsed =
        Arrays.asList(correctHeader, periodsParsed1, periodsParsed2);

    CSVParser allFieldsEmpty = new CSVParser("data/stars/star-data-all-fields-empty.csv");
    List<String> allFieldsEmpty1 = new ArrayList<>();
    List<List<String>> allFieldsEmptyParsed =
        Arrays.asList(correctHeader, allFieldsEmpty1);

    assertEquals(periods.getData(), periodsParsed);
    assertEquals(allFieldsEmpty.getData(), allFieldsEmptyParsed);
  }

  @Test
  public void testIsCorrectHeader()
      throws FileNotFoundException, IOException {
    CSVParser threeRows = new CSVParser("data/stars/three-star.csv");
    CSVParser stardata = new CSVParser("data/stars/stardata.csv");

    CSVParser incorrectHeader = new CSVParser("data/stars/star-data-incorrect-header.csv");

    assertTrue(threeRows.isCorrectHeader(correctHeader));
    assertTrue(stardata.isCorrectHeader(correctHeader));
    assertFalse(incorrectHeader.isCorrectHeader(correctHeader));
  }

  @Test
  public void testRemoveHeader()
      throws FileNotFoundException, IOException {
    CSVParser tenRows = new CSVParser("data/stars/ten-star.csv");
    CSVParser starData = new CSVParser("data/stars/stardata.csv");

    assertEquals(tenRows.get(0), correctHeader);
    tenRows.removeHeader();
    assertNotEquals(tenRows.get(0), correctHeader);

    assertEquals(starData.get(0), correctHeader);
    starData.removeHeader();
    assertNotEquals(starData.get(0), correctHeader);
  }

  @Test
  public void testIsCorrectRowLength()
      throws FileNotFoundException, IOException {
    CSVParser tenRows = new CSVParser("data/stars/ten-star.csv");
    CSVParser starData = new CSVParser("data/stars/stardata.csv");
    CSVParser incorrectRows = new CSVParser("data/stars/star-data-incorrect-col-number.csv");

    assertTrue(tenRows.isCorrectRowLength(5));
    assertFalse(tenRows.isCorrectRowLength(4));

    assertTrue(starData.isCorrectRowLength(5));
    assertFalse(starData.isCorrectRowLength(4));

    assertFalse(incorrectRows.isCorrectRowLength(5));
  }

  @Test
  public void testFindEntry()
      throws FileNotFoundException, IOException {
    CSVParser starData = new CSVParser("data/stars/stardata.csv");
    CSVParser tenRows = new CSVParser("data/stars/ten-star.csv");

    assertEquals(starData.findEntry("Dotty_16", 1), 113852);
    assertEquals(starData.findEntry("119.99155", 3), 53561);
    assertEquals(tenRows.findEntry("Barnard's Star", 1), 9);
    assertEquals(tenRows.findEntry("3759", 0), 5);
  }

  @Test
  public void testFindEntryNotInTable()
      throws FileNotFoundException, IOException {
    CSVParser starData = new CSVParser("data/stars/stardata.csv");

    assertThrows(IllegalArgumentException.class,
        () -> {
          starData.findEntry("Ray 8", 1);
        });
    assertThrows(IllegalArgumentException.class,
        () -> {
          starData.findEntry("60352", 1);
        });
  }
}
