package trees

import nodes.TreeNode

abstract class AbstractTree<K : Comparable<K>, V> : Iterable<Pair<K, V>> {
    protected var root: TreeNode<K, V>? = null
    protected var size: Int = 0

    protected fun searchNode(key: K): TreeNode<K, V>? {
        var currNode = this.root
        while (currNode?.key != key) {
            if (currNode == null) break
            else if (currNode.key < key) currNode = currNode.right
            else currNode = currNode.left
        }
        return currNode
    }

    open fun insert(key: K, value: V): Boolean {
        var foundNode = searchNode(key)
        var parentNode: TreeNode<K, V>?
        var foundNodeIsLeft = false
        if (foundNode != null) {
            foundNode.value = value
            return true
        } else {
            parentNode = this.root
            foundNode = this.root
            if (foundNode == null) {
                if (this.size == 0) {
                    this.root = TreeNode(key, value)
                    ++this.size
                    return true
                } else return false
            }
            else {
                while (foundNode != null) {
                    if (parentNode == null) return false
                    else if (parentNode.key < key) {
                        foundNode = parentNode.right
                        foundNodeIsLeft = false
                    } else {
                        foundNode = parentNode.left
                        foundNodeIsLeft = true
                    }
                    if (foundNode != null) parentNode = foundNode
                }
                if (foundNodeIsLeft) parentNode?.left = TreeNode(key, value)
                else parentNode?.right = TreeNode(key, value)
                ++this.size
                return true
            }
        }
    }

    open fun search(key: K): V {
        val foundNode = searchNode(key)
        if (foundNode == null) throw Exception("Key is not found in the tree")
        else return foundNode.value
    }

    open fun delete(key: K): Boolean {
        val deleteNode = searchNode(key)
        val rightSubtreeRoot: TreeNode<K, V>?
        var currNode: TreeNode<K, V>?
        var rightSubtreeMinParent: TreeNode<K, V>?
        var rightSubtreeMin: TreeNode<K, V>?
        val deleteNodeIsLeft: Boolean
        var deleteNodeIsRoot = false
        val tempKey : K
        val tempValue : V
        if (deleteNode == null) return false
        else {
            currNode = this.root
            if (currNode?.key == key) deleteNodeIsRoot = true
            while ((!deleteNodeIsRoot) and (currNode?.left?.key != key) and (currNode?.right?.key != key)) {
                if (currNode == null) break
                else if (currNode.key < key) currNode = currNode.right
                else currNode = currNode.left
            }
            deleteNodeIsLeft = currNode?.left?.key == key
            if ((deleteNode.left == null) and (deleteNode.right == null)) {
                if (deleteNodeIsRoot) this.root = null
                else if (deleteNodeIsLeft) currNode?.left = null
                else currNode?.right = null
            } else if ((deleteNode.left == null) and (deleteNode.right != null)) {
                if (deleteNodeIsRoot) this.root = deleteNode.right
                else if (deleteNodeIsLeft) currNode?.left = deleteNode.right
                else currNode?.right = deleteNode.right
            } else if ((deleteNode.left != null) and (deleteNode.right == null)) {
                if (deleteNodeIsRoot) this.root = deleteNode.left
                else if (deleteNodeIsLeft) currNode?.left = deleteNode.left
                else currNode?.right = deleteNode.left
            } else {
                rightSubtreeRoot = deleteNode.right
                if ((rightSubtreeRoot?.left == null)) {
                    if (deleteNodeIsRoot) {
                        if (rightSubtreeRoot != null) {
                            this.root?.key = rightSubtreeRoot.key
                            this.root?.value = rightSubtreeRoot.value
                            this.root?.right = null
                        }
                        else return false
                    }
                    else if (deleteNodeIsLeft) currNode?.left = rightSubtreeRoot
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
                        else ++this.size
                        this.delete(rightSubtreeMin.key)
                        deleteNode.key = tempKey
                        deleteNode.value = tempValue
                    }
                }
            }
            --this.size
            return true
        }
    }
}

