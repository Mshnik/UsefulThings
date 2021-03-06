package grid;

import java.util.*;

import common.Util;
import common.dataStructures.ConsList;
import functional.impl.Function2;

/**
 * A collection for arbitrarily deep nested arrays
 * Allows for the creation of variable-depth (determined at runtime) matrices.
 * Elements in a grid must extend tile, which allows an element to know its
 * location (treated like a hashcode). A grid is treated as an array,
 * meaning that get and set (and similar) operations may throw an
 * ArrayIndexOutOfBoundsException if the listed location is outside the
 * bounds of the grid
 *
 * @param <T> - The type of elements to store.
 * @author Mshnik
 */
public class Grid<T extends Tile> implements Collection<T>, Cloneable {

  protected final Object[] vals;
  protected final int[] bounds;

  protected int modCount;

  /**
   * The dimensionality of this Grid - how many coordinates a location has.
   * All get and set operations must be Integer arrays of this length
   */
  public final int dimension;

  /**
   * The number of elements currently stored in this Grid
   */
  private int size;

  /**
   * Initializes an empty grid
   *
   * @param bounds - the bounds of this grid. This also determines the dimensionality of the grid
   */
  public Grid(Integer... bounds) {
    this(Util.unboxArr(bounds));
  }

  /**
   * Initializes an empty grid
   *
   * @param bounds - the bounds of this grid. This also determines the dimensionality of the grid
   */
  public Grid(int[] bounds) {
    dimension = bounds.length;
    this.bounds = Arrays.copyOf(bounds, bounds.length);
    vals = recCreateArrays(0);
    size = 0;
    modCount = 0;
  }

  /**
   * Returns a new grid that is the merged version of the two input grids (which must be of the same dimension)
   *
   * @param grid1    - the first grid to merge
   * @param grid2    - the second grid to merge
   * @param combinor - a functional interface describing how to combine two tiles into one.
   *                 The first argument to combine will be a tile from grid1, and the second will be a tile from grid2.
   * @return - A grid with array size equal to the max of the two grids on each dimension, and each element combined
   */
  public static <T extends Tile> Grid<T> merge(Grid<? extends T> grid1, Grid<? extends T> grid2, Function2<T,T,T> combinor)
      throws IllegalDimensionException {
    if (grid1.dimension != grid2.dimension)
      throw new IllegalDimensionException(grid2.dimension, grid1);

    Integer[] bounds = new Integer[grid1.dimension];
    for (int i = 0; i < grid1.dimension; i++) {
      bounds[i] = Math.max(grid1.bounds[i], grid2.bounds[i]);
    }

    Grid<T> grid = new Grid<T>(bounds);
    for (Integer[] loc : grid.buildCoordinates()) {
      grid.add(combinor.apply(grid1.getSafe(loc), grid2.getSafe(loc)));
    }

    return grid;
  }

  /**
   * Returns the union of of this with other. If this and other both have a tile
   * at a given location, uses this' tile. Only places null at a location of both tiles
   * at that location are null
   *
   * @param other - the grid to merge with
   * @return - the merged grid
   */
  public Grid<T> union(Grid<? extends T> other) throws IllegalDimensionException {
    return merge(this, other, (t, t2) -> t != null ? t : t2);
  }

  /**
   * Creates arrays of the given depth, up to the length of bounds
   * Used as a helper during construction, shouldn't be used otherwise.
   */
  private Object[] recCreateArrays(int depth) {
    if (vals != null)
      throw new RuntimeException("Can't call recCreateArrays not at construction time");
    if (depth == bounds.length) return null;

    Object[] vals = new Object[bounds[depth]];
    for (int i = 0; i < bounds[depth]; i++) {
      vals[i] = recCreateArrays(depth + 1);
    }
    return vals;
  }

  /**
   * Returns the bounds of this grid
   */
  public int[] getBounds() {
    return Arrays.copyOf(bounds, bounds.length);
  }

  /**
   * Returns the size (number of tiles) of the grid
   */
  public int size() {
    return size;
  }

  /**
   * Returns true iff this grid is empty (size == 0)
   */
  public boolean isEmpty() {
    return size() == 0;
  }

