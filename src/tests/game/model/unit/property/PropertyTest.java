package game.model.unit.property;

import com.google.inject.Guice;
import com.google.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static asserts.Asserts.assertThrows;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNotNull;

/** @author Mshnik */
@RunWith(JUnit4.class)
public final class PropertyTest {
  @Inject private PropertyFactory factory;

  @Before
  public void setup() {
    Guice.createInjector(PropertyModule.forClass(FakeProperty.class)).injectMembers(this);
  }

  @Test
  public void injectsPropertyFactory() {
    assertNotNull(factory);
  }

  @Test
  public void createsProperty() {
    Property<Double> property = factory.createProperty(FakeProperty.ATTACK, 10.0);
    assertThat(property).isNotNull();
    assertThat(property.id()).isEqualTo(FakeProperty.ATTACK);
    assertThat(property.get()).isEqualTo(10.0);
  }

  @Test
  public void throwsOnOtherEnum() {
    assertThrows(Exception.class, () -> factory.createProperty(OtherProperty.NONE, null));
  }

  @Test
  public void throwsOnInvalidType() {
    assertThrows(Exception.class, () -> factory.createProperty(FakeProperty.ATTACK, "Foo"));
  }

  private enum OtherProperty implements PropertyId {
    NONE;

    @Override
    public Class<?> expectedValueClass() {
      return null;
    }
  }
}
