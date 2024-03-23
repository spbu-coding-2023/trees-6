package trees.unbalancedBST

import nodes.UnbalancedBSTNode
import trees.AbstractTree

class UnbalancedBSTree<K : Comparable<K>, V> : AbstractTree<K, V, UnbalancedBSTNode<K, V>>() {

    override fun createNode(key: K, value: V): UnbalancedBSTNode<K, V> = UnbalancedBSTNode(key, value)

    private fun getPairs(): MutableList<Pair<K, V>> {
        val trajectory = mutableListOf<Pair<K, V>>()
        val stack: MutableList<UnbalancedBSTNode<K, V>?>
        var currNode : UnbalancedBSTNode<K, V>? = root ?: return mutableListOf()
        stack = mutableListOf(currNode)
        while (stack.size > 0) {
            currNode = stack[stack.size - 1]
            stack.removeAt(stack.size - 1)
            if (currNode != null) {
                stack.add(currNode.left)
                stack.add(currNode.right)
                trajectory.add(Pair(currNode.key, currNode.value))
            }
        }
        return trajectory
    }

    override fun iterator(): Iterator<Pair<K, V>> = UnbalancedBSTIterator(this.getPairs())
}

