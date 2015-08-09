package common.dataStructures;

import java.util.Iterator;
import common.StringUtil;

/** A traditional Trie - stores Strings by breaking them down 
 * into lists of characters.
 * @author Mshnik
 */
public class Trie extends AbsTrie<String, Character>{

	@Override
	protected Iterator<Character> toSequence(String t) {
		return StringUtil.charIterator(t);
	}

	@Override
	protected String fromSequence(Iterator<Character> iter) {
		StringBuilder s = new StringBuilder();
		while(iter.hasNext()) {
			s.append(iter.next());
		}
		return s.toString();
	}
	
	@Override
	protected int compareC(Character c1, Character c2) {
		return c1.compareTo(c2);
	}

}
