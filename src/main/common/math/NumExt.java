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
//    } else if (t instanceof BigInteger)  {
//      return (NumExt<T>) new BigIntExt((BigInteger) t);
//    } else if (t instanceof BigDecimal)  {
//      return (NumExt<T>) new BigDecimalExt((BigDecimal) t);
    } else {
      throw new UnsupportedOperationException("Unsupported numerical type " + t.getClass());
    }
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
