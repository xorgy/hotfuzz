class FullTextMergeIndex<T : Any>(val depth: Int = 6) : FullTextIndex<T> {
    val index = HashMap<T, HashSet<String>>();
    override fun insert(k: T, index_string: String) {
        val gs = mkgrams(index_string, depth);
        index.getOrPut(k) { HashSet<String>() }.addAll(gs);
    }
    override fun removeK(k: T) {
        index.remove(k);
    }

    override fun query(query: String): List<T> {
        val gs = mkgrams(query, depth);
        return index.entries.sortedByDescending{ (_, v) -> gs.intersect(v).count() }.map { (k, _) -> k };
    }

    fun toInvert() : FullTextInvertIndex<T> {
        val invert = FullTextInvertIndex<T>();
        index.forEach { (key, gs) ->
            gs.iterator().forEach {
                invert.index.getOrPut(it) { HashSet<T>() }.add(key);
            }
        }
        return invert;
    }

}