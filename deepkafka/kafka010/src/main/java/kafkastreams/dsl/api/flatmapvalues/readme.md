# name 
flatMapValues

# return
KStream → KStream

# description

Takes one record and produces zero, one, or more records, while retaining the key of the original record. You can modify the record values and the value type. (details)

flatMapValues is preferable to flatMap because it will not cause data re-partitioning. However, it does not allow you to modify the key or key type like flatMap does.

# code
## KStream

    /**
     * Create a new {@code KStream} by transforming the value of each record in this stream into zero or more values
     * with the same key in the new stream.
     * Transform the value of each input record into zero or more records with the same (unmodified) key in the output
     * stream (value type can be altered arbitrarily).
     * The provided {@link ValueMapper} is applied to each input record and computes zero or more output values.
     * Thus, an input record {@code <K,V>} can be transformed into output records {@code <K:V'>, <K:V''>, ...}.
     * This is a stateless record-by-record operation (cf. {@link #transformValues(ValueTransformerSupplier, String...)}
     * for stateful value transformation).
     * <p>
     * The example below splits input records {@code <null:String>} containing sentences as values into their words.
     * <pre>{@code
     * KStream<byte[], String> inputStream = builder.stream("topic");
     * KStream<byte[], String> outputStream = inputStream.flatMapValues(new ValueMapper<String, Iterable<String>> {
     *     Iterable<String> apply(String value) {
     *         return Arrays.asList(value.split(" "));
     *     }
     * });
     * }</pre>
     * <p>
     * The provided {@link ValueMapper} must return an {@link Iterable} (e.g., any {@link java.util.Collection} type)
     * and the return value must not be {@code null}.
     * <p>
     * Splitting a record into multiple records with the same key preserves data co-location with respect to the key.
     * Thus, <em>no</em> internal data redistribution is required if a key based operator (like an aggregation or join)
     * is applied to the result {@code KStream}. (cf. {@link #flatMap(KeyValueMapper)})
     *
     * @param processor a {@link ValueMapper} the computes the new output values
     * @param <VR>      the value type of the result stream
     * @return a {@code KStream} that contains more or less records with unmodified keys and new values of different type
     * @see #selectKey(KeyValueMapper)
     * @see #map(KeyValueMapper)
     * @see #flatMap(KeyValueMapper)
     * @see #mapValues(ValueMapper)
     * @see #transform(TransformerSupplier, String...)
     * @see #transformValues(ValueTransformerSupplier, String...)
     */
    <VR> KStream<K, VR> flatMapValues(final ValueMapper<? super V, ? extends Iterable<? extends VR>> processor);