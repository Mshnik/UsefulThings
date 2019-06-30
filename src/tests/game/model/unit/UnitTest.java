package game.model.unit;

import com.google.inject.Guice;
import com.google.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/** @author Mshnik */
@RunWith(JUnit4.class)
public final class UnitTest {
  private static final double UNIT_ONE_ATTACK = 50.0;
  private static final double UNIT_ONE_DEFENSE = 10.0;
  private static final int UNIT_ONE_HEALTH = 10;
  private static final double UNIT_TWO_ATTACK = 1.0;
  private static final double UNIT_TWO_DEFENSE = .05;
  private static final int UNIT_TWO_HEALTH = 15;

  @Inject private UnitFactory unitFactory;

  @Before
  public void setup() {
    Guice.createInjector(
            UnitModule.newBuilder()
                .setPropertyClass(FakeProperty.class)
                .addUnitTemplate(
                    UnitTemplate.newBuilder(FakeUnit.FakeUnitTemplateId.UNIT_ONE)
                        .addProperty(FakeProperty.ATTACK, UNIT_ONE_ATTACK)
                        .addProperty(FakeProperty.DEFENSE, UNIT_ONE_DEFENSE)
                        .addProperty(FakeProperty.HEALTH, UNIT_ONE_HEALTH)
                        .build())
                .addUnitTemplate(
                    UnitTemplate.newBuilder(FakeUnit.FakeUnitTemplateId.UNIT_TWO)
                        .addProperty(FakeProperty.ATTACK, UNIT_TWO_ATTACK)
                        .addProperty(FakeProperty.DEFENSE, UNIT_TWO_DEFENSE)
                        .addProperty(FakeProperty.HEALTH, UNIT_TWO_HEALTH)
                        .build())
                .build())
        .injectMembers(this);
  }

  @Test
  public void injectsFactory() {
    assertNotNull(unitFactory);
  }

  @Test
  public void createsUnitWithDefaultValues() {
    FakeUnit fakeUnit = unitFactory.createUnit(FakeUnit.FakeUnitTemplateId.UNIT_ONE);

    assertEquals(fakeUnit.getProperty(FakeProperty.ATTACK).get(), UNIT_ONE_ATTACK);
    assertEquals(fakeUnit.getProperty(FakeProperty.DEFENSE).get(), UNIT_ONE_DEFENSE);
    assertEquals(fakeUnit.getProperty(FakeProperty.HEALTH).get(), UNIT_ONE_HEALTH);
  }
}
