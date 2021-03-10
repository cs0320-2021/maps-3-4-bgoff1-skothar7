package edu.brown.cs.madhavramesh.kdtree;

import edu.brown.cs.madhavramesh.points.HasCoordinates;
import edu.brown.cs.madhavramesh.points.Point;
import edu.brown.cs.madhavramesh.stars.Star;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TreeNodeTest {
  private List<HasCoordinates> stars;
  private List<TreeNode> nodes;

  @Before
  public void setUpIntegers() {
    TreeNode<Point> n1 = new TreeNode(null, null, 3);
    TreeNode<Point> n2 = new TreeNode(null, null, 0);
    TreeNode<Point> n3 = new TreeNode(null, null, 2);
    TreeNode<Point> n4 = new TreeNode(null, null, -1);
    TreeNode<Point> n5 = new TreeNode(null, null, 7);
    TreeNode<Point> n6 = new TreeNode(null, null, 12);
    TreeNode<Point> n7 = new TreeNode(null, null, -32);

    nodes = Arrays.asList(n1, n2, n3, n4, n5, n6, n7);
  }

  @Before
  public void setUpStars() {
    HasCoordinates s1 =
        new Star(0, "Sol", 0, 0, 0);
    HasCoordinates s2 =
        new Star(1, "", 282.43485,0.00449,5.368840);
    HasCoordinates s3 =
        new Star(70667, "Proxima Centauri", 7.26388,1.55643,0.68697);
    HasCoordinates s4 =
        new Star(3, "Rigel Kentaurus B", -0.47175,-0.36132,-1.15037);
    HasCoordinates s5 =
        new Star(3759, "Rigel Kentaurus A", 43.04329,0.00285,-15.24144);
    HasCoordinates s6 =
        new Star(71454, "Barnard's Star", -0.50359,-0.42128,-1.1767);
    HasCoordinates s7 =
        new Star(87666, "96 G. Psc", -2.28262,0.64697,0.29354);

    stars = Arrays.asList(s1, s2, s3, s4, s5, s6, s7);

    TreeNode<Point> n1 = new TreeNode(null, null, s1);
    TreeNode<Point> n2 = new TreeNode(null, null, s2);
    TreeNode<Point> n3 = new TreeNode(null, null, s3);
    TreeNode<Point> n4 = new TreeNode(null, null, s4);
    TreeNode<Point> n5 = new TreeNode(null, null, s5);
    TreeNode<Point> n6 = new TreeNode(null, null, s6);
    TreeNode<Point> n7 = new TreeNode(null, null, s7);

    nodes = Arrays.asList(n1, n2, n3, n4, n5, n6, n7);
  }

  @After
  public void tearDown() {
    nodes = null;
  }

  @Test
  public void testCreateKDTreeIntegers() {
    setUpIntegers();
    TreeNode<Integer> empty = new TreeNode(null, null, null);
    TreeNode<Integer> one = nodes.get(0);
    TreeNode<Integer> two =
        new TreeNode(nodes.get(0), null, -3);
    TreeNode<Integer> three =
        new TreeNode(nodes.get(1), nodes.get(2), 10);
    TreeNode<Integer> four =
        new TreeNode(two, nodes.get(3), 23);
    TreeNode<Integer> big =
        new TreeNode(
            new TreeNode(four, nodes.get(5), 32),
            new TreeNode(nodes.get(4), nodes.get(6), -12),
            0);

    assertNull(empty.getValue());

    assertEquals((int) one.getValue(), 3);
    assertNull(one.getLeft());
    assertNull(one.getRight());

    assertEquals((int) two.getValue(), -3);
    assertEquals(two.getLeft(), nodes.get(0));
    assertNull(two.getLeft().getLeft());
    assertNull(two.getRight());

    assertEquals((int) three.getValue(), 10);
    assertEquals(three.getLeft(), nodes.get(1));
    assertEquals(three.getRight(), nodes.get(2));

    assertEquals((int) four.getValue(), 23);
    assertEquals(four.getLeft(), two);
    assertEquals(four.getLeft().getLeft(), nodes.get(0));
    assertEquals(four.getRight(), nodes.get(3));

    assertEquals((int) big.getValue(), 0);
    assertEquals(big.getLeft().getLeft(), four);
    assertEquals(big.getLeft().getLeft().getLeft(), two);
    assertEquals(big.getLeft().getRight(), nodes.get(5));
    assertEquals(big.getRight().getLeft(), nodes.get(4));
    assertEquals(big.getRight().getRight(), nodes.get(6));

    tearDown();
  }

  @Test
  public void testCreateKDTreeStars() {
    setUpStars();
    TreeNode<Star> one = nodes.get(0);
    TreeNode<Star> two =
        new TreeNode(nodes.get(1), null, stars.get(0));
    TreeNode<Star> three =
        new TreeNode(nodes.get(2), nodes.get(3), stars.get(1));
    TreeNode<Star> four =
        new TreeNode(two, nodes.get(3), stars.get(4));
    TreeNode<Star> big =
        new TreeNode(
            new TreeNode(four, nodes.get(5), stars.get(2)),
            new TreeNode(nodes.get(4), nodes.get(6), stars.get(1)),
            stars.get(0));

    assertEquals(one.getValue(), stars.get(0));
    assertNull(one.getLeft());
    assertNull(one.getRight());

    assertEquals(two.getValue(), stars.get(0));
    assertEquals(two.getLeft(), nodes.get(1));
    assertNull(two.getLeft().getLeft());
    assertNull(two.getRight());

    assertEquals(three.getValue(), stars.get(1));
    assertEquals(three.getLeft(), nodes.get(2));
    assertEquals(three.getRight(), nodes.get(3));

    assertEquals(four.getValue(), stars.get(4));
    assertEquals(four.getLeft(), two);
    assertEquals(four.getLeft().getLeft(), nodes.get(1));
    assertEquals(four.getRight(), nodes.get(3));

    assertEquals(big.getValue(), stars.get(0));
    assertEquals(big.getLeft().getLeft(), four);
    assertEquals(big.getLeft().getLeft().getLeft(), two);
    assertEquals(big.getLeft().getRight(), nodes.get(5));
    assertEquals(big.getRight().getLeft(), nodes.get(4));
    assertEquals(big.getRight().getRight(), nodes.get(6));

    tearDown();
  }

  @Test
  public void testPreorder() {
    setUpIntegers();
    TreeNode<Integer> empty = new TreeNode(null, null, null);
    TreeNode<Integer> one = nodes.get(0);
    TreeNode<Integer> two = new TreeNode(nodes.get(0), nodes.get(2), -32);
    TreeNode<Integer> big =
        new TreeNode(
            one,
            new TreeNode(
                new TreeNode(two, nodes.get(4),13),
                new TreeNode(nodes.get(3), nodes.get(2), -12),
                23),
            0);

    List<Integer> oneList = Arrays.asList(3);
    List<Integer> twoList = Arrays.asList(-32, 3, 2);
    List<Integer> bigList = Arrays.asList(0, 3, 23, 13, -32, 3, 2, 7, -12, -1, 2);

    assertEquals(empty.preorder(), null);
    assertEquals(one.preorder(), oneList);
    assertEquals(two.preorder(), twoList);
    assertEquals(big.preorder(), bigList);
  }
}