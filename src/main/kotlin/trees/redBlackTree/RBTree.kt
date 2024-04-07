package trees.redBlackTree

import nodes.RBTreeNode
import trees.AbstractTree

open class RBTree <K : Comparable<K>, V> : AbstractTree<K, V, RBTreeNode<K, V>>() {
	// Using an implementation of https://algs4.cs.princeton.edu/33balanced/RedBlackBST.java.html

	override fun createNode(key: K, value: V): RBTreeNode<K, V> = RBTreeNode(key, value) // default: Red
	private fun checkNodeRed(node: RBTreeNode<K, V>?): Boolean = node != null && node.isRed()

	override fun insert(key: K, value: V): Boolean {
		root = insertNode(key, value, root) // result??
		size++

		root?.setColor(RBTreeNode.Color.Black)
		return true
	}
	private fun insertNode(key: K, value: V, node: RBTreeNode<K, V>?): RBTreeNode<K, V> {
		var currNode = node ?: return RBTreeNode(key, value)

		when {
			key < currNode.key -> currNode.left = insertNode(key, value, currNode.left)
			key > currNode.key -> currNode.right = insertNode(key, value, currNode.right)
			else -> currNode.value = value
		}

		if (checkNodeRed(currNode.right) && !checkNodeRed(currNode.left)) {
			currNode = rotateLeft(currNode)!! // !!!!!
		}
		if (checkNodeRed(currNode.left) && checkNodeRed(currNode.left?.left)) {
			currNode = rotateRight(currNode)!! // !!!!!
		}
		if (checkNodeRed(currNode.left) && checkNodeRed(currNode.right)) {
			flipColors(currNode)
		}

		return currNode
	}

	private fun rotateLeft(node: RBTreeNode<K, V>): RBTreeNode<K, V>? {
		val tempNode: RBTreeNode<K, V>? = node.right

		node.right = tempNode?.left
		tempNode?.left = node

		tempNode?.setColor(node.color)
		node.setColor(RBTreeNode.Color.Red)

		return tempNode
	}
	private fun rotateRight(node: RBTreeNode<K, V>): RBTreeNode<K, V>? {
		val tempNode: RBTreeNode<K, V>? = node.left

		node.left = tempNode?.right
		tempNode?.right = node

		tempNode?.setColor(node.color)
		node.setColor(RBTreeNode.Color.Red)

		return tempNode
	}
	private fun flipColors(currNode: RBTreeNode<K, V>) {
		if (checkNodeRed(currNode)) {
			currNode.setColor(RBTreeNode.Color.Black)
		} else
		{
			currNode.setColor(RBTreeNode.Color.Red)
		}

		if (checkNodeRed(currNode.left)) {
			currNode.setColor(RBTreeNode.Color.Black)
		} else
		{
			currNode.setColor(RBTreeNode.Color.Red)
		}

		if (checkNodeRed(currNode.right)) {
			currNode.setColor(RBTreeNode.Color.Black)
		} else
		{
			currNode.setColor(RBTreeNode.Color.Red)
		}
	}

	override fun delete(key: K): Boolean {
		if (root == null && search(key) == null) {
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
			currNode?.left = deleteNode(key, currNode?.left)
		}
		else {
			if (checkNodeRed(currNode.left)) {
				currNode = rotateRight(currNode)
			}

			if (key == currNode?.key && currNode.right == null) {
				return null
			}

			if (!checkNodeRed(currNode?.right) && !checkNodeRed(currNode?.right?.left)) {
				currNode = moveRedRight(currNode)
			}

			if (key == currNode?.key) {
				val minNode = minNode(currNode.right)

				currNode.key = minNode!!.key // !!!!!
				currNode.value = minNode.value

				currNode.right = deleteMin(currNode.right)
			}
			else {
				currNode?.right = deleteNode(key, currNode?.right)
			}
		}

		return balance(currNode)
	}

	private fun minNode(node: RBTreeNode<K, V>?): RBTreeNode<K, V>? {
		return if (node == null) {
			null
		} else {
			minNode(node.left)
		}
	}
	private fun deleteMin(node: RBTreeNode<K, V>?): RBTreeNode<K, V>? {
		var currNode: RBTreeNode<K, V>? = node ?: return null

		if (currNode?.left == null) {
			return null
		}

		if (!checkNodeRed(currNode.left) && !checkNodeRed(currNode.left?.left)) {
			currNode = moveRedLeft(currNode)
		}

		currNode?.left = deleteMin(currNode?.left)
		return balance(currNode)
	}

	private fun moveRedLeft(node: RBTreeNode<K, V>): RBTreeNode<K, V>? {
		var currNode: RBTreeNode<K, V>? = node

		if (currNode != null) {
			flipColors(currNode)

			if (checkNodeRed(currNode.right?.left)) {
				currNode.right = rotateRight(currNode.right!!) // !!!!!
				currNode = rotateLeft(currNode)

				if (currNode != null) {
					flipColors(currNode)
				}
			}

		}

		return currNode
	}
	private fun moveRedRight(node: RBTreeNode<K, V>?): RBTreeNode<K, V>? {
		var currNode: RBTreeNode<K, V>? = node

		if (currNode != null) {
			flipColors(currNode)

			if (checkNodeRed(currNode.left?.left)) {
				currNode = rotateRight(currNode)

				if (currNode != null) {
					flipColors(currNode)
				}
			}
		}

		return currNode
	}
	private fun balance(node: RBTreeNode<K, V>?): RBTreeNode<K, V>? {
		var currNode = node

		if (currNode != null) {
			if (checkNodeRed(currNode.right) && !checkNodeRed(currNode.left)) {
				currNode = rotateLeft(currNode)
			}
			if (checkNodeRed(currNode?.left) && checkNodeRed(currNode?.left?.left)) {
				currNode = rotateRight(currNode!!) // !!!!!
			}
			if (currNode != null && checkNodeRed(currNode.left) && checkNodeRed(currNode.right)) {
				flipColors(currNode)
			}
		}

		return currNode
	}

	override fun iterator(): Iterator<Pair<K, V>> {
		return RBTreeIterator(this.getPairs())
	}
}
