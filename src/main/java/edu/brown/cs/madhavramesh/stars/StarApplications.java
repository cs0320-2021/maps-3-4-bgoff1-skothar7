package edu.brown.cs.madhavramesh.stars;

import edu.brown.cs.madhavramesh.kdtree.KDTree;
import edu.brown.cs.madhavramesh.points.CoordinateObjects;
import edu.brown.cs.madhavramesh.points.HasCoordinates;
import edu.brown.cs.madhavramesh.points.Point;
import edu.brown.cs.madhavramesh.repl.CSVParser;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

/** Contains methods that can be applied to stars. */
public class StarApplications {
  private final List<String> header =
      Arrays.asList("StarID", "ProperName", "X", "Y", "Z");

  private CSVParser stars;
  private CoordinateObjects<Star> starsAsPoints;
  private static KDTree<Star> starsAsTree;

  /** Loads a CSV file and stores it.
   * @param path path to CSV file
   * @throws IllegalArgumentException if file has invalid header or row width
   * @throws IOException if file not found
   */
  public StarApplications(String path)
      throws IllegalArgumentException, IOException {
    stars = new CSVParser(path);
    if (!stars.isCorrectHeader(header)) {
      throw new IllegalArgumentException("ERROR: Invalid header");
    } else if (!stars.isCorrectRowLength(header.size())) {
      throw new IllegalArgumentException("ERROR: Number of elements "
          + "in each row do not match");
    }

    stars.removeHeader();
    starsAsPoints = convertStarsToCoordinateObjects(stars);
    starsAsTree = new KDTree(convertStarsToHasCoordinates(stars));
  }

  /** Finds a nonnegative, integral number of neighbors next to
   * a certain point.
   * @param k Number of neighbors to find
   * @param x X coordinate of point
   * @param y Y coordinate of point
   * @param z Z coordinate of point
   * @return List of Star IDs corresponding to neighboring Stars found
   */
  public List<Star> naiveNeighbors(int k, double x, double y, double z)
    throws IllegalArgumentException {
    HasCoordinates startPos = new Point(x, y, z);
    return starsAsPoints.kNearestNeighborsNaive(k, startPos);
  }

  /** Finds a nonnegative, integral number of neighbors next to
   * a certain star (doesn't include the star as a neighbor).
   * @param k Number of neighbors to find
   * @param name Name of star
   * @return List of Star IDs corresponding to neighboring Stars found
   */
  public List<Star> naiveNeighbors(int k, String name)
    throws IllegalArgumentException {
    HasCoordinates startPos = findStarPos(name);
    CoordinateObjects<Star> starsWithNameRemoved = removeStar(name);
    return starsWithNameRemoved.kNearestNeighborsNaive(k, startPos);
  }

  /** Finds a nonnegative, integral number of neighbors within a certain
   * radius of a point.
   * @param r Search radius
   * @param x X coordinate of point
   * @param y Y coordinate of point
   * @param z Z coordinate of point
   * @return List of Star IDs corresponding to neighboring Stars found
   */
  public List<Star> naiveRadius(double r, double x, double y, double z)
    throws IllegalArgumentException {
    HasCoordinates startPos = new Point(x, y, z);
    return starsAsPoints.nearestNeighborsInRadiusNaive(r, startPos);
  }

  /** Finds a nonnegative, integral number of neighbors within a certain
   * radius of a star.
   * @param r Search radius
   * @param name Name of star
   * @return List of Star IDs corresponding to neighboring Stars found
   */
  public List<Star> naiveRadius(double r, String name)
    throws IllegalArgumentException {
    HasCoordinates startPos = findStarPos(name);
    List<Star> starsInList = starsAsPoints.getPoints();
    starsInList.removeIf(s -> (s.getName().equals(name)));
    CoordinateObjects<Star> starsWithNameRemoved = new CoordinateObjects<>(starsInList);
    return starsWithNameRemoved.nearestNeighborsInRadiusNaive(r, startPos);
  }

  /** Finds the coordinates corresponding to a given star.
   * @param name Name of star
   * @return Point3D containing coordinates of star
   */
  Point findStarPos(String name) {
    int matchingRowIndex = stars.findEntry(name, 1);
    List<String> matchingRow = stars.get(matchingRowIndex);
    double x = Double.parseDouble(matchingRow.get(2));
    double y = Double.parseDouble(matchingRow.get(3));
    double z = Double.parseDouble(matchingRow.get(4));
    return new Point(Arrays.asList(x, y, z));
  }

  CoordinateObjects<Star> removeStar(String name) {
    List<Star> starsInList = starsAsPoints.getPoints();
    starsInList.removeIf(s -> (s.getName().equals(name)));
    return new CoordinateObjects<>(starsInList);
  }

  /** Converts each star in the CSV file to a Points object. */
  private CoordinateObjects<Star> convertStarsToCoordinateObjects(CSVParser starsCSV)
    throws IllegalArgumentException {
    CoordinateObjects<Star> starPoints = new CoordinateObjects<Star>(new ArrayList<>());
    try {
      for (List<String> row : starsCSV.getData()) {
        int id = Integer.parseInt(row.get(0));
        String name = row.get(1);
        double x = Double.parseDouble(row.get(2));
        double y = Double.parseDouble(row.get(3));
        double z = Double.parseDouble(row.get(4));
        Star rowStar = new Star(id, name, x, y, z);
        starPoints.addPoint(rowStar);
      }
      return starPoints;
    } catch (NumberFormatException ne) {
      throw new IllegalArgumentException("ERROR: CSV file contains rows "
          + "that are not Integer, String, Double, Double, Double");
    }
  }

  /** Converts each star in the CSV file to a HasCoordinates object. */
  private ArrayList<HasCoordinates> convertStarsToHasCoordinates(CSVParser starsCSV)
      throws IllegalArgumentException {
    ArrayList<HasCoordinates> starPoints = new ArrayList<>();
    try {
      for (List<String> row : starsCSV.getData()) {
        int id = Integer.parseInt(row.get(0));
        String name = row.get(1);
        double x = Double.parseDouble(row.get(2));
        double y = Double.parseDouble(row.get(3));
        double z = Double.parseDouble(row.get(4));
        Star rowStar = new Star(id, name, x, y, z);
        starPoints.add(rowStar);
      }
      return starPoints;
    } catch (NumberFormatException ne) {
      throw new IllegalArgumentException("ERROR: CSV file contains rows "
          + "that are not Integer, String, Double, Double, Double");
    }
  }

  /** Returns the IDs corresponding to a List of Stars.
   * @param rawStars List of stars
   * @return IDs corresponding to stars
   */
  public static List<String> extractStarIDs(List<Star> rawStars) {
    List<String> ids = new ArrayList<>();
    for (Star s : rawStars) {
      ids.add(String.valueOf(s.getID()));
    }
    return ids;
  }

  public static List<String> extractStarFields(List<Star> rawStars) {
    List<String> starsInfo = new ArrayList<>();
    for (Star s : rawStars) {
      String id = String.valueOf(s.getID());
      String name = s.getName();
      String x = String.format("%.5f", s.getX());
      String y = String.format("%.5f", s.getY());
      String z = String.format("%.5f", s.getZ());
      String output = name + "\t" + id + "\t(" + x + ", " + y + ", " + z + ")";
      starsInfo.add(output);
    }
    return starsInfo;
  }

  /** Returns a List of List of Strings representation of CSV file.
   * @return List of List of Strings representing CSV file
   */
  public CSVParser getCSV() {
    return stars;
  }

  public KDTree getStarsAsTree() {
    return starsAsTree;
  }
}
