package graph;

import graph.matching.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import common.Util;
import common.types.Tuple2;
import common.dataStructures.ConsList;
import common.dataStructures.NotInCollectionException;
import common.dataStructures.UnionFind;

/**
 * Holder class for various algorithms for graphs and matching
 **/
public class Algorithm {

  /**
   * Prevent construction on class Algorithm
   */
  private Algorithm() {
  }

  /**
   * Returns the sum of the weights for the given path in the given graph.
   *
   * @return - 0 if path == null or path.size() < 2, the sum of the edge weights otherwise
   * @throws an NotInCollectionException if the path makes an illegal jump -
   * there is no such edge to travel
   */
  public static <V, E extends Weighted> int sumPathWeight(Graph<V, E> g, LinkedList<V> path)
      throws NotInCollectionException {
    if (path == null || path.size() < 2) return 0;

    int s = 0;
    Iterator<V> one = path.iterator();
    Iterator<V> two = path.iterator();
    two.next(); //Advance two forward one.
    while (two.hasNext()) {
      V v1 = one.next();
      V v2 = two.next();
      E e = g.getConnection(v1, v2);
      if (e == null)
        throw new NotInCollectionException("Can't get cost of " + v1 + " to " + v2, e);
      s += e.getWeight();
    }

    return s;
  }

  /**
   * Returns a new directed graph that is a copy of g
   * If g is directed, merely clones g and returns that clone.
   * If g is undirected, creates a new directed graph that is a copy of g.
   * Each undirected edge in g is copied to two directed edges. Self edges
   * are only copied once to avoid redundancy.
   */
  public static <V, E extends Copyable<E>> Graph<V, E> makeDirectedGraph(Graph<V, E> g) {
    if (g.isDirected()) return g.clone();

    Graph<V, E> g2 = new Graph<V, E>(true);
    for (V v : g.vertexSet()) {
      g2.addVertex(v);
    }
    for (E e : g.edgeSet()) {
      if (g.isSelfEdge(e)) {
        g2.addEdge(g.sourceOf(e), g.sinkOf(e), e);
      } else {
        g2.addEdge(g.sourceOf(e), g.sinkOf(e), e);
        E e2 = e.copy();
        g2.addEdge(g.sinkOf(e), g.sourceOf(e), e2);
      }
    }
    return g2;
  }

  /**
   * Attempts to find the shortest path in g from start to goal
   *
   * @param start - a vertex in g, the start of the path
   * @param goal  - a vertex in g, the end of the path
   * @return - the path as a list, where return[0] is start and return[last] is goal.
   * returns null if start or goal isn't in g, or there is no such path.
   */
  public static <V, E extends Weighted> LinkedList<V> dijkstra(Graph<V, E> g, V start, V goal)
      throws RuntimeException, NotInCollectionException {
    if (!g.containsVertex(start) || !g.containsVertex(goal))
      throw new NotInCollectionException("Can't tun dijkstra's algorithm", start, goal);
    for (E e : g.edgeSet()) {
      if (e.getWeight() <= 0)
        throw new RuntimeException("Can't run dijkstra's algorithm on graph with non-positive weights");
    }

    final HashMap<V, Integer> distance = new HashMap<>();
    Comparator<V> distanceComparator = new Comparator<V>() {
      @Override
      public int compare(V o1, V o2) {
        return distance.get(o1) - distance.get(o2);
      }
    };

    final HashMap<V, V> previous = new HashMap<>();

    //Initalize step
    for (V v : g.vertexSet()) {
      distance.put(v, Integer.MAX_VALUE);
      previous.put(v, null);
    }

    distance.put(start, 0);
    HashSet<V> frontier = new HashSet<V>();

    frontier.add(start);

    //Iteration
    do {
      //Find next node (closest) and remove from frontier
      V next = Collections.min(frontier, distanceComparator);
      frontier.remove(next);
      //Found the goal - break out of loop
      if (next.equals(goal))
        break;

      //Look at each edge leaving next
      for (E e : g.edgeSetOfSource(next)) {
        V neighbor = g.getOther(e, next);
        //Relax edge - check if shortest path to neighbor is beaten by next -> neighbor.
        if (distance.get(neighbor) > distance.get(next) + e.getWeight()) {
          distance.put(neighbor, distance.get(next) + e.getWeight());
          previous.put(neighbor, next);
          if (!frontier.contains(neighbor))
            frontier.add(neighbor);
        }
      }

    } while (!frontier.isEmpty());

    //If the frontier is empty and goal distance is infinite, then the goal was never found.
    if (frontier.isEmpty() && distance.get(goal).equals(Integer.MAX_VALUE)) {
      return null;
    }

    //Assemble path
    LinkedList<V> path = new LinkedList<V>();
    V v = goal;
    do {
      path.push(v);
      v = previous.get(v);
    } while (v != null);

    return path;
  }

