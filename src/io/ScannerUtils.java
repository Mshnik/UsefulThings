package io;

import java.util.Scanner;
import common.LambdaUtils.Getter;
import common.LambdaUtils.Validator;

public class ScannerUtils {

  private ScannerUtils(){}

  private static <T> Getter<T> createGetter(Class<T> clazz, Scanner s) {
    if(clazz.equals(Short.class)) return () -> clazz.cast(s.nextShort());
    if(clazz.equals(Integer.class)) return () -> clazz.cast(s.nextInt());
    if(clazz.equals(Long.class)) return () -> clazz.cast(s.nextLong());
    if(clazz.equals(Double.class)) return () -> clazz.cast(s.nextDouble());
    if(clazz.equals(String.class)) return () -> clazz.cast(s.nextLine());

    throw new IllegalArgumentException("Can't create getter for " + clazz + ", only scannable classes"
        + " are Short, Integer, Long, Double, String");
  }

  public static Scanner defaultScanner(){
    return new Scanner(System.in);
  }

  public static <T> T get(Class<T> clazz, Scanner s, String prompt, String failMessage) {
    return get(clazz, s, prompt, failMessage, (t) -> true);
  }

  public static <T> T get(Class<T> clazz,  Scanner s, String prompt, String failMessage, Validator<T> validator){
    Getter<T> getter = createGetter(clazz, s);
    while(true){
      System.out.print(prompt);
      try{
        T t = getter.get();
        if(validator.validate(t)) return t;
      }catch(Exception e){
        System.out.println("\n"+failMessage);
      }
    }
  }

}
