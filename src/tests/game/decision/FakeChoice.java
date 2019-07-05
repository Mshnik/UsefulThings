package game.decision;

/** @author Mshnik */
enum FakeChoice implements Choice {
  CHOICE_1(true),
  CHOICE_2(true),
  CHOICE_3(true),
  CHOICE_SELECTABLE(true),
  CHOICE_NOT_SELECTABLE(false);

  private final boolean selectable;

  FakeChoice(boolean selectable) {
    this.selectable = selectable;
  }

  @Override
  public boolean selectable() {
    return selectable;
  }
}