  /**
   * Returns an arbitrary cycle in the graph. If none are present,
   * returns null. This method is well slower than the isDAG method,
   * so if cycle detection (instead of identity) is desired and the graph
   * is directed, use that method.
   */
  public static <V, E> List<E> getCycle(Graph<V, E> g) {
    //If no edges, return null.
    if (g.edgeSize() == 0) return null;

    //Check for self loops
    for (E e : g.edgeSet()) {
      if (g.isSelfEdge(e)) {
        List<E> lst = new ArrayList<E>();
        lst.add(e);
        return lst;
      }
    }

    //No self loops at this point
    if (g.isDirected()) {  //Use DFS method to detect and find

      HashSet<V> visited = new HashSet<V>();
      HashMap<V, E> prev = new HashMap<V, E>();

      ArrayList<V> toTry = new ArrayList<V>(g.vertexSet());
      while (!toTry.isEmpty()) {
        LinkedList<V> queue = new LinkedList<V>();
        queue.add(Util.randomElement(toTry));

        while (!queue.isEmpty()) {
          V current = queue.poll();
          //Base case - we've already been here. Assemble and return
          if (visited.contains(current)) {
            LinkedList<E> cycle = new LinkedList<E>();

            //Start one earlier to prevent off by one edge err
            current = g.getOther(prev.get(current), current);

            E edge = prev.get(current);
            V next = g.sourceOf(edge);
            while (!cycle.contains(edge)) {
              cycle.push(edge);
              edge = prev.get(next);
              next = g.sourceOf(edge);
            }
            return cycle;
          }

          visited.add(current);

          for (E travel : g.edgeSetOfSource(current)) {
            V next = g.sinkOf(travel);
            queue.add(next);
            prev.put(next, travel);
          }
        }

        //This DFS didn't work. Remove all the things we looked at.
        toTry.removeAll(visited);
        visited.clear();
        prev.clear();
      }

      //No cycle found
      return null;
    } else {            //Use Union-Find to detect, helper to find
      UnionFind<V> uf = new UnionFind<V>(g.vertexSet());

      for (E e : g.edgeSet()) {
        V v1 = g.sourceOf(e);
        V v2 = g.sinkOf(e);

        //Base case - v1 and v2 are already connected
        if (uf.find(v1).equals(uf.find(v2))) {
          return getCycleHelper(g, v1, v1, new ConsList<E>());
        }

        uf.union(v1, v2);
      }

      //No cycle found
      return null;
    }
  }

  /**
   * Returns a cycle by iteratively DFS-ing, at the given start node.
   * This method only works once start is guaranteed to be in at least one cycle,
   * and there are no self-edges.
   */
  private static <V, E> List<E> getCycleHelper(Graph<V, E> g, V start, V current, ConsList<E> path) {
    //Base case - cycle found. Reverse path and return
    if (start == current && path.size() != 0) {
      List<E> lst = new ArrayList<E>();
      for (E e : path.reverse()) lst.add(e);
      return lst;
    }

    //Non-base case. Try each out edge in current.
    for (E e : g.edgeSetOfSource(current)) {
      //If it's not in the path already, try going that way
      if (!path.contains(e)) {
        List<E> cycle = getCycleHelper(g, start, g.getOther(e, current), path.cons(e));
        if (cycle != null) return cycle;
      }
    }
    //No cycle found. This case ends
    return null;
  }

