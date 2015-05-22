package common.dataStructures;

import java.util.Arrays;

/** A simple exception class for use in data structures in this package.
 * Thrown whenever an operation would require the presence of an element
 * in a collection, but that element is not present. This is distinct from
 * merely failing to perform the operation (removing a non-present element),
 * but moreover implies that the call was in error (connecting a present vertex
 * in a graph to a vertex that doesn't exist).
 * @author Mshnik
 */
@SuppressWarnings("serial")
public class NotInCollectionException extends RuntimeException{
	
	/** Constructs a NotInCollection exception with a message describing the issue
	 * @param msg  - custom message to prepend to this exception
	 * @param objects - the offending objects that are not present
	 */
	public NotInCollectionException(String msg, Object... objects){
		super(msg + " - " + Arrays.deepToString(objects) + 
				(objects.length > 1 ? " aren't" : " isn't") + " contained in collection");
	}
}