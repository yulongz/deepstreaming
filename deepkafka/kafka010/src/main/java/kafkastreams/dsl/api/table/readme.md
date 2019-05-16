# name 
table

# return
input topic → KTable

# description

Reads the specified Kafka input topic into a KTable. The topic is interpreted as a changelog stream, where records with the same key are interpreted as UPSERT aka INSERT/UPDATE (when the record value is not null) or as DELETE (when the value is null) for that key. (details)

Slightly simplified, in the case of a KTable, the local KTable instance of every application instance will be populated with data from only a subset the partitions of the input topic. Collectively, i.e. across all application instances, all the partitions of the input topic will be read and processed.

You must provide a name for the table (more precisely, for the internal state store that backs the table). This is required, among other things, for supporting interactive queries against the table.

When to provide serdes explicitly:

If you do not specify serdes explicitly, the default serdes from the configuration are used.
You must specificy serdes explicitly if the key and/or value types of the records in the Kafka input topic do not match the configured default serdes.
See Data types and serialization for information about configuring default serdes, available serdes, and implementing your own custom serdes.
Several variants of table exist to e.g. specify the auto.offset.reset policy to be used when reading from the input topic.

# code
## KStreamBuilder


    /**
     * Create a {@link KTable} for the specified topic.
     * The default {@code "auto.offset.reset"} strategy and default key and value deserializers as specified in the
     * {@link StreamsConfig config} are used.
     * Input {@link KeyValue records} with {@code null} key will be dropped.
     * <p>
     * Note that the specified input topics must be partitioned by key.
     * If this is not the case the returned {@link KTable} will be corrupted.
     * <p>
     * The resulting {@link KTable} will be materialized in a local {@link KeyValueStore} with the given
     * {@code storeName}.
     * However, no internal changelog topic is created since the original input topic can be used for recovery (cf.
     * methods of {@link KGroupedStream} and {@link KGroupedTable} that return a {@link KTable}).
     * <p>
     * To query the local {@link KeyValueStore} it must be obtained via
     * {@link KafkaStreams#store(String, QueryableStoreType) KafkaStreams#store(...)}:
     * <pre>{@code
     * KafkaStreams streams = ...
     * ReadOnlyKeyValueStore<String,Long> localStore = streams.store(storeName, QueryableStoreTypes.<String, Long>keyValueStore());
     * String key = "some-key";
     * Long valueForKey = localStore.get(key); // key must be local (application state is shared over all running Kafka Streams instances)
     * }</pre>
     * For non-local keys, a custom RPC mechanism must be implemented using {@link KafkaStreams#allMetadata()} to
     * query the value of the key on a parallel running instance of your Kafka Streams application.
     *
     * @param topic     the topic name; cannot be {@code null}
     * @param storeName the state store name; cannot be {@code null}
     * @return a {@link KTable} for the specified topic
     */
    public <K, V> KTable<K, V> table(final String topic,
                                     final String storeName) {
        return table(null, null, null, topic, storeName);
    }

    /**
     * Create a {@link KTable} for the specified topic.
     * The default key and value deserializers as specified in the {@link StreamsConfig config} are used.
     * Input {@link KeyValue records} with {@code null} key will be dropped.
     * <p>
     * Note that the specified input topics must be partitioned by key.
     * If this is not the case the returned {@link KTable} will be corrupted.
     * <p>
     * The resulting {@link KTable} will be materialized in a local {@link KeyValueStore} with the given
     * {@code storeName}.
     * However, no internal changelog topic is created since the original input topic can be used for recovery (cf.
     * methods of {@link KGroupedStream} and {@link KGroupedTable} that return a {@link KTable}).
     * <p>
     * To query the local {@link KeyValueStore} it must be obtained via
     * {@link KafkaStreams#store(String, QueryableStoreType) KafkaStreams#store(...)}:
     * <pre>{@code
     * KafkaStreams streams = ...
     * ReadOnlyKeyValueStore<String,Long> localStore = streams.store(storeName, QueryableStoreTypes.<String, Long>keyValueStore());
     * String key = "some-key";
     * Long valueForKey = localStore.get(key); // key must be local (application state is shared over all running Kafka Streams instances)
     * }</pre>
     * For non-local keys, a custom RPC mechanism must be implemented using {@link KafkaStreams#allMetadata()} to
     * query the value of the key on a parallel running instance of your Kafka Streams application.
     *
     * @param offsetReset the {@code "auto.offset.reset"} policy to use for the specified topic if no valid committed
     *                    offsets are available
     * @param topic       the topic name; cannot be {@code null}
     * @param storeName   the state store name; cannot be {@code null}
     * @return a {@link KTable} for the specified topic
     */
    public <K, V> KTable<K, V> table(final AutoOffsetReset offsetReset,
                                     final String topic,
                                     final String storeName) {
        return table(offsetReset, null, null, topic, storeName);
    }

    /**
     * Create a {@link KTable} for the specified topic.
     * The default {@code "auto.offset.reset"} strategy as specified in the {@link StreamsConfig config} is used.
     * Input {@link KeyValue records} with {@code null} key will be dropped.
     * <p>
     * Note that the specified input topics must be partitioned by key.
     * If this is not the case the returned {@link KTable} will be corrupted.
     * <p>
     * The resulting {@link KTable} will be materialized in a local {@link KeyValueStore} with the given
     * {@code storeName}.
     * However, no internal changelog topic is created since the original input topic can be used for recovery (cf.
     * methods of {@link KGroupedStream} and {@link KGroupedTable} that return a {@link KTable}).
     * <p>
     * To query the local {@link KeyValueStore} it must be obtained via
     * {@link KafkaStreams#store(String, QueryableStoreType) KafkaStreams#store(...)}:
     * <pre>{@code
     * KafkaStreams streams = ...
     * ReadOnlyKeyValueStore<String,Long> localStore = streams.store(storeName, QueryableStoreTypes.<String, Long>keyValueStore());
     * String key = "some-key";
     * Long valueForKey = localStore.get(key); // key must be local (application state is shared over all running Kafka Streams instances)
     * }</pre>
     * For non-local keys, a custom RPC mechanism must be implemented using {@link KafkaStreams#allMetadata()} to
     * query the value of the key on a parallel running instance of your Kafka Streams application.
     *
     * @param keySerde  key serde used to send key-value pairs,
     *                  if not specified the default key serde defined in the configuration will be used
     * @param valSerde  value serde used to send key-value pairs,
     *                  if not specified the default value serde defined in the configuration will be used
     * @param topic     the topic name; cannot be {@code null}
     * @param storeName the state store name; cannot be {@code null}
     * @return a {@link KTable} for the specified topic
     */
    public <K, V> KTable<K, V> table(final Serde<K> keySerde,
                                     final Serde<V> valSerde,
                                     final String topic,
                                     final String storeName) {
        return table(null, keySerde, valSerde, topic, storeName);
    }

    /**
     * Create a {@link KTable} for the specified topic.
     * Input {@link KeyValue records} with {@code null} key will be dropped.
     * <p>
     * Note that the specified input topics must be partitioned by key.
     * If this is not the case the returned {@link KTable} will be corrupted.
     * <p>
     * The resulting {@link KTable} will be materialized in a local {@link KeyValueStore} with the given
     * {@code storeName}.
     * However, no internal changelog topic is created since the original input topic can be used for recovery (cf.
     * methods of {@link KGroupedStream} and {@link KGroupedTable} that return a {@link KTable}).
     * <p>
     * To query the local {@link KeyValueStore} it must be obtained via
     * {@link KafkaStreams#store(String, QueryableStoreType) KafkaStreams#store(...)}:
     * <pre>{@code
     * KafkaStreams streams = ...
     * ReadOnlyKeyValueStore<String,Long> localStore = streams.store(storeName, QueryableStoreTypes.<String, Long>keyValueStore());
     * String key = "some-key";
     * Long valueForKey = localStore.get(key); // key must be local (application state is shared over all running Kafka Streams instances)
     * }</pre>
     * For non-local keys, a custom RPC mechanism must be implemented using {@link KafkaStreams#allMetadata()} to
     * query the value of the key on a parallel running instance of your Kafka Streams application.
     *
     * @param offsetReset the {@code "auto.offset.reset"} policy to use for the specified topic if no valid committed
     *                    offsets are available
     * @param keySerde    key serde used to send key-value pairs,
     *                    if not specified the default key serde defined in the configuration will be used
     * @param valSerde    value serde used to send key-value pairs,
     *                    if not specified the default value serde defined in the configuration will be used
     * @param topic       the topic name; cannot be {@code null}
     * @param storeName   the state store name; cannot be {@code null}
     * @return a {@link KTable} for the specified topic
     */
    public <K, V> KTable<K, V> table(final AutoOffsetReset offsetReset,
                                     final Serde<K> keySerde,
                                     final Serde<V> valSerde,
                                     final String topic,
                                     final String storeName) {
        final String source = newName(KStreamImpl.SOURCE_NAME);
        final String name = newName(KTableImpl.SOURCE_NAME);
        final ProcessorSupplier<K, V> processorSupplier = new KTableSource<>(storeName);

        addSource(offsetReset, source, keySerde == null ? null : keySerde.deserializer(), valSerde == null ? null : valSerde.deserializer(), topic);
        addProcessor(name, processorSupplier, source);

        final KTableImpl<K, ?, V> kTable = new KTableImpl<>(this, name, processorSupplier, Collections.singleton(source), storeName);

        // only materialize the KTable into a state store if the storeName is not null
        if (storeName != null) {
            final StateStoreSupplier storeSupplier = new RocksDBKeyValueStoreSupplier<>(storeName,
                    keySerde,
                    valSerde,
                    false,
                    Collections.<String, String>emptyMap(),
                    true);

            addStateStore(storeSupplier, name);
            connectSourceStoreAndTopic(storeName, topic);
        }

        return kTable;
    }
