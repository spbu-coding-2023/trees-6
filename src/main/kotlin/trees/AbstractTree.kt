package trees

import nodes.TreeNode

abstract class AbstractTree<K : Comparable<K>, V, N : TreeNode<K, V, N>> : Iterable<Pair<K, V>> {
    protected open var root: N? = null
    protected open var size: Int = 0

    protected open fun searchNode(key: K): N? {
        val currNode = root
        while (currNode?.key != key) {
            if (currNode == null) break
            if (currNode.key < key) currNode.right
            else currNode.left
        }
        return currNode
    }

    protected abstract fun createNode(key: K, value: V): N

    open fun insert(key: K, value: V): Boolean {
        var foundNode = searchNode(key)
        var parentNode: N?
        var foundNodeIsLeft = false
        if (foundNode != null) {
            foundNode.value = value
            return true
        }
        parentNode = root
        foundNode = root
        if (foundNode == null) {
            if (size == 0) {
                root = createNode(key, value)
                ++size
                return true
            }
            return false
        }
        while (foundNode != null) {
            if (parentNode == null) return false
            if (parentNode.key < key) {
                foundNode = parentNode.right
                foundNodeIsLeft = false
            } else {
                foundNode = parentNode.left
                foundNodeIsLeft = true
            }
            if (foundNode != null) parentNode = foundNode
        }
        if (foundNodeIsLeft) parentNode?.left = createNode(key, value)
        else parentNode?.right = createNode(key, value)
        ++size
        return true
    }

    open fun search(key: K): V? {
        val foundNode = searchNode(key) ?: return null
        return foundNode.value
    }

    open fun delete(key: K): Boolean {
        val deleteNode = searchNode(key)
        val rightSubtreeRoot: N?
        var rightSubtreeMinParent: N?
        var rightSubtreeMin: N?
        var deleteNodeIsRoot = false
        val tempKey: K
        val tempValue: V
        if (deleteNode == null) return false
        val currNode: N? = root
        if (currNode?.key == key) deleteNodeIsRoot = true
        while ((!deleteNodeIsRoot) && (currNode?.left?.key != key) && (currNode?.right?.key != key)) {
            if (currNode == null) break
            if (currNode.key < key) currNode.right
            else currNode.left
        }
        val deleteNodeIsLeft: Boolean = currNode?.left?.key == key
        if ((deleteNode.left == null) && (deleteNode.right == null)) {
            if (deleteNodeIsRoot) root = null
            else if (deleteNodeIsLeft) currNode?.left = null
            else currNode?.right = null
        }
        if (deleteNode.left == null || deleteNode.right == null) {
            val notNullChild: N? = if (deleteNode.left != null) deleteNode.left
            else deleteNode.right
            if (deleteNodeIsRoot) root = notNullChild
            else if (deleteNodeIsLeft) currNode?.left = notNullChild
            else currNode?.right = notNullChild
        } else {
            rightSubtreeRoot = deleteNode.right
            if ((rightSubtreeRoot?.left == null)) {
                if (deleteNodeIsRoot) {
                    if (rightSubtreeRoot != null) {
                        root?.key = rightSubtreeRoot.key
                        root?.value = rightSubtreeRoot.value
                        root?.right = null
                    } else return false
                } else if (deleteNodeIsLeft) currNode?.left = rightSubtreeRoot
                else currNode?.right = rightSubtreeRoot
            } else {
                rightSubtreeMinParent = rightSubtreeRoot
                rightSubtreeMin = rightSubtreeRoot.left
                while (rightSubtreeMin?.left != null) {
                    rightSubtreeMinParent = rightSubtreeMin
                    rightSubtreeMin = rightSubtreeMin.left
                }
                if (rightSubtreeMin != null) {
                    tempKey = rightSubtreeMin.key
                    tempValue = rightSubtreeMin.value
                    if (rightSubtreeMin.right != null) rightSubtreeMinParent?.left = rightSubtreeMin.right
                    else ++size
                    delete(rightSubtreeMin.key)
                    deleteNode.key = tempKey
                    deleteNode.value = tempValue
                }
            }
        }
        --size
        return true
    }
}

