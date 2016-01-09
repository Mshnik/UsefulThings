package graph.matching;

import org.junit.Test;

import java.util.*;

import static common.JUnitUtil.*;

/**
 * @author Mshnik
 */
public class AgentTest {

  @Test
  public void testAgent() {
    Set<Integer> s = new HashSet<>(Arrays.asList(1, 2, 3));
    Agent<Integer> a = Agent.create(s);
    assertEquals(s, a.getAcceptableItems());
    assertTrue(a.isAcceptable(1));
    assertTrue(a.isAcceptable(2));
    assertTrue(a.isAcceptable(3));
    assertFalse(a.isAcceptable(4));
  }

  @Test
  public void testRankedAgent() {
    Map<Character, Integer> m = new HashMap<>();
    for(int i = 0; i < 3; i++) {
      m.put((char) ('A' + i), 3 - i);
    }
    m.put('D', 1);

    RankedAgent<Character> a = RankedAgent.create(m);
    assertEquals(m, a.getPreferences());
    assertEquals(m.keySet(), a.getAcceptableItems());

    assertTrue(a.isAcceptable('A'));
    assertTrue(a.isAcceptable('B'));
    assertTrue(a.isAcceptable('C'));
    assertFalse(a.isAcceptable('X'));

    assertTrue(a.prefers('A', 'B'));
    assertFalse(a.prefers('B', 'A'));
    assertTrue(a.prefers('D', 'X'));
    assertFalse(a.prefers('C', 'D'));
    assertFalse(a.prefers('X', 'Y'));

    assertTrue(a.prefersWeakly('A', 'B'));
    assertFalse(a.prefersWeakly('B', 'A'));
    assertTrue(a.prefersWeakly('D', 'X'));
    assertTrue(a.prefersWeakly('C', 'D'));
    assertTrue(a.prefersWeakly('X', 'Y'));
  }

  @Test
  public void testStrictlyRankedAgent() {
    List<Character> lst = Arrays.asList('A', 'B', 'C');
    Map<Character, Integer> m = new HashMap<>();
    for(int i = 0; i < 3; i++) {
      m.put((char) ('A' + i), 3 - i);
    }

    StrictlyRankedAgent<Character> a = StrictlyRankedAgent.create(lst);
    assertEquals(lst, a.getStrictPreferences());
    assertEquals(m, a.getPreferences());
    assertEquals(new HashSet<>(lst), a.getAcceptableItems());

    assertTrue(a.isAcceptable('A'));
    assertTrue(a.isAcceptable('B'));
    assertTrue(a.isAcceptable('C'));
    assertFalse(a.isAcceptable('X'));

    assertTrue(a.prefers('A', 'B'));
    assertFalse(a.prefers('B', 'A'));
    assertTrue(a.prefers('C', 'X'));
    assertFalse(a.prefers('X', 'Y'));

    assertTrue(a.prefersWeakly('A', 'B'));
    assertFalse(a.prefersWeakly('B', 'A'));
    assertTrue(a.prefersWeakly('C', 'X'));
    assertTrue(a.prefersWeakly('X', 'Y'));
  }
}
