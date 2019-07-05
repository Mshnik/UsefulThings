package game.model.unit.property;

/** @author Mshnik */
public enum FakeProperty implements PropertyId {
  ATTACK(Double.class),
  DEFENSE(Double.class),
  HEALTH(Integer.class);

  private final Class<?> expectedClass;

  private FakeProperty(Class<?> expectedClass) {
    this.expectedClass = expectedClass;
  }

  @Override
  public Class<?> expectedValueClass() {
    return expectedClass;
  }
}