  /**
   * Returns true if the given graph is a DAG, using a topological sort
   */
  public static <V, E> boolean isDAG(Graph<V, E> g) {
    if (!g.isDirected()) return false;

    final Graph<V, E> g2 = g.clone(); //create modifiable copy

    Comparator<V> minDegreeComparator = new Comparator<V>() {
      public int compare(V o1, V o2) {
        return g2.inDegreeOf(o1) - g2.inDegreeOf(o2);
      }
    };

    ArrayList<V> arrLst = new ArrayList<V>();
    arrLst.addAll(g2.vertexSet());

    while (g2.vertexSize() > 0) {
      V minV = Collections.min(arrLst, minDegreeComparator);

      //If minimum inDegree has in degree > 0, there is a cycle.
      if (g2.inDegreeOf(minV) > 0) return false;

      //minimum inDegree is 0 - remove from arrayList and from actual graph
      arrLst.remove(minV);
      g2.removeVertex(minV);
    }
    return true;
  }

  /**
   * Returns true if the given graph is Bipartite
   */
  public static <V, E> boolean isBipartite(Graph<V, E> g) {
    HashSet<V> sideA = new HashSet<V>();
    HashSet<V> sideB = new HashSet<V>();

    HashSet<V> allVertices = new HashSet<V>(g.vertexSet());

    LinkedList<V> queue = new LinkedList<V>();

    while (!allVertices.isEmpty()) {

      queue.clear();
      sideA.clear();
      sideB.clear();
      queue.add(Util.randomElement(allVertices));
      sideA.add(queue.getFirst());

      while (!queue.isEmpty()) {
        V v = queue.poll();
        allVertices.remove(v);

        HashSet<V> side;
        HashSet<V> otherSide;
        if (sideA.contains(v)) {
          side = sideA;
          otherSide = sideB;
        } else {
          side = sideB;
          otherSide = sideA;
        }

        for (V neighbor : g.neighborsOf(v)) {
          if (side.contains(neighbor)) {
            return false;
          }
          if (!otherSide.contains(neighbor)) {
            queue.addFirst(neighbor);
            otherSide.add(neighbor);
          }
        }
      }
    }
    return true;
  }

  /**
   * An instance represents a flow on a graph with edge type E
   * The first value is the total flow, the second is a map of each
   * edge in the graph to the amount of flow on that edge
   *
   * @author Mshnik
   */
  public static class Flow<E> extends Tuple2<Integer, HashMap<E, Integer>> {
    public Flow(Integer first, HashMap<E, Integer> second) {
      super(first, second);
    }
  }

  /**
   * An instance is created to perform a maxflow calculation.
   * This simplifies passing around the various hashMaps used in the calculation
   */
  private static class MaxFlow<V, E extends Flowable> {
    private HashMap<V, Integer> label;
    private HashMap<E, Integer> flow;
    private HashMap<V, Integer> excess;
    private final V source;
    private final V sink;
    private final Graph<V, E> g;
    private final Flow<E> flowObj;

    public MaxFlow(Graph<V, E> g, V source, V sink) {
      this.g = g;
      this.source = source;
      this.sink = sink;

      label = new HashMap<V, Integer>();
      excess = new HashMap<V, Integer>();
      flow = new HashMap<E, Integer>();

      for (V v : g.vertexSet()) {
        label.put(v, 0);
        excess.put(v, 0);
        for (V v2 : g.vertexSet()) {
          E e = g.getConnection(v, v2);
          if (e != null) {
            flow.put(e, 0);
          }
        }
      }

      //Label for start node is number of nodes
      label.put(source, g.vertexSize());

      //Assemble preflows for edges leaving source
      for (E e : g.edgeSetOfSource(source)) {
        if (!g.isSelfEdge(e)) {
          V edgeEnd = g.getOther(e, source);
          excess.put(source, excess.get(source) - e.getCapacity());
          excess.put(edgeEnd, excess.get(edgeEnd) + e.getCapacity());
          flow.put(e, e.getCapacity());
        }
      }

      //Main loop. Continue while an operation occurred
      boolean opOccured = false;
      do {
        opOccured = false;
        for (V v : g.vertexSet()) {
          for (E e : g.edgeSetOfSource(v)) {
            opOccured = push(v, e, true) | opOccured;
          }
          for (E e : g.edgeSetOfSink(v)) {
            opOccured = push(v, e, false) | opOccured;
          }
          if (!opOccured) opOccured = relabel(v) | opOccured;
        }
      } while (opOccured);

      flowObj = new Flow<E>(excess.get(sink), flow);

    }

