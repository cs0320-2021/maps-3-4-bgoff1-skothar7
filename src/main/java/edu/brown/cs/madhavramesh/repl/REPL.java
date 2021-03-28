package edu.brown.cs.madhavramesh.repl;

import edu.brown.cs.madhavramesh.stars.TriggerAction;
import edu.brown.cs.madhavramesh.stars.TriggerActionExecutor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/** REPL which loops until EOF command is given. Reads one line at a time,
 * interpreting each line as a command.
 */
public final class REPL {
  private REPL() {
  }

  /** Runs the REPL.
   * @param tas List of actions corresponding to commands that can be executed
   */
  public static void run(List<TriggerAction> tas) {
    try (BufferedReader br = new BufferedReader(
        new InputStreamReader(System.in))) {
      String rawInput;
      while ((rawInput = br.readLine()) != null) {
        try {
          // REGEX taken from https://stackoverflow.com/questions/1757065/
          String[] splitInput = rawInput.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
          String command = splitInput[0];
          String[] args = Arrays.copyOfRange(splitInput, 1, splitInput.length);

          TriggerActionExecutor executor = new TriggerActionExecutor(tas);
          System.out.print(executor.executeTriggerAction(command, args, true));
        } catch (IllegalArgumentException e) {
          System.err.println(e.getMessage());
        }
      }
    } catch (IOException e) {
      System.err.println("ERROR: Invalid input for REPL");
    }
  }
}
