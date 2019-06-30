package game.model.unit;

import java.util.Map;

/** @author Mshnik */
final class FakeUnit implements Unit {
  private final Map<PropertyId, Property<?>> properties;

  private FakeUnit(Map<PropertyId, Property<?>> properties) {
    this.properties = properties;
  }

  @Override
  public Property<?> getProperty(PropertyId id) {
    return properties.get(id);
  }

  static enum FakeUnitTemplateId implements UnitTemplateId<FakeUnit> {
    UNIT_ONE,
    UNIT_TWO;

    @Override
    public Constructor<FakeUnit> constructor() {
      return FakeUnit::new;
    }
  }
}
