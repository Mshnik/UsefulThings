package common.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;
import java.util.stream.Stream;

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

  /** Applies and returns the correct function by the type of n
   * @param n - the numerical argument to the function
   * @param byteFunc - the funtion to apply if n is a Byte
   * @param shortFunc - the function to apply if n is a Short
   * @param intFunc - the function to apply if n is an Integer
   * @param longFunc - the funtion to apply if n is a Long
   * @param floatFunc - the function to apply if n is a Float
   * @param doubleFunc - the function to apply if n is a Double
   * @param <R> - the return type of the functions
   * @return - the return of the selected function
   * @throws UnsupportedOperationException - if n is not one of the above types
   */
  private static <R> R applyByNumType(Number n,
                                      Function<Byte, R> byteFunc, Function<Short, R> shortFunc,
                                      Function<Integer, R> intFunc, Function<Long, R> longFunc,
                                      Function<Float, R> floatFunc, Function<Double, R> doubleFunc)
                                        throws UnsupportedOperationException {
    if (n instanceof Integer) {
      return intFunc.apply((Integer)n);
    } else if (n instanceof Double) {
      return doubleFunc.apply((Double)n);
    } else if (n instanceof Long) {
      return longFunc.apply((Long)n);
    } else if (n instanceof Float) {
      return floatFunc.apply((Float)n);
    } else if (n instanceof Short)  {
      return shortFunc.apply((Short)n);
    } else if (n instanceof Byte)  {
      return byteFunc.apply((Byte)n);
    } else {
      throw new UnsupportedOperationException("Unsupported Numerical Type " + n.getClass());
    }
  }

  @SuppressWarnings("unchecked")
  public static <T extends Number> NumExt<T> wrap(T t) {
    return (NumExt<T>)applyByNumType(t, ByteExt::new, ShortExt::new, IntExt::new, LongExt::new, FloatExt::new, DoubleExt::new);
  }

  public Stream<T> toStream() {
    return Stream.of(val);
  }

  public T asNumber() {
    return val;
  }

  public int asInt() {
    return val.intValue();
  }

  public long asLong() {
    return val.longValue();
  }

  public float asFloat() {
    return val.floatValue();
  }

  public double asDouble() {
    return val.doubleValue();
  }

//  public NumExt<T> add(NumExt<T> t2) {
//    return add(t2.val);
//  }
//  public NumExt<T> subtract(NumExt<T> t2) {
//    return subtract(t2.val);
//  }
//  public NumExt<T> multiply(NumExt<T> t2) {
//    return multiply(t2.val);
//  }
//  public NumExt<T> divide(NumExt<T> t2) {
//    return divide(t2.val);
//  }

  public <U extends Number> NumExt<U> apply(Function<T,U> f) {
    return wrap(f.apply(val));
  }

  public NumExt<?> add(NumExt<?> n) {
    return add(n.val);
  }

  public NumExt<?> subtract(NumExt<?> n) {
    return subtract(n.val);
  }

  public NumExt<?> multiply(NumExt<?> n) {
    return multiply(n.val);
  }

  public NumExt<?> divide(NumExt<?> n) {
    return divide(n.val);
  }

  public NumExt<?> add(Number n) {
    return applyByNumType(n, this::add, this::add, this::add, this::add, this::add, this::add);
  }

  public NumExt<?> subtract(Number n) {
    return applyByNumType(n, this::subtract, this::subtract, this::subtract,
                             this::subtract, this::subtract, this::subtract);
  }

  public NumExt<?> multiply(Number n) {
    return applyByNumType(n, this::multiply, this::multiply, this::multiply,
                             this::multiply, this::multiply, this::multiply);
  }

  public NumExt<?> divide(Number n) {
    return applyByNumType(n, this::divide, this::divide, this::divide,
                             this::divide, this::divide, this::divide);
  }

  public abstract NumExt<?> add(Byte t2);
  public abstract NumExt<?> subtract(Byte t2);
  public abstract NumExt<?> multiply(Byte t2);
  public abstract NumExt<?> divide(Byte t2);

  public abstract NumExt<?> add(Short t2);
  public abstract NumExt<?> subtract(Short t2);
  public abstract NumExt<?> multiply(Short t2);
  public abstract NumExt<?> divide(Short t2);

  public abstract NumExt<?> add(Integer t2);
  public abstract NumExt<?> subtract(Integer t2);
  public abstract NumExt<?> multiply(Integer t2);
  public abstract NumExt<?> divide(Integer t2);

  public abstract NumExt<?> add(Long t2);
  public abstract NumExt<?> subtract(Long t2);
  public abstract NumExt<?> multiply(Long t2);
  public abstract NumExt<?> divide(Long t2);

  public abstract NumExt<?> add(Float t2);
  public abstract NumExt<?> subtract(Float t2);
  public abstract NumExt<?> multiply(Float t2);
  public abstract NumExt<?> divide(Float t2);

  public abstract NumExt<?> add(Double t2);
  public abstract NumExt<?> subtract(Double t2);
  public abstract NumExt<?> multiply(Double t2);
  public abstract NumExt<?> divide(Double t2);

