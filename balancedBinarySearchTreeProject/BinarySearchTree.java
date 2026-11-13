/** @author Gaia Bertolino */
package Bertolino.bst;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class BinarySearchTree<T extends Comparable<? super T>> implements SortedCollection<T>{
	
	private static class Node<E>{
		E info;
		Node<E> left, right;
		int leftCount, rightCount;
	}//Node
	private Node<T> root=null;
	private Stack<Node<T>> unbalanced = new Stack<>();
	
	public int size() {
		return size(root);
	}//size
	private int size( Node<T> root ) {
		if( root==null ) return 0;
		return 1+size( root.left )+size( root.right );
	}//size
	
	public void clear() {
		root=null;
	}//clear
	
	public boolean contains( T x ) {
		return contains(root,x);
	}
	private boolean contains( Node<T> root, T e ) {
		if( root==null ) return false;
		if( root.info.equals(e) ) return true;
		if( root.info.compareTo(e)>0 ) return contains( root.left, e );
		return contains( root.right, e );
	}//contains
	
	public boolean isBalanced(Node<T> root){
		if(root== null)
			return true;
		return Math.abs(root.rightCount - root.leftCount) <= 1;
	}//isBalanced 

	private Node<T> insert(Node<T> root, List<T> list, int start, int end) {
		if (end<=start) {
			return root; 
		}
		int mid = (start+end)/2;
		root = new Node<T>();
		
		root.info = list.get(mid);

		root.right = insert(root.right, list, mid+1, end);
		root.left = insert(root.left, list, start, mid);
		
		if (root.right!= null)
			root.rightCount = 1 + root.right.rightCount + root.right.leftCount;
		else
			root.rightCount = 0;
		if (root.left!= null)
			root.leftCount = 1 + root.left.rightCount + root.left.leftCount;
		else
			root.leftCount = 0;
		
		return root;
	}//insert
	
	public void add( T x ) {
		unbalanced = new Stack<>();
		root=add(root,x);
		if (!unbalanced.isEmpty()) {
			List<T> list = new LinkedList<>();
			inOrder(list);
			root = unbalanced.pop();
			root.right = null;
			root.left = null;
			root.rightCount = 0;
			root.leftCount = 0;
			root = insert(root, list, 0, list.size());
		}
	}
	private Node<T> add( Node<T> root, T x ){
		if( root==null ) {
			Node<T> n=new Node<>();
			n.info=x; n.left=null; n.right=null;			
			return n;
		}
		if( root.info.compareTo(x)>0 ) {
			root.left=add( root.left, x );
			root.leftCount++;
			
			if (!isBalanced(root)) {
				unbalanced.push(root);
			}
			return root;
		}
		root.right=add( root.right, x );
		root.rightCount++;
		if (!isBalanced(root)) {
			unbalanced.push(root);
		}
		return root;
	}//add

	public void remove( T x) {
		unbalanced = new Stack<>();
		root=remove( root, x );
		if (!unbalanced.isEmpty()) {
			List<T> list = new LinkedList<>();
			inOrder(list);
			root = unbalanced.pop();
			root.right = null;
			root.left = null;
			root.rightCount = 0;
			root.leftCount = 0;
			root = insert(root, list, 0, list.size());
		}
	}
	private Node<T> remove( Node<T> root, T x ){
		if( root==null ) return null;
		if( root.info.compareTo(x)>0 ) {
			root.left=remove( root.left,x );
			root.leftCount--;
			if (!isBalanced(root)) {
				unbalanced.push(root);
			}
			return root;
		}
		if( root.info.compareTo(x)<0 ) {
			root.right=remove( root.right, x );
			root.rightCount--;
			if (!isBalanced(root)) {
				unbalanced.push(root);
			}
			return root;
		}
		if( root.left==null && root.right==null ) {
			return null;
		}
		if( root.left==null ) {
			return root.right;
		}
		if( root.right==null ) { 
			return root.left;
		}
		if( root.right.left==null ) {
			root.info=root.right.info;
			root.right=root.right.right; 
			return root;
		}
		Node<T> parent=root.right; 
		Node<T> child=parent.left;
		while( child.left!=null ) {
			parent=child; child=child.left;
		}
		root.info=child.info;
		parent.left=child.right; 
		root.rightCount--;
		return root;
	}//remove
	
	
	public void inOrder( List<T> list ) {
		inOrder( root, list );
	}
	private void inOrder( Node<T> root, List<T> list ) {
		if( root!=null ) {
			inOrder( root.left, list );
			list.add( root.info );
			inOrder( root.right, list ); 
		}
	}//inOrder
	
	public void preOrder( List<T> list ) {
		preOrder(root,list);
	}
	private void preOrder( Node<T> root, List<T> list ) {
		if( root!=null ) {
			list.add(root.info); 
			preOrder( root.left, list );
			preOrder( root.right, list );
		}
	}//preOrder
	
	public void postOrder( List<T> list ) {
		postOrder( root, list );
	}
	private void postOrder( Node<T> root, List<T> list ) {
		if( root!=null ) {
			postOrder( root.left, list );
			postOrder( root.right, list );
			list.add( root.info );
		}
	}//postOrder
	
	public T get( T x ) {
		return get( root,x );
	}
	private T get( Node<T> root, T e ) {
		if( root==null ) return null;
		if( root.info.equals(e) ) return root.info;
		if( root.info.compareTo(e)>0 ) return get( root.left, e );
		return get( root.right, e );
	}//get
	
	public boolean isEmpty() { return root==null; }
	public boolean isFull() { return false; }
	
	public Iterator<T> iterator(){ 
		return new BSTIterator(); 
	}//iterator()
	
	private class BSTIterator implements Iterator<T>{
		Stack<Node<T>> stack = new Stack<>();
		T current;
		
		public BSTIterator() {
			pushLeftSpine(root);
		}//BSTIterator
		
		private void pushLeftSpine(Node<T> root) {
			if (root == null)
				return;
			stack.add(root);
			pushLeftSpine(root.left);
		}//BSTIterator

		@Override
		public boolean hasNext() {
			if (stack.isEmpty())
				return false;
			return true;
		}//hasNext

		@Override
		public T next() {
			Node<T> next = stack.pop();
			pushLeftSpine(next.right);
			current = next.info;
			return current;
		}//next
		
		@Override
		public void remove() {
			BinarySearchTree.this.remove(current);
			current = null;
			stack = new Stack<>();
			pushLeftSpine(root);
		}//remove
		
	}//BSTIterator
	
	public String toString() {
		StringBuilder sb=new StringBuilder(100);
		sb.append('[');
		Iterator<T> it=this.iterator();
		while( it.hasNext() ) {
			sb.append( it.next() );
			if( it.hasNext() ) sb.append(", ");
		}
		sb.append(']');
		return sb.toString();
	}//toString
	
	public boolean equals( Object x ) {
		if( !(x instanceof BinarySearchTree) ) return false;
		if( x==this ) return true;
		@SuppressWarnings("unchecked")
		BinarySearchTree<T> other=(BinarySearchTree<T>)x;
		return equals( this.root, other.root );
	}//equals
	
	private boolean equals( Node<T> r1, Node<T> r2 ) {
		if( r1==null && r2==null ) return true;
		if( r1==null || r2==null ) return false;
		if( !r1.info.equals(r2.info) ) return false;
		return equals( r1.left, r2.left ) && equals( r1.right, r2.right );
	}//equals
	
	public int hashCode() {
		return toString().hashCode();
	}//hashCode
	
	public BinarySearchTree<T> copy(){
		BinarySearchTree<T> target = new BinarySearchTree<>();
		return copy(root, target);
	}//copy
	
	private BinarySearchTree<T> copy(Node<T> root, BinarySearchTree<T> target){
		if (root == null)
			return null;
		target.add(root.info);
		copy(root.left, target);
		copy(root.right, target);
		return target;
	}
	
	public void build( T[] a ) {
		clear();
		List<T> list = new ArrayList<>();
		inOrder(list);
		for (int i=0; i<list.size(); i++)
			a[i] = list.get(i);
	}//build
	
	public int height() {
		return height(root);
	}
	private int height(Node<T> root) {
		if (root == null)
			return -1;
		if (root.rightCount > root.leftCount) {
			return 1+height(root.right);
		}
		return 1+height(root.left);
	}//height

	public void levelOrder( List<T> visited ) {
		if( root==null ) return;
		LinkedList<Node<T>> queue=new LinkedList<>();
		queue.addLast( root );
		while( !queue.isEmpty() ) {
			Node<T> n=queue.removeFirst();
			//visit n
			visited.add(n.info);
			if( n.left!=null ) queue.addLast( n.left );
			if( n.right!=null ) queue.addLast( n.right );
		}
	}//levelOrder
	
	public static void main(String[] args) {
		
		BinarySearchTree<Integer> b = new BinarySearchTree <>();
		b.add(50);
		System.out.println("Added 50. "
				+ "\nNow b is: " + b +
				".\nThe root is: " + b.root.info +
				".\nOn the left there are " + b.root.leftCount + " nodes and on the right there are " + b.root.rightCount 
				+ "\nNo rebalancing was needed\n");
		
		b.add(37);
		System.out.println("Added 37. "
				+ "\nNow b is: " + b +
				".\nThe root is: " + b.root.info +
				".\nOn the left there are " + b.root.leftCount + " nodes and on the right there are " + b.root.rightCount 
				+ "\nNo rebalancing was needed\n");
		
		b.add(-1);
		System.out.println("Added -1. "
				+ "\nNow b is: " + b +
				".\nThe root is: " + b.root.info +
				".\nOn the left there are " + b.root.leftCount + " nodes and on the right there are " + b.root.rightCount 
				+ "\nRebalancing was needed\n");
		
		b.remove(50);
		System.out.println("Removed 50. "
				+ "\nNow b is: " + b +
				".\nThe root is: " + b.root.info +
				".\nOn the left there are " + b.root.leftCount + " nodes and on the right there are " + b.root.rightCount 
				+ "\nNo rebalancing was needed\n");
		
		b.add(4);
		System.out.println("Added 4. "
				+ "\nNow b is: " + b +
				".\nThe root is: " + b.root.info +
				".\nOn the left there are " + b.root.leftCount + " nodes and on the right there are " + b.root.rightCount 
				+ "\nRebalancing was needed\n");
		
		b.add(27);
		System.out.println("Added 27. "
				+ "\nNow b is: " + b +
				".\nThe root is: " + b.root.info +
				".\nOn the left there are " + b.root.leftCount + " nodes and on the right there are " + b.root.rightCount 
				+ "\nNo rebalancing was needed\n");
		
		b.remove(4);
		System.out.println("Removed 4. "
				+ "\nNow b is: " + b +
				".\nThe root is: " + b.root.info +
				".\nOn the left there are " + b.root.leftCount + " nodes and on the right there are " + b.root.rightCount 
				+ "\nNo rebalancing was needed\n");
		
		System.out.println("The tree before emptying it with the iterator is: " + b);
		Iterator<Integer> i = b.iterator();
		while (i.hasNext()) {
			i.next();
			i.remove();
		}
		System.out.println("The tree after emptying it with the iterator is: " + b);
	}
		
}//BinarySearchTree
