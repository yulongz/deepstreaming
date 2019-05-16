# name 
to

# return
KStream -> void
KTable -> void

# description

Terminal operation. Write the records to a Kafka topic. (KStream details, KTable details)

When to provide serdes explicitly:

If you do not specify serdes explicitly, the default serdes from the configuration are used.
You must specificy serdes explicitly if the key and/or value types of the KStream or KTable do not match the configured default serdes.
See Data types and serialization for information about configuring default serdes, available serdes, and implementing your own custom serdes.
Several variants of to exist to e.g. specify a custom StreamPartitioner that gives you control over how output records are distributed across the partitions of the output topic.

Causes data re-partitioning if any of the following conditions is true:

If the output topic has a different number of partitions than the stream/table.
If the KStream was marked for re-partitioning.
If you provide a custom StreamPartitioner to explicitly control how to distribute the output records across the partitions of the output topic.
If the key of an output record is null.


# code
## KStream


    /**
     * Materialize this stream to a topic using default serializers specified in the config and producer's
     * {@link DefaultPartitioner}.
     * The specified topic should be manually created before it is used (i.e., before the Kafka Streams application is
     * started).
     *
     * @param topic the topic name
     */
    void to(final String topic);

    /**
     * Materialize this stream to a topic using default serializers specified in the config and a customizable
     * {@link StreamPartitioner} to determine the distribution of records to partitions.
     * The specified topic should be manually created before it is used (i.e., before the Kafka Streams application is
     * started).
     *
     * @param partitioner the function used to determine how records are distributed among partitions of the topic,
     *                    if not specified producer's {@link DefaultPartitioner} will be used
     * @param topic       the topic name
     */
    void to(final StreamPartitioner<? super K, ? super V> partitioner,
            final String topic);

    /**
     * Materialize this stream to a topic. If {@code keySerde} provides a {@link WindowedSerializer WindowedSerializer}
     * for the key {@link WindowedStreamPartitioner} is used&mdash;otherwise producer's {@link DefaultPartitioner} is
     * used.
     * The specified topic should be manually created before it is used (i.e., before the Kafka Streams application is
     * started).
     *
     * @param keySerde key serde used to send key-value pairs,
     *                 if not specified the default serde defined in the configs will be used
     * @param valSerde value serde used to send key-value pairs,
     *                 if not specified the default serde defined in the configs will be used
     * @param topic    the topic name
     */
    void to(final Serde<K> keySerde,
            final Serde<V> valSerde,
            final String topic);

    /**
     * Materialize this stream to a topic using a customizable {@link StreamPartitioner} to determine the distribution
     * of records to partitions.
     * The specified topic should be manually created before it is used (i.e., before the Kafka Streams application is
     * started).
     *
     * @param keySerde    key serde used to send key-value pairs,
     *                    if not specified the default serde defined in the configs will be used
     * @param valSerde    value serde used to send key-value pairs,
     *                    if not specified the default serde defined in the configs will be used
     * @param partitioner the function used to determine how records are distributed among partitions of the topic,
     *                    if not specified and {@code keySerde} provides a {@link  WindowedSerializer} for the key
     *                    {@link WindowedStreamPartitioner} will be used&mdash;otherwise {@link DefaultPartitioner} will
     *                    be used
     * @param topic       the topic name
     */
    void to(final Serde<K> keySerde,
            final Serde<V> valSerde,
            final StreamPartitioner<? super K, ? super V> partitioner,
            final String topic);

## KTable


    /**
     * Materialize this changelog stream to a topic using default serializers and deserializers and producer's
     * {@link DefaultPartitioner}.
     * The specified topic should be manually created before it is used (i.e., before the Kafka Streams application is
     * started).
     *
     * @param topic the topic name
     */
    void to(final String topic);

    /**
     * Materialize this changelog stream to a topic using default serializers and deserializers and a customizable
     * {@link StreamPartitioner} to determine the distribution of records to partitions.
     * The specified topic should be manually created before it is used (i.e., before the Kafka Streams application is
     * started).
     *
     * @param partitioner the function used to determine how records are distributed among partitions of the topic,
     *                    if not specified producer's {@link DefaultPartitioner} will be used
     * @param topic       the topic name
     */
    void to(final StreamPartitioner<? super K, ? super V> partitioner,
            final String topic);

    /**
     * Materialize this changelog stream to a topic.
     * The specified topic should be manually created before it is used (i.e., before the Kafka Streams application is
     * started).
     * <p>
     * If {@code keySerde} provides a {@link WindowedSerializer} for the key {@link WindowedStreamPartitioner} is
     * used&mdash;otherwise producer's {@link DefaultPartitioner} is used.
     *
     * @param keySerde key serde used to send key-value pairs,
     *                 if not specified the default key serde defined in the configuration will be used
     * @param valSerde value serde used to send key-value pairs,
     *                 if not specified the default value serde defined in the configuration will be used
     * @param topic    the topic name
     */
    void to(final Serde<K> keySerde,
            final Serde<V> valSerde,
            final String topic);

    /**
     * Materialize this changelog stream to a topic using a customizable {@link StreamPartitioner} to determine the
     * distribution of records to partitions.
     * The specified topic should be manually created before it is used (i.e., before the Kafka Streams application is
     * started).
     *
     * @param keySerde    key serde used to send key-value pairs,
     *                    if not specified the default key serde defined in the configuration will be used
     * @param valSerde    value serde used to send key-value pairs,
     *                    if not specified the default value serde defined in the configuration will be used
     * @param partitioner the function used to determine how records are distributed among partitions of the topic,
     *                    if not specified and {@code keySerde} provides a {@link WindowedSerializer} for the key
     *                    {@link WindowedStreamPartitioner} will be used&mdash;otherwise {@link DefaultPartitioner} will
     *                    be used
     * @param topic      the topic name
     */
    void to(final Serde<K> keySerde,
            final Serde<V> valSerde,
            final StreamPartitioner<? super K, ? super V> partitioner,
            final String topic);

