import nodes.AvlNode
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import trees.AvlTree
class AvlTests
{
    private lateinit var tree: AvlTree<Int, Int>



    private fun checkInvariant_BST() : Boolean
    {
        val stack: MutableList<AvlNode<Int, Int>?>
        var currNode: AvlNode<Int, Int>? = tree.root ?: return true
        var child: AvlNode<Int, Int>?
        stack = mutableListOf(currNode)
        while (stack.size > 0) {
            currNode = stack[stack.size - 1]
            stack.removeAt(stack.size - 1)
            if (currNode != null) {
                child = currNode.left
                if (child != null) {
                    if (child.key > currNode.key) return false
                    stack.add(child)
                }
                child = currNode.right
                if (child != null) {
                    if (child.key < currNode.key) return false
                    stack.add(child)
                }
            }
        }
        return true
    }

    private fun checkInvariant_AVL() : Boolean
    {
        var currNode: AvlNode<Int, Int> = tree.root ?: return true

        val allNodes = mutableListOf<AvlNode<Int,Int>>();

        getAllNodes(currNode, allNodes);

        for (i in allNodes)
        {
            if(Math.abs(getTreeHeight(i.left) - getTreeHeight(i.right)) >= 2) return false;
        }
        return true;

    }

    private fun getTreeHeight(rootNode: AvlNode<Int, Int>?) : Int
    {
        if(rootNode == null) return 0;

        return Math.max(getTreeHeight(rootNode.left), getTreeHeight(rootNode.right)) + 1;

    }
    private fun getAllNodes(rootNode : AvlNode<Int,Int>, list : MutableList<AvlNode<Int,Int>> )
    {
        list.add(rootNode);

        rootNode.left?.let {getAllNodes(it,list)  }
        rootNode.right?.let {getAllNodes(it,list)  }
    }


    private fun checkInvariant(): Boolean {
        return checkInvariant_BST() && checkInvariant_AVL();
    }


    @BeforeEach
    fun setup() {
        tree = AvlTree()
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
        assert(checkInvariant())
        assert(tree.size == 5)
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
        assert(checkInvariant())
        assert(tree.size == 5)
    }

    @Test
    @DisplayName("search non-existent node")
    fun searchDefunctNode() {
        assert(tree.insert(5, 2))
        assert(tree.insert(15, 7))
        val value = tree.search(10)
        assert(value == null)
        assert(checkInvariant())
        assert(tree.size == 2)
    }

    @Test
    @DisplayName("change value of existing node")
    fun changeValue() {
        assert(tree.insert(5, 2))
        assert(tree.insert(5, 6))
        val value = tree.search(5)
        assert(value == 6)
        assert(checkInvariant())
        assert(tree.size == 1)
    }

    @Test
    @DisplayName("simple deletion test")
    fun deleteLeafNode() {
        assert(tree.insert(10, 5))
        assert(tree.insert(5, 2))
        assert(tree.insert(15, 3))
        assert(tree.delete(15))
        assert(checkInvariant())
        assert(tree.size == 2)
    }

    @Test
    @DisplayName("delete non-existent node")
    fun deleteDefunctNode() {
        assert(tree.insert(10, 1))
        assert(tree.insert(5, 1))
        assert(tree.insert(15, 1))
        assert(!tree.delete(1))
        assert(checkInvariant())
        assert(tree.size == 3)
    }

    @Test
    @DisplayName("delete single root node")
    fun deleteSingleRoot() {
        assert(tree.insert(1, 5))
        assert(tree.delete(1))
        assert(tree.size == 0)
    }

    @Test
    @DisplayName("delete left leaf with no child")
    fun deleteLeftLeafNoChild() {
        assert(tree.insert(10, 1))
        assert(tree.insert(5, 1))
        assert(tree.insert(15, 1))
        assert(tree.delete(5))
        assert(checkInvariant())
        assert(tree.size == 2)
    }

    @Test
    @DisplayName("delete right leaf with no child")
    fun deleteRightLeafNoChild() {
        assert(tree.insert(10, 1))
        assert(tree.insert(5, 1))
        assert(tree.insert(15, 1))
        assert(tree.delete(15))
        assert(checkInvariant())
        assert(tree.size == 2)
    }

    @Test
    @DisplayName("delete root with only left child")
    fun deleteRootOnlyLeftChild() {
        assert(tree.insert(10, 1))
        assert(tree.insert(5, 1))
        assert(tree.delete(10))
        assert(tree.root?.key == 5)
        assert(tree.size == 1)
    }

    @Test
    @DisplayName("delete root with only right child")
    fun deleteRootOnlyRightChild() {
        assert(tree.insert(10, 1))
        assert(tree.insert(15, 1))
        assert(tree.delete(10))
        assert(tree.root?.key == 15)
        assert(tree.size == 1)
    }

    @Test
    @DisplayName("delete left node with left child")
    fun deleteLeftNodeLeftChild() {
        assert(tree.insert(10, 1))
        assert(tree.insert(9, 1))
        assert(tree.insert(8, 1))
        assert(tree.delete(9))
        assert(checkInvariant())
        assert(tree.size == 2)
    }

    @Test
    @DisplayName("delete left node with right child")
    fun deleteLeftNodeRightChild() {
        assert(tree.insert(10, 1))
        assert(tree.insert(8, 1))
        assert(tree.insert(9, 1))
        assert(tree.delete(8))
        assert(checkInvariant())
        assert(tree.size == 2)
    }

    @Test
    @DisplayName("delete right node with left child")
    fun deleteRightNodeLeftChild() {
        assert(tree.insert(10, 1))
        assert(tree.insert(12, 1))
        assert(tree.insert(11, 1))
        assert(tree.delete(12))
        assert(checkInvariant())
        assert(tree.size == 2)
    }

    @Test
    @DisplayName("delete right node with right child")
    fun deleteRightNodeRightChild() {
        assert(tree.insert(10, 1))
        assert(tree.insert(11, 1))
        assert(tree.insert(12, 1))
        assert(tree.delete(11))
        assert(checkInvariant())
        assert(tree.size == 2)
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
        assert(checkInvariant())
        assert(tree.size == 4)
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
        assert(checkInvariant())
        assert(tree.size == 4)
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
        assert(checkInvariant())
        assert(tree.size == 4)
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
        assert(checkInvariant())
        assert(tree.size == 7)
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
        assert(checkInvariant())
        assert(tree.size == 8)
    }

    @Test
    @DisplayName("simple iterator test")
    fun iterateSimpleTree() {
        repeat(100) {
            assert(tree.insert(it, it))
        }
        for (keyValuePair in tree) {
            assert(keyValuePair.second == tree.search(keyValuePair.first))
        }
        assert(checkInvariant())
    }

    @Test
    @DisplayName("iterate through empty tree test")
    fun iterateEmptyTree() {
        var cnt = 0
        for (keyValuePair in tree) ++cnt
        assert(cnt == 0)
    }
}
