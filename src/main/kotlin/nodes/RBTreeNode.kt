package nodes

class RBTreeNode<K: Comparable<K>, V>(key: K, value: V) : TreeNode<K, V, RBTreeNode<K, V>>(key, value) {
	internal enum class Color {
		Red,
		Black
	}

	private var color: Color = Color.Red

	internal fun setColor(color: Color) {
		this.color = color
	}

	internal fun isRed() = this.color == Color.Red
}
