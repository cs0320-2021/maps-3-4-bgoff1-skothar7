package edu.brown.cs.madhavramesh.kdtree;

import edu.brown.cs.madhavramesh.Constants;

import java.util.ArrayList;
import java.util.List;

/** Node in a binary tree.
 * @param <T> Type of node
 */
public class TreeNode<T> {
  private TreeNode<T> left;
  private TreeNode<T> right;
  private T value;

  /** Constructs a node.
   * @param left left subtree
   * @param right right subtree
   * @param value value of node
   */
  public TreeNode(TreeNode<T> left, TreeNode<T> right, T value) {
    this.left = left;
    this.right = right;
    this.value = value;
  }

  public List<T> preorder() {
    if (value == null) {
      return null;
    } else {
      return preorderHelper(this, new ArrayList<>());
    }
  }

  private List<T> preorderHelper(TreeNode<T> curNode, List<T> traversal) {
    if (curNode == null) {
      return traversal;
    }
    traversal.add(curNode.getValue());
    traversal = preorderHelper(curNode.getLeft(), traversal);
    traversal = preorderHelper(curNode.getRight(), traversal);
    return traversal;
  }

  public TreeNode getLeft() {
    return left;
  }

  public TreeNode getRight() {
    return right;
  }

  public T getValue() {
    return value;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof TreeNode)) {
      return false;
    }
    if (obj == this) {
      return true;
    }

    TreeNode<T> n = (TreeNode) obj;
    boolean valsEqual = n.getValue().equals(value);
    boolean leftEqual =
        ((left == null) && (n.getLeft() == null)) || left.equals(n.getLeft());
    boolean rightEqual =
        ((right == null) && (n.getRight() == null)) || right.equals(n.getRight());
    return valsEqual && leftEqual && rightEqual;
  }

  //https://medium.com/codelog/overriding-hashcode-method-effective-java-notes-723c1fedf51c
  //https://stackoverflow.com/questions/24433184/
  //overriding-hashcode-with-a-class-with-two-generics-fields?rq=1
  @Override
  public int hashCode() {
    final int hash = 17;
    int result = Constants.THIRTY_ONE;

    result = result * hash + value.hashCode();
    result = result * hash + (left == null ? 0 : left.hashCode());
    result = result * hash + (right == null ? 0 : right.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return preorder().toString();
  }
}
