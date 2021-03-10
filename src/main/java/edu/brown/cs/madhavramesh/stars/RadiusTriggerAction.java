package edu.brown.cs.madhavramesh.stars;

import edu.brown.cs.madhavramesh.kdtree.KDTree;
import edu.brown.cs.madhavramesh.points.CoordinateObjects;
import edu.brown.cs.madhavramesh.points.HasCoordinates;
import edu.brown.cs.madhavramesh.points.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Finds k nearest neighbors next to a specific point or a Star.
 */
public class RadiusTriggerAction implements TriggerAction {
  private static StarApplications sa = StarsTriggerAction.getStarApplications();
  private final int shorterArgs = 2;
  private final int longerArgs = 4;

  /**
   * Returns command matching this TriggerAction.
   *
   * @return Command
   */
  public String command() {
    return "radius";
  }

  /**
   * Finds k nearest neighbors next to a specific point or a Star
   * depending on the number of arguments given.
   * @param args Arguments used to find k nearest neighbors
   * @param isREPL True if output will be printed in REPL, false if output will be
   *               printed in GUI
   * @return String output of Radius command
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
                + "were not provided for radius command");
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
              + "radius were not decimals");
    } catch (NullPointerException e) {
      System.err.println("ERROR: CSV file must be loaded first");
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
    } catch (RuntimeException e) {
      System.err.println(e.getMessage());
    } catch (Exception e) {
      System.err.println("ERROR: Could not run radius command");
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
    double k = Double.parseDouble(args[0]);
    double x = Double.parseDouble(args[1]);
    double y = Double.parseDouble(args[2]);
    double z = Double.parseDouble(args[3]);
    if (k < 0) {
      throw new RuntimeException("ERROR: Radius can not be negative");
    }
    HasCoordinates center = new Point(x, y, z);
    sa = StarsTriggerAction.getStarApplications();
    KDTree starTree = sa.getStarsAsTree();
    Star[] withinRadius = radius(starTree, k, center);
    return Arrays.asList(withinRadius);
  }

  /**
   * Finds k nearest neighbors next to a specific star.
   *
   * @param args Arguments used to find k nearest neighbors
   */
  private List<Star> executeWithName(String[] args)
          throws IllegalArgumentException {
    double k = Double.parseDouble(args[0]);
    if (k < 0) {
      throw new RuntimeException("ERROR: Radius can not be negative");
    }
    String name = args[1].replaceAll("\"", "");
    if (name.equals("")) {
      throw new IllegalArgumentException("ERROR: Name of star cannot be empty string");
    }
    sa = StarsTriggerAction.getStarApplications();
    CoordinateObjects<Star> starsWithNameRemoved = sa.removeStar(name);
    KDTree starTree = new KDTree(new ArrayList<>(starsWithNameRemoved.getPoints()));
    Star[] withinRadius = radius(starTree, k, name);
    return Arrays.asList(withinRadius);
  }

  /**
   * Returns the iDs of all Stars whose distance from the input coordinate is less than r.
   *
   * @param starTree KDTree of stars in which to search
   * @param r        radius in which to search
   * @param pos      position around whic to search
   * @return ArrayList
   */
  public static Star[] radius(KDTree starTree, double r, HasCoordinates pos) {
    ArrayList<Star> withinRadiusLeft = new ArrayList<>();
    ArrayList<Star> withinRadiusRight = new ArrayList<>();
    if (starTree.getNode().isEmpty()) {
      //if theres nothing to search through, return nothing
      return new Star[0];
    } else {
      Star node = (Star) starTree.getNode().get();
      //move down the branches that could contain the targets sphere of interest
      if (pos.getCoordinate(0) - node.getX() < r) {
        withinRadiusLeft = radius(starTree.getLeft(), r, pos, 1);
      }
      if (node.getX() - pos.getCoordinate(0) <= r) {
        withinRadiusRight = radius(starTree.getRight(), r, pos, 1);
      }
      //elements are added one at a time, as the branches are iterated down
      if ((node.dist(pos) <= r)) {
        withinRadiusRight.add(node);
      }
      //Streams used to filter data
      return Stream.of(withinRadiusLeft, withinRadiusRight).flatMap(los -> los.stream())
              .sorted(Comparator.comparingDouble(s -> s.dist(pos))).toArray(Star[]::new);
    }
  }

