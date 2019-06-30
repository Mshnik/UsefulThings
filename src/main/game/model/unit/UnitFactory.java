package game.model.unit;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.Map;
import java.util.stream.Collectors;

/** @author Mshnik */
@Singleton
public final class UnitFactory {
  private final UnitTemplateRegistry unitTemplates;
  private final PropertyFactory propertyFactory;

  @Inject
  UnitFactory(@UnitTemplates UnitTemplateRegistry unitTemplates, PropertyFactory propertyFactory) {
    this.unitTemplates = unitTemplates;
    this.propertyFactory = propertyFactory;
  }

  public <U extends Unit> U createUnit(UnitTemplateId<U> templateId) {
    UnitTemplate<U> template = unitTemplates.get(templateId);
    if (template == null) {
      throw new RuntimeException("Unknown templateId: " + templateId);
    }

    Map<PropertyId, Property<?>> properties =
        template
            .getProperties()
            .entrySet()
            .stream()
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey,
                    e -> propertyFactory.createProperty(e.getKey(), e.getValue())));

    if (template.getFreezeAtStart()) {
      properties.values().forEach(Property::freeze);
    }

    return templateId.constructor().construct(ImmutableMap.copyOf(properties));
  }
}
