# Binary search trees library
This library provides the ability to store key-value pairs in trees of three types: 
* Unbalanced binary search tree.
* AVL-Tree.
* Red-black tree.
## Table of contents
* [Functionality](#functionality)
* [File structure](#filestructure)
* [How to use](#howtouse)
* [Technologies used in the library](#technologies)
* [Our team and responsibility areas](#team)
* [Project status](#status)
* [Contributing](#contributing)
* [License](#license)
## Functionality <a name="functionality"></a>
You can store values of any types by keys of comparable type. Each tree provides you to:
* Insert values.
* Change inserted value by key.
* Search value by key.
* Delete value.
* Iterate through tree.
## File structure <a name="filestructure"></a>
```
src
├──main/kotlin
│  │
│  ├── nodes
│  │   ├── TreeNode.kt  # Node of abstract binary tree and BST class
│  │   ├── UnbalancedBSTNode.kt # Node of Unbalanced BST class
│  │   ├── AvlNode.kt # Node of AVL-tree class
│  │   ├── RBTreeNode.kt # Node of RB tree class
│  │      
│  ├── trees
│  │   ├── UnbalancedBSTree.kt # Unbalanced BST class
│  │   ├── AvlTree.kt # AVL-tree class
│  │   ├── RBTree.kt # RB tree class
│  │   ├── abstractTree
│  │       ├── AbstractTree.kt # Abstract binary tree class
│  │       ├── TreeIterator.kt # Tree iterator class
│
├──test/kotlin
│  │
│  ├── UnbalancedBSTTests.kt # Unit tests of Unbalanced BST
│  ├── AvlTests.kt # Unit tests of AVL-tree
│  ├── RBTreeTests.kt # Unit tests of RB tree
│  │
```
## How to use <a name="howtouse"></a>
See [DOCS.md](./DOCS.md)
## Technologies used in the library <a name="technologies"></a>
* [Kotlin 1.9.23](https://kotlinlang.org)
* [Gradle 8.4](https://gradle.org)
* [JDK 17](https://openjdk.org)
## Our team and responsibility areas <a name="team"></a>
* Kochergin Vyacheslav - Unbalanced binary search tree. [GitHub](https://github.com/VyacheslavIurevich), [Contact](https://t.me/se4life).
* Marchenko Vadim - AVL-tree. [GitHub](https://github.com/elbananium), [Contact](https://t.me/elbananum).
* Kuznetsov Dmitry - Red-black tree. [GitHub](https://github.com/f1i3g3), [Contact](https://t.me/f1i3g3).
## Project status <a name="status"></a>
In development. 
## Contributing <a name="contributing"></a>
See [CONTRIBUTING.md](./CONTRIBUTING.md)
## License <a name="license"></a>
See [LICENSE.md](./LICENSE.md)
