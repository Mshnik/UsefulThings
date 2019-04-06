package grid;

import java.util.Arrays;

public interface Tile {

  /**
   * Returns the location of this Tile in the grid it is stored in.
   * Shouldn't change once instantiated. Treat it like a hashcode
   */
  public Integer[] getLocation();

  class BaseTile<T> implements Tile {

    private final Integer[] location;

    public BaseTile(Integer... location) {
      this.location = Arrays.copyOf(location, location.length);
    }

    @Override
    public Integer[] getLocation() {
      return Arrays.copyOf(location, location.length);
    }
  }
}
