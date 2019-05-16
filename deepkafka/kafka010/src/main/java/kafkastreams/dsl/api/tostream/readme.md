# name 
toStream

# return
KTable → KStream

# description

Converts this table into a stream. (details)

# code
## KStream

    /**
     * Convert this changelog stream to a {@link KStream}.
     * <p>
     * Note that this is a logical operation and only changes the "interpretation" of the stream, i.e., each record of
     * this changelog stream is no longer treated as an update record (cf. {@link KStream} vs {@code KTable}).
     *
     * @return a {@link KStream} that contains the same records as this {@code KTable}
     */
    KStream<K, V> toStream();

    /**
     * Convert this changelog stream to a {@link KStream} using the given {@link KeyValueMapper} to select the new key.
     * <p>
     * For example, you can compute the new key as the length of the value string.
     * <pre>{@code
     * KTable<String, String> table = builder.table("topic");
     * KTable<Integer, String> keyedStream = table.toStream(new KeyValueMapper<String, String, Integer> {
     *     Integer apply(String key, String value) {
     *         return value.length();
     *     }
     * });
     * }</pre>
     * Setting a new key might result in an internal data redistribution if a key based operator (like an aggregation or
     * join) is applied to the result {@link KStream}.
     * <p>
     * This operation is equivalent to calling
     * {@code table.}{@link #toStream() toStream}{@code ().}{@link KStream#selectKey(KeyValueMapper) selectKey(KeyValueMapper)}.
     * <p>
     * Note that {@link #toStream()} is a logical operation and only changes the "interpretation" of the stream, i.e.,
     * each record of this changelog stream is no longer treated as an update record (cf. {@link KStream} vs {@code KTable}).
     *
     * @param mapper a {@link KeyValueMapper} that computes a new key for each record
     * @param <KR> the new key type of the result stream
     * @return a {@link KStream} that contains the same records as this {@code KTable}
     */
    <KR> KStream<KR, V> toStream(final KeyValueMapper<? super K, ? super V, ? extends KR> mapper);
