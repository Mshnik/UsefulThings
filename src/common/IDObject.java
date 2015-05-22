package common;

import java.util.concurrent.atomic.AtomicInteger;

/** An IDObject is any object that is assigned a unique, immutable ID when 
 * it is instantiated. This ID is used in the default equals(..) and hashCode()
 * implementations, and should be used in any implementations of methods that overwrite
 * those methods. <br><br>
 * The IDObject class is thread-safe: the Next_id field is represented by an 
 * Atomic Integer to make sure that that no two objects are assigned the same ID,
 * even in the presence of multiple threads.
 * @author Mshnik
 *
 */
public class IDObject {

	/** The ID that will be assigned to the next IDObject that is created */
	private static AtomicInteger next_id = new AtomicInteger();
	
	/** Returns the nextId - the value that will be given to the next instantiation
	 * of an IDObject
	 */
	public static int getNextID(){
		return next_id.get();
	}
	
	/** Resets the nextId to be nextID - the next instantiation of an IDObject will 
	 * have id nextID.<br>
	 * Only use this when all prior instantiations of IDObjects are no longer in use.
	 */
	public static void setNextID(int nextID){
		next_id.set(nextID);
	}
	
	/** The id of this IDObject */
	private final int id;
	
	/** Constructor for the IDObject class
	 * Creates a new IDObject with a unique identifier number.
	 */
	public IDObject(){
		id = next_id.getAndIncrement();
	}
	
	/** Returns the ID of this IDObject. */
	public final int getID(){
		return id;
	}
	
	/** Returns the ID of this IDObject as its hashcode.
	 * As this is unique to each IDObject, this will only be the same as
	 * another IDObject's hashCode if the two objects are equivalent.
	 */
	public int hashCode(){
		return id;
	}
	
	/** Returns true iff o is an IDObject with the same ID.
	 * Specifically, it returns {@code hashCode() == o.hashCode()}.
	 * Thus, if the subclass overwrites hashCode to return a new unique identifier,
	 * this method can be inherited without problem.
	 */
	public boolean equals(Object o){
		try{
			IDObject i = (IDObject) o;
			return hashCode() == i.hashCode();
		}catch(ClassCastException e){
			return false;
		}
	}
	
}
