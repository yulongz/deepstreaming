# name 
groupBy

# return
KStream → KGroupedStream
KTable → KGroupedTable

# description

Groups the records by a new key, which may be of a different key type. When grouping a table, you may also specify a new value and value type. groupBy is a shorthand for selectKey(...).groupByKey(). (KStream details, KTable details)

Grouping is a prerequisite for aggregating a stream or a table and ensures that data is properly partitioned (“keyed”) for subsequent operations.

When to set explicit serdes: Variants of groupBy exist to override the configured default serdes of your application, which you must do if the key and/or value types of the resulting KGroupedStream or KGroupedTable do not match the configured default serdes.

Note

Grouping vs. Windowing: A related operation is windowing, which lets you control how to “sub-group” the grouped records of the same key into so-called windows for stateful operations such as windowed aggregations or windowed joins.
Always causes data re-partitioning: groupBy always causes data re-partitioning. If possible use groupByKey instead, which will re-partition data only if required.


# code
## KStream


    /**
     * Group the records by their current key into a {@link KGroupedStream} while preserving the original values
     * and default serializers and deserializers.
     * Grouping a stream on the record key is required before an aggregation operator can be applied to the data
     * (cf. {@link KGroupedStream}).
     * If a record key is {@code null} the record will not be included in the resulting {@link KGroupedStream}.
     * <p>
     * If a key changing operator was used before this operation (e.g., {@link #selectKey(KeyValueMapper)},
     * {@link #map(KeyValueMapper)}, {@link #flatMap(KeyValueMapper)}, or
     * {@link #transform(TransformerSupplier, String...)}), and no data redistribution happened afterwards (e.g., via
     * {@link #through(String)}) an internal repartitioning topic will be created in Kafka.
     * This topic will be named "${applicationId}-XXX-repartition", where "applicationId" is user-specified in
     * {@link StreamsConfig} via parameter {@link StreamsConfig#APPLICATION_ID_CONFIG APPLICATION_ID_CONFIG}, "XXX" is
     * an internally generated name, and "-repartition" is a fixed suffix.
     * You can retrieve all generated internal topic names via {@link KafkaStreams#toString()}.
     * <p>
     * For this case, all data of this stream will be redistributed through the repartitioning topic by writing all
     * records to it, and rereading all records from it, such that the resulting {@link KGroupedStream} is partitioned
     * correctly on its key.
     * If the last key changing operator changed the key type, it is recommended to use
     * {@link #groupByKey(Serde, Serde)} instead.
     *
     * @return a {@link KGroupedStream} that contains the grouped records of the original {@code KStream}
     * @see #groupBy(KeyValueMapper)
     */
    KGroupedStream<K, V> groupByKey();

    /**
     * Group the records by their current key into a {@link KGroupedStream} while preserving the original values.
     * Grouping a stream on the record key is required before an aggregation operator can be applied to the data
     * (cf. {@link KGroupedStream}).
     * If a record key is {@code null} the record will not be included in the resulting {@link KGroupedStream}.
     * <p>
     * If a key changing operator was used before this operation (e.g., {@link #selectKey(KeyValueMapper)},
     * {@link #map(KeyValueMapper)}, {@link #flatMap(KeyValueMapper)}, or
     * {@link #transform(TransformerSupplier, String...)}), and no data redistribution happened afterwards (e.g., via
     * {@link #through(String)}) an internal repartitioning topic will be created in Kafka.
     * This topic will be named "${applicationId}-XXX-repartition", where "applicationId" is user-specified in
     * {@link StreamsConfig} via parameter {@link StreamsConfig#APPLICATION_ID_CONFIG APPLICATION_ID_CONFIG}, "XXX" is
     * an internally generated name, and "-repartition" is a fixed suffix.
     * You can retrieve all generated internal topic names via {@link KafkaStreams#toString()}.
     * <p>
     * For this case, all data of this stream will be redistributed through the repartitioning topic by writing all
     * records to it, and rereading all records from it, such that the resulting {@link KGroupedStream} is partitioned
     * correctly on its key.
     *
     * @param keySerde key serdes for materializing this stream,
     *                 if not specified the default serdes defined in the configs will be used
     * @param valSerde value serdes for materializing this stream,
     *                 if not specified the default serdes defined in the configs will be used
     * @return a {@link KGroupedStream} that contains the grouped records of the original {@code KStream}
     */
    KGroupedStream<K, V> groupByKey(final Serde<K> keySerde,
                                    final Serde<V> valSerde);
