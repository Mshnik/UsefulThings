package common.dataStructures;


import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static common.JUnitUtil.*;

public class StatsIteratorTest {


  @Test
  public void testIterator() {
    List<Double> lst = Arrays.asList(0.0, 1.0, 2.0, 3.0, 4.0, 5.0);
    StatsIterator<Double> s = new StatsIterator<>(lst.iterator());
    for(int i = 0; i < lst.size(); i++) {
      assertTrue(s.hasNext());
      assertEquals(i, s.next().intValue());
    }
    assertFalse(s.hasNext());
  }

  @Test
  public void testSumAndAverageStats() {
    List<Double> lst = Arrays.asList(0.0, 1.0, 2.0, 3.0, 4.0, 5.0);
    StatsIterator<Double> s = new StatsIterator<>(lst.iterator()).withSumStat().withAverageStat();
    double sum = 0;
    for(int i = 0; i < lst.size(); i++) {
      assertTrue(s.hasNext());
      sum += s.next();
      assertEquals((int)sum, ((Double)s.getStat(StatsIterator.SUM_KEY)).intValue());
      assertEquals((int) (sum / (i+1) * 1000), (int) (1000 * (double)s.getStat(StatsIterator.AVERAGE_KEY)));
    }
    assertFalse(s.hasNext());
  }

  @Test
  public void testSumAndAverageStatsWithWindow() {
    List<Double> lst = Arrays.asList(0.0, 1.0, 2.0, 3.0, 4.0, 5.0);
    StatsIterator<Double> s = new StatsIterator<>(lst.iterator(), 1).withSumStat().withAverageStat();
    for(int i = 0; i < lst.size(); i++) {
      assertTrue(s.hasNext());
      s.next();
      assertEquals(i, ((Double) s.getStat(StatsIterator.SUM_KEY)).intValue());
      assertEquals(i, ((Double)s.getStat(StatsIterator.AVERAGE_KEY)).intValue());
    }
    assertFalse(s.hasNext());
  }

  @Test
  public void testCustomStat() {
    List<Double> lst = Arrays.asList(0.0, 1.0, 2.0, 3.0, 4.0, 5.0);
    StatsIterator<Double> s = new StatsIterator<>(lst.iterator())
        .withStat("STRING_COMP", "", (state) -> state, (d, state) -> state + d, (d, state) -> state.substring(d.toString().length()));

    String sum = "";
    for(int i = 0; i < lst.size(); i++) {
      assertTrue(s.hasNext());
      sum += s.next();
      assertEquals(sum, s.getStat("STRING_COMP"));
    }
    assertFalse(s.hasNext());
  }
}
