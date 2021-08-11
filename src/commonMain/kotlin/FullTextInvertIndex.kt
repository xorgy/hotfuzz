class FullTextInvertIndex<T : Any>(val depth: Int = 6) : FullTextIndex<T> {
    val index = HashMap<String, HashSet<T>>();
    override fun insert(k: T, index_string: String) {
        val gs = mkgrams(index_string, depth);
        gs.forEach {
            index.getOrPut(it) { HashSet<T>() }.add(k);
        }
    }
    override fun removeK(k: T) {
        index.entries.forEach {
            it.value.remove(k);
            if (it.value.isEmpty()) {
                index.remove(it.key);
            }
        }
    }

    override fun query(query: String): List<T> {
        val gs = mkgrams(query, depth);
        val accum_map = HashMap<T, Int>();
        gs.forEach {
            index[it]?.forEach { k ->
                accum_map[k] = accum_map.getOrElse(k) {0} + 1;
            }
        }
        return accum_map.entries.sortedByDescending{ (_, v) -> v}.map { (k, _) -> k };
    }

    fun toMerge() : FullTextMergeIndex<T> {
        val merge = FullTextMergeIndex<T>();
        index.forEach { (gram, ks) ->
            ks.iterator().forEach {
                merge.index.getOrPut(it) { HashSet<String>() }.add(gram);
            }
        }
        return merge;
    }
}