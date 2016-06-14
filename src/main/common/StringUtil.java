package common;

import java.util.HashSet;
import java.util.Iterator;

//TODO - SPEC
//TODO - TEST
public class StringUtil {
  private StringUtil() {
  }

  private static final HashSet<Character> breakCharacters;

  static {
    breakCharacters = new HashSet<>();
    breakCharacters.add(' ');
    breakCharacters.add('_');
    breakCharacters.add('-');
    breakCharacters.add('.');
    breakCharacters.add('/');
    breakCharacters.add('\\');

  }


  public static String toPronounCase(String s) {
    if (s == null) return null;
    StringBuilder sb = new StringBuilder();
    boolean capitalize = true;
    char[] arr = s.toLowerCase().toCharArray();
    for (char c : arr) {
      if (capitalize) {
        sb.append(Character.toUpperCase(c));
        capitalize = false;
      } else {
        sb.append(c);
      }

      capitalize = breakCharacters.contains(c);
    }

    return sb.toString();
  }

  public static CharIterator charIterator(String s) {
    return new CharIterator(s);
  }

  public static class CharIterator implements Iterator<Character> {
    public final String source;
    private final char[] sourceArr;
    private int index;

    public CharIterator(String source) {
      this.source = source;
      sourceArr = source.toCharArray();
      index = 0;
    }

    public int currentIndex() {
      return index;
    }

    @Override
    public boolean hasNext() {
      return index < sourceArr.length;
    }

    @Override
    public Character next() {
      return sourceArr[index++];
    }

    public String toString() {
      if (hasNext()) {
        return source.substring(0, index) + "{" + sourceArr[index] + "}" + source.substring(index);
      } else {
        return source + "{}";
      }
    }
  }
}
