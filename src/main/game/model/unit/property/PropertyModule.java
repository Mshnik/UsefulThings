package game.model.unit.property;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

/** @author Mshnik */
public final class PropertyModule extends AbstractModule {
  private final Class<? extends PropertyId> propertyClass;

  private PropertyModule(Class<? extends PropertyId> propertyClass) {
    this.propertyClass = propertyClass;
  }

  public static PropertyModule forClass(Class<? extends PropertyId> propertyClass) {
    return new PropertyModule(propertyClass);
  }

  @Override
  protected void configure() {
    bind(new TypeLiteral<Class<? extends PropertyId>>() {})
        .annotatedWith(PropertyType.class)
        .toInstance(propertyClass);
  }
}
