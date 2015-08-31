package common.types;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TupleTest {

  private Tuple1<String> t1;
  private Tuple2<String, String> t2;
  private Tuple3<String, String, String> t3;
  private Tuple4<String, String, String, String> t4;
  private Tuple5<String, String, String, String, String> t5;
  private Tuple6<String, String, String, String, String, String> t6;
  private Tuple7<String, String, String, String, String, String, String> t7;
  private Tuple8<String, String, String, String, String, String, String, String> t8;

  private Tuple[] tupleArr;

  @Before
  public void setup() {
    t1 = Tuple.of("A");
    t2 = Tuple.of("A", "B");
    t3 = Tuple.of("A", "B", "C");
    t4 = Tuple.of("A", "B", "C", "D");
    t5 = Tuple.of("A", "B", "C", "D", "E");
    t6 = Tuple.of("A", "B", "C", "D", "E", "F");
    t7 = Tuple.of("A", "B", "C", "D", "E", "F", "G");
    t8 = Tuple.of("A", "B", "C", "D", "E", "F", "G", "H");

    tupleArr = new Tuple[]{t1, t2, t3, t4, t5, t6, t7, t8};
  }

  @Test
  public void testConstructionSettingFields() {
    assertEquals("A", t1._1);

    assertEquals("A", t2._1);
    assertEquals("B", t2._2);

    assertEquals("A", t3._1);
    assertEquals("B", t3._2);
    assertEquals("C", t3._3);

    assertEquals("A", t4._1);
    assertEquals("B", t4._2);
    assertEquals("C", t4._3);
    assertEquals("D", t4._4);

    assertEquals("A", t5._1);
    assertEquals("B", t5._2);
    assertEquals("C", t5._3);
    assertEquals("D", t5._4);
    assertEquals("E", t5._5);

    assertEquals("A", t6._1);
    assertEquals("B", t6._2);
    assertEquals("C", t6._3);
    assertEquals("D", t6._4);
    assertEquals("E", t6._5);
    assertEquals("F", t6._6);

    assertEquals("A", t7._1);
    assertEquals("B", t7._2);
    assertEquals("C", t7._3);
    assertEquals("D", t7._4);
    assertEquals("E", t7._5);
    assertEquals("F", t7._6);
    assertEquals("G", t7._7);

    assertEquals("A", t8._1);
    assertEquals("B", t8._2);
    assertEquals("C", t8._3);
    assertEquals("D", t8._4);
    assertEquals("E", t8._5);
    assertEquals("F", t8._6);
    assertEquals("G", t8._7);
    assertEquals("H", t8._8);
  }

  @Test
  public void testToStringAndToArray() {
    assertEquals("(A)", t1.toString());
    assertEquals("(A,B)", t2.toString());
    assertEquals("(A,B,C)", t3.toString());
    assertEquals("(A,B,C,D)", t4.toString());
    assertEquals("(A,B,C,D,E)", t5.toString());
    assertEquals("(A,B,C,D,E,F)", t6.toString());
    assertEquals("(A,B,C,D,E,F,G)", t7.toString());
    assertEquals("(A,B,C,D,E,F,G,H)", t8.toString());

    String[][] arrs = {
        {"A"},
        {"A","B"},
        {"A","B","C"},
        {"A","B","C","D"},
        {"A","B","C","D","E"},
        {"A","B","C","D","E","F"},
        {"A","B","C","D","E","F","G"},
        {"A","B","C","D","E","F","G","H"}
    };

    for (int i = 0; i < arrs.length; i++) {
      assertArrayEquals(tupleArr[i].vals, tupleArr[i].toArray());
      assertEquals(tupleArr[i].vals.length, tupleArr[i].size());
      assertArrayEquals(arrs[i], tupleArr[i].toArray());
    }
  }

  @Test
  public void testEqualsAndHashcode() {
    for(Tuple t : tupleArr) {
      assertTrue(t.equals(t));
      assertFalse(t.equals(null));
      assertTrue(t.equals(t.clone()));
      assertEquals(t.hashCode(), t.clone().hashCode());
    }

    for(Tuple t1 : tupleArr) {
      for(Tuple t2 : tupleArr) {
        if(t1 == t2) {
          continue;
        }
        assertFalse(t1.equals(t2));
        assertFalse(t2.equals(t1));
      }
    }
  }
}
