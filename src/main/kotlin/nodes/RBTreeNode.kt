package nodes

class RBTreeNode<K: Comparable<K>, V>(key: K, value: V) : TreeNode<K, V, RBTreeNode<K, V>>(key, value) {
	enum class Color {
		Red,
		Black
	}

	internal var color: Color = Color.Red
		private set

	internal fun setColor(color: Color) {
		this.color = color
	}

	internal fun isRed() = this.color == Color.Red
}
