package grid._2D;

import java.util.Iterator;
import java.util.LinkedList;

public class Path<T extends Tile2D> implements Iterable<T> {

  private final LinkedList<T> path;

  public Path() {
    path = new LinkedList<T>();
  }

  public void add(T t) throws DisjointPathException {
    Integer[] l1 = path.getLast().getLocation();
    Integer[] l2 = t.getLocation();
    Integer[] l3 = new Integer[l1.length];
    for (int i = 0; i < l1.length; i++) {
      l3[i] = l2[i] - l1[i];
    }
    Direction2D d = new Direction2D(l3);
    if (d.isCardinal())
      path.add(t);
    else
      throw new DisjointPathException(path.getLast() + " to " + t + " is disjoint");
  }

  public void clearToEnd(T t) {
    int i = path.indexOf(t);
    if (i == -1)
      throw new IllegalArgumentException(t + " not in " + this);
    while (i < path.size()) {
      path.remove(i);
    }
  }

  public LinkedList<T> getPath() {
    return new LinkedList<T>(path);
  }

  public Iterator<T> iterator() {
    return path.iterator();
  }

  static class DisjointPathException extends RuntimeException {
    /***/
    private static final long serialVersionUID = 1L;

    public DisjointPathException(String msg) {
      super(msg);
    }
  }

}
