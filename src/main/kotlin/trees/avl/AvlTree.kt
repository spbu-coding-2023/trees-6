package trees.avl

import nodes.AvlNode
import trees.AbstractTree

class AvlTree<K : Comparable<K>, V> : AbstractTree<K,V,AvlNode<K,V>>() {
    override fun createNode(key: K, value: V): AvlNode<K, V> = AvlNode(key, value);

    override fun iterator(): Iterator<Pair<K, V>> =  AvlTreeIterator(getPairs())

    private fun getPairs(): MutableList<Pair<K, V>> {
        val trajectory = mutableListOf<Pair<K, V>>()
        val stack: MutableList<AvlNode<K, V>?>
        var currNode: AvlNode<K, V>? = root ?: return mutableListOf()
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
    private fun balanceFactor(node: AvlNode<K, V>) = (node.right?.height ?: 0) - (node.left?.height ?: 0)


    private fun balanceTree(node : AvlNode<K,V>?) : AvlNode<K,V>?
    {
        if(node == null) return null;
        node.left =balanceTree(node.left);
        node.right = balanceTree(node.right);
        return balance(node);


    }

    private fun balance(node: AvlNode<K, V>): AvlNode<K, V> {
        node.updateHeight();
        val bf = balanceFactor(node);
        if (bf == 2)
        {
            val tempNode = node.right;
            if (tempNode != null && balanceFactor(tempNode) < 0) node.right = rightRotate(tempNode);

            return leftRotate(node);
        }

        if (bf == -2)
        {
            val tempNode = node.left;

            if (tempNode != null && balanceFactor(tempNode) > 0) node.left = leftRotate(tempNode);

            return rightRotate(node);
        }
        return node;
    }

    private fun rightRotate(treeRoot: AvlNode<K, V>): AvlNode<K,V>
    {
        val node = treeRoot.left;
        if (node != null) {
            treeRoot.left = node.right
            node.right = treeRoot;
            node.right?.updateHeight()
            node.left?.updateHeight();
            node.updateHeight();
            return node;
        };

        return treeRoot;
    }

    private fun leftRotate(treeRoot: AvlNode<K, V>): AvlNode<K,V>
    {
        val node = treeRoot.right;
        if(node != null)
        {
            treeRoot.right = node.left;
            node.left = treeRoot;


            node.right?.updateHeight()
            node.left?.updateHeight();
            node.updateHeight();
            return node;
        }
        return treeRoot;
    }




    override fun insert(key: K, value: V): Boolean {
        val result = super.insert(key, value);
        root = balanceTree(root);
        return result;
    }

    override fun delete(key: K): Boolean {
        val result = super.delete(key);
        root = balanceTree(root);
        return result;
    }





}