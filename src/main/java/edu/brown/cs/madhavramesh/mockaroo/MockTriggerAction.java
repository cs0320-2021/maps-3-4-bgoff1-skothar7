package edu.brown.cs.madhavramesh.mockaroo;

import edu.brown.cs.madhavramesh.stars.TriggerAction;

/**
 * Reads in a CSV file and outputs a String representation of
 * the data.
 */
public class MockTriggerAction implements TriggerAction {

  /**
   * Returns command matching this TriggerAction.
   *
   * @return String representing command
   */
  public String command() {
    return "mock";
  }

  /**
   * Reads CSV file and outputs a String representation of each row
   * in the CSV file.
   * @param args Arguments used to load CSV file
   * @param isREPL True if result is meant to be printed in terminal, false if meant
   *               to be printed in GUI
   * @return String output of Mock command
   */
  public String execute(String[] args, boolean isREPL) {
    StringBuilder result = new StringBuilder();
    try {
      String path = args[0];
      MockApplications ma = new MockApplications();

      ma.csvParser(path);
      for (MockPerson person : ma.getMockPeople()) {
        result.append(person.toString() + "\n");
      }
    } catch (Exception e) {
      System.out.print("");
    } finally {
      return result.toString();
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
}
