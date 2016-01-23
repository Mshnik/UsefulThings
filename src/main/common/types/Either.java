package common.types;

import common.dataStructures.DeArrList;
import functional.impl.Function2;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * A simple SumType implementation. Allows the creation of the Sum Type
 * A + B. Every instance of Either wraps either (haha.. tomatoes, rocks)
 * an instance of A or an instance of B. Either instances are immutable.
 * The types A and B can be the same, but in that case there isn't much use to using Either.
 *
 * @param <A> The first type to sum
 * @param <B> The second type to sum
 * @author Mshnik
 */
public class Either<A, B> {

  private final boolean isLeft;
  private final A a;
  private final B b;

  /**
   * Constructs a new Either
   *
   * @param isLeft - true if this is a Left, false if this is a Right
   */
  private Either(boolean isLeft, A a, B b) {
    this.isLeft = isLeft;
    this.a = a;
    this.b = b;
  }

  /** Creates a Either instance of the given A */
  public static <A, B> Either<A, B> createLeft(A a) {
    return new Either<>(true, a, null);
  }

  /** Creates a Either instance of the given B */
  public static <A, B> Either<A, B> createRight(B b) {
    return new Either<>(false, null, b);
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
   * If the wrapped value is an instance of B, throws a RuntimeException
   */
  public A asLeft() {
    if (isLeft()) return a;
    throw new RuntimeException();
  }

  /**
   * Returns the value of this Either as an an instance of B.
   * If the wrapped value is an instance of A, throws a RuntimeException
   */
  public B asRight() {
    if (!isLeft()) return b;
    throw new RuntimeException();
  }

  /**
   * Maps this Either to a new Left type. Apply the function if this is truly
   * a left, keeps this' value if this is a right.
   */
  public <C> Either<C,B> mapLeft(Function<A,C> f) {
    if(isLeft()) {
      return createLeft(f.apply(asLeft()));
    } else {
      return createRight(asRight());
    }
  }

  /**
   * Maps this Either to a new Right type. Apply the function if this is truly
   * a right, keeps this' value if this is a left.
   */
  public <D> Either<A,D> mapRight(Function<B,D> f) {
    if(isLeft()) {
      return createLeft(asLeft());
    } else {
      return createRight(f.apply(asRight()));
    }
  }

  /**
   * Maps this Either to two new types, applying the function that applies to
   * the true type of this Either.
   */
  public <C,D> Either<C,D> map(Function<A,C> f1, Function<B,D> f2) {
    if(isLeft()) {
      return createLeft(f1.apply(asLeft()));
    } else {
      return createRight(f2.apply(asRight()));
    }
  }

  /**
   * Returns the Object stored within this Either.
   */
  public Object getVal() {
    if(isLeft()) {
      return asLeft();
    } else {
      return asRight();
    }
  }

  /**
   * Returns the type of the Object stored within this Either
   */
  public Class<?> getType() {
    if(isLeft() && asLeft() != null) {
      return asLeft().getClass();
    } else if (! isLeft() && asRight() != null) {
      return asRight().getClass();
    } else {
      return null;
    }
  }

  /**
   * Two Eithers are equal iff:
   * <br>- They are both left halves or both right halves.
   * <br>- The objects they store are equivalent using Objects.equals.
   * Note that the two unused sides of the either can differ, and the
   * Eithers would still be considered equal.
   */
  @Override
  public boolean equals(Object o) {
    if(this == o) return true;
    if(o == null) return false;
    try {
      Either<?, ?> e = (Either<?, ?>) o;
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
