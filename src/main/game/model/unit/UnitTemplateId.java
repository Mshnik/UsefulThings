package game.model.unit;

import game.model.unit.property.Property;
import game.model.unit.property.PropertyId;

import java.util.Map;

/** @author Mshnik */
public interface UnitTemplateId<U extends Unit> {
  Constructor<U> constructor();

  @FunctionalInterface
  public interface Constructor<U extends Unit> {
    U construct(Map<PropertyId, Property<?>> properties);
  }
}
