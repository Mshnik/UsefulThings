package common.dataStructures;

import functional.impl.Function1;
import functional.impl.Function2;
import functional.util.FunctionState;

import java.util.HashMap;
import java.util.Iterator;

public class StatsIterator<T extends Number> implements Iterator<T> {

  private Iterator<T> underlyingIterator;
  private int windowSize;

  private DeArrList<T> data;
  private HashMap<String, FunctionState<T, ?>> stats;

  public StatsIterator(Iterator<T> underlyingIterator) {
    this(underlyingIterator, Integer.MAX_VALUE);
  }

  public StatsIterator(Iterator<T> underlyingIterator, int windowSize){
    this.underlyingIterator = underlyingIterator;
    data = new DeArrList<>();
    stats = new HashMap<>();
    setWindowSize(windowSize);
  }

  private void addData(T t){
    data.add(t);
    for(FunctionState<T, ?> f : stats.values()) {
      f.addData(t);
    }
  }

  private void removeData(){
    T t = data.removeFirst();
    for(FunctionState<T, ?> f : stats.values()) {
      f.removeData(t);
    }
  }

  public void setWindowSize(int newWindowSize) throws IllegalArgumentException{
    if(newWindowSize <= 0) {
      throw new IllegalArgumentException();
    }

    this.windowSize = newWindowSize;
    while(data.size() > windowSize) {
      removeData();
    }
  }

  @Override
  public boolean hasNext() {
    return underlyingIterator.hasNext();
  }

  @Override
  public T next() {
    T t = underlyingIterator.next();
    addData(t);
    if(data.size() > windowSize) {
      removeData();
    }
    return t;
  }

  public Object getStat(String statKey) {
    if (!stats.containsKey(statKey)) {
      throw new RuntimeException("StatsIterator " + this + " doesn't contain stat for key " + statKey);
    }
    return stats.get(statKey).getCurrentVal();
  }

  public <X> StatsIterator<T> withStat(String statKey, X initialVal, Function1<X, X> reportFunction,
                                   Function2<T, X, X> addDataFunction, Function2<T, X, X> removeDataFunction) {
    stats.put(statKey, new FunctionState<>(initialVal, reportFunction, addDataFunction, removeDataFunction));
    return this;
  }

  public static final String SUM_KEY = "SUM";

  public StatsIterator<T> withSumStat() {
    return withStat(SUM_KEY, 0.0, sum -> sum, (d, sum) -> sum + d.doubleValue(), (d, sum) -> sum - d.doubleValue());
  }

  public static final String AVERAGE_KEY = "AVERAGE";

  public StatsIterator<T> withAverageStat() {
    return withStat(AVERAGE_KEY, 0.0, (sum) -> sum/data.size(), (d, sum) -> sum + d.doubleValue(), (d, sum) -> sum - d.doubleValue());
  }
}
