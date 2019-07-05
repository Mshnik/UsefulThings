package game.model.unit;

import com.google.common.collect.ImmutableMap;
import game.model.unit.property.Property;
import game.model.unit.property.PropertyId;

import java.util.Map;

/** @author Mshnik */
final class FakeUnit implements Unit {
  private final Map<PropertyId, Property<?>> properties;

  private FakeUnit(Map<PropertyId, Property<?>> properties) {
    this.properties = properties;
  }

  @Override
  public ImmutableMap<PropertyId, Property<?>> getProperties() {
    return ImmutableMap.copyOf(properties);
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
