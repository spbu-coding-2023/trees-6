package nodes

open class TreeNode<K : Comparable<K>, V, N : TreeNode<K, V, N>>(
    internal var key: K,
    internal var value: V
) {
    internal open var left: N? = null
    internal open var right: N? = null
}

