# name 
mapValues

# return
KStream → KStream
KTable → KTable

# description

Takes one record and produces one record, while retaining the key of the original record. You can modify the record value and the value type. (KStream details, KTable details)

mapValues is preferable to map because it will not cause data re-partitioning. However, it does not allow you to modify the key or key type like map does.

# code
## KStream


    /**
     * Transform the value of each input record into a new value (with possible new type) of the output record.
     * The provided {@link ValueMapper} is applied to each input record value and computes a new value for it.
     * Thus, an input record {@code <K,V>} can be transformed into an output record {@code <K:V'>}.
     * This is a stateless record-by-record operation (cf.
     * {@link #transformValues(ValueTransformerSupplier, String...)} for stateful value transformation).
     * <p>
     * The example below counts the number of token of the value string.
     * <pre>{@code
     * KStream<String, String> inputStream = builder.stream("topic");
     * KStream<String, Integer> outputStream = inputStream.mapValues(new ValueMapper<String, Integer> {
     *     Integer apply(String value) {
     *         return value.split(" ").length;
     *     }
     * });
     * }</pre>
     * <p>
     * Setting a new value preserves data co-location with respect to the key.
     * Thus, <em>no</em> internal data redistribution is required if a key based operator (like an aggregation or join)
     * is applied to the result {@code KStream}. (cf. {@link #map(KeyValueMapper)})
     *
     * @param mapper a {@link ValueMapper} that computes a new output value
     * @param <VR>   the value type of the result stream
     * @return a {@code KStream} that contains records with unmodified key and new values (possibly of different type)
     * @see #selectKey(KeyValueMapper)
     * @see #map(KeyValueMapper)
     * @see #flatMap(KeyValueMapper)
     * @see #flatMapValues(ValueMapper)
     * @see #transform(TransformerSupplier, String...)
     * @see #transformValues(ValueTransformerSupplier, String...)
     */
    <VR> KStream<K, VR> mapValues(ValueMapper<? super V, ? extends VR> mapper);

## KTable
    /**
     * Create a new {@code KTable} by transforming the value of each record in this {@code KTable} into a new value
     * (with possible new type)in the new {@code KTable}.
     * For each {@code KTable} update the provided {@link ValueMapper} is applied to the value of the update record and
     * computes a new value for it, resulting in an update record for the result {@code KTable}.
     * Thus, an input record {@code <K,V>} can be transformed into an output record {@code <K:V'>}.
     * This is a stateless record-by-record operation.
     * <p>
     * The example below counts the number of token of the value string.
     * <pre>{@code
     * KTable<String, String> inputTable = builder.table("topic");
     * KTable<String, Integer> outputTable = inputTable.mapValue(new ValueMapper<String, Integer> {
     *     Integer apply(String value) {
     *         return value.split(" ").length;
     *     }
     * });
     * }</pre>
     * <p>
     * This operation preserves data co-location with respect to the key.
     * Thus, <em>no</em> internal data redistribution is required if a key based operator (like a join) is applied to
     * the result {@code KTable}.
     * <p>
     * Note that {@code mapValues} for a <i>changelog stream</i> works different to {@link KStream#mapValues(ValueMapper)
     * record stream filters}, because {@link KeyValue records} with {@code null} values (so-called tombstone records)
     * have delete semantics.
     * Thus, for tombstones the provided value-mapper is not evaluated but the tombstone record is forwarded directly to
     * delete the corresponding record in the result {@code KTable}.
     *
     * @param mapper a {@link ValueMapper} that computes a new output value
     * @param <VR>   the value type of the result {@code KTable}
     * @return a {@code KTable} that contains records with unmodified keys and new values (possibly of different type)
     */
    <VR> KTable<K, VR> mapValues(final ValueMapper<? super V, ? extends VR> mapper);

    

