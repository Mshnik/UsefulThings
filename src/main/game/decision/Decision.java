package game.decision;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/** @author Mshnik */
public final class Decision {
  private final ImmutableList<Choice> choices;
  private final Consumer<Choice> choiceConsumer;
  private final boolean allowWrap;
  private final SelectableBehavior selectableBehavior;

  private int selectedIndex;

  private Decision(
      ImmutableList<Choice> choices,
      Consumer<Choice> choiceConsumer,
      boolean allowWrap,
      SelectableBehavior selectableBehavior,
      int selectedIndex) {
    this.choices = choices;
    this.choiceConsumer = choiceConsumer;
    this.allowWrap = allowWrap;
    this.selectableBehavior = selectableBehavior;
    this.selectedIndex = selectedIndex;
  }

  public ImmutableList<Choice> getChoices() {
    return choices;
  }

  public boolean next() {
    if (!allowWrap && selectedIndex >= choices.size() - 1) {
      return false;
    }
    return move(1);
  }

  public boolean prev() {
    if (!allowWrap && selectedIndex <= 1) {
      return false;
    }
    return move(-1);
  }

  private boolean move(int delta) {
    switch (selectableBehavior) {
      case SKIP_OVER:
        Integer nextIndex = nextSelectableIndex(choices, selectedIndex, allowWrap, delta);
        if (nextIndex != null && nextIndex != selectedIndex) {
          selectedIndex = nextIndex;
          return true;
        } else {
          return false;
        }
      case THROW_ON_SELECT:
        selectedIndex = (selectedIndex + delta + choices.size()) % choices.size();
        return true;
      default:
        throw new UnsupportedOperationException();
    }
  }

  public Choice select() {
    Choice c = choices.get(selectedIndex);
    if (!c.selectable()) {
      throw new UnselectableException(c);
    }

    choiceConsumer.accept(c);
    return c;
  }

  @Nullable
  private static Integer nextSelectableIndex(
      List<Choice> choices, int currentIndex, boolean wrap, int delta) {
    if (!wrap && delta == 1 && currentIndex == choices.size() - 1) {
      return null;
    }
    if (!wrap && delta == -1 && currentIndex == 0) {
      return null;
    }

    int originalIndex = currentIndex;
    do {
      currentIndex = (currentIndex + delta + choices.size()) % choices.size();
      if (currentIndex == originalIndex || choices.get(currentIndex).selectable()) {
        return currentIndex;
      }
    } while (wrap || currentIndex < choices.size() - 1 || currentIndex > 1);
    return null;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {
    private ArrayList<Choice> choices = new ArrayList<>();
    private Consumer<Choice> choiceConsumer = c -> {};
    private boolean allowWrap = true;
    private int selectedIndex = 0;
    private SelectableBehavior behavior = SelectableBehavior.THROW_ON_SELECT;

    private Builder() {}

    public Builder addChoice(Choice c) {
      choices.add(c);
      return this;
    }

    public Builder addChoices(Choice... choices) {
      this.choices.addAll(Arrays.asList(choices));
      return this;
    }

    public Builder addChoices(Collection<Choice> choices) {
      this.choices.addAll(choices);
      return this;
    }

    public Builder withChoiceConsumer(Consumer<Choice> choiceConsumer) {
      this.choiceConsumer = choiceConsumer;
      return this;
    }

    public Builder withAllowWrap(boolean allowWrap) {
      this.allowWrap = allowWrap;
      return this;
    }

    public Builder withSelectedIndex(int selectedIndex) {
      this.selectedIndex = selectedIndex;
      return this;
    }

    public Builder withBehavior(SelectableBehavior behavior) {
      this.behavior = behavior;
      return this;
    }

    public Decision build() {
      if (choices.isEmpty()) {
        throw new RuntimeException("At least one choice expected");
      }
      if (selectedIndex < 0 || selectedIndex >= choices.size()) {
        throw new IllegalArgumentException("Selected index OOB: " + selectedIndex);
      }
      if (behavior == SelectableBehavior.SKIP_OVER) {
        if (choices.stream().noneMatch(Choice::selectable)) {
          throw new IllegalArgumentException(
              "Expected at least one selectable choice in " + choices);
        }
        selectedIndex = nextSelectableIndex(choices, selectedIndex, true, 1);
      }

      return new Decision(
          ImmutableList.copyOf(choices), choiceConsumer, allowWrap, behavior, selectedIndex);
    }
  }
}
