# name 
selectkey

# return
KStream → KStream

# description

Assigns a new key – possibly of a new key type – to each record. (details)

Marks the stream for data re-partitioning: Applying a grouping or a join after flatMap will result in re-partitioning of the records.

# code
## KStream


    /**
     * Set a new key (with possibly new type) for each input record.
     * The provided {@link KeyValueMapper} is applied to each input record and computes a new key for it.
     * Thus, an input record {@code <K,V>} can be transformed into an output record {@code <K':V>}.
     * This is a stateless record-by-record operation.
     * <p>
     * For example, you can use this transformation to set a key for a key-less input record {@code <null,V>} by
     * extracting a key from the value within your {@link KeyValueMapper}. The example below computes the new key as the
     * length of the value string.
     * <pre>{@code
     * KStream<Byte[], String> keyLessStream = builder.stream("key-less-topic");
     * KStream<Integer, String> keyedStream = keyLessStream.selectKey(new KeyValueMapper<Byte[], String, Integer> {
     *     Integer apply(Byte[] key, String value) {
     *         return value.length();
     *     }
     * });
     * }</pre>
     * <p>
     * Setting a new key might result in an internal data redistribution if a key based operator (like an aggregation or
     * join) is applied to the result {@code KStream}.
     *
     * @param mapper a {@link KeyValueMapper} that computes a new key for each record
     * @param <KR>   the new key type of the result stream
     * @return a {@code KStream} that contains records with new key (possibly of different type) and unmodified value
     * @see #map(KeyValueMapper)
     * @see #flatMap(KeyValueMapper)
     * @see #mapValues(ValueMapper)
     * @see #flatMapValues(ValueMapper)
     */
    <KR> KStream<KR, V> selectKey(KeyValueMapper<? super K, ? super V, ? extends KR> mapper);

    
    