# name 
through

# return
KStream -> KStream
KTable -> KTable

# description

Write the records to a Kafka topic and create a new stream/table from that topic. Essentially a shorthand for KStream#to() followed by KStreamBuilder#stream(), same for tables. (KStream details, KTable details)

When to provide serdes explicitly:

If you do not specify serdes explicitly, the default serdes from the configuration are used.
You must specificy serdes explicitly if the key and/or value types of the KStream or KTable do not match the configured default serdes.
See Data types and serialization for information about configuring default serdes, available serdes, and implementing your own custom serdes.
Several variants of through exist to e.g. specify a custom StreamPartitioner that gives you control over how output records are distributed across the partitions of the output topic.

Causes data re-partitioning if any of the following conditions is true:

If the output topic has a different number of partitions than the stream/table.
If the KStream was marked for re-partitioning.
If you provide a custom StreamPartitioner to explicitly control how to distribute the output records across the partitions of the output topic.
If the key of an output record is null.


# code
## KStream


    /**
     * Materialize this stream to a topic and creates a new {@code KStream} from the topic using default serializers and
     * deserializers and producer's {@link DefaultPartitioner}.
     * The specified topic should be manually created before it is used (i.e., before the Kafka Streams application is
     * started).
     * <p>
     * This is equivalent to calling {@link #to(String) #to(someTopicName)} and {@link KStreamBuilder#stream(String...)
     * KStreamBuilder#stream(someTopicName)}.
     *
     * @param topic the topic name
     * @return a {@code KStream} that contains the exact same (and potentially repartitioned) records as this {@code KStream}
     */
    KStream<K, V> through(final String topic);

    /**
     * Materialize this stream to a topic and creates a new {@code KStream} from the topic using default serializers and
     * deserializers and a customizable {@link StreamPartitioner} to determine the distribution of records to partitions.
     * The specified topic should be manually created before it is used (i.e., before the Kafka Streams application is
     * started).
     * <p>
     * This is equivalent to calling {@link #to(StreamPartitioner, String) #to(StreamPartitioner, someTopicName)} and
     * {@link KStreamBuilder#stream(String...) KStreamBuilder#stream(someTopicName)}.
     *
     * @param partitioner the function used to determine how records are distributed among partitions of the topic,
     *                    if not specified producer's {@link DefaultPartitioner} will be used
     * @param topic       the topic name
     * @return a {@code KStream} that contains the exact same (and potentially repartitioned) records as this {@code KStream}
     */
    KStream<K, V> through(final StreamPartitioner<? super K, ? super V> partitioner,
                          final String topic);

    /**
     * Materialize this stream to a topic, and creates a new {@code KStream} from the topic.
     * The specified topic should be manually created before it is used (i.e., before the Kafka Streams application is
     * started).
     * <p>
     * If {@code keySerde} provides a {@link WindowedSerializer} for the key {@link WindowedStreamPartitioner} is
     * used&mdash;otherwise producer's {@link DefaultPartitioner} is used.
     * <p>
     * This is equivalent to calling {@link #to(Serde, Serde, String) #to(keySerde, valSerde, someTopicName)} and
     * {@link KStreamBuilder#stream(Serde, Serde, String...) KStreamBuilder#stream(keySerde, valSerde, someTopicName)}.
     *
     * @param keySerde key serde used to send key-value pairs,
     *                 if not specified the default key serde defined in the configuration will be used
     * @param valSerde value serde used to send key-value pairs,
     *                 if not specified the default value serde defined in the configuration will be used
     * @param topic    the topic name
     * @return a {@code KStream} that contains the exact same (and potentially repartitioned) records as this {@code KStream}
     */
    KStream<K, V> through(final Serde<K> keySerde,
                          final Serde<V> valSerde,
                          final String topic);

    /**
     * Materialize this stream to a topic and creates a new {@code KStream} from the topic using a customizable
     * {@link StreamPartitioner} to determine the distribution of records to partitions.
     * The specified topic should be manually created before it is used (i.e., before the Kafka Streams application is
     * started).
     * <p>
     * This is equivalent to calling {@link #to(Serde, Serde, StreamPartitioner, String) #to(keySerde, valSerde,
     * StreamPartitioner, someTopicName)} and {@link KStreamBuilder#stream(Serde, Serde, String...)
     * KStreamBuilder#stream(keySerde, valSerde, someTopicName)}.
     *
     * @param keySerde    key serde used to send key-value pairs,
     *                    if not specified the default key serde defined in the configuration will be used
     * @param valSerde    value serde used to send key-value pairs,
     *                    if not specified the default value serde defined in the configuration will be used
     * @param partitioner the function used to determine how records are distributed among partitions of the topic,
     *                    if not specified and {@code keySerde} provides a {@link WindowedSerializer} for the key
     *                    {@link WindowedStreamPartitioner} will be used&mdash;otherwise {@link DefaultPartitioner} will
     *                    be used
     * @param topic       the topic name
     * @return a {@code KStream} that contains the exact same (and potentially repartitioned) records as this {@code KStream}
     */
    KStream<K, V> through(final Serde<K> keySerde,
                          final Serde<V> valSerde,
                          final StreamPartitioner<? super K, ? super V> partitioner,
                          final String topic);


