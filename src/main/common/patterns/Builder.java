package common.patterns;

import java.util.*;

/**
 * @author Mshnik
 */
public abstract class Builder<T> {

  public abstract T build();

  public Collection<T> build(int count) {
    List<T> lst = new ArrayList<>();
    for(int i = 0; i < count; i++) {
      lst.add(build());
    }
    return lst;
  }

  public Builder<T> buildAndAddTo(Collection<T> col) {
    col.add(build());
    return this;
  }

}