  /**
   * Returns the tile at the given location
   *
   * @param loc - the location as a set of coordinates to find a tile
   * @return the tile at the given location, or null if none.
   * @throws ArrayIndexOutOfBoundsException if any of the coordinates are OOB
   * @throws IllegalDimensionException if number of coordinates provided is incorrect
   */
  public T get(Integer... loc) {
    if (loc.length != dimension)
      throw new IllegalDimensionException(loc.length, this);
    return recGetTile(vals, 0, loc);
  }

  /**
   * Returns the tile at the given location. Returns null if the coordinates are OOB or illegal dimension
   *
   * @param loc - the location as a set of coordinates to find a tile
   * @return the tile at the given location, or null if none or OOB/IllegalDimension
   */
  public T getSafe(Integer... loc) {
    if (loc.length != dimension)
      return null;
    try {
      return recGetTile(vals, 0, loc);
    } catch (ArrayIndexOutOfBoundsException e) {
      return null;
    }
  }

  /**
   * Returns all tiles in this grid, in order of iteration
   */
  public List<T> getAll() {
    LinkedList<T> l = new LinkedList<T>();
    for (T t : this) {
      l.add(t);
    }
    return l;
  }

  /**
   * Returns the tile located at the given delta (as a vector) from the base tile
   *
   * @param base  - the tile to start the vector from (the origin point)
   * @param delta - the deltas to apply
   * @return - the tile at the given base + delta position
   * @throws ArrayIndexOutOfBoundsException if any of the coordinates are OOB
   * @throws IllegalDimensionException if number of coordinates provided is incorrect
   */
  public T getFrom(Tile base, Integer... delta) {
    if (delta.length != dimension)
      throw new IllegalDimensionException(delta.length, this);
    Integer[] d = Arrays.copyOf(base.getLocation(), delta.length);
    for (int i = 0; i < d.length; i++) {
      d[i] += delta[i];
    }
    return get(d);
  }

