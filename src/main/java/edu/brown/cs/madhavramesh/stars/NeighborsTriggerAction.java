package edu.brown.cs.madhavramesh.stars;

import edu.brown.cs.madhavramesh.kdtree.KDTree;
import edu.brown.cs.madhavramesh.points.CoordinateObjects;
import edu.brown.cs.madhavramesh.points.HasCoordinates;
import edu.brown.cs.madhavramesh.points.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;


/**
 * Finds k nearest neighbors next to a specific point or a Star.
 */
public class NeighborsTriggerAction implements TriggerAction {
  private StarApplications sa = StarsTriggerAction.getStarApplications();
  private final int shorterArgs = 2;
  private final int longerArgs = 4;

  private static PriorityQueue<Star> currentNearest = new PriorityQueue<>();
  private static ArrayList<Star> currentFringe;

  public static void setCurrentNearest(PriorityQueue<Star> cn) {
    currentNearest = cn;
  }

  public static void resetCurrentFringe() {
    currentFringe = new ArrayList<>();
  }

  /**
   * Returns command matching this TriggerAction.
   *
   * @return Command
   */
  public String command() {
    return "neighbors";
  }

  /**
   * Finds k nearest neighbors next to a specific point or a Star
   * depending on the number of arguments given.
   * @param args Arguments used to find k nearest neighbors
   * @param isREPL True if output will be printed in REPL, false if output will be
   *               printed in GUI
   * @return String output of Neighbors command
   */
  public String execute(String[] args, boolean isREPL) {
    currentFringe = new ArrayList<>();
    StringBuilder result = new StringBuilder();
    try {
      List<Star> neighbors;
      if (args.length == shorterArgs) {
        neighbors = executeWithName(args);
      } else if (args.length == longerArgs) {
        neighbors = executeWithPos(args);
      } else {
        throw new IllegalArgumentException("ERROR: Correct number of arguments "
                + "were not provided for neighbors command");
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
              + "neighbors were not decimals");
    } catch (NullPointerException e) {
      System.err.println("ERROR: CSV file must be loaded first");
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
    } catch (Exception e) {
      System.err.println("ERROR: Could not run neighbors command");
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
    HasCoordinates startPos = new Point(x, y, z);
    sa = StarsTriggerAction.getStarApplications();
    KDTree<Star> starTree = sa.getStarsAsTree();
    Star[] nearest = neighbors(starTree, k, startPos);
    return Arrays.asList(nearest);
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
    HasCoordinates startPos = sa.findStarPos(name);
    CoordinateObjects<Star> starsWithNameRemoved = sa.removeStar(name);
    KDTree<Star> treeWithNameRemoved =
        new KDTree<>(new ArrayList<>(starsWithNameRemoved.getPoints()));
    Star[] nearest = neighbors(treeWithNameRemoved, k, startPos);
    return Arrays.asList(nearest);
  }

  /**
   * Returns number of arguments that execute can take in.
   *
   * @return Possible numbers of arguments
   */
  public int[] getNumParameters() {
    return new int[]{shorterArgs, longerArgs};
  }

  /**
   * Sets the local variables of close neighbors to the inputted coordinates.
   * Ties chosen randomly.
   *
   * @param data KDTree of stars
   * @param k    number of neighbors to find
   * @param pos  coordinate around which to search
   * @return Star[]
   */
  public static Star[] neighbors(KDTree data, int k, HasCoordinates pos) {
    currentFringe = new ArrayList<>();
    currentNearest = new PriorityQueue<>(Comparator.comparingDouble(
            s -> -1 * s.dist(pos)));
    currentNearest.clear();
    int count = k;
    neighborSearcher(data, pos, 0, count);
    int moveFromFringe = Math.min(k - currentNearest.size(), currentFringe.size());
    Collections.shuffle(currentFringe);
    for (int i = 0; i < moveFromFringe; i++) {
      currentNearest.add(currentFringe.get(i));
    }
    return currentNearest.stream().sorted(Comparator.comparingDouble(
            s -> s.dist(pos))).toArray(Star[]::new);
  }

  private static void neighborSearcher(KDTree data, HasCoordinates pos,
                                       int layer, int count) {
    int dimension = 3;
    int index = layer % dimension;
    if (data.getNode().isPresent() && count != 0) {
      Star current = (Star) data.getNode().get();
      if (currentNearest.size() + currentFringe.size() < count) {
        /*
        The fringe is always loaded first, as the nearest is limited to count - 1
        Fringe contains the stars of the farthest distance that may still contain a near neighbor
        is always maintained at size > 0, but acts as only one slot in the queue it is the real
        head of the priority queue, ties are decided randomly from within it after the fact
        */
        if (currentFringe.size() == 0
                || current.dist(pos) == currentFringe.get(0).dist(pos)) {
          currentFringe.add(current);
        } else {
          if (current.dist(pos) > currentFringe.get(0).dist(pos)) {
            for (Star star : currentFringe) {
              currentNearest.add(star);
            }
            //When something is kicked off of the fringe, a new item must take its place
            currentFringe.clear();
            currentFringe.add(current);
          } else {
            currentNearest.add(current);
          }
        }
      } else {
        if (current.dist(pos) == currentFringe.get(0).dist(pos)) {
          currentFringe.add(current);
        } else {
          if (current.dist(pos) < currentFringe.get(0).dist(pos)) {
            if (currentNearest.size() < count - 1) {
              currentNearest.add(current);
            } else {
              currentNearest.add(current);
              Star moveToFringe = currentNearest.poll();
              currentFringe.clear();
              currentFringe.add(moveToFringe);
            }
          }
        }
      }
      //switch branches are mostly identical
      //there is one for each dimension that may be split on, denoted by 0, 1, 2 for x, y, z
      switch (index) {
        case 0:
          if (currentFringe.get(0).dist(pos) > Math.abs(current.getX() - pos.getCoordinate(0))
                  || currentFringe.size() + currentNearest.size() < count) {
            //goes down both sides if we haven't hit the quota yet
            if (data.getLeft().isPresent()) {
              neighborSearcher((KDTree) data.getLeft().get(), pos, index + 1, count);
            }
            if (data.getRight().isPresent()) {
              neighborSearcher((KDTree) data.getRight().get(), pos, index + 1, count);
            }
          } else {
            //if we have, it goes to the side closer to the current side
            if (current.getX() <= pos.getCoordinate(0)) {
              if (data.getRight().isPresent()) {
                neighborSearcher((KDTree) data.getRight().get(), pos, index + 1, count);
              }
            } else {
              if (data.getLeft().isPresent()) {
                neighborSearcher((KDTree) data.getLeft().get(), pos, index + 1, count);
              }
            }
          }
          break;
        case 1:
          if (currentFringe.get(0).dist(pos) > Math.abs(current.getY() - pos.getCoordinate(1))
                  || currentFringe.size() + currentNearest.size() < count) {
            if (data.getLeft().isPresent()) {
              neighborSearcher((KDTree) data.getLeft().get(), pos, index + 1, count);
            }
            if (data.getRight().isPresent()) {
              neighborSearcher((KDTree) data.getRight().get(), pos, index + 1, count);
            }
          } else {
            if (current.getY() <= pos.getCoordinate(1)) {
              if (data.getRight().isPresent()) {
                neighborSearcher((KDTree) data.getRight().get(), pos, index + 1, count);
              }
            } else {
              if (data.getLeft().isPresent()) {
                neighborSearcher((KDTree) data.getLeft().get(), pos, index + 1, count);
              }
            }
          }
          break;
        case 2:
          if (currentFringe.get(0).dist(pos) > Math.abs(current.getZ() - pos.getCoordinate(2))
                  || currentFringe.size() + currentNearest.size() < count) {
            if (data.getLeft().isPresent()) {
              neighborSearcher((KDTree) data.getLeft().get(), pos, index + 1, count);
            }
            if (data.getRight().isPresent()) {
              neighborSearcher((KDTree) data.getRight().get(), pos, index + 1, count);
            }
          } else {
            if (current.getZ() <= pos.getCoordinate(2)) {
              if (data.getRight().isPresent()) {
                neighborSearcher((KDTree) data.getRight().get(), pos, index + 1, count);
              }
            } else {
              if (data.getLeft().isPresent()) {
                neighborSearcher((KDTree) data.getLeft().get(), pos, index + 1, count);
              }
            }
          }
          break;
        default:
      }
    }
  }
}
