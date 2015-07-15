package common;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class LambdaUtils {

  private LambdaUtils(){}

  public static <T> void forEach(T[] arr, Consumer<T> f){
  	for(int i = 0; i < arr.length; i++){
  		f.accept(arr[i]);
  	}
  }
  
  public static <T> T[] map(T[] arr, T[] dest, UnaryOperator<T> f){
  	dest = Arrays.copyOf(dest, arr.length);
  	for(int i = 0; i < arr.length; i++){
  		dest[i] = f.apply(arr[i]);
  	}
  	return dest;
  }
  
  public static <T> T[] map(T[] arr, UnaryOperator<T> f){
  	return map(arr, arr, f);
  }

}
