package trees.binarySearchTree

class UnbalancedBSIterator <K : Comparable<K>, V> (treePairs : MutableList<Pair<K, V>>): Iterator<Pair<K, V>> {

    private var pairIndex = 0
    private val trajectory = treePairs

    override fun hasNext(): Boolean {
        return pairIndex < trajectory.size
    }

    override fun next(): Pair<K, V> {
        return trajectory[pairIndex++]
    }
}
