# name 
map

# return
KStream → KStream

# description

Takes one record and produces zero, one, or more records. You can modify the record keys and values, including their types. (details)

Marks the stream for data re-partitioning: Applying a grouping or a join after flatMap will result in re-partitioning of the records. If possible use flatMapValues instead, which will not cause data re-partitioning.

# code
## KStream

    /**
     * Transform each record of the input stream into zero or more records in the output stream (both key and value type
     * can be altered arbitrarily).
     * The provided {@link KeyValueMapper} is applied to each input record and computes zero or more output records.
     * Thus, an input record {@code <K,V>} can be transformed into output records {@code <K':V'>, <K'':V''>, ...}.
     * This is a stateless record-by-record operation (cf. {@link #transform(TransformerSupplier, String...)} for
     * stateful record transformation).
     * <p>
     * The example below splits input records {@code <null:String>} containing sentences as values into their words
     * and emit a record {@code <word:1>} for each word.
     * <pre>{@code
     * KStream<byte[], String> inputStream = builder.stream("topic");
     * KStream<String, Integer> outputStream = inputStream.flatMap(new KeyValueMapper<byte[], String, Iterable<KeyValue<String, Integer>>>() {
     *     Iterable<KeyValue<String, Integer>> apply(byte[] key, String value) {
     *         String[] tokens = value.split(" ");
     *         List<KeyValue<String, Integer>> result = new ArrayList<>(tokens.length);
     *
     *         for(String token : tokens) {
     *             result.add(new KeyValue<>(token, 1));
     *         }
     *
     *         return result;
     *     }
     * });
     * }</pre>
     * <p>
     * The provided {@link KeyValueMapper} must return an {@link Iterable} (e.g., any {@link java.util.Collection} type)
     * and the return value must not be {@code null}.
     * <p>
     * Flat-mapping records might result in an internal data redistribution if a key based operator (like an aggregation
     * or join) is applied to the result {@code KStream}. (cf. {@link #flatMapValues(ValueMapper)})
     *
     * @param mapper a {@link KeyValueMapper} that computes the new output records
     * @param <KR>   the key type of the result stream
     * @param <VR>   the value type of the result stream
     * @return a {@code KStream} that contains more or less records with new key and value (possibly of different type)
     * @see #selectKey(KeyValueMapper)
     * @see #map(KeyValueMapper)
     * @see #mapValues(ValueMapper)
     * @see #flatMapValues(ValueMapper)
     * @see #transform(TransformerSupplier, String...)
     * @see #transformValues(ValueTransformerSupplier, String...)
     */
    <KR, VR> KStream<KR, VR> flatMap(final KeyValueMapper<? super K, ? super V, ? extends Iterable<? extends KeyValue<? extends KR, ? extends VR>>> mapper);

    
    