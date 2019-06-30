package game.model.unit;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Module;

/** @author Mshnik */
public final class UnitModule extends AbstractModule {
  private final ImmutableList<Module> modules;

  private UnitModule(Module... modules) {
    this.modules = ImmutableList.copyOf(modules);
  }

  @Override
  protected void configure() {
    modules.forEach(this::install);
  }

  static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {
    private Class<? extends PropertyId> propertyClass;
    private final UnitTemplateRegistry.Builder unitTemplateRegistryBuilder =
        UnitTemplateRegistry.newBuilder();

    private Builder() {}

    public Builder setPropertyClass(Class<? extends PropertyId> propertyClass) {
      this.propertyClass = propertyClass;
      return this;
    }

    public <U extends Unit> Builder addUnitTemplate(UnitTemplate<U> template) {
      unitTemplateRegistryBuilder.addUnitTemplate(template.getId(), template);
      return this;
    }

    public UnitModule build() {
      if (propertyClass == null) {
        throw new RuntimeException("Missing required set: PropertyClass");
      }

      return new UnitModule(
          PropertyModule.forClass(propertyClass),
          new UnitTemplateRegistryModule(unitTemplateRegistryBuilder.build()));
    }
  }
}
