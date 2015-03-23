package common.dataStructures;

import java.util.Arrays;

@SuppressWarnings("serial")
public class NotInCollectionException extends RuntimeException{
	public NotInCollectionException(String msg, Object... objects){
		super(msg + " - " + Arrays.deepToString(objects) + 
				(objects.length > 1 ? " aren't" : " isn't") + " contained in collection");
	}
}