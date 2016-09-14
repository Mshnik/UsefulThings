package graph.matching;

import graph.Algorithm;
import org.junit.Test;

import java.util.*;

import static common.JUnitUtil.*;

/**
 * @author Mshnik
 */
public class MaxMatchingTest  {

  private static class TestAgent implements RankedAgent<String> {

    private final String name;
    private Map<String, Integer> prefs;

    private TestAgent(String name){
      this.name= name;
      prefs = new HashMap<>();
    }

    private TestAgent withPref(String item) {
      prefs.put(item, 0);
      return this;
    }

    private TestAgent withPref(String item, int pref) {
      prefs.put(item, pref);
      return this;
    }

    public Map<String, Integer> getPreferences() {
      return prefs;
    }

    public String toString() {
      return name;
    }
  }

  @Test
  public void testMaxMatching() {
    TestAgent alice = new TestAgent("alice").withPref("A").withPref("B");
    TestAgent bob = new TestAgent("bob").withPref("B").withPref("C");
    TestAgent charlie = new TestAgent("charlie").withPref("C");

    Matching<TestAgent, String> m = Algorithm.maxMatching(Arrays.asList(alice, bob, charlie),
                                                          Arrays.asList("A","B","C"));
    assertEquals(3, m.size());
    assertEquals("A", m.getMatchedB(alice));
    assertEquals("B", m.getMatchedB(bob));
    assertEquals("C", m.getMatchedB(charlie));

    Matching<TestAgent, String> m2 = Algorithm.maxMatching(Arrays.asList(alice, bob, charlie),
        Arrays.asList("A","B","D"));

    assertEquals(2, m2.size());
    assertEquals("A", m2.getMatchedB(alice));
    assertEquals("B", m2.getMatchedB(bob));
    assertTrue(m2.isUnmatched(charlie));

    charlie.withPref("B");
    Matching<TestAgent, String> m3 = Algorithm.maxMatching(Arrays.asList(alice, bob, charlie),
        Arrays.asList("A","B","D"));
    assertEquals(2, m2.size());
    assertEquals("A", m2.getMatchedB(alice));
    if (m2.isMatched(bob)) {
      assertEquals("B", m2.getMatchedB(bob));
      assertTrue(m2.isUnmatched(charlie));
    } else {
      assertEquals("B", m2.getMatchedB(charlie));
      assertTrue(m2.isUnmatched(bob));
    }
  }

  @Test
  public void testMaxValueMaxMatching() {
    TestAgent alice = new TestAgent("alice").withPref("A",2).withPref("B",1);
    TestAgent bob = new TestAgent("bob").withPref("B",2).withPref("C",1);
    TestAgent charlie = new TestAgent("charlie").withPref("C",2);
    Collection<TestAgent> agents = Arrays.asList(alice, bob, charlie);
    Collection<String> items = Arrays.asList("A","B","C");

    Matching<TestAgent, String> m = Algorithm.maxValueMaxMatching(agents,items,null);
    assertEquals(3, m.size());
    assertEquals("A", m.getMatchedB(alice));
    assertEquals("B", m.getMatchedB(bob));
    assertEquals("C", m.getMatchedB(charlie));

    //Check that changing values with only one max matching still gives that matching
    bob = bob.withPref("C",1000);
    Matching<TestAgent, String> m2 = Algorithm.maxValueMaxMatching(agents,items,null);
    assertEquals(3, m2.size());
    assertEquals("A", m2.getMatchedB(alice));
    assertEquals("B", m2.getMatchedB(bob));
    assertEquals("C", m2.getMatchedB(charlie));

    //Check that if two max matchings available, the better is chosen.
    bob = bob.withPref("C",1);
    charlie = charlie.withPref("B",1);
    Matching<TestAgent, String> m3 = Algorithm.maxValueMaxMatching(agents,items,null);
    assertEquals(3, m3.size());
    assertEquals("A", m3.getMatchedB(alice));
    assertEquals("B", m3.getMatchedB(bob));
    assertEquals("C", m3.getMatchedB(charlie));
  }
}
