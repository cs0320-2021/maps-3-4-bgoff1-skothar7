package edu.brown.cs.madhavramesh.stars;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Reads in a given CSV file and stores the data in
 * a StarApplications object.
 */
public class StarsTriggerAction implements TriggerAction {
  private static StarApplications sa;

  /**
   * Returns command matching this TriggerAction.
   *
   * @return String representing command
   */
  public String command() {
    return "stars";
  }

  /** Reads CSV file and outputs number of rows in it.
   * @param args Arguments used to load CSV file
   * @param isREPL True if result is meant to be printed in terminal, false if meant
   *               to be printed in GUI
   * @return String output of Stars command
   */
  public String execute(String[] args, boolean isREPL) {
    String path = args[0];
    String result = "";
    try {
      sa = new StarApplications(path);
      int numStars = sa.getCSV().size();
      result = String.format("Read %d stars from %s\n", numStars, path);
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
    } catch (FileNotFoundException e) {
      System.err.println(e.getMessage());
    } catch (IOException e) {
      System.err.println(e.getMessage());
    } finally {
      return result;
    }
  }

  /**
   * Returns number of arguments that execute can take in.
   *
   * @return Array of possible numbers of arguments
   */
  public int[] getNumParameters() {
    return new int[]{1};
  }

  /**
   * Returns StarApplications object representing the CSV file.
   * Will be null if CSV file has not been read in yet.
   *
   * @return StarApplications object representing CSV file
   */
  public static StarApplications getStarApplications() {
    return sa;
  }
}
