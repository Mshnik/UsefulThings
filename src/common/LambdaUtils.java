package common;

public class LambdaUtils {

  public static interface Getter<T>{
    public T get();
  }

  public static interface Validator<T>{
    public boolean validate(T t);
  }

}
