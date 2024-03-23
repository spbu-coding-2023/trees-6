package nodes

open class TreeNode<K : Comparable<K>, V, N : TreeNode<K, V, N>>(var key: K, var value: V) {
    var left: N? = null
    var right: N? = null
}
