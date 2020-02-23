package com.micro.structure;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SentenceTree<T> {
	private Map<T, DataNode<T>> roots;
	private int size = -1;  // 用于检验输出句子的合法性
	
	public SentenceTree() {
		
	}
	public SentenceTree(int size) {
		this.size = size;
	}
	
	public int getLength(T value) {
		if (value != null) {
			if (value instanceof String) {
				return ((String) value).length();
			} else if (value.getClass().isArray()) {
				return Array.getLength(value);
			}
			return String.valueOf(value).length();
		}
		return 0;
	}
	
	public DataNode<T> add(DataNode<T> parent, T value, int startIndex) {
		Map<T, DataNode<T>> children = parent.getChildren();
		DataNode<T> child = null;
		int wordCount = 0;
		if (value != null) {
			wordCount = getLength(value);
		}
		if (children == null) {
			children = new HashMap<T, DataNode<T>>();
			parent.setChildren(children);
			child = new DataNode<T>(value);
			child.startIndex = startIndex;
			child.wordCount += wordCount;
			child.setParent(parent);
			children.put(value, child);
		} else {
			child = children.get(value);
			if (child == null) {
				child = new DataNode<T>(value);
				child.startIndex = startIndex;
				child.wordCount += wordCount;
				child.setParent(parent);
				children.put(value, child);
			}
		}
		return child;
	}
	
	public DataNode<T> createRoot(T value) {
		DataNode<T> root = null;
		int wordCount = 0;
		if (value != null) {
			wordCount = getLength(value);
		}
		if (roots == null) {
			root = new DataNode<T>(value);
			root.startIndex = 0;
			root.wordCount = wordCount;
			roots = new HashMap<T, DataNode<T>>();
			roots.put(value, root);
		} else {
			root = roots.get(value);
			if (root == null) {
				root = new DataNode<T>(value);
				root.startIndex = 0;
				root.wordCount = wordCount;
				roots.put(value, root);
			}
		}
		
		return root;
	}
	
	public Map<T, DataNode<T>> getRoots() {
		return roots;
	}
	
	public List<String> list() {
		if (roots == null || roots.size() == 0) { return null; }
		List<String> strList = new LinkedList<String>();
		for (Map.Entry<T, DataNode<T>> entry : roots.entrySet()) {
			list(strList, entry.getValue());
		}
		return strList;
	}
	
	private void list(List<String> strList, DataNode<T> node) {
		if (node.isLeaf()) {
			DataNode<T> n = node;
			if (n.getValue() != null) {
				StringBuilder sb = new StringBuilder();
				boolean startFlag = true;
//				int wordCount = 0;
				do {
					T val = n.getValue();
					String value = String.valueOf(val);
					if (startFlag) {
						startFlag = false;
					} else {
						value += " ";
					}
					sb.insert(0, value);
					n = n.getParent();
				} while (n != null);
				if (this.size < 0) {
					strList.add(sb.toString());
				} else if (node.wordCount + node.startIndex == this.size) {
					strList.add(sb.toString());
				}
			}
		} else {
			Map<T, DataNode<T>> children = node.getChildren();
			for (Map.Entry<T, DataNode<T>> entry : children.entrySet()) {
				list(strList, entry.getValue());
			}
		}
	}
	
	
}
