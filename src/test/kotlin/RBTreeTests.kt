import nodes.RBTreeNode
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import trees.redBlackTree.RBTree
import kotlin.math.max

class RBTreeTests {

	private lateinit var tree: RBTree<Int, Int>

	private fun checkTreeInvariants(): Boolean { // assuming tree size is correct
		var currNode: RBTreeNode<Int, Int>? = tree.root
		if (currNode == null) {
			return true // empty tree
		} else if (currNode.isRed()) {
			println("red root!")
			return false // red root error
		} else {
			val stack: MutableList<RBTreeNode<Int, Int>?> = mutableListOf(currNode)

			while (stack.size > 0) {
				currNode = stack[stack.size - 1]

				stack.removeAt(stack.size - 1)

				if (currNode != null) {
					val leftChild: RBTreeNode<Int, Int>? = currNode.left

					if (leftChild != null) {
						if (leftChild.key > currNode.key) {
							println("left should be <= curr!")
							return false
						}

						if (leftChild.isRed() && currNode.isRed()) {
							println("left red + curr red")
							return false
						}

						stack.add(leftChild)
					}

					val rightChild: RBTreeNode<Int, Int>? = currNode.right

					if (rightChild != null) {
						if (rightChild.key < currNode.key) {
							println("right should be >= curr!")
							return false
						}

						if (rightChild.isRed() && currNode.isRed()) {
							println("right red + curr red")
							return false
						}

						stack.add(rightChild)
					}

					if (getBH(0, leftChild) != getBH(0, rightChild)) { // BH from this place
						return false
					}
				}
			}

			return true
		}
	}

	private fun getBH(value: Int, node: RBTreeNode<Int, Int>?): Int {
		if (node == null) {
			return value
		} else
		{
			var s: Int = value
			if (!node.isRed()) {
				s++
			}

			return max(getBH(s, node.left), getBH(s, node.right))
		}
	}

	@BeforeEach
	fun setup() {
		tree = RBTree()
	}

	@Test
	@DisplayName("check empty tree")
	fun checkEmptyTree() {
		assert(tree.size == 0)
	}

