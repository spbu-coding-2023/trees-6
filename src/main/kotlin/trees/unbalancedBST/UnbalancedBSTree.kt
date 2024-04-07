package trees.unbalancedBST

import nodes.UnbalancedBSTNode
import trees.AbstractTree

class UnbalancedBSTree<K : Comparable<K>, V> : AbstractTree<K, V, UnbalancedBSTNode<K, V>>() {

    override fun createNode(key: K, value: V): UnbalancedBSTNode<K, V> = UnbalancedBSTNode(key, value)

    override fun iterator(): Iterator<Pair<K, V>> = UnbalancedBSTIterator(getPairs())
}