  @Override
  public Object[] toArray() {
    Object[] arr = new Object[size];
    int i = 0;
    for (Tile t : this) {
      arr[i] = t;
      i++;
    }
    return arr;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <X> X[] toArray(X[] a) {
    X[] a2 = null;
    if (a.length >= size)
      a2 = a;
    else
      a2 = Arrays.copyOf(a, size);
    int i = 0;
    for (T t : this) {
      a2[i] = (X) t;
      i++;
    }
    return a2;
  }

  /**
   * Returns a map representation of this grid: location of a tile -&gt; tile
   */
  public Map<List<Integer>, T> toMap() {
    HashMap<List<Integer>, T> m = new HashMap<>();
    for (T t : this) {
      m.put(Collections.unmodifiableList(Arrays.asList(t.getLocation())), t);
    }
    return m;
  }

  /**
   * Returns true iff there is a tile at the given location
   *
   * @param loc - the location as a set of coordinates to find a tile
   * @return true iff there is a tile at the given location.
   * @throws ArrayIndexOutOfBoundsException if any of the coordinates are OOB
   * @throws IllegalDimensionException if number of coordinates provided is incorrect
   */
  public boolean containsAt(Integer... loc) throws IllegalDimensionException {
    if (loc.length != dimension)
      throw new IllegalDimensionException(loc.length, this);
    return get(loc) != null;
  }

  /**
   * Returns true iff this grid contains o at its location.
   * Note that if o was added to this grid, but then had its location changed, this will return false.
   *
   * @param o - the object (must be instance of Tile) to look for
   * @return true iff o is in this grid.
   */
  public boolean contains(Object o) {
    if (!(o instanceof Tile)) return false;
    return (Tile) o == get(((Tile) o).getLocation());
  }

  /**
   * Returns true iff this grid contains all of the objects in c
   */
  @Override
  public boolean containsAll(Collection<?> c) {
    for (Object o : c) {
      if (!contains(o)) return false;
    }
    return true;
  }

  /**
   * Adds the given tile to this grid, at its location. This will overwrite any previous tile at
   * that location
   *
   * @param t - the tile to add
   * @return true if this grid changed as a result of this addition
   */
  public boolean add(T t) {
    if (get(t.getLocation()) == t) return false;

    modCount++;
    if (!containsAt(t.getLocation()))
      size++;
    recSetTile(vals, 0, t.getLocation(), t);
    return true;
  }

  /**
   * Adds each of the tiles in the collection to this grid. @see Grid.add(t)
   *
   * @return true - if the grid changed as a result of these additions
   */
  public boolean addAll(Collection<? extends T> c) {
    boolean b = false;
    for (T t : c) {
      b = add(t) || b;
    }
    return b;
  }

  /**
   * Removes the given tile from this grid, if present
   *
   * @param o - the Object (must be instance of tile) to attempt to remove
   * @return true iff o was removed.
   */
  public boolean remove(Object o) {
    if (!(o instanceof Tile)) return false;
    Tile t = (Tile) o;
    T t2 = get(t.getLocation());
    if (t == t2) {
      recSetTile(vals, 0, t.getLocation(), null);
      size--;
      modCount++;
      return true;
    }
    return false;
  }

  public boolean removeAll(Collection<?> c) {
    boolean b = true;
    for (Object t : c) {
      b = remove(t) && b;
    }
    return b;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    boolean b = false;
    for (Tile t : toArray(new Tile[size()])) {
      if (!c.contains(t)) {
        b = remove(t) || b;
      }
    }
    return b;
  }

  @Override
  public void clear() {
    for (Tile t : toArray(new Tile[size()])) {
      remove(t);
    }
  }

  /**
   * Clears a tile from the given position
   *
   * @return the tile that was removed, or null if no tile was at the listed coordinates.
   * @throws ArrayIndexOutOfBoundsException if any of the coordinates are OOB
   * @throws IllegalDimensionException if number of coordinates provided is incorrect
   **/
  public T remove(Integer... pos) {
    if (pos.length != dimension)
      throw new IllegalDimensionException(pos.length, this);
    T t = get(pos);
    if (t != null) {
      remove(t);
    }
    return t;
  }

  /**
   * Recursive helper for getting a tile from the grid
   *
   * @param arr   - the current array to search
   * @param depth - the current depth in the search
   * @param loc   - the full set of coordinates to search on
   * @return - the tile at the given location, or null iff none
   */
  @SuppressWarnings("unchecked")
  private T recGetTile(Object[] arr, int depth, Integer[] loc) {
    int index = loc[depth];
    if (depth == dimension - 1) {
      return (T) arr[index];
    } else {
      return recGetTile((Object[]) arr[index], depth + 1, loc);
    }
  }

  /**
   * Recursive helper for setting a tile on the grid
   *
   * @param arr   - the current array to search
   * @param depth - the current depth in the search
   * @param loc   - the full set of coordinates to search on
   * @param t     - the tile to set. Can be null to clear the given position
   */
  private void recSetTile(Object[] arr, int depth, Integer[] loc, T t) {
    int index = loc[depth];
    if (depth == dimension - 1) {
      arr[index] = t;
    } else {
      recSetTile((Object[]) arr[index], depth + 1, loc, t);
    }
  }

  /**
   * Returns all coordinate groups that are in bounds for this grid
   */
  public ArrayList<Integer[]> buildCoordinates() {
    return recBuild(new ConsList<>(), new ArrayList<>(), 0, bounds);
  }

  /**
   * Recursively builds int arrays within the given ranges.
   *
   * @param template - represented in reverse to get O(1) prepend. Reverse when used.
   * @param built    - the list of coordinates built.
   * @param depth    - the depth to recurse to
   * @param bounds   - the max val at each depth[i].
   */
  private static ArrayList<Integer[]> recBuild(ConsList<Integer> template,
                                               ArrayList<Integer[]> built, int depth, int[] bounds) {
    if (depth == bounds.length) {
      built.add(template.reverse().toArray(new Integer[template.size]));
      return built;
    }
    for (int i = 0; i < bounds[depth]; i++) {
      recBuild(template.cons(i), built, depth + 1, bounds);
    }
    return built;
  }

  /**
   * Returns a deep string of the array represented by this grid as
   * its toString. Uses Arrays.deepToString to fully express the grid
   */
  public String toString() {
    return Arrays.deepToString(vals);
  }

  /**
   * Two grids are equal if they represent the same elements
   *
   * See: {@link java.util.Arrays#deepEquals(Object[] a1, Object[] a2)}
   */
  public boolean equals(Object o) {
    if (!(o instanceof Grid<?>)) return false;
    Grid<?> g = (Grid<?>) o;
    return Arrays.deepEquals(vals, g.vals);
  }

  /**
   * Hashes a grid based of of its elements
   *
   * See: {@link java.util.Arrays#deepHashCode(Object[])}
   */
  public int hashCode() {
    return Arrays.deepHashCode(vals);
  }

  /**
   * Returns an Iterator over the tiles in this grid
   * Iterates in order of most inner dimensions first
   */
  @Override
  public Iterator<T> iterator() {
    return new GridIterator();
  }

  /**
   * Returns a copy of this Grid - contains the same elements.
   */
  public Grid<T> clone() {
    return clone(Util.boxArr(bounds));
  }

  /**
   * Returns a copy of this Grid with the given bounds.
   * Must be the same dimension of bounds as this, but can be resized.
   * Elements that are now out of bounds will not be added.
   * Elements are shallowly copied - no copies of the elements are made.
   *
   * @param bounds - the bounds of the new grid
   * @return a copy of this grid according to the above rules.
   */
  public Grid<T> clone(Integer... bounds) {
    if (bounds.length != this.bounds.length)
      throw new IllegalDimensionException(bounds.length, this);

    Grid<T> g = new Grid<>(bounds);
    for (T t : this) {
      try {
        g.add(t);
      } catch (ArrayIndexOutOfBoundsException e) {
      }
    }
    return g;
  }

  /**
   * See {@link grid.Grid#clone(Integer... bounds)}
   */
  public Grid<T> resize(Integer... bounds) {
    return clone(bounds);
  }

  @SuppressWarnings("serial")
  static class IllegalDimensionException extends RuntimeException {
    public IllegalDimensionException(int d, Grid<?> g) {
      super("Illegal Dimension: " + d + " for Grid with dimension " + g.dimension);
    }
  }

  /**
   * A class for iterating over a grid.
   * Doesn't through concurrent modification exceptions, but has unspecified
   * behavior if there is concurrent modification
   *
   * @author MPatashnik
   */
  class GridIterator implements Iterator<T> {

    /**
     * The position in the grid this iterator is currently looking at
     */
    private Integer[] pos;

    /**
     * True if there is another entry in the grid
     */
    private boolean next;

    /**
     * The element T most recently returned by next().
     * null until next is called
     */
    private T elm;

    /** True if elm has been removed, false otherwise */
    private boolean elmRemoved;

    /** The expected mod count, for checking for concurrent modification */
    private int expectedModCount;

    /**
     * Constructs a GridIterator
     */
    public GridIterator() {
      pos = new Integer[dimension];
      for (int i = 0; i < dimension; i++) {
        pos[i] = 0;
      }
      next = dimension > 0 && size > 0;
      elm = null;
      elmRemoved = false;
      expectedModCount = modCount;
    }

    @Override
    public boolean hasNext() throws ConcurrentModificationException {
      if (modCount != expectedModCount) {
        throw new ConcurrentModificationException();
      }
      return next;
    }

    @Override
    public T next() {
      while (!containsAt(pos)) {
        if (!recInc(dimension - 1))
          throw new NoSuchElementException();
      }
      elm = get(pos);
      next = recInc(dimension - 1);
      while (next && !containsAt(pos)) {
        next = recInc(dimension - 1);
      }
      elmRemoved = false;
      return elm;
    }

    /**
     * Helper method for moving the position array to the next position
     *
     * @param depth - the depth of position (index in arr) to alter
     * @return - true if there is another position, false if the end of the grid has been reached
     */
    private boolean recInc(int depth) {
      if (depth == -1)
        return false;
      pos[depth]++;
      if (pos[depth] == bounds[depth]) {
        pos[depth] = 0;
        return recInc(depth - 1);
      }
      return true;
    }

    /**
     * Removes the tile this iterator is currently on and moves to the next tile
     */
    @Override
    public void remove() {
      if (! elmRemoved) {
        Grid.this.remove(elm);
        expectedModCount++;
        elmRemoved = true;
      }
    }

  }
}
