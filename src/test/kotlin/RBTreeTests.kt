import nodes.RBTreeNode
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import trees.RBTree
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

					if (getBH(0, leftChild) != getBH(0, rightChild)) { // BH from this place, there's all balanced up the tree already
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
		} else {
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

	// Tests for BST

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
	@DisplayName("simple tree test")
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
	@DisplayName("delete node")
	fun deleteLeafNode() {
		assert(tree.insert(10, 5))
		assert(tree.insert(5, 2))
		assert(tree.insert(15, 3))

		assert(tree.delete(15))

		assert(tree.size == 2)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete root")
	fun deleteRootNode() {
		assert(tree.insert(10, 5))
		assert(tree.insert(15, 2))
		assert(tree.insert(5, 3))

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

	// Some additional tests (including cases of red-black tree)

	@Test
	@DisplayName("another root deletion test")
	fun deleteRootFromSomeTree1() {
		assert(tree.insert(8, 14))
		assert(tree.insert(18, 27))
		assert(tree.insert(5, 5))
		assert(tree.insert(15, 91))
		assert(tree.insert(17, 84))
		assert(tree.insert(25, 25))
		assert(tree.insert(40, 4))
		assert(tree.insert(80, 0))

		assert(tree.delete(8))

		assert(tree.size == 7)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("another root deletion test")
	fun deleteRootFromSomeTree2() {
		assert(tree.insert(8, 14))
		assert(tree.insert(4, 27))
		assert(tree.insert(2, 5))
		assert(tree.insert(6, 91))
		assert(tree.insert(1, 84))
		assert(tree.insert(3, 25))
		assert(tree.insert(5, 4))
		assert(tree.insert(7, 0))
		assert(tree.insert(12, 0))

		assert(tree.delete(8))

		assert(tree.size == 8)
		assert(checkTreeInvariants())
	}

	// brush all code
	@Test
	@DisplayName("delete red node with left subtree and right leaf")
	fun deleteRedNodeLeftSubtreeRightLeaf() {
		assert(tree.insert(20, 1))
		assert(tree.insert(10, 1))
		assert(tree.insert(40, 1))
		assert(tree.insert(30, 1))
		assert(tree.insert(45, 5))
		assert(tree.insert(33, 90))
		assert(tree.delete(40))

		assert(tree.size == 5)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete red node with no children")
	fun deleteRedNodeNoChildren() {
		assert(tree.insert(20, 7))
		assert(tree.insert(10, 7))
		assert(tree.insert(40, 34))
		assert(tree.insert(30, 55))
		assert(tree.insert(45, 90))
		assert(tree.insert(33, 66))
		assert(tree.delete(33))

		assert(tree.size == 5)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete black node with one right red child")
	fun deleteBlackNodeOneRightRedChild() {
		assert(tree.insert(20, 20))
		assert(tree.insert(10, 10))
		assert(tree.insert(40, 40))
		assert(tree.insert(30, 40))
		assert(tree.insert(45, 40))
		assert(tree.insert(25, 40))
		assert(tree.delete(30))

		assert(tree.size == 5)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete black node with one left red child")
	fun deleteBlackNodeOneLeftRedChild() {
		assert(tree.insert(20, 6))
		assert(tree.insert(10, 4))
		assert(tree.insert(40, 3))
		assert(tree.insert(31, 3))
		assert(tree.insert(45, 2))
		assert(tree.insert(34, 1))
		assert(tree.delete(31))

		assert(tree.size == 5)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete black node with no children and red sibling")
	fun deleteBlackNodeNoChildrenRedSibling() {
		assert(tree.insert(20, 4))
		assert(tree.insert(10, 45))
		assert(tree.insert(40, 55))
		assert(tree.insert(30, 19))
		assert(tree.insert(45, 10))
		assert(tree.insert(33, 3))
		assert(tree.delete(10))

		assert(tree.size == 5)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete black node with left subtree and right leaf")
	fun deleteBlackNodeLeftSubtreeRightLeaf() {
		assert(tree.insert(100, 76))
		assert(tree.insert(80, 67))
		assert(tree.insert(120, 4))
		assert(tree.insert(60, 6))
		assert(tree.insert(90, 7))
		assert(tree.insert(65, 5))
		assert(tree.delete(100))

		assert(tree.size == 5)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete left black node with red right child")
	fun deleteLeftBlackNodeRedRightChild() {
		assert(tree.insert(100, 45))
		assert(tree.insert(80, 567))
		assert(tree.insert(120, 2))
		assert(tree.insert(60, 3))
		assert(tree.insert(90, 4))
		assert(tree.insert(88, 5))
		assert(tree.delete(60))

		assert(tree.size == 5)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete black node with no children and black sibling")
	fun deleteBlackNodeNoChildrenBlackSibling() {
		assert(tree.insert(110, 5))
		assert(tree.insert(220, 7))
		assert(tree.insert(80, 6))
		assert(tree.insert(230, 5))
		assert(tree.delete(230))
		assert(tree.delete(80))

		assert(tree.size == 2)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete left red node with no children")
	fun deleteLeftRedNodeNoChildren() {
		assert(tree.insert(23, 1))
		assert(tree.insert(11, 2))
		assert(tree.insert(37, 3))
		assert(tree.insert(17, 4))

		assert(tree.delete(17))

		assert(tree.size == 3)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete red node with two black children")
	fun deleteRedNodeTwoBlackChildren() {
		assert(tree.insert(23, 1))
		assert(tree.insert(37, 2))
		assert(tree.insert(20, 3))
		assert(tree.insert(19, 4))
		assert(tree.insert(22, 5))
		assert(tree.delete(20))

		assert(tree.size == 4)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete black node with one red child")
	fun deleteBlackNodeOneRedChild() {
		assert(tree.insert(5, 9))
		assert(tree.insert(3, 9))
		assert(tree.insert(6, 9))
		assert(tree.insert(4, 9))
		assert(tree.insert(1, 9))
		assert(tree.insert(7, 9))
		assert(tree.delete(6))

		assert(tree.size == 5)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete black node with two children")
	fun deleteBlackNodeTwoChildren() {
		assert(tree.insert(5, 4))
		assert(tree.insert(3, 3))
		assert(tree.insert(6, 2))
		assert(tree.insert(4, 1))
		assert(tree.insert(1, 0))
		assert(tree.delete(3))

		assert(tree.size == 4)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete black node with no children and black sibling which has left red child")
	fun deleteBlackNodeNoChildrenBlackSiblingLeftRedChild() {
		assert(tree.insert(3, -1))
		assert(tree.insert(1, -2))
		assert(tree.insert(5, -3))
		assert(tree.insert(4, -4))
		assert(tree.delete(1))

		assert(tree.size == 3)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete black node with no children and black sibling which has right red child")
	fun deleteBlackNodeNoChildrenBlackSiblingRightRedChild() {
		assert(tree.insert(3, 4))
		assert(tree.insert(1, 4))
		assert(tree.insert(5, 4))
		assert(tree.insert(6, 5))
		assert(tree.delete(1))

		assert(tree.size == 3)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("delete black node with no children and red sibling which has only black children")
	fun deleteBlackNodeNoChildrenRedSiblingBlackChildren() {
		assert(tree.insert(6, -1))
		assert(tree.insert(4, 1))
		assert(tree.insert(7, -1))
		assert(tree.insert(2, 1))
		assert(tree.insert(5, -1))
		assert(tree.insert(1, 1))
		assert(tree.delete(7))

		assert(tree.size == 5)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("full deletion test of some tree in different orders")
	fun fullDeletion1() {
		assert(tree.insert(8, 14))
		assert(tree.insert(4, 27))
		assert(tree.insert(2, 5))
		assert(tree.insert(6, 91))
		assert(tree.insert(1, 84))
		assert(tree.insert(3, 25))
		assert(tree.insert(5, 4))
		assert(tree.insert(7, 0))
		assert(tree.insert(12, 0))
		assert(tree.size == 9)

		assert(tree.delete(8))
		assert(!tree.delete(9))
		assert(tree.delete(12))
		assert(tree.delete(1))
		assert(tree.delete(3))
		assert(tree.delete(7))
		assert(tree.delete(4))
		assert(!tree.delete(8))
		assert(tree.delete(5))
		assert(tree.delete(2))
		assert(tree.delete(6))

		assert(tree.size == 0)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("full deletion test of some tree in different orders")
	fun fullDeletion2() {
		assert(tree.insert(8, 14))
		assert(tree.insert(4, 27))
		assert(tree.insert(2, 5))
		assert(tree.insert(6, 91))
		assert(tree.insert(1, 84))
		assert(tree.insert(3, 25))
		assert(tree.insert(7, 4))
		assert(tree.insert(5, 0))
		assert(tree.insert(12, 0))
		assert(tree.size == 9)

		assert(tree.delete(8))
		assert(!tree.delete(9))
		assert(tree.delete(12))
		assert(tree.delete(1))
		assert(tree.delete(3))
		assert(tree.delete(7))
		assert(tree.delete(4))
		assert(!tree.delete(8))
		assert(tree.delete(5))
		assert(tree.delete(2))
		assert(tree.delete(6))

		assert(tree.size == 0)
		assert(checkTreeInvariants())
	}

	@Test
	@DisplayName("full deletion test of some tree in different orders")
	fun fullDeletion3() {
		assert(tree.insert(8, 14))
		assert(tree.insert(4, 27))
		assert(tree.insert(2, 5))
		assert(tree.insert(6, 91))
		assert(tree.insert(1, 84))
		assert(tree.insert(3, 25))
		assert(tree.insert(5, 4))
		assert(tree.insert(7, 0))
		assert(tree.insert(12, 0))
		assert(tree.size == 9)

		assert(tree.delete(8))
		assert(!tree.delete(9))
		assert(tree.delete(1))
		assert(tree.delete(3))
		assert(tree.delete(7))
		assert(tree.delete(4))
		assert(!tree.delete(8))
		assert(tree.delete(5))
		assert(tree.delete(12))
		assert(tree.delete(2))
		assert(tree.delete(6))

		assert(tree.size == 0)
		assert(checkTreeInvariants())

	}

	@Test
	@DisplayName("full deletion test of some tree in different orders")
	fun fullDeletion4() {
		assert(tree.insert(8, 14))
		assert(tree.insert(4, 27))
		assert(tree.insert(2, 5))
		assert(tree.insert(6, 91))
		assert(tree.insert(1, 84))
		assert(tree.insert(3, 25))
		assert(tree.insert(5, 4))
		assert(tree.insert(7, 0))
		assert(tree.insert(12, 0))
		assert(tree.size == 9)

		assert(tree.delete(8))
		assert(!tree.delete(9))
		assert(tree.delete(1))
		assert(tree.delete(3))
		assert(tree.delete(7))
		assert(tree.delete(4))
		assert(tree.delete(12))
		assert(!tree.delete(8))
		assert(tree.delete(5))
		assert(tree.delete(2))
		assert(tree.delete(6))

		assert(tree.size == 0)
		assert(checkTreeInvariants())

	}

	// Iterator tests for BST

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
