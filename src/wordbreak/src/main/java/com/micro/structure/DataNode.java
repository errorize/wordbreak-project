package com.micro.structure;

import java.util.Map;

public class DataNode<T2> {
	private DataNode<T2> parent;
	
	private T2 value;
	private Map<T2, DataNode<T2>> children;
	public int startIndex = 0;
	public int wordCount = 0;
	
	public DataNode() { }
	
	public DataNode(T2 value) {
		this.value = value;
	}
	
	public boolean isLeaf() {
		return children == null || children.isEmpty();
	}

	public T2 getValue() {
		return value;
	}

	public void setValue(T2 value) {
		this.value = value;
	}

	public Map<T2, DataNode<T2>> getChildren() {
		return children;
	}

	public void setChildren(Map<T2, DataNode<T2>> children) {
		this.children = children;
	}
	
	public DataNode<T2> getParent() {
		return parent;
	}

	public void setParent(DataNode<T2> parent) {
		this.parent = parent;
	}

}
