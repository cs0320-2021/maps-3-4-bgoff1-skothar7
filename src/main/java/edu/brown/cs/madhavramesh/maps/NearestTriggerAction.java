package edu.brown.cs.madhavramesh.maps;

import edu.brown.cs.madhavramesh.kdtree.KDTree;
import edu.brown.cs.madhavramesh.stars.TriggerAction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Executes nearest command and finds nearest traversable node to
 * the input point.
 */
public class NearestTriggerAction implements TriggerAction {

  private final int argsCount = 2;

  /**
   * Returns command matching this TriggerAction.
   *
   * @return Command
   */
  @Override
  public String command() {
    return "nearest";
  }

  /**
   * Returns number of arguments that execute can take in.
   *
   * @return Possible numbers of arguments
   */
  @Override
  public int[] getNumParameters() {
    return new int[] {argsCount};
  }

  /**
   * Searches for the nearest traversable node to the input point.
   *
   * @param args   Arguments used to find relevant ways
   * @param isREPL True if result is meant to be printed in terminal, false if meant
   *               to be printed in GUI
   * @return String representing result
   */
  @Override
  public String execute(String[] args, boolean isREPL) {
    StringBuilder result = new StringBuilder();
    try {
      if (args.length != getNumParameters()[0]) {
        throw new IOException("Nearest must have 2 parameters");
      }
      double lat = Double.parseDouble(args[0].trim());
      double lon = Double.parseDouble(args[1].trim());

      KDTree<MapNode> currentNodes = Maps.getNodesTree();
      MapNode targetNode = closestNode(lat, lon, currentNodes);

      result.append(targetNode.getStringID() + "\n");
      return result.toString();
    } catch (NumberFormatException e) {
      System.err.println("ERROR: Arguments provided after ways were not doubles");
    } catch (NullPointerException e) {
      System.err.println("ERROR: Data must be loaded first");
    } catch (IllegalArgumentException | IOException e) {
      System.err.println(e.getMessage());
    } catch (Exception e) {
      System.err.println("ERROR: Could not run ways command");
    } finally {
      return result.toString();
    }
  }

  public static MapNode closestNode(double lat, double lon, KDTree<MapNode> nodes) {
    if (nodes==null || nodes.getNode().isEmpty()) {
      throw new NullPointerException();
    }
    return closestNode(lat, lon, nodes.getNode().get(), nodes, 0);
  }

  private static MapNode closestNode(double lat, double lon, MapNode c,
                                     KDTree<MapNode> nodes, int d) {
    MapNode mock = new MapNode("", lat, lon);
    int index = d % 2;
    MapNode currentNode = nodes.getNode().get();
    MapNode closest = c;
    MapNode closestLeft = null;
    MapNode closestRight = null;
    List<MapNode> possibleClosest;
    double shortestDistance;
    if (Double.compare(mock.dist(closest), mock.dist(currentNode)) == 0) {
      //https://stackoverflow.com/questions/40311442/how-to-randomly-select-one-from-two-integers
      closest = new Random().nextBoolean() ? currentNode : closest;
    }
    if (Double.compare(mock.dist(closest), mock.dist(currentNode)) > 0) {
      closest = currentNode;
    }
    if (Double.compare(mock.dist(closest), Math.abs(
        currentNode.getCoordinate(index) - mock.getCoordinate(index))) > 0) {
      if (nodes.getRight().isPresent()) {
        closestRight = closestNode(lat, lon, closest, nodes.getRight().get(), index + 1);
      }
      if (nodes.getLeft().isPresent()) {
        closestLeft = closestNode(lat, lon, closest, nodes.getLeft().get(), index + 1);
      }
    } else {
      //if (Double.compare(currentNode.getCoordinate(index), mock.getCoordinate(index)) < 0) {
        if (nodes.getRight().isPresent()) {
          closestRight = closestNode(lat, lon, closest, nodes.getRight().get(), index + 1);
        }
      }// else {
        if (nodes.getLeft().isPresent()) {
          closestLeft = closestNode(lat, lon, closest, nodes.getLeft().get(), index + 1);
        }
      //}
    //}
      //}
    //}
    possibleClosest = new ArrayList<>(Collections.singletonList(currentNode));
    if (closestLeft != null) {
      possibleClosest.add(closestLeft);
    }
    if (closestRight != null) {
      possibleClosest.add(closestRight);
    }
    possibleClosest.sort(Comparator.comparingDouble(ln -> ln.dist(mock)));
    shortestDistance = possibleClosest.get(0).dist(mock);
    for (int i = possibleClosest.size() - 1; i >= 0; i--) {
      if (possibleClosest.get(i).dist(mock) != shortestDistance) {
        possibleClosest.remove(i);
     } //else {
        //System.out.println(i + ": "+ possibleClosest.get(i).getStringID());
      //}
    }
   Collections.shuffle(possibleClosest);
    return possibleClosest.get(0);
  }
}
