package game.model.unit;

import java.util.Map;

/** @author Mshnik */
public interface UnitTemplateId<U extends Unit> {
  Constructor<U> constructor();

  @FunctionalInterface
  public interface Constructor<U extends Unit> {
    U construct(Map<PropertyId, Property<?>> properties);
  }
}
