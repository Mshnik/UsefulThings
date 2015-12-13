package io.classCreation;

import common.types.Tuple2;
import io.TextIO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

abstract class JavaFile {

  private static final String EXTENSION = ".java";

  public enum Access{
    PUBLIC,
    PROTECTED,
    DEFAULT,
    PRIVATE;

    public String toString() {
      return super.toString().toLowerCase();
    }
  }

  protected abstract static class Method{
    protected String name;
    protected String returnType;
    protected ArrayList<String> genericTypes;
    protected ArrayList<Tuple2<String, String>> args;
    protected ArrayList<String> throwClauses;

    protected String body;

    public abstract String toString();
  }

  private String packageName;
  private ArrayList<String> imports;

  private Access classAccess;
  private String className;
  private ArrayList<String> extensions;






  public void write(String sourceRoot) throws IOException {
    String path = sourceRoot + File.separator + packageName.replaceAll("\\.", File.separator)
                  + File.separator + className + EXTENSION;
    TextIO.write(new File(path), toString());
  }

}
