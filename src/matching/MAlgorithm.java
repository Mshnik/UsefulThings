package matching;

import java.util.*;
public class MAlgorithm {

	/** Prevent construction on class MAlgorithm */
	private MAlgorithm(){}

	/** Runs the SerialDictatorship algorithm on the given agents and items.
	 * Uses a random ordering over the agents for the priority order.
	 * @param agents - the set of agents. Each A must have a preference order over items.
	 * @param items - the items. No preferences for items are taken into account.
	 * @return - a Matching of agents and items.
	 */
	public static <A extends Agent<I>, I> Matching<A,I> serialDictator(Set<A> agents, Set<I> items){
		List<A> ordering = new ArrayList<A>(agents);
		Collections.shuffle(ordering);
		return serialDictator(ordering, items);
	}

	/** Runs the SerialDictatorship algorithm on the given agents and items.
	 * @param agents - the list of agents. Each A must have a preference order over items.
	 * 						The order that the agents appear in the list is the preference order over agents
	 * @param items - the items. No preferences for items are taken into account.
	 * @return - a Matching of agents and items.
	 * @throws IllegalArgumentException if any agent appears in the list twice.
	 */
	public static <A extends Agent<I>, I> Matching<A,I> serialDictator(List<A> agents, Set<I> items)
			throws IllegalArgumentException {
		//Make sure agent doesn't appear twice in ordering
		HashSet<A> agentsSet = new HashSet<A>(agents);
		if(agents.size() != agentsSet.size())
			throw new IllegalArgumentException("Can't use ordering " + agents + " over agents " + agentsSet);

		Matching<A,I> matching = new Matching<A,I>();
		matching.addAllA(agents);
		matching.addAllB(items);
		for(A agent : agents){
			for(I item : agent.getPreferences()){
				if(matching.isUnmatched(item)){
					matching.match(agent, item);
					break;
				}
			}
		}
		return matching;
	}	
}
