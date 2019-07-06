package game.decision;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static asserts.Asserts.assertThrows;
import static com.google.common.truth.Truth.assertThat;

/** @author Mshnik */
@RunWith(JUnit4.class)
public final class DecisionTest {

  @Test
  public void throwsOnNoChoices() {
    assertThrows(Exception.class, Decision.newBuilder()::build);
  }

  @Test
  public void buildsDecision() {
    Decision decision =
        Decision.newBuilder()
            .addChoice(FakeChoice.CHOICE_1)
            .addChoice(FakeChoice.CHOICE_2)
            .addChoice(FakeChoice.CHOICE_3)
            .build();

    assertThat(decision.getChoices())
        .containsExactly(FakeChoice.CHOICE_1, FakeChoice.CHOICE_2, FakeChoice.CHOICE_3);
  }

  @Test
  public void selectReturnsFirstElementByDefault() {
    Decision decision =
        Decision.newBuilder().addChoice(FakeChoice.CHOICE_1).addChoice(FakeChoice.CHOICE_2).build();
    assertThat(decision.select()).isEqualTo(FakeChoice.CHOICE_1);
  }

  @Test
  public void nextReturnsTrueAndWraps() {
    Decision decision =
        Decision.newBuilder().addChoice(FakeChoice.CHOICE_1).addChoice(FakeChoice.CHOICE_2).build();
    assertThat(decision.select()).isEqualTo(FakeChoice.CHOICE_1);
    assertThat(decision.next()).isTrue();
    assertThat(decision.next()).isTrue();
    assertThat(decision.select()).isEqualTo(FakeChoice.CHOICE_1);
  }

  @Test
  public void previousReturnsTrueAndWraps() {
    Decision decision =
        Decision.newBuilder().addChoice(FakeChoice.CHOICE_1).addChoice(FakeChoice.CHOICE_2).build();
    assertThat(decision.select()).isEqualTo(FakeChoice.CHOICE_1);
    assertThat(decision.prev()).isTrue();
    assertThat(decision.prev()).isTrue();
    assertThat(decision.select()).isEqualTo(FakeChoice.CHOICE_1);
  }

  @Test
  public void nextReturnsFalseAndDoesNotWrap() {
    Decision decision =
        Decision.newBuilder()
            .addChoice(FakeChoice.CHOICE_1)
            .addChoice(FakeChoice.CHOICE_2)
            .withAllowWrap(false)
            .build();
    assertThat(decision.select()).isEqualTo(FakeChoice.CHOICE_1);
    assertThat(decision.next()).isTrue();
    assertThat(decision.next()).isFalse();
    assertThat(decision.select()).isEqualTo(FakeChoice.CHOICE_2);
  }

  @Test
  public void prevReturnsFalseAndDoesNotWrap() {
    Decision decision =
        Decision.newBuilder()
            .addChoice(FakeChoice.CHOICE_1)
            .addChoice(FakeChoice.CHOICE_2)
            .withAllowWrap(false)
            .build();
    assertThat(decision.select()).isEqualTo(FakeChoice.CHOICE_1);
    assertThat(decision.prev()).isFalse();
    assertThat(decision.select()).isEqualTo(FakeChoice.CHOICE_1);
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
    assertThat(decision.select()).isEqualTo(FakeChoice.CHOICE_2);
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
    assertThat(choiceList).containsExactly(FakeChoice.CHOICE_1);
  }

  @Test
  public void throwOnSelectBehaviorAllowsSelectButThrows() {
    Decision decision =
        Decision.newBuilder()
            .addChoice(FakeChoice.CHOICE_NOT_SELECTABLE)
            .withBehavior(SelectableBehavior.THROW_ON_SELECT)
            .build();

    assertThrows(Exception.class, decision::select);
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

    assertThat(decision.select()).isEqualTo(FakeChoice.CHOICE_SELECTABLE);
    assertThat(decision.next()).isFalse();
    assertThat(decision.select()).isEqualTo(FakeChoice.CHOICE_SELECTABLE);
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

    assertThat(decision.select()).isEqualTo(FakeChoice.CHOICE_SELECTABLE);
    assertThat(decision.next()).isFalse();
    assertThat(decision.select()).isEqualTo(FakeChoice.CHOICE_SELECTABLE);
  }
}
