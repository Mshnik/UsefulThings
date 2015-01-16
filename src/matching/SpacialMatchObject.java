package matching;

import java.awt.geom.Point2D;
import java.util.Collection;

import javax.tools.JavaFileManager.Location;

public class SpacialMatchObject extends MatchObject<SpacialMatchObject>{

	private static Point2D min = new Point2D.Double(Integer.MAX_VALUE, Integer.MAX_VALUE);
	private static Point2D max = new Point2D.Double(Integer.MIN_VALUE, Integer.MIN_VALUE);
	
	public static double getMinX(){
		return min.getX();
	}
	
	public static double getMinY(){
		return min.getY();
	}
	
	public static double getMaxX(){
		return max.getX();
	}
	
	public static double getMaxY(){
		return max.getY();
	}
	
	private Vector location;
	private Vector vec;
	
	public SpacialMatchObject(String name, Type t, Vector startLoc){
		super(name, t);
		location = startLoc;
		updateMinMax();
	}
	
	private void updateMinMax(){
		min = new Point2D.Double(Math.min(min.getX(),  location.getX()), Math.min(min.getY(), location.getY()));
		max = new Point2D.Double(Math.max(max.getX(),  location.getX()), Math.max(max.getY(), location.getY()));
	}
	
	public Point2D getLocation(){
		return new Point2D.Double(location.getX(), location.getY());
	}
	
	public void moveTo(Vector loc){
		location = new Vector(loc);
		updateMinMax();
	}
	
	public void addVec(Vector vec){
		location.addVector(vec);
		updateMinMax();
	}
	
	public String toString(){
		return super.toString() + " at " + location;
	}
	
	
	private static final double K = 5;
	private static final double R = 5;
	
	public static void iterate(Collection<SpacialMatchObject> objects){
		
		for(SpacialMatchObject o : objects){
			Vector vec = new Vector();
			//Add attractive forces towards preferences
			int i = 1;
			for(SpacialMatchObject m : o.getPreferences()){
				if(! o.location.equals(m.location))
				vec.addVector(new Vector(K / (i * i),  o.location.to(m.location)));
				i++;
			}
			
			//Add repulsive forces for all similar type objects
			for(SpacialMatchObject o2 : objects){
				if(o == o2 || ! o.type.equals(o2.type)) continue;
				
				vec.addVector(new Vector(R, o2.location.to(o.location)));
			}
			o.vec = vec;
		}
		
		
		
		//Finalize location changes
		for(SpacialMatchObject o : objects){
			o.addVec(o.vec);
			o.vec = null;
		}
	}
}
