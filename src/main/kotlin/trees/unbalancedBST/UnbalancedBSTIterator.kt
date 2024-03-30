package trees.unbalancedBST

class UnbalancedBSTIterator<K : Comparable<K>, V>(treePairs: MutableList<Pair<K, V>>) : Iterator<Pair<K, V>> {

    private var pairIndex = 0
    private val trajectory = treePairs

    override fun hasNext(): Boolean = pairIndex < trajectory.size
    override fun next(): Pair<K, V> = trajectory[pairIndex++]
}
