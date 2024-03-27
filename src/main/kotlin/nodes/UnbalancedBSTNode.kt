package nodes

class UnbalancedBSTNode<K : Comparable<K>, V>(key: K, value: V) : TreeNode<K, V, UnbalancedBSTNode<K, V>>(key, value)
