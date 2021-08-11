interface FullTextIndex<T : Any> : FullTextQueriable<T> {
    abstract fun insert(k: T, index_string: String);
    abstract fun removeK(k: T);
    fun insertBounded(k: T, index_string: String) {
        this.insert(k, boundWrap(index_string));
    };
}