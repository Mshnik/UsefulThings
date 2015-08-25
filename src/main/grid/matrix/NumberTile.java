package grid.matrix;

import grid.Tile;

import java.util.Arrays;

public class NumberTile<N extends Number> implements Tile {

  private N val;
  private Integer[] loc;

  public NumberTile(N val, Integer... loc) {
    if (val == null)
      throw new IllegalArgumentException("Can't construct number tile with null number");
    this.val = val;
    this.loc = loc;
  }

  public N getVal() {
    return val;
  }

  public void setVal(N num) {
    val = num;
  }

  @Override
  public Integer[] getLocation() {
    return Arrays.copyOf(loc, loc.length);
  }

  public String toString() {
    return val.toString();
  }

  public int hashCode() {
    return Arrays.deepHashCode(loc);
  }

  public boolean equals(Object o) {
    try {
      NumberTile<?> n = (NumberTile<?>) o;
      return Arrays.deepEquals(loc, n.loc);
    } catch (ClassCastException e) {
      return false;
    }
  }
}
