package nodes

open class TreeNode<K : Comparable<K>, V, N : TreeNode<K, V, N>>(var key: K, var value: V) {
    open var left: N? = null
    open var right: N? = null
}
