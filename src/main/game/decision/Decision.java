package game.decision;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.function.Consumer;

/** @author Mshnik */
public final class Decision {
  private final ImmutableList<Choice> choices;
  private final Consumer<Choice> choiceConsumer;
  private final boolean allowWrap;

  private int selectedIndex;

  private Decision(
      ImmutableList<Choice> choices,
      Consumer<Choice> choiceConsumer,
      boolean allowWrap,
      int selectedIndex) {
    this.choices = choices;
    this.choiceConsumer = choiceConsumer;
    this.allowWrap = allowWrap;
    this.selectedIndex = selectedIndex;

    if (selectedIndex < 0 || selectedIndex >= choices.size()) {
      throw new IllegalArgumentException("Selected index OOB: " + selectedIndex);
    }
  }

  @VisibleForTesting
  ImmutableList<Choice> getChoices() {
    return choices;
  }

  public boolean next() {
    if (!allowWrap && selectedIndex >= choices.size() - 1) {
      return false;
    }
    selectedIndex = (selectedIndex + 1) % choices.size();
    return true;
  }

  public boolean prev() {
    if (!allowWrap && selectedIndex <= 1) {
      return false;
    }
    selectedIndex = (selectedIndex - 1 + choices.size()) % choices.size();
    return true;
  }

  public Choice select() {
    choiceConsumer.accept(choices.get(selectedIndex));
    return choices.get(selectedIndex);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {
    private ArrayList<Choice> choices = new ArrayList<>();
    private Consumer<Choice> choiceConsumer = c -> {};
    private boolean allowWrap = true;
    private int selectedIndex = 0;

    private Builder() {}

    public Builder addChoice(Choice c) {
      choices.add(c);
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

    public Decision build() {
      if (choices.isEmpty()) {
        throw new RuntimeException("At least one choice expected");
      }
      return new Decision(ImmutableList.copyOf(choices), choiceConsumer, allowWrap, selectedIndex);
    }
  }
}
