package com.tbf;

/**
 * A node for a linked list that holds any type T
 *
 * @param <T>
 */

public class Node<T> {
	
	private Node<T> next;
	private final T item;
	
	public Node(T item) {
		this.item = item;
		this.next = null;
	}

	public Node<T> getNext() {
		return this.next;
	}

	public void setNext(Node<T> next) {
		this.next = next;
	}

	public T getItem() {
		return this.item;
	}

}
