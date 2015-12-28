package common.math;

//TODO - SPEC
//TODO - TEST
public class Vector3 extends Vector {

  public Vector3(double x, double y, double z) {
    super(x, y, z);
  }

  public Vector3() {
    this(0.0, 0.0, 0.0);
  }

  public double x() {
    return get(0);
  }

  public double y() {
    return get(1);
  }

  public double z() {
    return get(2);
  }

  public Vector3 invert() {
    return new Vector3(-x(), -y(), -z());
  }

  public Vector3 add(Vector3 other) {
    return new Vector3(x() + other.x(), y() + other.y(), z() + other.z());
  }

  public Vector3 subtract(Vector3 other) {
    return add(other.invert());
  }

  public double dot(Vector3 other) {
    return x() * other.x() + y() * other.y() + z() * other.z();
  }

  public Vector3 cross(Vector3 other) {
    return new Vector3(
        y() * other.z() - z() * other.y(),
        z() * other.x() - x() * other.z(),
        x() * other.y() - y() * other.x()
    );
  }

  //Test this - dubious if correct
  public double angle(Vector3 other) {
    return Math.atan2(cross(other).magnitude(), dot(other));
  }
}
