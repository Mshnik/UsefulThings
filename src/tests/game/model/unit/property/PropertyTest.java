package game.model.unit.property;

import com.google.inject.Guice;
import com.google.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

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
    assertNotNull(property);
    assertEquals(property.id(), FakeProperty.ATTACK);
    assertEquals(property.get(), Double.valueOf(10));
  }

  @Test
  public void throwsOnOtherEnum() {
    try {
      factory.createProperty(OtherProperty.NONE, null);
      fail("Expected failure.");
    } catch (Exception e) {
    }
  }

  @Test
  public void throwsOnInvalidType() {
    try {
      factory.createProperty(FakeProperty.ATTACK, "Foo");
      fail("Expected failure.");
    } catch (Exception e) {
    }
  }

  private enum OtherProperty implements PropertyId {
    NONE;

    @Override
    public Class<?> expectedValueClass() {
      return null;
    }
  }
}