    /**
     * Helper method for the maxFlow calculation.
     */
    private boolean push(V u, E e, boolean forward) {
      V v = g.getOther(e, u);
      if (excess.get(u) <= 0 || label.get(u) != label.get(v) + 1)
        return false;
      int mult = (forward ? 1 : -1);
      int delta = Math.min(excess.get(u), e.getCapacity() - flow.get(e));
      flow.put(e, flow.get(e) + mult * delta);
      excess.put(u, excess.get(u) - mult * delta);
      excess.put(v, excess.get(v) + mult * delta);
      return delta != 0;
    }

    /**
     * Helper method for the maxFlow calculation.
     */
    private boolean relabel(V u) {
      if (excess.get(u) <= 0 || u == source || u == sink)
        return false;
      int minVal = Integer.MAX_VALUE;
      for (V v : g.vertexSet()) {
        E e = g.getConnection(u, v);
        if (e != null && flow.get(e) < e.getCapacity()) {
          if (label.get(u) > label.get(v))
            return false;
          minVal = Math.min(minVal, label.get(v));
        }
      }
      if (minVal == Integer.MAX_VALUE) return false;
      int oldVal = label.get(u);
      label.put(u, minVal + 1);
      return oldVal != minVal + 1;
    }

    private Flow<E> computeMaxFlow() {
      return flowObj;
    }
  }

  /**
   * Returns the maximum flow possible on graph g.
   * Uses the preflow push algorithm to compute the max flow.
   * Only valid on directed graphs
   *
   * @param g      - the graph to find the flow on
   * @param source - the vertex to treat as the source of all flow
   * @param sink   - the vertex to treat as the sink of all flow
   * @return - a flow object, contianing the value of the max flow and the placement of all flow
   * @throws IllegalArgumentException if source or sink is null, or if they are the same node, or if undirected
   */
  public static <V, E extends Flowable> Flow<E> maxFlow(Graph<V, E> g, V source, V sink)
      throws IllegalArgumentException {
    if (source == null || sink == null || source.equals(sink))
      throw new IllegalArgumentException("Source and Sink must be non-null and distinct");
    if (!g.isDirected()) {
      throw new IllegalArgumentException("Can only compute maxflow on directed graphs");
    }

    return new MaxFlow<V, E>(g, source, sink).computeMaxFlow();
  }

  /**
   * Runs the SerialDictatorship algorithm on the given agents and items.
   * Uses a random ordering over the agents for the priority order.
   *
   * @param agents - the set of agents. Each A must have a preference order over items.
   * @param items  - the items. No preferences for items are taken into account.
   * @return - a Matching of agents and items.
   */
  public static <A extends StrictAgent<I>, I> Matching<A, I> serialDictator(Set<A> agents, Set<I> items) {
    List<A> ordering = new ArrayList<A>(agents);
    Collections.shuffle(ordering);
    return serialDictator(ordering, items);
  }

  /**
   * Runs the SerialDictatorship algorithm on the given agents and items.
   *
   * @param agents - the list of agents. Each A must have a preference order over items.
   *               The order that the agents appear in the list is the preference order over agents
   * @param items  - the items. No preferences for items are taken into account.
   * @return - a Matching of agents and items.
   * @throws IllegalArgumentException if any agent appears in the list twice.
   */
  public static <A extends StrictAgent<I>, I> Matching<A, I> serialDictator(List<A> agents, Set<I> items)
      throws IllegalArgumentException {
    //Make sure agent doesn't appear twice in ordering
    HashSet<A> agentsSet = new HashSet<A>(agents);
    if (agents.size() != agentsSet.size())
      throw new IllegalArgumentException("Can't use ordering " + agents + " over agents " + agentsSet);

    Matching<A, I> matching = new Matching<A, I>();
    matching.addAllA(agents);
    matching.addAllB(items);
    for (A agent : agents) {
      for (I item : agent.getRankPreferences()) {
        if (matching.isUnmatched(item)) {
          matching.match(agent, item);
          break;
        }
      }
    }
    return matching;
  }

