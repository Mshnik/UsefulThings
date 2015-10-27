package common.types;

import functional.BiFunction;

import java.util.Objects;

/**
 * A simple SumType implementation. Allows the creation of the Sum Type
 * A + B. Every instance of Either is either (haha.. tomatoes, rocks)
 * an instance of Left, with a value of type A, or an instance of Right,
 * with a value of type B.
 * The types can be the same, but in that case there isn't much use to using Either.
 *
 * @param <A> The first type to sum
 * @param <B> The second type to sum
 * @author Mshnik
 */
public abstract class Either<A, B> {

  private boolean isLeft;

  /**
   * Constructs a new Either
   *
   * @param isLeft - true if this is a Left, false if this is a Right
   */
  Either(boolean isLeft) {
    this.isLeft = isLeft;
  }

  /** Constructs a new Either, selecting from the two arguments with the given criteria
   * @param a - the first argument to select from
   * @param b - the second argument to select from
   * @param selectA - a function that will be applied to a and b. Should return true if a is to be selected, false for b
   * @param <A> - the type of the first argument
   * @param <B> - the type of the second argument
   * @return - either a Left(a) if selectA(a,b) returns true, or a Right(b) if selectA(a,b) returns false.
   */
  public static <A,B> Either<A,B> selectFrom(A a, B b, BiFunction<A,B,Boolean> selectA) {
    if (selectA.apply(a,b)) {
      return new Left<>(a);
    } else {
      return new Right<>(b);
    }
  }

  /** Constructs a new Either, selecting from the two arguments,
   * selects the first if the condition is true, the second if the condition is false
   */
  public static <A,B> Either<A,B> selectFirstIf(A a, B b, boolean selectA) {
    if (selectA) {
      return new Left<>(a);
    } else {
      return new Right<>(b);
    }
  }

  /** Constructs a new Either, selecting the non-null argument. If both or neither are non-null,
   * creates a Left (from the first argument).
   */
  public static <A,B> Either<A,B> selectNonNull(A a, B b) {
    return selectFrom(a,b, (x,y) -> x != null);
  }

  /**
   * Returns true if this is a Left, false if this is a Right
   */
  public boolean isLeft() {
    return isLeft;
  }

  /**
   * Returns the value of this Either as an an instance of A.
   * If the wrapped value is an instance of B, returns null.
   */
  @SuppressWarnings("unchecked")
  public A asLeft() {
    if (isLeft())
      return (A) getVal();
    return null;
  }

  /**
   * Returns the value of this Either as an an instance of B.
   * If the wrapped value is an instance of A, returns null.
   */
  @SuppressWarnings("unchecked")
  public B asRight() {
    if (!isLeft())
      return (B) getVal();
    return null;
  }

  /**
   * Returns the Object stored within this Either.
   * Should have a stricter type bound (A or B) when implemented by subclasses.
   */
  public abstract Object getVal();

  /**
   * Returns the type of the Object stored within this Either
   */
  public abstract Class<?> getType();

  /**
   * Two Eithers are equal iff:
   * <br>- They are both Left or both Right, the only two direct subclasses
   * <br>- The objects they store are equivalent using Objects.equals.
   */
  @Override
  public boolean equals(Object o) {
    try {
      @SuppressWarnings("unchecked")
      Either<A, B> e = (Either<A, B>) o;
      return (!(isLeft ^ e.isLeft)) && Objects.equals(getVal(), e.getVal());
    } catch (ClassCastException e) {
      return false;
    }
  }

  /**
   * Hashes an either based on the value it stores.
   * This maintains the hash invariant (two equal objects have the same hashcode),
   * but is not a perfect hashcode because a Left(a) and Right(a) will have the
   * same hashcode but are not equivalent.
   */
  @Override
  public int hashCode() {
    return getVal().hashCode();
  }

}
