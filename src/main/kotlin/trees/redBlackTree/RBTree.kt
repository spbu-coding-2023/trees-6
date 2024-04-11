package trees.redBlackTree

import nodes.RBTreeNode
import trees.AbstractTree

open class RBTree <K : Comparable<K>, V> : AbstractTree<K, V, RBTreeNode<K, V>>() {
	// Using an implementation of https://algs4.cs.princeton.edu/33balanced/RedBlackBST.java.html version by Robert Sedgewick & Kevin Wayne

	override fun createNode(key: K, value: V): RBTreeNode<K, V> = RBTreeNode(key, value) // default: Red
	private fun checkNodeRed(node: RBTreeNode<K, V>?): Boolean = node != null && node.isRed()

	override fun insert(key: K, value: V): Boolean {
		root = insertNode(key, value, root)
		size++

		root?.setColor(RBTreeNode.Color.Black)
		return true
	}
	private fun insertNode(key: K, value: V, node: RBTreeNode<K, V>?): RBTreeNode<K, V> {
		val currNode = node ?: return createNode(key, value)

		when {
			key < currNode.key -> currNode.left = insertNode(key, value, currNode.left)
			key > currNode.key -> currNode.right = insertNode(key, value, currNode.right)
			else -> {
				currNode.value = value
				size--
			}
		}

		if (checkNodeRed(currNode.right) && !checkNodeRed(currNode.left)) {
			rotateLeft(currNode)
		}
		if (checkNodeRed(currNode.left) && checkNodeRed(currNode.left?.left)) {
			rotateRight(currNode)
		}
		if (checkNodeRed(currNode.left) && checkNodeRed(currNode.right)) {
			flipColors(currNode)
		}

		return currNode
	}

	private fun rotateLeft(currNode: RBTreeNode<K, V>?) {
		if (currNode?.right != null) { // guaranteed that the right is not null
			val tempNode: RBTreeNode<K, V> = createNode(currNode.key, currNode.value)
			tempNode.left = currNode.left
			tempNode.right = currNode.right?.left

			currNode.key = currNode.right!!.key
			currNode.value = currNode.right!!.value

			currNode.left = tempNode
			currNode.right = currNode.right?.right
		}
	}
	private fun rotateRight(currNode: RBTreeNode<K, V>?) {
		if (currNode?.left != null) { // guaranteed that the left is not null
			val tempNode: RBTreeNode<K, V> = createNode(currNode.key, currNode.value)
			tempNode.left = currNode.left?.right
			tempNode.right = currNode.right

			currNode.key = currNode.left!!.key
			currNode.value = currNode.left!!.value

			currNode.left = currNode.left?.left
			currNode.right = tempNode
		}
	}
	private fun flipColors(currNode: RBTreeNode<K, V>) {
		if (checkNodeRed(currNode)) {
			currNode.setColor(RBTreeNode.Color.Black)
		} else {
			currNode.setColor(RBTreeNode.Color.Red)
		}

		if (checkNodeRed(currNode.left)) {
			currNode.left?.setColor(RBTreeNode.Color.Black)
		} else {
			currNode.left?.setColor(RBTreeNode.Color.Red)
		}

		if (checkNodeRed(currNode.right)) {
			currNode.right?.setColor(RBTreeNode.Color.Black)
		} else {
			currNode.right?.setColor(RBTreeNode.Color.Red)
		}
	}

	override fun delete(key: K): Boolean {
		if (search(key) == null) {
			return false
		}

		if (!checkNodeRed(root?.left) && !checkNodeRed(root?.right)) {
			root?.setColor(RBTreeNode.Color.Red)
		}

		root = deleteNode(key, root)
		size--
		root?.setColor(RBTreeNode.Color.Black)

		return true
	}
	private fun deleteNode(key: K, node: RBTreeNode<K, V>?): RBTreeNode<K, V>? {
		if (node == null) {
			return null
		}

		var currNode = node
		if (key < currNode.key)  {
			if (!checkNodeRed(currNode.left) && !checkNodeRed(currNode.left?.left)) {
				currNode = moveRedLeft(currNode)
			}
			currNode.left = deleteNode(key, currNode.left)
		}
		else {
			if (checkNodeRed(currNode.left)) {
				rotateRight(currNode)
			}

			if (key == currNode.key && currNode.right == null) {
				return null
			}

			if (!checkNodeRed(currNode.right) && !checkNodeRed(currNode.right?.left)) {
				currNode = moveRedRight(currNode)
			}

			if (key == currNode?.key) {
				val minNode = minNode(currNode.right)

				if (minNode != null) {
					currNode.key = minNode.key
					currNode.value = minNode.value
				}

				currNode.right = deleteMin(currNode.right)
			}
			else {
				currNode?.right = deleteNode(key, currNode?.right)
			}
		}

		return balance(currNode)
	}

	private fun minNode(node: RBTreeNode<K, V>?): RBTreeNode<K, V>? {
		return if (node == null) null else minNode(node.left)
	}
	private fun deleteMin(node: RBTreeNode<K, V>?): RBTreeNode<K, V>? {
		var currNode: RBTreeNode<K, V>? = node ?: return null

		if (currNode?.left == null) {
			return null
		}

		if (!checkNodeRed(currNode.left) && !checkNodeRed(currNode.left?.left)) {
			currNode = moveRedLeft(currNode)
		}

		currNode.left = deleteMin(currNode.left)
		return balance(currNode)
	}

	private fun moveRedLeft(node: RBTreeNode<K, V>): RBTreeNode<K, V> {
		val currNode: RBTreeNode<K, V> = node

		flipColors(currNode)

		if (checkNodeRed(currNode.right?.left)) {
			rotateRight(currNode.right)
			rotateLeft(currNode)

			flipColors(currNode)
		}

		return currNode
	}
	private fun moveRedRight(node: RBTreeNode<K, V>?): RBTreeNode<K, V>? {
		val currNode: RBTreeNode<K, V>? = node

		if (currNode != null) {
			flipColors(currNode)

			if (checkNodeRed(currNode.left?.left)) {
				rotateRight(currNode)

				flipColors(currNode)
			}
		}

		return currNode
	}
	private fun balance(currNode: RBTreeNode<K, V>?): RBTreeNode<K, V>? {

		if (currNode != null) {
			if (checkNodeRed(currNode.right) && !checkNodeRed(currNode.left)) {
				rotateLeft(currNode)
			}
			if (checkNodeRed(currNode.left) && checkNodeRed(currNode.left?.left)) {
				rotateRight(currNode)
			}
			if (checkNodeRed(currNode.left) && checkNodeRed(currNode.right)) {
				flipColors(currNode)
			}
		}

		return currNode
	}
}