  /**
   * Runs the ttc algorithm on the set of endowedAgents
   *
   * @param agents - the set of agents. The set of items to allocate is derived
   *               from the initialEndowment of each agent in agents.
   *               Each agent is expected to have preferences over the avaliable
   *               items as their getPreferences() return. Preferences on items
   *               that are unavailable are skipped.
   * @return a Matching of agents and items.
   * @throws RuntimeException if an agent exists who finds none of the avaliable items
   * acceptable at any step in the algorithm. If all agents have complete preferences
   * over the available items, this will never occur.
   */
  public static <A extends StrictAgent<I> & Endowed<I>, I> Matching<A, I> ttc(Set<A> agents)
      throws RuntimeException {
    Matching<A, I> matching = new Matching<A, I>();
    HashMap<I, A> reverseInitialEndowment = new HashMap<>();

    matching.addAllA(agents);
    Graph<A, Object> g = new Graph<A, Object>();
    for (A agent : agents) {
      g.addVertex(agent);
      matching.addB(agent.getInitialEndowment());
      reverseInitialEndowment.put(agent.getInitialEndowment(), agent);
    }

    HashMap<A, Integer> currentPreference = new HashMap<>();

    //Add initial edges - top choice among present items for each agent
    for (A agent : agents) {

      for (int i = 0; i < agent.getRankPreferences().length; i++) {
        if (matching.contains(agent.getRankPreferences()[i])) {
          currentPreference.put(agent, i);
          g.addEdge(agent, reverseInitialEndowment.get(agent.getRankPreferences()[i]), new Object());
          break;
        }
      }

      if (!currentPreference.containsKey(agent))
        throw new RuntimeException("Can't run ttc on " + agents + "," + agent + " doesn't like any item");
    }

    while (g.vertexSize() > 0) {
      while (true) {
        List<Object> cycle = Algorithm.getCycle(g);
        if (cycle == null) break;

        //For each edge, match along the edge
        for (Object edge : cycle) {
          A taker = g.sourceOf(edge);
          A giver = g.sinkOf(edge);
          matching.match(taker, giver.getInitialEndowment());
        }
        //Remove cycle from graph
        for (Object edge : cycle) {
          g.removeVertex(g.sourceOf(edge));
          g.removeVertex(g.sinkOf(edge));
        }
      }

      //No cycles currently. Move everyone to their next choice in the graph
      for (A agent : agents) {
        if (matching.isMatched(agent)) continue;

        boolean foundItem = false;
        for (int i = currentPreference.get(agent); i < agent.getRankPreferences().length; i++) {
          I pref = agent.getRankPreferences()[i];
          if (!matching.isMatched(pref)) {
            currentPreference.put(agent, i);
            g.addEdge(agent, reverseInitialEndowment.get(pref), new Object());
            foundItem = true;
            break;
          }
        }

        if (!foundItem)
          throw new RuntimeException("Can't run ttc on " + agents + "," + agent + " doesn't like any item");
      }
    }
    return matching;
  }

  /** */
  public static <A extends StrictAgent<B>, B extends StrictAgent<A>> Matching<A, B> stableMarriage(Set<A> proposers, Set<B> proposees) {

    Matching<A, B> matching = new Matching<A, B>(proposers, proposees);

    HashMap<A, Integer> lowestProposal = new HashMap<A, Integer>();
    HashMap<A, Boolean> doneProposing = new HashMap<A, Boolean>();
    for (A a : proposers) {
      lowestProposal.put(a, -1); //Hasn't proposed to anyone yet
      doneProposing.put(a, false); //Not done yet
    }

    while (true) {
      Set<A> unmatchedProposers = matching.getUnmatchedA();

      //Base case - everyone left unmatched doesn't find anyone acceptable
      //Also true if there are no unmatched people
      boolean unmatchedAreHappier = true;
      for (A a : unmatchedProposers) {
        unmatchedAreHappier = unmatchedAreHappier && doneProposing.get(a);
      }
      if (unmatchedAreHappier) {
        return matching;
      }

      //Otherwise, for each proposer, go through and propose to his lowest proposal + 1
      for (A a : unmatchedProposers) {
        int nextProposal = lowestProposal.get(a);

        boolean proposing = true;

        //Find next actual existing woman
        do {
          nextProposal++;
          if (nextProposal == a.getRankPreferences().length) {
            proposing = false;
            doneProposing.put(a, true);
          }
        } while (proposing && !proposees.contains(a.getRankPreferences()[nextProposal]));

        //If not proposing, just do nothing - no good proposees left to propose to
        if (proposing) {
          B b = a.getRankPreferences()[nextProposal];

          if (matching.isUnmatched(b) && Agent.isAcceptable(b, a)
              || Agent.prefers(b, a, matching.getMatchedA(b))) {
            matching.match(a, b);
          }
        }
      }
    }
  }

}
