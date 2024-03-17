package nodes

class TreeNode<K : Comparable<K>, V>(var key: K, var value: V) {
    var left: TreeNode<K, V>? = null
    var right: TreeNode<K, V>? = null
}