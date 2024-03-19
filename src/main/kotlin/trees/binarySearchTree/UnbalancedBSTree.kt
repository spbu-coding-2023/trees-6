package trees.binarySearchTree

import trees.AbstractTree
import nodes.TreeNode

class UnbalancedBSTree <K : Comparable<K>, V>  : AbstractTree<K, V>() {
    private fun getPairs(): MutableList<Pair<K, V>>{
        val trajectory = mutableListOf<Pair<K, V>>()
        val stack : MutableList<TreeNode<K, V>?>
        var currNode = this.root
        if (currNode == null) return mutableListOf()
        else {
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
    }

    override fun iterator() : Iterator<Pair<K, V>> {
        return UnbalancedBSIterator(this.getPairs())
    }
}
