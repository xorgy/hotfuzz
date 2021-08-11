interface FullTextQueriable<T : Any> {
    abstract fun query(query: String) : List<T>;
    fun queryBounded(query: String) : List<T> {
        return this.query(boundWrap(query));
    };
}