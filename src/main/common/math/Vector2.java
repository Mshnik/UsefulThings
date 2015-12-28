package common.math;

import common.types.Tuple;
import common.types.Tuple2;

//TODO - SPEC
//TODO - TEST
public class Vector2 extends Vector {

  public Vector2(double x, double y) {
    super(x, y);
  }

  public Vector2() {
    this(0, 0);
  }

  public static Vector2 fromPolarCoordinates(double r, double theta) {
    return new Vector2(r * Math.cos(theta), r * Math.sin(theta));
  }

  public double x() {
    return get(0);
  }

  public double y() {
    return get(1);
  }

  public Tuple2<Double, Double> toPolarCoordinates() {
    return Tuple.of(magnitude(), Math.atan2(y(), x()));
  }

  public Vector2 rotate(double theta) {
    Tuple2<Double, Double> p = toPolarCoordinates();
    return fromPolarCoordinates(p._1, p._2 + theta);
  }

  public Vector2 invert() {
    return new Vector2(-x(), -y());
  }

  public Vector2 add(Vector2 other) {
    return new Vector2(x() + other.x(), y() + other.y());
  }

  public Vector2 subtract(Vector2 other) {
    return add(other.invert());
  }

  public double dot(Vector2 other) {
    return x() * other.x() + y() * other.y();
  }

  public double angle(Vector2 other) {
    return Math.acos(dot(other) / (magnitude() * other.magnitude()));
  }

  public double cross(Vector2 other) {
    return new Vector3(x(), y(), 0).cross(new Vector3(other.x(), other.y(), 0)).z();
  }
}
