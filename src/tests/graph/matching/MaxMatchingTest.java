package graph.matching;

import graph.Algorithm;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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
  public void testMinMaxMatching() {
    TestAgent alice = new TestAgent("alice").withPref("A").withPref("B");
    TestAgent bob = new TestAgent("bob").withPref("B").withPref("C");
    TestAgent charlie = new TestAgent("charlie").withPref("C");

    Matching<TestAgent, String> m = Algorithm.maxMatching(new HashSet<>(Arrays.asList(alice, bob, charlie)),
                                                          new HashSet<>(Arrays.asList("A","B","C")));
    assertEquals(3, m.size());
    assertEquals("A", m.getMatchedB(alice));
    assertEquals("B", m.getMatchedB(bob));
    assertEquals("C", m.getMatchedB(charlie));

    Matching<TestAgent, String> m2 = Algorithm.maxMatching(new HashSet<>(Arrays.asList(alice, bob, charlie)),
        new HashSet<>(Arrays.asList("A","B","D")));

    assertEquals(2, m2.size());
    assertEquals("A", m2.getMatchedB(alice));
    assertEquals("B", m2.getMatchedB(bob));
    assertTrue(m2.isUnmatched(charlie));

    charlie.withPref("B");
    Matching<TestAgent, String> m3 = Algorithm.maxMatching(new HashSet<>(Arrays.asList(alice, bob, charlie)),
        new HashSet<>(Arrays.asList("A","B","D")));
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
}
