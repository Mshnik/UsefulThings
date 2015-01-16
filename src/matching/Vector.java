package matching;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Objects;

/** A simple geometric vector class (a la Point), which allows doubles for its fields */
public class Vector extends Point2D.Double{
	
	/** Constructor: a vector <0,0>. */
	public Vector() {
		super();
	}
	
	/** Constructor: a vector <x,y>.  */
	public Vector(double x, double y) {
		super(x,y);
	}
	
	/** Constructor: a vector with same values as v. */
	public Vector(Vector v) {
		super(v.x, v.y);
	}
	
	/** Constructor for a foce vector. Length k in direction of v */
	public Vector(double l, Vector v){
		super();
		Vector d = new Vector(v).unit();
		x = d.x * l;
		y = d.y * l;
	}
	
	/** Add v's components to this vector and
	 * return this Vector */
	public Vector addVector(Vector v) {
		x += v.x;
		y += v.y;
		return this;
	}
	
	/** Multiply both of this vector's components by s and 
	 * return this Vector. */
	public Vector mult(double s) {
		x *= s;
		y *= s;
		return this;
	}
	
	/** Invert both of this' components - change this vector to <1/x, 1/y> -- 
	 * and returns this Vector.
	 * Precondition: the x and y components are not 0. */
	public Vector invert() {
		x = 1/x;
		y = 1/y;
		return this;
	}
	
	/** Make this vector have length 1, with the same direction, and
	 * Return this Vector. */
	public Vector unit() {
		double l = length();
		x = x/l;
		y = y/l;
		return this;
	}
	
	/** Return the dot product of a and b/ */
	public static double dot(Vector a, Vector b) {
		return a.x * b.x + a.y * b.y;
	}
	
	/** Return the dot product of this Vector and b. */
	public double dot(Vector b) {
		return dot(this, b);
	}
	
	/** Return the cross product of a and b.
	 * Because vectors are 2-d, the return is a vector of the form <0, 0, z>
	 * So a 1-D vector. */
	public static double cross(Vector a, Vector b) {
		return a.x * b.y - b.x * a.y;
	}
	
	/** Return the cross product of this and b. */
	public double cross(Vector b) {
		return cross(this, b);
	}
	
	/** Return the length of a. */
	public static double length(Vector a) {
		return a.length();
	}
	
	/** Returns the length of this vector. */
	public double length() {
		return Math.sqrt(x*x + y*y);
	}
	
	/** Return the length of this vector. */
	public double magnitude() {
		return length();
	}
	
	/** Return the distance between a and b. */
	public static double distance(Vector a, Vector b) {
		return Point.distance(a.x, a.y, b.x, b.y);
	}
	
	/** Return the distance between this vector and b. */
	public double distance(Vector b) {
		return distance(this, b);
	}
	
	/** Return a new vector from this to b. */
	public Vector to(Vector b) {
		return new Vector(b.x - x, b.y - y);
	}
	
	/** Return the cosine of the angle between a and b */
	public static double cos(Vector a, Vector b) {
		return dot(a,b)/(a.length()*b.length());
	}
	
	/** Return the cosine of the angle between this and b. */
	public double cos(Vector b) {
		return cos(this, b);
	}
	
	/** Return the angle between a and b, in radians.
	 * Return is in the range 0.. PI */
	public static double radAngle(Vector a, Vector b) {
		return Math.acos(cos(a,b));
	}
	
	/** Returns the angle between this and b, in radians.
	 * Return is in the range 0 .. PI */
	public double radAngle(Vector b) {
		return radAngle(this, b);
	}
	
	/** Tolerance to consider two vector components equal. */
	public static final double TOLERANCE = 0.000001;
	
	/** Hashes a vector based on its components, using function Objects.hash.
	 * Round both components to the nearest TOLERANCE */
	@Override
	public int hashCode() {
		return Objects.hash(getX(), getY());
	}
	
	/** Return true iff ob is a Vector and is equal to this one.
	 * Two vectors are equal if their components are equal -
	 * thus checks if they have the same hashcodes, which are built off of their components */
	@Override
	public boolean equals(Object ob) {
		if (!(ob instanceof Vector))
			return false;
		
		return x == ((Vector) ob).x && y == ((Vector) ob).y;
	}

	/** Return a string representation of this vector: <x, y> . */
	@Override
	public String toString() {
		return "<" + x + "," + y + ">";
	}
}
