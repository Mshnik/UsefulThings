package game.decision;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/** @author Mshnik */
@RunWith(JUnit4.class)
public final class DecisionTest {

  @Test
  public void throwsOnNoChoices() {
    try {
      Decision.newBuilder().build();
      fail("Expected failure");
    } catch (Exception e) {
    }
  }

  @Test
  public void buildsDecision() {
    Decision decision =
        Decision.newBuilder()
            .addChoice(FakeChoice.CHOICE_1)
            .addChoice(FakeChoice.CHOICE_2)
            .addChoice(FakeChoice.CHOICE_3)
            .build();

    assertEquals(
        decision.getChoices(),
        ImmutableList.of(FakeChoice.CHOICE_1, FakeChoice.CHOICE_2, FakeChoice.CHOICE_3));
  }

  @Test
  public void selectReturnsFirstElementByDefault() {
    Decision decision =
        Decision.newBuilder().addChoice(FakeChoice.CHOICE_1).addChoice(FakeChoice.CHOICE_2).build();
    assertEquals(decision.select(), FakeChoice.CHOICE_1);
  }

  @Test
  public void nextReturnsTrueAndWraps() {
    Decision decision =
        Decision.newBuilder().addChoice(FakeChoice.CHOICE_1).addChoice(FakeChoice.CHOICE_2).build();
    assertEquals(decision.select(), FakeChoice.CHOICE_1);
    assertTrue(decision.next());
    assertTrue(decision.next());
    assertEquals(decision.select(), FakeChoice.CHOICE_1);
  }

  @Test
  public void previousReturnsTrueAndWraps() {
    Decision decision =
        Decision.newBuilder().addChoice(FakeChoice.CHOICE_1).addChoice(FakeChoice.CHOICE_2).build();
    assertEquals(decision.select(), FakeChoice.CHOICE_1);
    assertTrue(decision.prev());
    assertTrue(decision.prev());
    assertEquals(decision.select(), FakeChoice.CHOICE_1);
  }

  @Test
  public void nextReturnsFalseAndDoesNotWrap() {
    Decision decision =
        Decision.newBuilder()
            .addChoice(FakeChoice.CHOICE_1)
            .addChoice(FakeChoice.CHOICE_2)
            .withAllowWrap(false)
            .build();
    assertEquals(decision.select(), FakeChoice.CHOICE_1);
    assertTrue(decision.next());
    assertFalse(decision.next());
    assertEquals(decision.select(), FakeChoice.CHOICE_2);
  }

  @Test
  public void prevReturnsFalseAndDoesNotWrap() {
    Decision decision =
        Decision.newBuilder()
            .addChoice(FakeChoice.CHOICE_1)
            .addChoice(FakeChoice.CHOICE_2)
            .withAllowWrap(false)
            .build();
    assertEquals(decision.select(), FakeChoice.CHOICE_1);
    assertFalse(decision.prev());
    assertEquals(decision.select(), FakeChoice.CHOICE_1);
  }

  @Test
  public void initialIndexPicksFirstSelect() {
    Decision decision =
        Decision.newBuilder()
            .addChoice(FakeChoice.CHOICE_1)
            .addChoice(FakeChoice.CHOICE_2)
            .addChoice(FakeChoice.CHOICE_3)
            .withSelectedIndex(1)
            .build();
    assertEquals(decision.select(), FakeChoice.CHOICE_2);
  }

  @Test
  public void consumerIsCalledOnSelect() {
    List<Choice> choiceList = new ArrayList<>();

    Decision decision =
        Decision.newBuilder()
            .addChoice(FakeChoice.CHOICE_1)
            .addChoice(FakeChoice.CHOICE_2)
            .addChoice(FakeChoice.CHOICE_3)
            .withChoiceConsumer(choiceList::add)
            .build();
    decision.select();
    assertEquals(choiceList, ImmutableList.of(FakeChoice.CHOICE_1));
  }

  @Test
  public void throwOnSelectBehaviorAllowsSelectButThrows() {
    Decision decision =
        Decision.newBuilder()
            .addChoice(FakeChoice.CHOICE_NOT_SELECTABLE)
            .withBehavior(SelectableBehavior.THROW_ON_SELECT)
            .build();

    try {
      decision.select();
      fail("Expected failure");
    } catch (UnselectableException e) {
    }
  }

  @Test
  public void skipBehaviorSkips() {
    Decision decision =
        Decision.newBuilder()
            .addChoice(FakeChoice.CHOICE_SELECTABLE)
            .addChoice(FakeChoice.CHOICE_NOT_SELECTABLE)
            .addChoice(FakeChoice.CHOICE_NOT_SELECTABLE)
            .withBehavior(SelectableBehavior.SKIP_OVER)
            .build();

    assertEquals(decision.select(), FakeChoice.CHOICE_SELECTABLE);
    assertTrue(decision.next());
    assertEquals(decision.select(), FakeChoice.CHOICE_SELECTABLE);
  }

  @Test
  public void skipBehaviorDoesNotAdvanceIfNoWrap() {
    Decision decision =
        Decision.newBuilder()
            .addChoice(FakeChoice.CHOICE_SELECTABLE)
            .addChoice(FakeChoice.CHOICE_NOT_SELECTABLE)
            .addChoice(FakeChoice.CHOICE_NOT_SELECTABLE)
            .withAllowWrap(false)
            .withBehavior(SelectableBehavior.SKIP_OVER)
            .build();

    assertEquals(decision.select(), FakeChoice.CHOICE_SELECTABLE);
    assertFalse(decision.next());
    assertEquals(decision.select(), FakeChoice.CHOICE_SELECTABLE);
  }
}
