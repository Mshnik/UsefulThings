package common.types;

import common.dataStructures.DeArrList;
import functional.impl.Function2;

import java.util.List;
import java.util.Objects;

/**
 * A simple SumType implementation. Allows the creation of the Sum Type
 * A + B. Every instance of Either is either (haha.. tomatoes, rocks)
 * an instance of Left, with a value of type A, or an instance of Right,
 * with a value of type B.
 * <br><br>
 * The types and constructors for Left and Right are not exposed.
 * Instead, to construct new instance of Left and Right, the createLeft and createRight
 * methods of class Either should be used.
 * <br><br>
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

  /** Creates a Either instance of the given A */
  public static <A, B> Either<A, B> createLeft(A a) {
    return new Left<>(a);
  }

  /** Creates a Either instance of the given B */
  public static <A, B> Either<A, B> createRight(B b) {
    return new Right<>(b);
  }

  /** Constructs a new Either, selecting from the two arguments with the given criteria
   * @param a - the first argument to select from
   * @param b - the second argument to select from
   * @param selectA - a function that will be applied to a and b. Should return true if a is to be selected, false for b
   * @param <A> - the type of the first argument
   * @param <B> - the type of the second argument
   * @return - either a Left(a) if selectA(a,b) returns true, or a Right(b) if selectA(a,b) returns false.
   */
  public static <A,B> Either<A,B> selectFrom(A a, B b, Function2<A,B,Boolean> selectA) {
    if (selectA.apply(a,b)) {
      return createLeft(a);
    } else {
      return createRight(b);
    }
  }

  /** Constructs a new Either, selecting from the two arguments,
   * selects the first if the condition is true, the second if the condition is false
   */
  public static <A,B> Either<A,B> selectFirstIf(A a, B b, boolean selectA) {
    if (selectA) {
      return createLeft(a);
    } else {
      return createRight(b);
    }
  }

  /** Constructs a new Either, selecting the non-null argument. If both or neither are non-null,
   * creates a Left (from the first argument).
   */
  public static <A,B> Either<A,B> selectNonNull(A a, B b) {
    return selectFrom(a,b, (x,y) -> x != null);
  }

  /** Filters the given collection into a list of A, removing B instances */
  public static <A,B> List<A> filterA(Iterable<Either<A,B>> col) {
    DeArrList<A> lst = new DeArrList<>();
    for(Either<A,B> e : col) {
      if(e.isLeft()) {
        lst.add(e.asLeft());
      }
    }
    return lst;
  }

  /** Filters the given collection into a list of B, removing A instances */
  public static <A,B> List<B> filterB(Iterable<Either<A,B>> col) {
    DeArrList<B> lst = new DeArrList<>();
    for(Either<A,B> e : col) {
      if(! e.isLeft()) {
        lst.add(e.asRight());
      }
    }
    return lst;
  }

  /** Filters the given collection into a tuple of lists of the two types */
  public static <A,B> Tuple2<List<A>, List<B>> filterAndSplit(Iterable<Either<A,B>> col) {
    Tuple2<List<A>,List<B>> t = Tuple.of(new DeArrList<>(), new DeArrList<>());
    for(Either<A,B> e : col) {
      if(e.isLeft()) {
        t._1.add(e.asLeft());
      } else {
        t._2.add(e.asRight());
      }
    }
    return t;
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
    throw new RuntimeException();
  }

  /**
   * Returns the value of this Either as an an instance of B.
   * If the wrapped value is an instance of A, returns null.
   */
  @SuppressWarnings("unchecked")
  public B asRight() {
    if (!isLeft())
      return (B) getVal();
    throw new RuntimeException();
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
    if(this == o) return true;
    if(o == null) return false;
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
    return Objects.hashCode(getVal());
  }

}
