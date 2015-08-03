package common.dataStructures;

import static org.junit.Assert.*;
import org.junit.Test;

public class TrieTest {

	@Test
	public void testAdd() {
		Trie t = new Trie();
		
		Trie.TrieNode root = t.getRoot();
		assertEquals(null, root.c);
		assertEquals(false, root.isTerminatingNode);
		assertEquals(null, root.parent);
		assertTrue(root.children.isEmpty());
		
		boolean added = t.add("A");
		assertEquals(null, root.c);
		assertEquals(false, root.isTerminatingNode);
		assertEquals(null, root.parent);
		assertEquals(1, root.children.size());
		assertTrue(added);
	
		Trie.TrieNode aNode = root.children.get('A');
		assertEquals('A', aNode.c.charValue());
		assertEquals(true, aNode.isTerminatingNode);
		assertEquals(root, aNode.parent);
		assertTrue(aNode.children.isEmpty());
		
		added = t.add("ABC");
		Trie.TrieNode bNode = aNode.children.get('B');
		Trie.TrieNode cNode = bNode.children.get('C');
		assertTrue(added);
		
		assertEquals(null, root.c);
		assertEquals(false, root.isTerminatingNode);
		assertEquals(null, root.parent);
		assertEquals('A', aNode.c.charValue());
		assertEquals(true, aNode.isTerminatingNode);
		assertEquals(root, aNode.parent);
		assertEquals(1, aNode.children.size());
		assertEquals('B', bNode.c.charValue());
		assertEquals(false, bNode.isTerminatingNode);
		assertEquals(aNode, bNode.parent);
		assertEquals(1, bNode.children.size());
		assertEquals('C', cNode.c.charValue());
		assertEquals(true, cNode.isTerminatingNode);
		assertEquals(bNode, cNode.parent);
		assertTrue(cNode.children.isEmpty());
	}
	
	@Test
	public void testToListAndIterator() {
		Trie t = new Trie();
		assertTrue(t.toList().isEmpty());
		
		DeArrList<String> lst = new DeArrList<>();
		t.add("Hello");
		lst.add("Hello");
		assertEquals(lst, t.toList());
		
		t.add("Hi");
		lst.add("Hi");
		assertEquals(lst, t.toList());
		
		t.add("Hello!");
		lst.add(1, "Hello!");
		assertEquals(lst, t.toList());
		
		t.add("ZZZ");
		lst.add("ZZZ");
		assertEquals(lst, t.toList());
	}

}
