package game.model.unit;

/** @author Mshnik */
public final class Property<T> implements Cloneable {
  private final PropertyId id;
  private T value;
  private T resetToValue;
  private boolean frozen;

  Property(PropertyId id, T initialValue) {
    this.id = id;
    value = initialValue;
    resetToValue = initialValue;
    frozen = false;
  }

  public PropertyId id() {
    return id;
  }

  public T get() {
    return value;
  }

  public void set(T t) {
    if (frozen) {
      throw new FrozenPropertyException(this);
    }
    value = t;
  }

  public void updateReset() {
    if (frozen) {
      throw new FrozenPropertyException(this);
    }
    resetToValue = value;
  }

  public void reset() {
    if (frozen) {
      throw new FrozenPropertyException(this);
    }
    value = resetToValue;
  }

  public void freeze() {
    this.frozen = true;
  }

  public void unfreeze() {
    this.frozen = false;
  }

  public boolean isFrozen() {
    return frozen;
  }
}
