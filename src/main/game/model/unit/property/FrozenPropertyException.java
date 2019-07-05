package game.model.unit.property;

/** @author Mshnik */
final class FrozenPropertyException extends RuntimeException {
  FrozenPropertyException(Property<?> property) {
    super(String.format("Can't mutate property %s, it is currently frozen.", property));
  }
}
