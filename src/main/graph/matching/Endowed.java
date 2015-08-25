package graph.matching;

/**
 * An instance of Endowed is an agent (or anything else) that
 * possesses some initial item before matchings have been created.
 * The only requirement of an endowed object is that it knows the identity
 * of its endowment, of type X. Used in matching markets with initial endowments.
 *
 * @param <X> - the type of the object that is endowed.
 * @author Mshnik
 */
public interface Endowed<X> {

  /**
   * Returns the initial endowment of this Endowed. Should never change
   * between calls.
   */
  public X getInitialEndowment();
}
