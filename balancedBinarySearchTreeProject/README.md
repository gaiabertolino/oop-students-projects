# Self-Balancing Binary Search Tree

![Objects & Classes](https://img.shields.io/badge/Objects_%26_Classes-1f6feb?style=for-the-badge)
![Encapsulation](https://img.shields.io/badge/Encapsulation-2ea44f?style=for-the-badge)
![References & Identity](https://img.shields.io/badge/References_%26_Identity-8250df?style=for-the-badge)
![Abstract Classes & Interfaces](https://img.shields.io/badge/Abstract_Classes_%26_Interfaces-1b7c83?style=for-the-badge)

> Try Part 1 on your own first. Read Part 2 and the reference code only afterwards.

---

## Part 1 – What you are asked to do

You are given the interface `SortedCollection<T>`: `size`, `clear`, `contains`, `add`, `remove`, `get`, `isEmpty`, `isFull`, plus `Iterable<T>`.

Implement the generic class `BinarySearchTree<T extends Comparable<? super T>>` that implements this interface **and keeps itself balanced**:

1. Each node stores its value, its two children, and **how many nodes are in its left and right subtrees**.
2. A tree is *balanced* if, at every node, these two counts differ by **at most 1**.
3. After every `add` / `remove`, detect whether some node became unbalanced. If so, **rebuild** the tree from its sorted (in-order) list of elements, always choosing the middle element as the root (as in binary search).
4. Provide an **in-order iterator** that does not copy the whole tree (use a stack). It must support `remove()` and stay valid after a rebalance.
5. Also implement:
   - the traversals `inOrder`, `preOrder`, `postOrder` and `levelOrder`
   - `equals`, `hashCode`, `toString`
   - `copy()`, `build(T[] a)` (fill `a` with the sorted elements) and `height()`
6. Write a `main` that adds and removes integers, prints the root and subtree counts after each step, and finally empties the tree using the iterator.

---

## Part 2 – How the reference solution works

### Structure
```
SortedCollection<T>  (interface, given)
          ▲
BinarySearchTree<T>
   ├─ static class Node<E>      info, left, right, leftCount, rightCount
   ├─ Stack<Node<T>> unbalanced (unbalanced nodes found during add/remove)
   └─ class BSTIterator         (stack-based in-order iterator)
```

### Key ideas
- **Counters:** `leftCount` / `rightCount` = number of nodes in each subtree. The recursive `add` / `remove` update the counter of the branch they go down.
- **Detecting imbalance:** on the way back up the recursion, `isBalanced(node)` checks `|rightCount − leftCount| ≤ 1`. Unbalanced nodes are pushed onto `unbalanced`.
- **Rebalancing (`insert`):** if the stack is not empty, take the in-order list and rebuild recursively:
  - `mid = (start+end)/2` becomes the root
  - recurse on `[start, mid)` and `[mid+1, end)`
  - recompute each node's counters from its children
- **Iterator:** `pushLeftSpine` pushes a node and its whole left spine. `next()` pops the smallest element, then pushes the left spine of that node's right child. `remove()` calls the tree's `remove`, then **rebuilds the stack**, because the tree may have been restructured.
- **`height()`:** walks down the side with more nodes (using the counters) instead of exploring both sides.

### How to run
```bash
javac -encoding UTF-8 -d out *.java
java -cp out Bertolino.bst.BinarySearchTree
```

### Questions for discussion
1. The whole tree is rebuilt in O(n) after an imbalance. Could you rebuild only the unbalanced subtree? How does this compare with AVL or red–black trees?
2. Look at `build(T[] a)`: it calls `clear()` **before** `inOrder(...)`. What happens?
3. What does `copy()` return for an empty tree? What happens to the counters when you `remove(x)` an element that is not in the tree?
4. Is "follow the larger subtree" always the correct way to compute the height?

---

## Concepts practised in detail

### Main focus

- **Objects and classes:** a generic class with a `static` nested class (`Node`) and an inner class (`BSTIterator`).
- **Encapsulation:** `Node` and the recursive helpers are `private`; only the collection operations are public.
- **References and object identity:**
  - nodes are linked by references
  - `equals` compares structure, not `==`
  - `copy()` creates an independent tree
  - the inner iterator refers to its outer tree through `BinarySearchTree.this`
- **Abstract classes and interfaces:** implementing `SortedCollection<T>`, `Iterable<T>` and `Iterator<T>`, with the bounded generic `T extends Comparable<? super T>`.

### Touched on

- **Constructors and life cycle:** nodes are created on `add` and dropped (garbage-collected) on `remove`, `clear` and rebuilds.
- **Polymorphism:** the tree works for any `T`, because elements are compared through `compareTo`.
