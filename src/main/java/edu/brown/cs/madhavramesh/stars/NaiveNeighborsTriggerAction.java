package edu.brown.cs.madhavramesh.stars;

import java.util.List;

/**
 * Finds k nearest neighbors next to a specific point or a Star.
 */
public class NaiveNeighborsTriggerAction implements TriggerAction {
  private StarApplications sa;
  private final int shorterArgs = 2;
  private final int longerArgs = 4;

  /**
   * Returns command matching this TriggerAction.
   *
   * @return Command
   */
  public String command() {
    return "naive_neighbors";
  }

  /**
   * Finds k nearest neighbors next to a specific point or a Star
   * depending on the number of arguments given.
   * @param args Arguments used to find k nearest neighbors
   * @param isREPL True if result is meant to be printed in terminal, false if meant
   *               to be printed in GUI
   * @return String output of Naive Neighbors command
   */
  public String execute(String[] args, boolean isREPL) {
    StringBuilder result = new StringBuilder();
    try {
      List<Star> neighbors;
      if (args.length == shorterArgs) {
        neighbors = executeWithName(args);
      } else if (args.length == longerArgs) {
        neighbors = executeWithPos(args);
      } else {
        throw new IllegalArgumentException("ERROR: Correct number of arguments "
                + "were not provided for naive_neighbors command");
      }

      List<String> neighborsToDisplay;
      if (isREPL) {
        neighborsToDisplay = StarApplications.extractStarIDs(neighbors);
      } else {
        neighborsToDisplay = StarApplications.extractStarFields(neighbors);
      }

      for (String neighborToDisplay : neighborsToDisplay) {
        result.append(neighborToDisplay + "\n");
      }
      return result.toString();

    } catch (NumberFormatException e) {
      System.err.println("ERROR: Arguments provided after "
              + "naive_neighbors were not decimals");
    } catch (NullPointerException e) {
      System.err.println("ERROR: CSV file must be loaded first");
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
    } catch (Exception e) {
      System.err.println("ERROR: Could not run naive_neighbors command");
    } finally {
      return result.toString();
    }
  }

  /**
   * Finds k nearest neighbors next to a specific point.
   *
   * @param args Arguments used to find k nearest neighbors
   */
  private List<Star> executeWithPos(String[] args) {
    int k = Integer.parseInt(args[0]);
    double x = Double.parseDouble(args[1]);
    double y = Double.parseDouble(args[2]);
    double z = Double.parseDouble(args[3]);

    sa = StarsTriggerAction.getStarApplications();
    return sa.naiveNeighbors(k, x, y, z);
  }

  /**
   * Finds k nearest neighbors next to a specific star.
   *
   * @param args Arguments used to find k nearest neighbors
   */
  private List<Star> executeWithName(String[] args)
          throws IllegalArgumentException {
    int k = Integer.parseInt(args[0]);
    String name = args[1].replaceAll("\"", "");
    if (name.equals("")) {
      throw new IllegalArgumentException("ERROR: Name of star cannot be empty string");
    }

    sa = StarsTriggerAction.getStarApplications();
    return sa.naiveNeighbors(k, name);
  }

  /**
   * Returns number of arguments that execute can take in.
   *
   * @return Possible numbers of arguments
   */
  public int[] getNumParameters() {
    return new int[]{shorterArgs, longerArgs};
  }
}
