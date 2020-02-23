package com.micro.structure;

import java.util.HashMap;
import java.util.Map;

/**
 * 为创建单词树而编写该类
 * @author micro
 *
 */
public class WordTree {
//	i, like, sam, sung, samsung, mobile, ice, cream, man go
	private Map<Character, Node> roots = null;
	private static final int INIT_MAP_SIZE = 8;
	
	public WordTree() {
		this(null);
	}
	
	public WordTree(String []words) {
		if (words == null) { return; }
		roots = new HashMap<Character, Node>(words.length);
		for (String word : words) {
			this.add(word);
		}
	}
	
	public boolean isWordInDictionary(char []charArray) {
		if (charArray == null || charArray.length == 0) {
			return false;
		}
		if (this.roots == null || this.roots.size() == 0) { return false; }
		Node head = this.roots.get(charArray[0]);
		if (head == null) { return false; }
		
		for (int i = 1; i < charArray.length; ++i) {
			Node child = head.getChild(charArray[i]);
			if (child == null) {
				return false;
			}
			head = child;
		}
		return head.isLeaf();
	}
	
	public Map<Character, Node> getRoots() {
		return this.roots;
	}
	
	public void add(String word) {
		if (word == null || word.length() == 0) { return; }
		if (roots == null) {
			roots = new HashMap<Character, Node>(INIT_MAP_SIZE);
		}
		if (Character.isLetter(word.charAt(0))) {  // 如果第一个字符是字母猜测是单词
			word = word.toLowerCase();
		}
		Character key = word.charAt(0);
		Node node = roots.get(key);
		if (node == null) {
			node = new Node(key);
			roots.put(key, node);
		}
		add(node, word, 1);
	}
	
	private void add(Node node, String word, int charIndex) {
		if (charIndex == word.length()) {
			node.isLeaf = true;
			return;
		}
		Character key = word.charAt(charIndex);
		Node node2 = null;
		if (node.children == null) {
			node.children = new HashMap<Character, Node>();
			node2 = new Node(word, charIndex);
			node.children.put(key, node2);
		} else {
			node2 = node.children.get(key);
		}
		if (node2 == null) {
			node2 = new Node(word, charIndex);
			node.children.put(key, node2);
		}
		add(node2, word, ++charIndex);
	}
	
	class Node {
		private Character value;
		private Map<Character, Node> children = null;
		private boolean isLeaf = false;
		
		public Node(String word, int charIndex) {
			this.value = word.charAt(charIndex);
		}
		
		public Node(Character value) {
			this.value = value;
		}
		
		public boolean isLeaf() {
//			return children == null || children.isEmpty();
			return isLeaf;
		}
		
		public Character getValue() {
			return this.value;
		}
		
		public Map<Character, Node> getChildren() {
			return children;
		}
		
		public Node getChild(Character key) {
			if (children == null) { return null; }
			return children.get(key);
		}
	}

}
