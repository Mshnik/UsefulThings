package common.math;

import java.util.function.Function;
import java.util.stream.Stream;

//TODO - SPEC
/**
 * @author Mshnik
 */
public abstract class NumExt extends Number implements Comparable<Number> {

  public static final NumExt ZERO = wrap(0);
  public static final NumExt ONE = wrap(1);
  public static final NumExt NEG_ONE = wrap(-1);


  /** Applies and returns the correct function by the type of n
   * @param n - the numerical argument to the function
   * @param numExtFunc - the function to apply if n is already a NumExt
   * @param byteFunc - the function to apply if n is a Byte
   * @param shortFunc - the function to apply if n is a Short
   * @param intFunc - the function to apply if n is an Integer
   * @param longFunc - the function to apply if n is a Long
   * @param floatFunc - the function to apply if n is a Float
   * @param doubleFunc - the function to apply if n is a Double
   * @param <R> - the return type of the functions
   * @return - the return of the selected function
   * @throws UnsupportedOperationException - if n is not one of the above types
   */
  private static <R> R applyByNumType(Number n,
                                      Function<NumExt, R> numExtFunc,
                                      Function<Byte, R> byteFunc, Function<Short, R> shortFunc,
                                      Function<Integer, R> intFunc, Function<Long, R> longFunc,
                                      Function<Float, R> floatFunc, Function<Double, R> doubleFunc)
      throws UnsupportedOperationException {
    if (n instanceof NumExt) {
      return numExtFunc.apply((NumExt)n);
    } else if (n instanceof Integer) {
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

  public static NumExt wrap(Number t) {
    return applyByNumType(t, x -> x, ByteExt::new, ShortExt::new, IntExt::new, LongExt::new, FloatExt::new, DoubleExt::new);
  }

  public abstract Number getVal();

  public abstract NumExt negate();

  public String toString() {
    return getVal().toString();
  }

  public boolean equals(NumExt n1) {
    return this == n1 || n1 != null && Math.abs(n1.doubleValue() - doubleValue()) == 0.0;
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (! (o instanceof NumExt)) return false;

    NumExt n = (NumExt)o;
    return equals(n);
  }

  public int hashCode() {
    return intValue();
  }

  public int signum() {
    if (equals(wrap(0))) return 0;
    else if (doubleValue() < 0) return -1;
    else return 0;
  }

  public int compareTo(Number n) {
    if (equals(wrap(n))) return 0;
    else if (doubleValue() < n.doubleValue()) return -1;
    else return 1;
  }

  public boolean isZero() {
    return equals(wrap(0));
  }

  public <X extends Number> X getAs(Class<X> clazz) {
    return clazz.cast(getVal());
  }

  public Stream<Number> toStream() {
    return Stream.of(getVal());
  }

  public int intValue() {
    return getVal().intValue();
  }

  public long longValue() {
    return getVal().longValue();
  }

  public float floatValue() {
    return getVal().floatValue();
  }

  public double doubleValue() {
    return getVal().doubleValue();
  }

  public NumExt apply(Function<Number,Number> f) {
    return wrap(f.apply(getVal()));
  }

  public NumExt add(NumExt n) {
    return add(n.getVal());
  }

  public NumExt subtract(NumExt n) {
    return subtract(n.getVal());
  }

  public NumExt multiply(NumExt n) {
    return multiply(n.getVal());
  }

  public NumExt divide(NumExt n) {
    return divide(n.getVal());
  }

  public NumExt add(Number n) {
    return applyByNumType(n, this::add, this::add, this::add, this::add, this::add, this::add, this::add);
  }

  public NumExt subtract(Number n) {
    return applyByNumType(n, this::subtract, this::subtract, this::subtract, this::subtract,
        this::subtract, this::subtract, this::subtract);
  }

  public NumExt multiply(Number n) {
    return applyByNumType(n, this::multiply, this::multiply, this::multiply, this::multiply,
        this::multiply, this::multiply, this::multiply);
  }

  public NumExt divide(Number n) {
    return applyByNumType(n, this::divide, this::divide, this::divide, this::divide,
        this::divide, this::divide, this::divide);
  }

  public abstract NumExt add(Byte t2);
  public abstract NumExt subtract(Byte t2);
  public abstract NumExt multiply(Byte t2);
  public abstract NumExt divide(Byte t2);

  public abstract NumExt add(Short t2);
  public abstract NumExt subtract(Short t2);
  public abstract NumExt multiply(Short t2);
  public abstract NumExt divide(Short t2);

  public abstract NumExt add(Integer t2);
  public abstract NumExt subtract(Integer t2);
  public abstract NumExt multiply(Integer t2);
  public abstract NumExt divide(Integer t2);

  public abstract NumExt add(Long t2);
  public abstract NumExt subtract(Long t2);
  public abstract NumExt multiply(Long t2);
  public abstract NumExt divide(Long t2);

  public abstract NumExt add(Float t2);
  public abstract NumExt subtract(Float t2);
  public abstract NumExt multiply(Float t2);
  public abstract NumExt divide(Float t2);

  public abstract NumExt add(Double t2);
  public abstract NumExt subtract(Double t2);
  public abstract NumExt multiply(Double t2);
  public abstract NumExt divide(Double t2);

  private static class ByteExt extends NumExt {

    private byte val;

    private ByteExt(Byte val) {
      if(val == null) throw new IllegalArgumentException("Can't wrap a null value");
      this.val = val;
    }

    public Number getVal() {
      return val;
    }

    public NumExt negate() {
      return wrap(-val);
    }

    public NumExt add(Byte t2) {
      return wrap((byte)(val + t2));
    }

    public NumExt subtract(Byte t2) {
      return wrap((byte)(val - t2));
    }

    public NumExt multiply(Byte t2) {
      return wrap((byte)(val * t2));
    }

    public NumExt divide(Byte t2) {
      return wrap((byte)(val / t2));
    }

    public NumExt add(Short t2) {
      return wrap((short)(val + t2));
    }

    public NumExt subtract(Short t2) {
      return wrap((short)(val - t2));
    }

    public NumExt multiply(Short t2) {
      return wrap((short)(val * t2));
    }

    public NumExt divide(Short t2) {
      return wrap((short)(val / t2));
    }

    public NumExt add(Integer t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Integer t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Integer t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Integer t2) {
      return wrap(val / t2);
    }

    public NumExt add(Long t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Long t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Long t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Long t2) {
      return wrap(val / t2);
    }

    public NumExt add(Float t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Float t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Float t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Float t2) {
      return wrap(val / t2);
    }

    public NumExt add(Double t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Double t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Double t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Double t2) {
      return wrap(val / t2);
    }

  }

  private static class ShortExt extends NumExt {

    private short val;

    private ShortExt(Short val) {
      if(val == null) throw new IllegalArgumentException("Can't wrap a null value");
      this.val = val;
    }

    public Number getVal() {
      return val;
    }

    public NumExt negate() {
      return wrap(-val);
    }

    public NumExt add(Byte t2) {
      return wrap((short)(val + t2));
    }

    public NumExt subtract(Byte t2) {
      return wrap((short)(val - t2));
    }

    public NumExt multiply(Byte t2) {
      return wrap((short)(val * t2));
    }

    public NumExt divide(Byte t2) {
      return wrap((short)(val / t2));
    }

    public NumExt add(Short t2) {
      return wrap((short)(val + t2));
    }

    public NumExt subtract(Short t2) {
      return wrap((short)(val - t2));
    }

    public NumExt multiply(Short t2) {
      return wrap((short)(val * t2));
    }

    public NumExt divide(Short t2) {
      return wrap((short)(val / t2));
    }

    public NumExt add(Integer t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Integer t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Integer t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Integer t2) {
      return wrap(val / t2);
    }

    public NumExt add(Long t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Long t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Long t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Long t2) {
      return wrap(val / t2);
    }

    public NumExt add(Float t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Float t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Float t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Float t2) {
      return wrap(val / t2);
    }

    public NumExt add(Double t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Double t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Double t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Double t2) {
      return wrap(val / t2);
    }

  }

  private static class IntExt extends NumExt {

    private int val;

    private IntExt(Integer val) {
      if(val == null) throw new IllegalArgumentException("Can't wrap a null value");
      this.val = val;
    }

    public Number getVal() {
      return val;
    }

    public NumExt negate() {
      return wrap(-val);
    }

    public NumExt add(Byte t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Byte t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Byte t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Byte t2) {
      return wrap(val / t2);
    }

    public NumExt add(Short t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Short t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Short t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Short t2) {
      return wrap(val / t2);
    }

    public NumExt add(Integer t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Integer t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Integer t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Integer t2) {
      return wrap(val / t2);
    }

    public NumExt add(Long t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Long t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Long t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Long t2) {
      return wrap(val / t2);
    }

    public NumExt add(Float t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Float t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Float t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Float t2) {
      return wrap(val / t2);
    }

    public NumExt add(Double t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Double t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Double t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Double t2) {
      return wrap(val / t2);
    }

  }

  private static class LongExt extends NumExt {

    private long val;

    private LongExt(Long val) {
      if(val == null) throw new IllegalArgumentException("Can't wrap a null value");
      this.val = val;
    }

    public Number getVal() {
      return val;
    }

    public NumExt negate() {
      return wrap(-val);
    }

    public NumExt add(Byte t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Byte t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Byte t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Byte t2) {
      return wrap(val / t2);
    }

    public NumExt add(Short t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Short t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Short t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Short t2) {
      return wrap(val / t2);
    }

    public NumExt add(Integer t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Integer t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Integer t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Integer t2) {
      return wrap(val / t2);
    }

    public NumExt add(Long t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Long t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Long t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Long t2) {
      return wrap(val / t2);
    }

    public NumExt add(Float t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Float t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Float t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Float t2) {
      return wrap(val / t2);
    }

    public NumExt add(Double t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Double t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Double t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Double t2) {
      return wrap(val / t2);
    }

  }

  private static class FloatExt extends NumExt {

    private float val;

    private FloatExt(Float val) {
      if(val == null) throw new IllegalArgumentException("Can't wrap a null value");
      this.val = val;
    }

    public Number getVal() {
      return val;
    }

    public NumExt negate() {
      return wrap(-val);
    }

    public NumExt add(Byte t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Byte t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Byte t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Byte t2) {
      return wrap(val / t2);
    }

    public NumExt add(Short t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Short t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Short t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Short t2) {
      return wrap(val / t2);
    }

    public NumExt add(Integer t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Integer t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Integer t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Integer t2) {
      return wrap(val / t2);
    }

    public NumExt add(Long t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Long t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Long t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Long t2) {
      return wrap(val / t2);
    }

    public NumExt add(Float t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Float t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Float t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Float t2) {
      return wrap(val / t2);
    }

    public NumExt add(Double t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Double t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Double t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Double t2) {
      return wrap(val / t2);
    }

  }

  private static class DoubleExt extends NumExt {

    private double val;

    private DoubleExt(Double val) {
      if(val == null) throw new IllegalArgumentException("Can't wrap a null value");
      this.val = val;
    }

    public Number getVal() {
      return val;
    }

    public NumExt negate() {
      return wrap(-val);
    }

    public NumExt add(Byte t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Byte t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Byte t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Byte t2) {
      return wrap(val / t2);
    }

    public NumExt add(Short t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Short t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Short t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Short t2) {
      return wrap(val / t2);
    }

    public NumExt add(Integer t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Integer t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Integer t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Integer t2) {
      return wrap(val / t2);
    }

    public NumExt add(Long t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Long t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Long t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Long t2) {
      return wrap(val / t2);
    }

    public NumExt add(Float t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Float t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Float t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Float t2) {
      return wrap(val / t2);
    }

    public NumExt add(Double t2) {
      return wrap(val + t2);
    }

    public NumExt subtract(Double t2) {
      return wrap(val - t2);
    }

    public NumExt multiply(Double t2) {
      return wrap(val * t2);
    }

    public NumExt divide(Double t2) {
      return wrap(val / t2);
    }
  }

}
