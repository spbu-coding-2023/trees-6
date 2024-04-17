package trees.abstractTree

import nodes.TreeNode

abstract class AbstractTree<K : Comparable<K>, V, N : TreeNode<K, V, N>> : Iterable<Pair<K, V>> {
    internal open var root: N? = null
    open var size: Int = 0
        protected set

    protected open fun searchNode(key: K): N? {
        var currNode = root
        while (currNode?.key != key) {
            if (currNode == null) break // if key is not in tree, it returns null
            currNode = if (currNode.key < key) currNode.right
            else currNode.left
        }
        return currNode
    }

    open fun search(key: K): V? {
        val foundNode = searchNode(key) ?: return null
        return foundNode.value
    }

    protected abstract fun createNode(key: K, value: V): N

    open fun insert(key: K, value: V): Boolean {
        var foundNode = searchNode(key) // we look if key is already in tree
        if (foundNode != null) {
            foundNode.value = value // if so, we change its value to a new one
            return true
        }
        // empty tree case
        if (root == null) {
            root = createNode(key, value)
            ++size
            return true
        }
        var parentNode = root
        var foundNodeIsLeft = false
        foundNode = root
        while (foundNode != null) {
            if (parentNode == null) return false
            if (parentNode.key < key) {
                foundNode = parentNode.right
                foundNodeIsLeft = false
            } else {
                foundNode = parentNode.left
                foundNodeIsLeft = true
            }
            if (foundNode != null) parentNode = foundNode // if foundNode isn't null, it may have children
        }
        if (foundNodeIsLeft) parentNode?.left = createNode(key, value)
        else parentNode?.right = createNode(key, value)
        ++size
        return true
    }

    private fun rewriteNode(receiver: N?, giver: N?) {
        if (giver == null) throw Exception("Can't rewrite node")
        val tempKey = giver.key
        val tempValue = giver.value
        delete(giver.key)
        receiver?.key = tempKey
        receiver?.value = tempValue
        ++size // because giver wasn't actually deleted, it was replaced
    }

    open fun delete(key: K): Boolean {
        val deleteNodeIsRoot = root?.key == key
        var deletableNodeParent = root
        // search for deletable node parent
        while ((!deleteNodeIsRoot) && (deletableNodeParent != null) && (deletableNodeParent.left?.key != key) && (deletableNodeParent.right?.key != key)) {
            deletableNodeParent = if (deletableNodeParent.key < key) deletableNodeParent.right
            else deletableNodeParent.left
        }
        if (deletableNodeParent == null) return false
        val deleteNodeIsLeft: Boolean = deletableNodeParent.left?.key == key
        val deletableNode = if (deleteNodeIsRoot) root
        else if (deleteNodeIsLeft) deletableNodeParent.left
        else deletableNodeParent.right
        if (deletableNode == null) return false
        // no children nodes
        if ((deletableNode.left == null) && (deletableNode.right == null)) {
            if (deleteNodeIsRoot) root = null
            else if (deleteNodeIsLeft) deletableNodeParent.left = null
            else deletableNodeParent.right = null
        }
        // 1 child node
        else if (deletableNode.left == null || deletableNode.right == null) {
            val notNullChild = if (deletableNode.left != null) deletableNode.left
            else deletableNode.right
            if (deleteNodeIsRoot) root = notNullChild
            else if (deleteNodeIsLeft) deletableNodeParent.left = notNullChild
            else deletableNodeParent.right = notNullChild
            // 2 children
        } else {
            val rightSubtreeRoot = deletableNode.right ?: return false
            if ((rightSubtreeRoot.left == null)) {
                if (deleteNodeIsRoot) rewriteNode(root, rightSubtreeRoot)
                else if (deleteNodeIsLeft) rewriteNode(deletableNodeParent.left, rightSubtreeRoot)
                else rewriteNode(deletableNodeParent.right, rightSubtreeRoot)
            } else {
                var rightSubtreeMinParent = rightSubtreeRoot
                var rightSubtreeMin = rightSubtreeRoot.left
                // search for rightSubtreeMin
                while (rightSubtreeMin?.left != null) {
                    rightSubtreeMinParent = rightSubtreeMin
                    rightSubtreeMin = rightSubtreeMin.left
                }
                if (rightSubtreeMin == null) return false
                val rightSubtreeMinRight = rightSubtreeMin.right
                rewriteNode(deletableNode, rightSubtreeMin)
                rightSubtreeMinParent.left = rightSubtreeMinRight
            }
        }
        --size
        return true
    }

    private fun getPairs(): MutableList<Pair<K, V>> {
        val trajectory = mutableListOf<Pair<K, V>>()
        var currNode: N? = root ?: return mutableListOf()
        val stack = mutableListOf(currNode)
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

    override fun iterator(): Iterator<Pair<K, V>> = TreeIterator(getPairs())
}

