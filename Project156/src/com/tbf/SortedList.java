package com.tbf;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * A sorted list that uses a linked list for its structure.
 * Parameterized to hold any type T
 *
 * @param <T>
 */

public class SortedList<T> implements Iterable<T>{

	private Node<T> head;
	private int size;
	private Comparator<T> comparator;
	
	public SortedList(Comparator<T> comparator) {
		this.comparator = comparator;
		this.head = null;
		this.size = 0;
	}

	/**
	 * Returns the size of the list, the number of elements currently stored in it.
	 */
	public int getSize() {
		return this.size;
	}
	
	/**
	 * Clears out the contents of the list, making it an empty list.
	 */
	public void clear() {
		while(this.head != null) {
			this.head = this.head.getNext();
			this.size--;
		}
	}
	
	/**
	 * Inserts a node with the given key into the list and maintains order by using a comparator
	 * to place the key into the right place.
	 * 
	 * @param key
	 */
	public void insert(T key) {
		Node<T> newNode = new Node<T>(key);
		Node<T> curr = this.head;
		Node<T> prev = null;
		while(curr != null && comparator.compare(newNode.getItem(), curr.getItem()) > 0) {
			prev = curr;
			curr = curr.getNext();
		}
		if(prev == null) {
			this.head = newNode;
		} else {
			prev.setNext(newNode);
		}
		newNode.setNext(curr);
		this.size++;
	}
	
	/**
	 * Given a generic list, inserts each element in the list into the sorted list
	 * 
	 * @param keys
	 */
	public void batchInsert(List<T> keys) {
		for(int i=0; i<keys.size(); i++) {
			insert(keys.get(i));
		}
	}

	/**
	 * This method removes the node from the given index, indices start at 0.
	 * Remaining elements' indices are reduced.
	 * 
	 * @param position
	 */
	public void removeAtIndex(int index) {
		if(this.size == 0) {
			throw new IllegalStateException("You cannot remove from an empty list");
		} else if(index == 0) {
			this.head = this.head.getNext();
			size--;
			return;
		} else if(index >= this.size) {
			throw new IndexOutOfBoundsException("Invalid Position Provided");
		}
		Node<T> prev = getNodeAtIndex(index-1);
		Node<T> curr = prev.getNext();
		prev.setNext(curr.getNext());
		this.size--;
	}
	
	/**
	 * Removes the node with the given value
	 * 
	 * @param key
	 */
	public void removeWithValue(T key) {
		if(this.size == 0) {
			throw new IllegalStateException("You cannot remove from an empty list");
		}
		Node<T> prev = null;
		Node<T> curr = this.head;
		while(curr != null && !curr.getItem().equals(key)) {
			prev = curr;
			curr = curr.getNext();
		}
		//removing first node
		if(prev == null) {
			this.head = this.head.getNext();
			size--;
			//removing last node
		} else if(curr.getNext() == null) {
			prev.setNext(null);
			size--;
		} else {
			prev.setNext(curr.getNext());
			size--;
		}
	}
	
	/**
	 * This is a private helper method that returns a corresponding node
	 * to the given index.
	 * 
	 * @param position
	 * @return
	 */
	private Node<T> getNodeAtIndex(int index) {
		if(index < 0 || index >= this.size) {
			throw new IndexOutOfBoundsException("Invalid Position Provided");
		}
		Node<T> curr = this.head;
		for(int i=0; i<index; i++) {
			curr = curr.getNext();
		}
		return curr;
	}
	
	/**
	 * Private helper method that returns a corresponding node to the
	 * given key
	 * 
	 * @param key
	 * @return
	 */
	private Node<T> getNodeWithValue(T key) {
		Node<T> curr = this.head;
		while(curr != null && !curr.getItem().equals(key)) {
			curr = curr.getNext();
		}
		return curr;
	}
	
	/**
	 * Returns the element stored at the given index.
	 * 
	 * @param position
	 * @return
	 */
	public T getItemAtIndex(int index) {
		return getNodeAtIndex(index).getItem();
	}
	
	/**
	 * Prints the list to the standard output.
	 */
	public void print() {
		for(T item : this) {
			System.out.println(item.toString());
		}
	}
	
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			Node<T> curr = head;
			@Override
			public boolean hasNext() {
				if(curr == null)
					return false;
				else
					return true;
			}
			@Override
			public T next() {
				T item = curr.getItem();
				curr = curr.getNext();
				return item;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("not implemented");
			}};	}
}
