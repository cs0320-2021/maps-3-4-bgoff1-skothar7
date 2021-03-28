package edu.brown.cs.madhavramesh.kdtree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class BoundedPriorityQueue<T> extends PriorityQueue<T> {
  private PriorityQueue<T> curQ;
  private Comparator<? super T> c;
  private int maxSize;

  public BoundedPriorityQueue(Comparator<? super T> c, int maxSize)
      throws IllegalArgumentException {
    if (maxSize < 1) {
      throw new IllegalArgumentException("Size of Bounded Priority Queue cannot be less than 1");
    }
    this.curQ = new PriorityQueue<>(c);
    this.c = c;
    this.maxSize = maxSize;
  }

  public BoundedPriorityQueue(BoundedPriorityQueue<T> otherQ) {
    this.curQ = otherQ.getQueue();
    this.c = otherQ.getComparator();
    this.maxSize = otherQ.getMaxSize();
  }

  public boolean add(T elem)
      throws IllegalArgumentException {
    if (elem == null) {
      throw new IllegalArgumentException("Cannot add null element to Bounded Priority Queue");
    }
    if (curQ.size() >= maxSize) {
      T highestPriority = curQ.peek();
      if (c.compare(elem, highestPriority) < 0) {
        return false;
      } else {
        curQ.poll();
      }
    }
    curQ.add(elem);
    return true;
  }

  public List<T> toList() {
    PriorityQueue<T> curQCopy = new PriorityQueue<>(curQ);
    List<T> curQList = new ArrayList<>();
    while (!curQCopy.isEmpty()) {
      curQList.add(curQCopy.poll());
    }
    return curQList;
  }

  public PriorityQueue<T> getQueue() {
    return new PriorityQueue<T>(curQ);
  }

  public Comparator<? super T> getComparator() {
    return c;
  }

  public int getMaxSize() {
    return maxSize;
  }

  @Override
  public String toString() {
    String output = "[";
    for (T elem : toList()) {
      output += (elem.toString() + " ");
    }
    output += "]";
    return output;
  }
}
