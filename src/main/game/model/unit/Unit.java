package game.model.unit;

import com.google.common.collect.ImmutableMap;
import game.model.unit.property.Property;
import game.model.unit.property.PropertyId;

/** @author Mshnik */
public interface Unit {
  ImmutableMap<PropertyId, Property<?>> getProperties();

  default Property<?> getProperty(PropertyId id) {
    return getProperties().get(id);
  }
}
