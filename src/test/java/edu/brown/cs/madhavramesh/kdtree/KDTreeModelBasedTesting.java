package edu.brown.cs.madhavramesh.kdtree;

import edu.brown.cs.madhavramesh.points.CoordinateObjects;
import edu.brown.cs.madhavramesh.points.HasCoordinates;
import edu.brown.cs.madhavramesh.points.Point;
import edu.brown.cs.madhavramesh.stars.NeighborsTriggerAction;
import edu.brown.cs.madhavramesh.stars.RadiusTriggerAction;
import edu.brown.cs.madhavramesh.stars.Star;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class KDTreeModelBasedTesting {
  private final int MAX_UNICODE = 65535;
  private final int MAX_STARS = 1000;
  private final int NAME_LENGTH = 1000;
  private final double MAX_COORDINATE_VAL = 1000;


  private Random randomGenerator;
  private int numStars;

  public KDTreeModelBasedTesting() {
    randomGenerator = new Random();
    this.numStars = randomGenerator.nextInt(MAX_STARS) + 1;
  }

  public KDTreeModelBasedTesting(int numStars) {
    randomGenerator = new Random();
    this.numStars = numStars;
  }

  private Star generateStar() {
    int id = 2 * randomGenerator.nextInt(Integer.MAX_VALUE) - Integer.MAX_VALUE;

    StringBuilder nameSB = new StringBuilder();
    for (int i = 0; i < randomGenerator.nextInt(NAME_LENGTH); i++) {
      char randomSymbol = (char) randomGenerator.nextInt(MAX_UNICODE);
      nameSB.append(randomSymbol);
    }
    String name = nameSB.toString();

    double x = 2 * (MAX_COORDINATE_VAL * randomGenerator.nextDouble() - (MAX_COORDINATE_VAL / 2));
    double y = 2 * (MAX_COORDINATE_VAL * randomGenerator.nextDouble() - (MAX_COORDINATE_VAL / 2));
    double z = 2 * (MAX_COORDINATE_VAL * randomGenerator.nextDouble() - (MAX_COORDINATE_VAL / 2));

    Star randomStar = new Star(id, name, x, y, z);
    return randomStar;
  }

  private List<Star> generateStars() {
    List<Star> randomStars = new ArrayList<>();
    for (int i = 0; i < numStars; i++) {
      randomStars.add(generateStar());
    }
    return randomStars;
  }

  private List<Double> generateQuery(boolean neighbors) {
    double kOrR = randomGenerator.nextDouble() * MAX_COORDINATE_VAL;
    double x = 2 * (MAX_COORDINATE_VAL * randomGenerator.nextDouble() - (MAX_COORDINATE_VAL / 2));
    double y = 2 * (MAX_COORDINATE_VAL * randomGenerator.nextDouble() - (MAX_COORDINATE_VAL / 2));
    double z = 2 * (MAX_COORDINATE_VAL * randomGenerator.nextDouble() - (MAX_COORDINATE_VAL / 2));

    return Arrays.asList(kOrR, x, y, z);
  }

  private boolean distancesEqual(List<Star> result1, List<Star> result2, HasCoordinates reference) {
    if (result1.size() != result2.size()) {
      return false;
    }

    List<Double> distances1 = new ArrayList<>();
    List<Double> distances2 = new ArrayList<>();
    for (int i = 0; i < result1.size(); i++) {
      distances1.add(result1.get(i).dist(reference));
      distances2.add(result2.get(i).dist(reference));
    }

    return distances1.equals(distances2);
  }

  private boolean testSpecificCase(CoordinateObjects<Star> naiveRepresentation,
                                   KDTree kdTree,
                                   double kOrR,
                                   HasCoordinates point,
                                   boolean neighbors) {
    List<Star> naiveResults;
    List<Star> kdTreeResults;
    if (neighbors) {
      naiveResults = naiveRepresentation.kNearestNeighborsNaive((int) kOrR, point);
      kdTreeResults = Arrays.asList(NeighborsTriggerAction.neighbors(kdTree, (int) kOrR, point));
    } else {
      naiveResults = naiveRepresentation.nearestNeighborsInRadiusNaive(kOrR, point);
      kdTreeResults = Arrays.asList(RadiusTriggerAction.radius(kdTree, kOrR, point));
    }

    return distancesEqual(naiveResults, kdTreeResults, point);
  }

  private boolean testEdgeCases(boolean neighbors) {
    List<Star> randomStars = generateStars();
    List<Double> randomQuery = generateQuery(randomGenerator.nextBoolean());

    double kOrR = randomQuery.get(0).doubleValue();
    double x = randomQuery.get(1).doubleValue();
    double y = randomQuery.get(2).doubleValue();
    double z = randomQuery.get(3).doubleValue();

    HasCoordinates randomPoint = new Point(x, y, z);

    CoordinateObjects<Star> naiveRepresentation = new CoordinateObjects<>(randomStars);
    KDTree kdTree = new KDTree(new ArrayList<>(randomStars));

    CoordinateObjects<Star> naiveRepresentationEmpty = new CoordinateObjects<>(new ArrayList<>());
    KDTree kdTreeEmpty = new KDTree(new ArrayList<>());

    boolean edgeCasesTest =
        testSpecificCase(naiveRepresentation, kdTree, 0, randomPoint, neighbors)
            && testSpecificCase(naiveRepresentation, kdTree,
            randomStars.size() + 10, randomPoint, neighbors)
            && testSpecificCase(naiveRepresentation, kdTree, 0, randomPoint, neighbors)
            && testSpecificCase(naiveRepresentationEmpty, kdTreeEmpty,
            kOrR, randomPoint, neighbors);

    return edgeCasesTest;
  }

  public boolean runMBTOnce(boolean neighbors) {
    List<Star> randomStars = generateStars();
    List<Double> randomQuery = generateQuery(randomGenerator.nextBoolean());

    double kOrR = randomQuery.get(0).doubleValue();
    double x = randomQuery.get(1).doubleValue();
    double y = randomQuery.get(2).doubleValue();
    double z = randomQuery.get(3).doubleValue();

    HasCoordinates randomPoint = new Point(x, y, z);

    CoordinateObjects<Star> naiveRepresentation = new CoordinateObjects<>(randomStars);
    KDTree kdTree = new KDTree(new ArrayList<>(randomStars));

    return testSpecificCase(naiveRepresentation, kdTree, kOrR, randomPoint, neighbors);
  }

  public boolean runMBTMultipleTimes(int numTimes, boolean neighbors) {
    if (numTimes < 0) {
      throw new IllegalArgumentException(
          "ERROR: Number of times to run MBT must be a positive integer");
    }

    if (!testEdgeCases(neighbors)) {
      return false;
    }

    for (int i = 0; i < numTimes; i++) {
      if (!runMBTOnce(neighbors)) {
        return false;
      }
    }
    return true;
  }
}