	@Test
	@DisplayName("simple insertion test")
	fun insertSomeNodes() {
		assert(tree.insert(2, 5))
		assert(tree.insert(-5, 6))
		assert(tree.insert(0, 55))
		assert(tree.insert(10, 1))
		assert(tree.insert(5, 7))

		assert(tree.size == 5)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("simple search test")
	fun searchExistentNode() {
		assert(tree.insert(5, 7))
		assert(tree.insert(2, 6))
		assert(tree.insert(10, 5))
		assert(tree.insert(7, 5))
		assert(tree.insert(8, 12))

		val value = tree.search(8)
		assert(value == 12)

		assert(tree.size == 5)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("search non-existent node")
	fun searchDefunctNode() {
		assert(tree.insert(5, 2))
		assert(tree.insert(15, 7))

		val value = tree.search(10)
		assert(value == null)

		assert(tree.size == 2)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("change value of existing node")
	fun changeValue() {
		assert(tree.insert(5, 2))
		assert(tree.insert(5, 6))

		val value = tree.search(5)
		assert(value == 6)

		assert(tree.size == 1)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("simple deletion test")
	fun deleteLeafNode() {
		assert(tree.insert(10, 5))
		assert(tree.insert(5, 2))
		assert(tree.insert(15, 3))

		assert(tree.delete(15))

		assert(tree.size == 2)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete non-existent node")
	fun deleteDefunctNode() {
		assert(tree.insert(10, 1))
		assert(tree.insert(5, 1))
		assert(tree.insert(15, 1))

		assert(!tree.delete(1))
		assert(tree.size == 3)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete single root node")
	fun deleteSingleRoot() {
		assert(tree.insert(1, 5))
		assert(tree.delete(1))
		assert(tree.size == 0)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete from empty tree")
	fun deleteFromEmptyTree() {
		assert(!tree.delete(1))

		assert(tree.size == 0)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete left leaf with no child")
	fun deleteLeftLeafNoChild() {
		assert(tree.insert(10, 1))
		assert(tree.insert(5, 1))
		assert(tree.insert(15, 1))

		assert(tree.delete(5))

		assert(tree.size == 2)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete right leaf with no child")
	fun deleteRightLeafNoChild() {
		assert(tree.insert(10, 1))
		assert(tree.insert(5, 1))
		assert(tree.insert(15, 1))

		assert(tree.delete(15))

		assert(tree.size == 2)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete root with only left child")
	fun deleteRootOnlyLeftChild() {
		assert(tree.insert(10, 1))
		assert(tree.insert(5, 1))

		assert(tree.delete(10))

		assert(tree.size == 1)
		assert(tree.root?.key == 5)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete root with only right child")
	fun deleteRootOnlyRightChild() {
		assert(tree.insert(10, 1))
		assert(tree.insert(15, 1))

		assert(tree.delete(10))

		assert(tree.size == 1)
		assert(tree.root?.key == 15)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete left node with left child")
	fun deleteLeftNodeLeftChild() {
		assert(tree.insert(10, 1))
		assert(tree.insert(9, 1))
		assert(tree.insert(8, 1))

		assert(tree.delete(9))

		assert(tree.size == 2)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete left node with right child")
	fun deleteLeftNodeRightChild() {
		assert(tree.insert(10, 1))
		assert(tree.insert(8, 1))
		assert(tree.insert(9, 1))

		assert(tree.delete(8))

		assert(tree.size == 2)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete right node with left child")
	fun deleteRightNodeLeftChild() {
		assert(tree.insert(10, 1))
		assert(tree.insert(12, 1))
		assert(tree.insert(11, 1))

		assert(tree.delete(12))

		assert(tree.size == 2)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete right node with right child")
	fun deleteRightNodeRightChild() {
		assert(tree.insert(10, 1))
		assert(tree.insert(11, 1))
		assert(tree.insert(12, 1))

		assert(tree.delete(11))

		assert(tree.size == 2)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete root with two children, right subtree root has no left children")
	fun deleteRootTwoChildrenRightSubtreeRootHasOnlyRightChildren() {
		assert(tree.insert(10, 1))
		assert(tree.insert(5, 1))
		assert(tree.insert(11, 1))
		assert(tree.insert(12, 1))
		assert(tree.insert(15, 1))

		assert(tree.delete(11))

		assert(tree.size == 4)
		assert(checkTreeInvariants())

	}

	@Test
	@DisplayName("delete left node with two children, right subtree root has no left children")
	fun deleteLeftNodeTwoChildrenRightSubtreeRootHasNoLeftChildren() {
		assert(tree.insert(10, 1))
		assert(tree.insert(6, 1))
		assert(tree.insert(5, 1))
		assert(tree.insert(7, 1))
		assert(tree.insert(8, 1))

		assert(tree.delete(6))

		assert(tree.size == 4)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete right node with two children, right subtree root has no left children")
	fun deleteRightNodeTwoChildrenRightSubtreeRootHasNoLeftChildren() {
		assert(tree.insert(10, 1))
		assert(tree.insert(15, 1))
		assert(tree.insert(12, 1))
		assert(tree.insert(16, 1))
		assert(tree.insert(18, 1))

		assert(tree.delete(15))

		assert(tree.size == 4)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete node with two children, right subtree root has left children, right subtree min is a leaf")
	fun deleteNodeTwoChildrenRightSubtreeRootHasNoLeftChildrenRightSubtreeMinLeaf() {
		assert(tree.insert(10, 1))
		assert(tree.insert(15, 1))
		assert(tree.insert(12, 1))
		assert(tree.insert(20, 1))
		assert(tree.insert(25, 1))
		assert(tree.insert(18, 1))
		assert(tree.insert(19, 1))
		assert(tree.insert(16, 1))

		assert(tree.delete(15))

		assert(tree.size == 7)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete node with two children, right subtree root has left children, right subtree min has right child")
	fun deleteNodeTwoChildrenRightSubtreeRootHasNoLeftChildrenRightSubtreeMinHasRightChild() {
		assert(tree.insert(10, 1))
		assert(tree.insert(15, 1))
		assert(tree.insert(12, 1))
		assert(tree.insert(20, 1))
		assert(tree.insert(25, 1))
		assert(tree.insert(18, 1))
		assert(tree.insert(19, 1))
		assert(tree.insert(16, 1))
		assert(tree.insert(17, 1))

		assert(tree.delete(15))

		assert(tree.size == 8)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("another simple tree test")
	fun simpleTreeSample1() {
		assert(tree.insert(8, 14))
		assert(tree.insert(18, 27))
		assert(tree.insert(5, 5))
		assert(tree.insert(15, 91))
		assert(tree.insert(17, 84))
		assert(tree.insert(25, 25))
		assert(tree.insert(40, 4))
		assert(tree.insert(80, 0))

		assert(tree.size == 8)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("another simple tree test")
	fun simpleTreeSample2() {
		assert(tree.insert(24, 14))
		assert(tree.insert(5, 27))
		assert(tree.insert(1, 5))
		assert(tree.insert(15, 51))

		assert(tree.size == 4)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("simple iterator test")
	fun iterateSimpleTree() {
		repeat(100) {
			assert(tree.insert(it, it))
			assert(tree.insert(100 - it, it))
		}
		for (keyValuePair in tree) {
			assert(keyValuePair.second == tree.search(keyValuePair.first))
		}
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("iterate through empty tree test")
	fun iterateEmptyTree() {
		var cnt = 0
		for (keyValuePair in tree) ++cnt
		assert(cnt == 0)

		assert(checkTreeInvariants())
	}
}
