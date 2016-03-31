package common.math;

import java.math.BigDecimal;
import java.math.BigInteger;
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
    } else if (t instanceof Long) {
      return (NumExt<T>) new LongExt((Long)t);
    } else if (t instanceof Float) {
      return (NumExt<T>) new FloatExt((Float) t);
    } else if (t instanceof Short)  {
      return (NumExt<T>) new ShortExt((Short) t);
    } else if (t instanceof Byte)  {
      return (NumExt<T>) new ByteExt((Byte) t);
    } else if (t instanceof BigInteger)  {
      return (NumExt<T>) new BigIntExt((BigInteger) t);
    } else if (t instanceof BigDecimal)  {
      return (NumExt<T>) new BigDecimalExt((BigDecimal) t);
    } else {
      throw new UnsupportedOperationException("Unsupported numerical type " + t.getClass());
    }
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

  public abstract NumExt<T> apply(Function<T,T> f);
  public abstract NumExt<T> add(T t2);
  public abstract NumExt<T> subtract(T t2);
  public abstract NumExt<T> multiply(T t2);
  public abstract NumExt<T> divide(T t2);

  public static <T extends Number> T apply(T t1, Function<T, T> f) {
    return wrap(t1).apply(f).asNumber();
  }

  public static <T extends Number> T add(T t1, T t2) {
    return wrap(t1).add(t2).asNumber();
  }

  public static <T extends Number> T subtract(T t1, T t2) {
    return wrap(t1).subtract(t2).asNumber();
  }

  public static <T extends Number> T multiply(T t1, T t2) {
    return wrap(t1).multiply(t2).asNumber();
  }

  public static <T extends Number> T divide(T t1, T t2) {
    return wrap(t1).divide(t2).asNumber();
  }

  private static class IntExt extends NumExt<Integer> {

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
  private static class LongExt extends NumExt<Long> {

    private LongExt(Long val) {
      super(val);
    }

    public NumExt<Long> apply(Function<Long, Long> f) {
      return wrap(f.apply(val));
    }

    public NumExt<Long> add(Long i) {
      return wrap(val + i);
    }

    public NumExt<Long> subtract(Long i) {
      return wrap(val - i);
    }

    public NumExt<Long> multiply(Long i) {
      return wrap(val * i);
    }

    public NumExt<Long> divide(Long i) {
      return wrap(val / i);
    }

  }
  private static class FloatExt extends NumExt<Float> {

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
  private static class DoubleExt extends NumExt<Double> {

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
  private static class ShortExt extends NumExt<Short> {

    private ShortExt(Short val) {
      super(val);
    }

    public NumExt<Short> apply(Function<Short, Short> f) {
      return wrap(f.apply(val));
    }

    public NumExt<Short> add(Short i) {
      return wrap((short)(val + i));
    }

    public NumExt<Short> subtract(Short i) {
      return wrap((short)(val - i));
    }

    public NumExt<Short> multiply(Short i) {
      return wrap((short)(val * i));
    }

    public NumExt<Short> divide(Short i) {
      return wrap((short)(val / i));
    }

  }
  private static class ByteExt extends NumExt<Byte> {

    private ByteExt(Byte val) {
      super(val);
    }

    public NumExt<Byte> apply(Function<Byte, Byte> f) {
      return wrap(f.apply(val));
    }

    public NumExt<Byte> add(Byte i) {
      return wrap((byte)(val + i));
    }

    public NumExt<Byte> subtract(Byte i) {
      return wrap((byte)(val - i));
    }

    public NumExt<Byte> multiply(Byte i) {
      return wrap((byte)(val * i));
    }

    public NumExt<Byte> divide(Byte i) {
      return wrap((byte)(val / i));
    }

  }

  private static class BigIntExt extends NumExt<BigInteger> {

    private BigIntExt(BigInteger val) {
      super(val);
    }

    public NumExt<BigInteger> apply(Function<BigInteger, BigInteger> f) {
      return wrap(f.apply(val));
    }

    public NumExt<BigInteger> add(BigInteger i) {
      return wrap(val.add(i));
    }

    public NumExt<BigInteger> subtract(BigInteger i) {
      return wrap(val.subtract(i));
    }

    public NumExt<BigInteger> multiply(BigInteger i) {
      return wrap(val.multiply(i));
    }

    public NumExt<BigInteger> divide(BigInteger i) {
      return wrap(val.divide(i));
    }

  }
  private static class BigDecimalExt extends NumExt<BigDecimal> {

    private BigDecimalExt(BigDecimal val) {
      super(val);
    }

    public NumExt<BigDecimal> apply(Function<BigDecimal, BigDecimal> f) {
      return wrap(f.apply(val));
    }

    public NumExt<BigDecimal> add(BigDecimal i) {
      return wrap(val.add(i));
    }

    public NumExt<BigDecimal> subtract(BigDecimal i) {
      return wrap(val.subtract(i));
    }

    public NumExt<BigDecimal> multiply(BigDecimal i) {
      return wrap(val.multiply(i));
    }

    public NumExt<BigDecimal> divide(BigDecimal i) {
      return wrap(val.divide(i));
    }

  }

}
