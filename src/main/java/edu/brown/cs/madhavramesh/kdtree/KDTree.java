package edu.brown.cs.madhavramesh.kdtree;

import edu.brown.cs.madhavramesh.maps.MapNode;
import edu.brown.cs.madhavramesh.points.HasCoordinates;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class KDTree<T extends HasCoordinates> {

  private int dimension;
  private int layer;
  private Optional<T> node;
  private Optional<KDTree<T>> left;
  private Optional<KDTree<T>> right;

  /**
   * Returns the node of a KDTree.
   *
   * @return Optional
   */
  public Optional<T> getNode() {
    return node;
  }

  /**
   * Returns the left side of a KDTree.
   *
   * @return Optional
   */
  public Optional<KDTree<T>> getLeft() {
    return left;
  }

  /**
   * Returns the right side of a KDTree.
   *
   * @return Optional
   */
  public Optional<KDTree<T>> getRight() {
    return right;
  }

  /**
   * Returns the dimension count KDTree.
   *
   * @return int
   */
  public int getNumDimensions() {
    return dimension;
  }

  /**
   * Creates a new, empty KDTree of the given dimension.
   *
   * @param d dimension
   */
  public KDTree(int d) {
    layer = 0;
    dimension = d;
    node = Optional.empty();
    left = Optional.empty();
    right = Optional.empty();
  }

  /**
   * Creates a new KDTree with the given one coordinate.
   *
   * @param coordinate point at the KDTree root node
   */
  public KDTree(T coordinate) {
    layer = 0;
    dimension = coordinate.getNumDimensions();
    node = Optional.of(coordinate);
    left = Optional.empty();
    right = Optional.empty();
  }

  /**
   * Creates a new KDTree with the given one coordinate at the specified root layer.
   *
   * @param coordinate point at the KDTree root node
   * @param l layer at which to start construction
   */
  public KDTree(T coordinate, int l) {
    layer = l;
    dimension = coordinate.getNumDimensions();
    node = Optional.of(coordinate);
    left = Optional.empty();
    right = Optional.empty();
  }

  /**
   * Creates a new KDTree of the given Coordinates.
   *
   * @param coordinates List of Coordinates
   */
  public KDTree(List<T> coordinates) {
    if (coordinates.isEmpty()) {
      dimension = 0;
      node = Optional.empty();
      left = Optional.empty();
      right = Optional.empty();
    } else {
      layer = 0;
      dimension = coordinates.get(0).getNumDimensions();

      int index = layer % dimension;
      coordinates.sort(Comparator.comparingDouble(c -> c.getCoordinate(index)));
      int size = coordinates.size();
      int center = size / 2;
      T median = coordinates.get(center);
      node = Optional.of(median);

      boolean notMedian = true;
      while (notMedian && (center != 0)) {
        T potMedian = coordinates.get(center - 1);
        if (potMedian.equals(median)) {
          center = center - 1;
        } else {
          notMedian = false;
        }
      }

      List<T> leftList = coordinates.subList(0, center);
      List<T> rightList = coordinates.subList(center + 1, size);

      if (leftList.isEmpty()) {
        left = Optional.empty();
      } else {
        left = Optional.of(new KDTree<>(leftList, index + 1));
      }
      if (rightList.isEmpty()) {
        right = Optional.empty();
      } else {
        right = Optional.of(new KDTree<>(rightList, index + 1));
      }
    }
  }

  /**
   * Creates a new KDTree of the given Coordinates with a specified root layer.
   * Used primarily to recur from the above constructor.
   *
   * @param coordinates List of Coordinates
   * @param l Layer at which to start construction of KDTree
   */
  public KDTree(List<T> coordinates, int l) {
    layer = l;
    dimension = coordinates.get(0).getNumDimensions();
    int index = layer % dimension;
    coordinates.sort(Comparator.comparingDouble(c -> c.getCoordinate(index)));
    int size = coordinates.size();
    int center = size / 2;
    T median = coordinates.get(center);
    node = Optional.of(median);

    boolean notMedian = true;
    while (notMedian && (center != 0)) {
      T potMedian = coordinates.get(center - 1);
      if (potMedian.equals(median)) {
        center = center - 1;
      } else {
        notMedian = false;
      }
    }

    List<T> leftList = coordinates.subList(0, center);
    List<T> rightList = coordinates.subList(center + 1, coordinates.size());

    if (leftList.isEmpty()) {
      left = Optional.empty();
    } else {
      left = Optional.of(new KDTree<>(leftList, index + 1));
    }
    if (rightList.isEmpty()) {
      right = Optional.empty();
    } else {
      right = Optional.of(new KDTree<>(rightList, index + 1));
    }
  }

  /**
   * Inserts the given Coordinate into the KDTree.
   *
   * @param coordinate point to be inserted
   */
  public void insert(T coordinate) {
    if (this.node.isEmpty()) {
      this.node = Optional.of(coordinate);
    } else {
      if (coordinate.getCoordinate(0)
              < this.node.get().getCoordinate(0)) {
        if (this.left.isEmpty()) {
          this.left = Optional.of(new KDTree<>(coordinate, 1));
        } else {
          this.left.get().insert(coordinate, 1);
        }
      } else {
        if (this.right.isEmpty()) {
          this.right = Optional.of(new KDTree<>(coordinate, 1));
        } else {
          this.right.get().insert(coordinate, 1);
        }
      }
    }
  }

  /**
   * Inserts the given Coordinate into the KDTree at the given layer.
   * Used primarily to recur from the above insert method.
   *
   * @param coordinate point to be inserted
   * @param l          layer at which to insert
   */
  public void insert(T coordinate, int l) {
    int index = this.layer % this.dimension;
    if (this.node.isEmpty()) {
      this.node = Optional.of(coordinate);
    } else {
      if (coordinate.getCoordinate(index) < this.node.get().getCoordinate(index)) {
        if (this.left.isEmpty()) {
          this.left = Optional.of(new KDTree<>(coordinate, 1 + l));
        } else {
          this.left.get().insert(coordinate, 1 + l);
        }
      } else {
        if (this.right.isEmpty()) {
          this.right = Optional.of(new KDTree<>(coordinate, 1 + l));
        } else {
          this.right.get().insert(coordinate, 1 + l);
        }
      }
    }
  }

  /**
   * Displays KDTree as a String.
   *
   * @return String
   */
  @Override
  public String toString() {
    String nodeOutput = "none";
    String leftOutput = "none";
    String rightOutput = "none";
    String leftTree = "";
    String rightTree = "";
    if (this.getNode().isEmpty()) {
      return nodeOutput;
    }
    nodeOutput = ((MapNode) this.getNode().get()).getStringID();
    if (this.getLeft().isPresent()) {
      leftOutput = ((MapNode) this.getLeft().get().getNode().get()).getStringID();
      leftTree = this.getLeft().get().toString();
    }
    if (this.getRight().isPresent()) {
      rightOutput = ((MapNode) this.getRight().get().getNode().get()).getStringID();
      rightTree = this.getRight().get().toString();
    }
    String treeOutput = "node: " + nodeOutput + " left: " + leftOutput + " right: " + rightOutput;
    return treeOutput + "\n" + leftTree + "\n" + rightTree;
  }
}
