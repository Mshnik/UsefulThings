package game.model.unit;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;

/** @author Mshnik */
final class UnitTemplateRegistry {
  private final ImmutableMap<UnitTemplateId<?>, UnitTemplate<?>> unitTemplates;

  private UnitTemplateRegistry(ImmutableMap<UnitTemplateId<?>, UnitTemplate<?>> unitTemplates) {
    this.unitTemplates = unitTemplates;
  }

  @SuppressWarnings("unchecked")
  <U extends Unit> UnitTemplate<U> get(UnitTemplateId<U> unitTemplateId) {
    return (UnitTemplate<U>) unitTemplates.get(unitTemplateId);
  }

  static Builder newBuilder() {
    return new Builder();
  }

  static final class Builder {
    private final HashMap<UnitTemplateId<?>, UnitTemplate<?>> unitTemplates = new HashMap<>();

    private Builder() {}

    <U extends Unit> Builder addUnitTemplate(
        UnitTemplateId<U> unitTemplateId, UnitTemplate<U> unitTemplate) {
      if (unitTemplates.containsKey(unitTemplate.getId())) {
        throw new IllegalArgumentException(
            "TemplateId " + unitTemplate.getId() + " already present in " + unitTemplates);
      }

      unitTemplates.put(unitTemplateId, unitTemplate);
      return this;
    }

    UnitTemplateRegistry build() {
      return new UnitTemplateRegistry(ImmutableMap.copyOf(unitTemplates));
    }
  }
}
