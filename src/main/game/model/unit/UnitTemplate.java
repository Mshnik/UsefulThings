package game.model.unit;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;

/** @author Mshnik */
final class UnitTemplate<U extends Unit> {
  private final UnitTemplateId<U> templateId;
  private final ImmutableMap<PropertyId, Object> properties;

  private UnitTemplate(UnitTemplateId<U> templateId, ImmutableMap<PropertyId, Object> properties) {
    this.templateId = templateId;
    this.properties = properties;
  }

  UnitTemplateId<U> getId() {
    return templateId;
  }

  ImmutableMap<PropertyId, Object> getProperties() {
    return properties;
  }

  static <U extends Unit> Builder<U> newBuilder(UnitTemplateId<U> templateId) {
    return new Builder<>(templateId);
  }

  static final class Builder<U extends Unit> {
    private final UnitTemplateId<U> templateId;
    private final HashMap<PropertyId, Object> properties = new HashMap<>();

    private Builder(UnitTemplateId<U> templateId) {
      this.templateId = templateId;
    }

    Builder<U> addProperty(PropertyId propertyId, Object value) {
      if (!propertyId.expectedValueClass().isInstance(value)) {
        throw new RuntimeException(
            "Expected values of type " + propertyId.expectedValueClass() + ", found " + value);
      }
      if (properties.containsKey(propertyId)) {
        throw new IllegalArgumentException(
            "PropertyId " + propertyId + " already exists in this template.");
      }
      properties.put(propertyId, value);
      return this;
    }

    UnitTemplate<U> build() {
      return new UnitTemplate<>(templateId, ImmutableMap.copyOf(properties));
    }
  }

  @Override
  public String toString() {
    return templateId + ": " + properties;
  }
}