## KTable

    /**
     * Materialize this changelog stream to a topic and creates a new {@code KTable} from the topic using default
     * serializers and deserializers and producer's {@link DefaultPartitioner}.
     * The specified topic should be manually created before it is used (i.e., before the Kafka Streams application is
     * started).
     * <p>
     * This is equivalent to calling {@link #to(String) #to(someTopicName)} and
     * {@link KStreamBuilder#table(String, String) KStreamBuilder#table(someTopicName, queryableStoreName)}.
     * <p>
     * The resulting {@code KTable} will be materialized in a local state store with the given store name (cf.
     * {@link KStreamBuilder#table(String, String)})
     * The store name must be a valid Kafka topic name and cannot contain characters other than ASCII alphanumerics, '.', '_' and '-'.
     *
     * @param topic     the topic name
     * @param storeName the state store name used for the result {@code KTable}; valid characters are ASCII
     *                  alphanumerics, '.', '_' and '-'
     * @return a {@code KTable} that contains the exact same (and potentially repartitioned) records as this {@code KTable}
     */
    KTable<K, V> through(final String topic,
                         final String storeName);

    /**
     * Materialize this changelog stream to a topic and creates a new {@code KTable} from the topic using default
     * serializers and deserializers and a customizable {@link StreamPartitioner} to determine the distribution of
     * records to partitions.
     * The specified topic should be manually created before it is used (i.e., before the Kafka Streams application is
     * started).
     * <p>
     * This is equivalent to calling {@link #to(StreamPartitioner, String) #to(partitioner, someTopicName)} and
     * {@link KStreamBuilder#table(String, String) KStreamBuilder#table(someTopicName, queryableStoreName)}.
     * <p>
     * The resulting {@code KTable} will be materialized in a local state store with the given store name (cf.
     * {@link KStreamBuilder#table(String, String)})
     *
     * @param partitioner the function used to determine how records are distributed among partitions of the topic,
     *                    if not specified producer's {@link DefaultPartitioner} will be used
     * @param topic       the topic name
     * @param storeName   the state store name used for the result {@code KTable}
     * @return a {@code KTable} that contains the exact same (and potentially repartitioned) records as this {@code KTable}
     */
    KTable<K, V> through(final StreamPartitioner<? super K, ? super V> partitioner,
                         final String topic,
                         final String storeName);

    /**
     * Materialize this changelog stream to a topic and creates a new {@code KTable} from the topic.
     * The specified topic should be manually created before it is used (i.e., before the Kafka Streams application is
     * started).
     * <p>
     * If {@code keySerde} provides a {@link WindowedSerializer} for the key {@link WindowedStreamPartitioner} is
     * used&mdash;otherwise producer's {@link DefaultPartitioner} is used.
     * <p>
     * This is equivalent to calling {@link #to(Serde, Serde, String) #to(keySerde, valueSerde, someTopicName)} and
     * {@link KStreamBuilder#table(String, String) KStreamBuilder#table(someTopicName, queryableStoreName)}.
     * <p>
     * The resulting {@code KTable} will be materialized in a local state store with the given store name (cf.
     * {@link KStreamBuilder#table(String, String)})
     *
     * @param keySerde  key serde used to send key-value pairs,
     *                  if not specified the default key serde defined in the configuration will be used
     * @param valSerde  value serde used to send key-value pairs,
     *                  if not specified the default value serde defined in the configuration will be used
     * @param topic     the topic name
     * @param storeName the state store name used for the result {@code KTable}
     * @return a {@code KTable} that contains the exact same (and potentially repartitioned) records as this {@code KTable}
     */
    KTable<K, V> through(final Serde<K> keySerde, Serde<V> valSerde,
                         final String topic,
                         final String storeName);

    /**
     * Materialize this changelog stream to a topic and creates a new {@code KTable} from the topic using a customizable
     * {@link StreamPartitioner} to determine the distribution of records to partitions.
     * The specified topic should be manually created before it is used (i.e., before the Kafka Streams application is
     * started).
     * <p>
     * This is equivalent to calling {@link #to(Serde, Serde, StreamPartitioner, String)
     * #to(keySerde, valueSerde, partitioner, someTopicName)} and
     * {@link KStreamBuilder#table(String, String) KStreamBuilder#table(someTopicName, queryableStoreName)}.
     * <p>
     * The resulting {@code KTable} will be materialized in a local state store with the given store name (cf.
     * {@link KStreamBuilder#table(String, String)})
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
     * @param storeName  the state store name used for the result {@code KTable}
     * @return a {@code KTable} that contains the exact same (and potentially repartitioned) records as this {@code KTable}
     */
    KTable<K, V> through(final Serde<K> keySerde,
                         final Serde<V> valSerde,
                         final StreamPartitioner<? super K, ? super V> partitioner,
                         final String topic,
                         final String storeName);
