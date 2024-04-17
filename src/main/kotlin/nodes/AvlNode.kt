package nodes

import kotlin.math.max

class AvlNode<K : Comparable<K>, V>(key: K, value: V) : TreeNode<K,V,AvlNode<K,V>>(key,value)
{
    internal var height : Int = 1
        private set




    public fun updateHeight()
    {
        val leftHeight = left?.height ?: 0
        val rightHeight = right?.height ?: 0
        height = max(leftHeight, rightHeight) + 1;
    }

}