package game.model.unit;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/** @author Mshnik */
@Singleton
final class PropertyFactory {
  private final Class<? extends PropertyId> propertyTypeClass;

  @Inject
  PropertyFactory(@PropertyType Class<? extends PropertyId> propertyType) {
    this.propertyTypeClass = propertyType;
  }

  <T> Property<T> createProperty(PropertyId id, T initialValue) {
    if (!propertyTypeClass.isInstance(id)) {
      throw new RuntimeException("Invalid id. Expected value of type " + propertyTypeClass);
    }
    if (!id.expectedValueClass().isInstance(initialValue)) {
      throw new RuntimeException(
          "Expected value of type " + id.expectedValueClass() + ", found " + initialValue);
    }
    return new Property<>(id, initialValue);
  }
}