//  public abstract NumExt<?> add(BigInteger t2);
//  public abstract NumExt<?> subtract(BigInteger t2);
//  public abstract NumExt<?> multiply(BigInteger t2);
//  public abstract NumExt<?> divide(BigInteger t2);
//
//  public abstract NumExt<?> add(BigDecimal t2);
//  public abstract NumExt<?> subtract(BigDecimal t2);
//  public abstract NumExt<?> multiply(BigDecimal t2);
//  public abstract NumExt<?> divide(BigDecimal t2);


  private static class ByteExt extends NumExt<Byte> {

    private ByteExt(Byte val) {
      super(val);
    }

    public NumExt<Byte> add(Byte t2) {
      return wrap((byte)(val + t2));
    }

    public NumExt<Byte> subtract(Byte t2) {
      return wrap((byte)(val - t2));
    }

    public NumExt<Byte> multiply(Byte t2) {
      return wrap((byte)(val * t2));
    }

    public NumExt<Byte> divide(Byte t2) {
      return wrap((byte)(val / t2));
    }

    public NumExt<Short> add(Short t2) {
      return wrap((short)(val + t2));
    }

    public NumExt<Short> subtract(Short t2) {
      return wrap((short)(val - t2));
    }

    public NumExt<Short> multiply(Short t2) {
      return wrap((short)(val * t2));
    }

    public NumExt<Short> divide(Short t2) {
      return wrap((short)(val / t2));
    }

    public NumExt<Integer> add(Integer t2) {
      return wrap(val + t2);
    }

    public NumExt<Integer> subtract(Integer t2) {
      return wrap(val - t2);
    }

    public NumExt<Integer> multiply(Integer t2) {
      return wrap(val * t2);
    }

    public NumExt<Integer> divide(Integer t2) {
      return wrap(val / t2);
    }

    public NumExt<Long> add(Long t2) {
      return wrap(val + t2);
    }

    public NumExt<Long> subtract(Long t2) {
      return wrap(val - t2);
    }

    public NumExt<Long> multiply(Long t2) {
      return wrap(val * t2);
    }

    public NumExt<Long> divide(Long t2) {
      return wrap(val / t2);
    }

    public NumExt<Float> add(Float t2) {
      return wrap(val + t2);
    }

    public NumExt<Float> subtract(Float t2) {
      return wrap(val - t2);
    }

    public NumExt<Float> multiply(Float t2) {
      return wrap(val * t2);
    }

    public NumExt<Float> divide(Float t2) {
      return wrap(val / t2);
    }

    public NumExt<Double> add(Double t2) {
      return wrap(val + t2);
    }

    public NumExt<Double> subtract(Double t2) {
      return wrap(val - t2);
    }

    public NumExt<Double> multiply(Double t2) {
      return wrap(val * t2);
    }

    public NumExt<Double> divide(Double t2) {
      return wrap(val / t2);
    }

  }

  private static class ShortExt extends NumExt<Short> {

    private ShortExt(Short val) {
      super(val);
    }

    public NumExt<Short> add(Byte t2) {
      return wrap((short)(val + t2));
    }

    public NumExt<Short> subtract(Byte t2) {
      return wrap((short)(val - t2));
    }

    public NumExt<Short> multiply(Byte t2) {
      return wrap((short)(val * t2));
    }

    public NumExt<Short> divide(Byte t2) {
      return wrap((short)(val / t2));
    }

    public NumExt<Short> add(Short t2) {
      return wrap((short)(val + t2));
    }

    public NumExt<Short> subtract(Short t2) {
      return wrap((short)(val - t2));
    }

    public NumExt<Short> multiply(Short t2) {
      return wrap((short)(val * t2));
    }

    public NumExt<Short> divide(Short t2) {
      return wrap((short)(val / t2));
    }

    public NumExt<Integer> add(Integer t2) {
      return wrap(val + t2);
    }

    public NumExt<Integer> subtract(Integer t2) {
      return wrap(val - t2);
    }

    public NumExt<Integer> multiply(Integer t2) {
      return wrap(val * t2);
    }

    public NumExt<Integer> divide(Integer t2) {
      return wrap(val / t2);
    }

    public NumExt<Long> add(Long t2) {
      return wrap(val + t2);
    }

    public NumExt<Long> subtract(Long t2) {
      return wrap(val - t2);
    }

    public NumExt<Long> multiply(Long t2) {
      return wrap(val * t2);
    }

    public NumExt<Long> divide(Long t2) {
      return wrap(val / t2);
    }

    public NumExt<Float> add(Float t2) {
      return wrap(val + t2);
    }

    public NumExt<Float> subtract(Float t2) {
      return wrap(val - t2);
    }

    public NumExt<Float> multiply(Float t2) {
      return wrap(val * t2);
    }

    public NumExt<Float> divide(Float t2) {
      return wrap(val / t2);
    }

    public NumExt<Double> add(Double t2) {
      return wrap(val + t2);
    }

    public NumExt<Double> subtract(Double t2) {
      return wrap(val - t2);
    }

    public NumExt<Double> multiply(Double t2) {
      return wrap(val * t2);
    }

    public NumExt<Double> divide(Double t2) {
      return wrap(val / t2);
    }

  }

  private static class IntExt extends NumExt<Integer> {

    private IntExt(Integer val) {
      super(val);
    }

    public NumExt<Integer> add(Byte t2) {
      return wrap(val + t2);
    }

    public NumExt<Integer> subtract(Byte t2) {
      return wrap(val - t2);
    }

    public NumExt<Integer> multiply(Byte t2) {
      return wrap(val * t2);
    }

    public NumExt<Integer> divide(Byte t2) {
      return wrap(val / t2);
    }

    public NumExt<Integer> add(Short t2) {
      return wrap(val + t2);
    }

    public NumExt<Integer> subtract(Short t2) {
      return wrap(val - t2);
    }

    public NumExt<Integer> multiply(Short t2) {
      return wrap(val * t2);
    }

    public NumExt<Integer> divide(Short t2) {
      return wrap(val / t2);
    }

    public NumExt<Integer> add(Integer t2) {
      return wrap(val + t2);
    }

    public NumExt<Integer> subtract(Integer t2) {
      return wrap(val - t2);
    }

    public NumExt<Integer> multiply(Integer t2) {
      return wrap(val * t2);
    }

    public NumExt<Integer> divide(Integer t2) {
      return wrap(val / t2);
    }

    public NumExt<Long> add(Long t2) {
      return wrap(val + t2);
    }

    public NumExt<Long> subtract(Long t2) {
      return wrap(val - t2);
    }

    public NumExt<Long> multiply(Long t2) {
      return wrap(val * t2);
    }

    public NumExt<Long> divide(Long t2) {
      return wrap(val / t2);
    }

    public NumExt<Float> add(Float t2) {
      return wrap(val + t2);
    }

    public NumExt<Float> subtract(Float t2) {
      return wrap(val - t2);
    }

    public NumExt<Float> multiply(Float t2) {
      return wrap(val * t2);
    }

    public NumExt<Float> divide(Float t2) {
      return wrap(val / t2);
    }

    public NumExt<Double> add(Double t2) {
      return wrap(val + t2);
    }

    public NumExt<Double> subtract(Double t2) {
      return wrap(val - t2);
    }

    public NumExt<Double> multiply(Double t2) {
      return wrap(val * t2);
    }

    public NumExt<Double> divide(Double t2) {
      return wrap(val / t2);
    }

  }

  private static class LongExt extends NumExt<Long> {

    private LongExt(Long val) {
      super(val);
    }

    public NumExt<Long> add(Byte t2) {
      return wrap(val + t2);
    }

    public NumExt<Long> subtract(Byte t2) {
      return wrap(val - t2);
    }

    public NumExt<Long> multiply(Byte t2) {
      return wrap(val * t2);
    }

    public NumExt<Long> divide(Byte t2) {
      return wrap(val / t2);
    }

    public NumExt<Long> add(Short t2) {
      return wrap(val + t2);
    }

    public NumExt<Long> subtract(Short t2) {
      return wrap(val - t2);
    }

    public NumExt<Long> multiply(Short t2) {
      return wrap(val * t2);
    }

    public NumExt<Long> divide(Short t2) {
      return wrap(val / t2);
    }

    public NumExt<Long> add(Integer t2) {
      return wrap(val + t2);
    }

    public NumExt<Long> subtract(Integer t2) {
      return wrap(val - t2);
    }

    public NumExt<Long> multiply(Integer t2) {
      return wrap(val * t2);
    }

    public NumExt<Long> divide(Integer t2) {
      return wrap(val / t2);
    }

    public NumExt<Long> add(Long t2) {
      return wrap(val + t2);
    }

    public NumExt<Long> subtract(Long t2) {
      return wrap(val - t2);
    }

    public NumExt<Long> multiply(Long t2) {
      return wrap(val * t2);
    }

    public NumExt<Long> divide(Long t2) {
      return wrap(val / t2);
    }

    public NumExt<Float> add(Float t2) {
      return wrap(val + t2);
    }

    public NumExt<Float> subtract(Float t2) {
      return wrap(val - t2);
    }

    public NumExt<Float> multiply(Float t2) {
      return wrap(val * t2);
    }

    public NumExt<Float> divide(Float t2) {
      return wrap(val / t2);
    }

    public NumExt<Double> add(Double t2) {
      return wrap(val + t2);
    }

    public NumExt<Double> subtract(Double t2) {
      return wrap(val - t2);
    }

    public NumExt<Double> multiply(Double t2) {
      return wrap(val * t2);
    }

    public NumExt<Double> divide(Double t2) {
      return wrap(val / t2);
    }

  }

  private static class FloatExt extends NumExt<Float> {

    private FloatExt(Float val) {
      super(val);
    }

    public NumExt<Float> add(Byte t2) {
      return wrap(val + t2);
    }

    public NumExt<Float> subtract(Byte t2) {
      return wrap(val - t2);
    }

    public NumExt<Float> multiply(Byte t2) {
      return wrap(val * t2);
    }

    public NumExt<Float> divide(Byte t2) {
      return wrap(val / t2);
    }

    public NumExt<Float> add(Short t2) {
      return wrap(val + t2);
    }

    public NumExt<Float> subtract(Short t2) {
      return wrap(val - t2);
    }

    public NumExt<Float> multiply(Short t2) {
      return wrap(val * t2);
    }

    public NumExt<Float> divide(Short t2) {
      return wrap(val / t2);
    }

    public NumExt<Float> add(Integer t2) {
      return wrap(val + t2);
    }

    public NumExt<Float> subtract(Integer t2) {
      return wrap(val - t2);
    }

    public NumExt<Float> multiply(Integer t2) {
      return wrap(val * t2);
    }

    public NumExt<Float> divide(Integer t2) {
      return wrap(val / t2);
    }

    public NumExt<Float> add(Long t2) {
      return wrap(val + t2);
    }

    public NumExt<Float> subtract(Long t2) {
      return wrap(val - t2);
    }

    public NumExt<Float> multiply(Long t2) {
      return wrap(val * t2);
    }

    public NumExt<Float> divide(Long t2) {
      return wrap(val / t2);
    }

    public NumExt<Float> add(Float t2) {
      return wrap(val + t2);
    }

    public NumExt<Float> subtract(Float t2) {
      return wrap(val - t2);
    }

    public NumExt<Float> multiply(Float t2) {
      return wrap(val * t2);
    }

    public NumExt<Float> divide(Float t2) {
      return wrap(val / t2);
    }

    public NumExt<Double> add(Double t2) {
      return wrap(val + t2);
    }

    public NumExt<Double> subtract(Double t2) {
      return wrap(val - t2);
    }

    public NumExt<Double> multiply(Double t2) {
      return wrap(val * t2);
    }

    public NumExt<Double> divide(Double t2) {
      return wrap(val / t2);
    }

  }

  private static class DoubleExt extends NumExt<Double> {

    private DoubleExt(Double val) {
      super(val);
    }

    public NumExt<Double> add(Byte t2) {
      return wrap(val + t2);
    }

    public NumExt<Double> subtract(Byte t2) {
      return wrap(val - t2);
    }

    public NumExt<Double> multiply(Byte t2) {
      return wrap(val * t2);
    }

    public NumExt<Double> divide(Byte t2) {
      return wrap(val / t2);
    }

    public NumExt<Double> add(Short t2) {
      return wrap(val + t2);
    }

    public NumExt<Double> subtract(Short t2) {
      return wrap(val - t2);
    }

    public NumExt<Double> multiply(Short t2) {
      return wrap(val * t2);
    }

    public NumExt<Double> divide(Short t2) {
      return wrap(val / t2);
    }

    public NumExt<Double> add(Integer t2) {
      return wrap(val + t2);
    }

    public NumExt<Double> subtract(Integer t2) {
      return wrap(val - t2);
    }

    public NumExt<Double> multiply(Integer t2) {
      return wrap(val * t2);
    }

    public NumExt<Double> divide(Integer t2) {
      return wrap(val / t2);
    }

    public NumExt<Double> add(Long t2) {
      return wrap(val + t2);
    }

    public NumExt<Double> subtract(Long t2) {
      return wrap(val - t2);
    }

    public NumExt<Double> multiply(Long t2) {
      return wrap(val * t2);
    }

    public NumExt<Double> divide(Long t2) {
      return wrap(val / t2);
    }

    public NumExt<Double> add(Float t2) {
      return wrap(val + t2);
    }

    public NumExt<Double> subtract(Float t2) {
      return wrap(val - t2);
    }

    public NumExt<Double> multiply(Float t2) {
      return wrap(val * t2);
    }

    public NumExt<Double> divide(Float t2) {
      return wrap(val / t2);
    }

    public NumExt<Double> add(Double t2) {
      return wrap(val + t2);
    }

    public NumExt<Double> subtract(Double t2) {
      return wrap(val - t2);
    }

    public NumExt<Double> multiply(Double t2) {
      return wrap(val * t2);
    }

    public NumExt<Double> divide(Double t2) {
      return wrap(val / t2);
    }

  }

}
