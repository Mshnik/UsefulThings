package game.model.unit;

import com.google.inject.AbstractModule;

/** @author Mshnik */
final class UnitTemplateRegistryModule extends AbstractModule {
  private final UnitTemplateRegistry unitTemplates;

  UnitTemplateRegistryModule(UnitTemplateRegistry unitTemplates) {
    this.unitTemplates = unitTemplates;
  }

  @Override
  protected void configure() {
    bind(UnitTemplateRegistry.class).annotatedWith(UnitTemplates.class).toInstance(unitTemplates);
  }
}
