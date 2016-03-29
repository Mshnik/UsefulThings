package common.math;


import java.util.function.Function;

//TODO - SPEC
/**
 * @author Mshnik
 */
public abstract class NumExt<T extends Number> {

  protected final T val;

  private NumExt(T t) {
    if(t == null) throw new IllegalArgumentException("Can't wrap a null value");
    val = t;
  }

  @SuppressWarnings("unchecked")
  public static <T extends Number> NumExt<T> wrap(T t) {
    if (t instanceof Integer) {
      return (NumExt<T>) new IntExt((Integer)t);
    } else if (t instanceof Double) {
      return (NumExt<T>) new DoubleExt((Double)t);
    } else if (t instanceof Float) {
      return (NumExt<T>) new FloatExt((Float) t);
    } else {
      throw new UnsupportedOperationException("Unknown numerical type " + t.getClass());
    }
  }

  public T asNumber() {
    return val;
  }

  public int asInt() {
    return val.intValue();
  }

  public float asFloat() {
    return val.floatValue();
  }

  public double asDouble() {
    return val.doubleValue();
  }

  public abstract NumExt<T> apply(Function<T,T> f);
  public abstract NumExt<T> add(T t2);
  public abstract NumExt<T> subtract(T t2);
  public abstract NumExt<T> multiply(T t2);
  public abstract NumExt<T> divide(T t2);

  public static class IntExt extends NumExt<Integer> {

    private IntExt(Integer val) {
      super(val);
    }

    public NumExt<Integer> apply(Function<Integer, Integer> f) {
      return wrap(f.apply(val));
    }

    public NumExt<Integer> add(Integer i) {
      return wrap(val + i);
    }

    public NumExt<Integer> subtract(Integer i) {
      return wrap(val - i);
    }

    public NumExt<Integer> multiply(Integer i) {
      return wrap(val * i);
    }

    public NumExt<Integer> divide(Integer i) {
      return wrap(val / i);
    }

  }
  public static class FloatExt extends NumExt<Float> {

    private FloatExt(Float val) {
      super(val);
    }

    public NumExt<Float> apply(Function<Float, Float> f) {
      return wrap(f.apply(val));
    }

    public NumExt<Float> add(Float i) {
      return wrap(val + i);
    }

    public NumExt<Float> subtract(Float i) {
      return wrap(val - i);
    }

    public NumExt<Float> multiply(Float i) {
      return wrap(val * i);
    }

    public NumExt<Float> divide(Float i) {
      return wrap(val / i);
    }
  }
  public static class DoubleExt extends NumExt<Double> {

    private DoubleExt(Double val) {
      super(val);
    }

    public NumExt<Double> apply(Function<Double, Double> f) {
      return wrap(f.apply(val));
    }

    public NumExt<Double> add(Double i) {
      return wrap(val + i);
    }

    public NumExt<Double> subtract(Double i) {
      return wrap(val - i);
    }

    public NumExt<Double> multiply(Double i) {
      return wrap(val * i);
    }

    public NumExt<Double> divide(Double i) {
      return wrap(val / i);
    }

  }
}
