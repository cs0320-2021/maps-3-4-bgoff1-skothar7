package edu.brown.cs.madhavramesh.stars;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

/** Invoker that executes a given command. */
public class TriggerActionExecutor {
  private Map<String, TriggerAction> actions = new HashMap<>();

  /** Construct a TriggerActionExecutor using a list of possible actions
   * that can be executed.
   * @param tas List of actions corresponding to commands that can
   *            be executed
   */
  public TriggerActionExecutor(List<TriggerAction> tas) {
    for (TriggerAction ta : tas) {
      actions.put(ta.command(), ta);
    }
  }

  /** Executes a given command using the arguments passed in.
   * @param command Command in question
   * @param args Arguments that command will take in
   * @param isREPL whether command is being called through the REPL or not
   * @return String output of command
   */
  public String executeTriggerAction(String command, String[] args, boolean isREPL)
    throws IllegalArgumentException {
    String result = "";
    TriggerAction correctAction = actions.get(command);
    if (correctAction != null) {
      boolean invalidArgs = true;
      for (int possibleNumParameters : correctAction.getNumParameters()) {
        if (args.length == possibleNumParameters) {
          invalidArgs = false;
          result = correctAction.execute(args, isREPL);
        }
      }
      if (invalidArgs) {
        throw new IllegalArgumentException("ERROR: Invalid number of arguments");
      }
    } else {
      throw new IllegalArgumentException("ERROR: Command not understood");
    }
    return result;
  }
}
