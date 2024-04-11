# Documentation
You can store your data in key-value pairs using these trees:
* Unbalanced binary search tree.
* AVL-Tree.
* Red-black tree.

Keys' type must be comparable, value can represent any type you want. 

## How to use
To start working with a tree, you need to import it from our library.
```kotlin
import trees.UnbalancedBSTree
```
```kotlin
import trees.AvlTree
```
```kotlin
import trees.RBTree
```
We will show you an example with UnbalancedBST.
Firstly, you need to create a tree and specify types of keys and values.
```kotlin
val tree = UnbalancedBSTree<Int, Int>() 
```
Example of insertion. insert() method returns boolean value, which shows if insertion succeeded.
```kotlin
if (tree.insert(2, 5)) println("Key-value pair (2, 5) inserted successfully")
```
Example of searching by key. search() method returns value, which is stored in pair with such key (if there is no such key in the tree, search() returns null).
```kotlin
val foundValue = tree.search(2)
```
Example of deletion by key. delete() method returns boolean value, which shows if deletion succeeded.
```kotlin
if (tree.delete(2)) println("Key 5 deleted from the tree successfully")
```
Examples of iterating through the tree.
```kotlin
for (keyValuePair in tree) {
    println(keyValuePair)
}
```
```kotlin
tree.forEach {
    println(it)
}
```

