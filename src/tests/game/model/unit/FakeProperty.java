package game.model.unit;

/** @author Mshnik */
enum FakeProperty implements PropertyId {
  ATTACK(Double.class),
  DEFENSE(Double.class),
  HEALTH(Integer.class);

  private final Class<?> expectedClass;

  FakeProperty(Class<?> expectedClass) {
    this.expectedClass = expectedClass;
  }

  @Override
  public Class<?> expectedValueClass() {
    return expectedClass;
  }
}
