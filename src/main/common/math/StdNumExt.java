package common.math;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Mshnik
 */
abstract class StdNumExt extends NumExt implements Comparable<Number> {

  public static final NumExt ZERO = new IntExt(0);
  public static final NumExt ONE = new IntExt(1);
  public static final NumExt TWO = new IntExt(2);
  public static final NumExt NEG_ONE = new IntExt(-1);

  static StdNumExt wrapStd(Number n) {
    return applyByNumType(n, ByteExt::new, ShortExt::new, IntExt::new,
        LongExt::new, FloatExt::new, DoubleExt::new);
  }

  static <R> R applyByNumType(Number n,
                              Function<Byte, R> byteFunc, Function<Short, R> shortFunc,
                              Function<Integer, R> intFunc, Function<Long, R> longFunc,
                              Function<Float, R> floatFunc, Function<Double, R> doubleFunc) {
    if (n instanceof Byte) {
      return byteFunc.apply((Byte)n);
    } else if (n instanceof Short) {
      return shortFunc.apply((Short)n);
    } else if (n instanceof Integer) {
      return intFunc.apply((Integer)n);
    } else if (n instanceof Long) {
      return longFunc.apply((Long)n);
    } else if (n instanceof Float) {
      return floatFunc.apply((Float)n);
    } else if (n instanceof Double) {
      return doubleFunc.apply((Double)n);
    } else {
      throw new UnsupportedOperationException("Unsupported numerical type " + n.getClass());
    }
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

  public double fractionalValue() {
    return doubleValue() - intValue();
  }

  public int signum() {
    if (equals(wrap(0))) return 0;
    else if (doubleValue() < 0) return -1;
    else return 1;
  }

  public NumExt apply(Function<Number,Number> f) {
    return wrap(f.apply(getVal()));
  }

  public StdNumExt asByte() {
    return wrap(byteValue());
  }

  public NumExt asShort() {
    return wrap(shortValue());
  }

  public NumExt asInt() {
    return wrap(intValue());
  }

  public NumExt asFloat() {
    return wrap(floatValue());
  }

  public NumExt asDouble() {
    return wrap(doubleValue());
  }

  public NumExt round() {
    double frac = Math.abs(fractionalValue());
    int signum = signum();
    if (frac < 0.5 && signum > 0 || frac > 0.5 && signum < 0) return roundDown();
    else return roundUp();
  }

  public NumExt roundUp() {
    if(isInteger() || intValue() == doubleValue()) return this;
    return wrap(signum() == 1 ? intValue()+1 : intValue());
  }

  public NumExt roundDown() {
    if(isInteger() || intValue() == doubleValue()) return this;
    return wrap(signum() == 1 ? intValue() : intValue() - 1);
  }

  public StdNumExt abs() {
    if (signum() >= 0) return this;
    else return negate();
  }

  public NumExt mod(Number n) {
    NumExt wholePart = divide(n).asInt().multiply(n);
    if (signum() >= 0) {
      return subtract(wholePart);
    } else {
      return add(wholePart).add(n);
    }
  }

  public NumExt gcd(Number n) {
    return abs().gcd(wrap(n).abs());
  }

  private NumExt gcd(NumExt n) {
    if (isZero(0.0000001)) return n;
    if (n.isZero(0.0000001)) return this;

    NumExt q = divide(n).asInt();
    NumExt r = mod(n);

    return n.gcd(r);
  }

  public NumExt log(double base) {
    return wrap(Math.log(doubleValue())/Math.log(base));
  }

  @Override
  public NumExt add(Number n) {
    return applyByNumType(n, x -> add(x.getVal()), this::add, this::add, this::add, this::add, this::add, this::add, this::add, this::add);
  }

  @Override
  public NumExt subtract(Number n) {
    return applyByNumType(n, x -> subtract(x.getVal()), this::subtract, this::subtract, this::subtract, this::subtract, this::subtract, this::subtract, this::subtract, this::subtract);
  }

  @Override
  public NumExt multiply(Number n) {
    return applyByNumType(n, x -> multiply(x.getVal()), this::multiply, this::multiply, this::multiply, this::multiply, this::multiply, this::multiply, this::multiply, this::multiply);
  }

  @Override
  public NumExt divide(Number n) {
    return applyByNumType(n, x -> divide(x.getVal()), this::divide, this::divide, this::divide, this::divide, this::divide, this::divide, this::divide, this::divide);
  }


  public final NumExt add(StdNumExt t2) {
    return applyByNumType(t2.getVal(), this::add, this::add, this::add, this::add, this::add, this::add);
  }

  public final NumExt subtract(StdNumExt t2) {
    return applyByNumType(t2.getVal(), this::subtract, this::subtract, this::subtract, this::subtract, this::subtract, this::subtract);
  }

  public final NumExt multiply(StdNumExt t2) {
    return applyByNumType(t2.getVal(), this::multiply, this::multiply, this::multiply, this::multiply, this::multiply, this::multiply);
  }

  public final NumExt divide(StdNumExt t2) {
    return applyByNumType(t2.getVal(), this::divide, this::divide, this::divide, this::divide, this::divide, this::divide);
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

  public NumExt add(Rational t2) {
    return t2.add(this);
  }
  public NumExt subtract(Rational t2) {
    return t2.subtract(this);
  }
  public NumExt multiply(Rational t2) {
    return t2.multiply(this);
  }
  public NumExt divide(Rational t2) {
    return t2.divide(this);
  }

  public NumExt add(Real t2) {
    return t2.add(this);
  }
  public NumExt subtract(Real t2) {
    return t2.subtract(this);
  }
  public NumExt multiply(Real t2) {
    return t2.multiply(this);
  }
  public NumExt divide(Real t2) {
    return t2.divide(this);
  }

  private static class ByteExt extends StdNumExt {

    private byte val;

    private ByteExt(Byte val) {
      if(val == null) throw new IllegalArgumentException("Can't wrap a null value");
      this.val = val;
    }

    public Number getVal() {
      return val;
    }

    public boolean isInteger() {
      return true;
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

  private static class ShortExt extends StdNumExt {

    private short val;

    private ShortExt(Short val) {
      if(val == null) throw new IllegalArgumentException("Can't wrap a null value");
      this.val = val;
    }

    public Number getVal() {
      return val;
    }

    public boolean isInteger() {
      return true;
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

  private static class IntExt extends StdNumExt {

    private int val;

    private IntExt(Integer val) {
      if(val == null) throw new IllegalArgumentException("Can't wrap a null value");
      this.val = val;
    }

    public Number getVal() {
      return val;
    }

    public boolean isInteger() {
      return true;
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

  private static class LongExt extends StdNumExt {

    private long val;

    private LongExt(Long val) {
      if(val == null) throw new IllegalArgumentException("Can't wrap a null value");
      this.val = val;
    }

    public Number getVal() {
      return val;
    }

    public boolean isInteger() {
      return true;
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

  private static class FloatExt extends StdNumExt {

    private float val;

    private FloatExt(Float val) {
      if(val == null) throw new IllegalArgumentException("Can't wrap a null value");
      this.val = val;
    }

    public Number getVal() {
      return val;
    }

    public boolean isInteger() {
      return false;
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

  private static class DoubleExt extends StdNumExt {

    private double val;

    private DoubleExt(Double val) {
      if(val == null) throw new IllegalArgumentException("Can't wrap a null value");
      this.val = val;
    }

    public Number getVal() {
      return val;
    }

    public boolean isInteger() {
      return false;
    }

    public NumExt negate() {
      return wrapStd(-val);
    }

    public NumExt add(Byte t2) {
      return wrapStd(val + t2);
    }

    public NumExt subtract(Byte t2) {
      return wrapStd(val - t2);
    }

    public NumExt multiply(Byte t2) {
      return wrapStd(val * t2);
    }

    public NumExt divide(Byte t2) {
      return wrapStd(val / t2);
    }

    public NumExt add(Short t2) {
      return wrapStd(val + t2);
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