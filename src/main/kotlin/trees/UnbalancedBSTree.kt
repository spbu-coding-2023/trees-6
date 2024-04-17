package trees

import nodes.UnbalancedBSTNode
import trees.abstractTree.AbstractTree

class UnbalancedBSTree<K : Comparable<K>, V> : AbstractTree<K, V, UnbalancedBSTNode<K, V>>() {

    override fun createNode(key: K, value: V): UnbalancedBSTNode<K, V> = UnbalancedBSTNode(key, value)

}