  /**
   * Returns the iDs of all Stars whose distance from star with the inputted name is less than r.
   *
   * @param starTree KDTree of loaded stars
   * @param r        radius in which to search
   * @param name     name of star around which to search
   * @return ArrayList
   */
  public static Star[] radius(KDTree starTree, double r, String name) {
    try {
      HasCoordinates target = sa.findStarPos(name);
      if (target == null) {
        //throw error is target start not in list of loaded stars
        throw new RuntimeException("ERROR: Star not found");
      }
      ArrayList<Star> withinRadiusLeft = new ArrayList<>();
      ArrayList<Star> withinRadiusRight = new ArrayList<>();
      if (starTree.getNode().isEmpty()) {
        return new Star[0];
      } else {
        //same process as above, but adapted for a star Object
        Star node = (Star) starTree.getNode().get();
        if (target.getCoordinate(0) - node.getX() < r) {
          withinRadiusLeft = radius(starTree.getLeft(), r, target, 1);
        }
        if (node.getX() - target.getCoordinate(0) <= r) {
          withinRadiusRight = radius(starTree.getRight(), r, target, 1);
        }
        if ((node.dist(target) <= r)) {
          withinRadiusRight.add(node);
        }
        //Streams used to filter data
        return Stream.of(withinRadiusLeft, withinRadiusRight).flatMap(los -> los.stream())
                .filter(star -> !star.getName().equals(name)).toArray(Star[]::new);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new Star[0];
    }
  }

  /**
   * Returns the iDs of all Stars whose distance from the input coordinate
   * is less than r in the given KDTree, slicing on coordinate l.
   *
   * @param tree Optional KDTree of stars to search around
   * @param r    radius in which to search
   * @param pos  position around which to search
   * @param l    layer in KDTree to start searching
   * @return ArrayList
   */
  public static ArrayList<Star> radius(Optional<KDTree> tree, double r,
                                       HasCoordinates pos, int l) {
    //this function introduces a layer parameter, so that the radius function two above
    //can work without one using 0 as a standard value
    int layer = l;
    int dimension = 3;
    int index = layer % dimension;
    ArrayList<Star> withinRadiusLeft = new ArrayList<>();
    ArrayList<Star> withinRadiusRight = new ArrayList<>();
    if (tree.isEmpty()) {
      return new ArrayList<>();
    } else {
      Star node = (Star) tree.get().getNode().get();
      switch (index) {
        case 0:
          if (pos.getCoordinate(0) - node.getX() < r) {
            withinRadiusLeft = radius(tree.get().getLeft(), r, pos, layer + 1);
          }
          if (node.getX() - pos.getCoordinate(0) <= r) {
            withinRadiusRight = radius(tree.get().getRight(), r, pos, layer + 1);
          }
          if ((node.dist(pos) <= r)) {
            withinRadiusRight.add(node);
          }
          break;
        case 1:
          if (pos.getCoordinate(1) - node.getY() < r) {
            withinRadiusLeft = radius(tree.get().getLeft(), r, pos, layer + 1);
          }
          if (node.getY() - pos.getCoordinate(1) <= r) {
            withinRadiusRight = radius(tree.get().getRight(), r, pos, layer + 1);
          }
          if ((node.dist(pos) <= r)) {
            withinRadiusRight.add(node);
          }
          break;
        case 2:
          if (pos.getCoordinate(2) - node.getZ() < r) {
            withinRadiusLeft = radius(tree.get().getLeft(), r, pos, layer + 1);
          }
          if (node.getZ() - pos.getCoordinate(2) <= r) {
            withinRadiusRight = radius(tree.get().getRight(), r, pos, layer + 1);
          }
          if ((node.dist(pos) <= r)) {
            withinRadiusRight.add(node);
          }
          break;
        default:
      }
      //Streams used to filter data
      return new ArrayList<Star>(Stream.of(withinRadiusLeft, withinRadiusRight)
              .flatMap(los -> los.stream()).collect(Collectors.toList()));
    }
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
