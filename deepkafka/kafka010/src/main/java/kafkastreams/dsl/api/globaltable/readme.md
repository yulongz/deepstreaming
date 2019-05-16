# name 
globalTable

# return
input topic → GlobalKTable

# description

Reads the specified Kafka input topic into a GlobalKTable. The topic is interpreted as a changelog stream, where records with the same key are interpreted as UPSERT aka INSERT/UPDATE (when the record value is not null) or as DELETE (when the value is null) for that key. (details)

Slightly simplified, in the case of a GlobalKTable, the local GlobalKTable instance of every application instance will be populated with data from all the partitions of the input topic. In other words, when using a global table, every application instance will get its own, full copy of the topic’s data.

You must provide a name for the table (more precisely, for the internal state store that backs the table). This is required, among other things, for supporting interactive queries against the table.

When to provide serdes explicitly:

If you do not specify serdes explicitly, the default serdes from the configuration are used.
You must specificy serdes explicitly if the key and/or value types of the records in the Kafka input topic do not match the configured default serdes.
See Data types and serialization for information about configuring default serdes, available serdes, and implementing your own custom serdes.
Several variants of globalTable exist to e.g. specify explicit serdes.


# code
## KStreamBuilder


    /**
     * Create a {@link GlobalKTable} for the specified topic.
     * The default key and value deserializers as specified in the {@link StreamsConfig config} are used.
     * Input {@link KeyValue records} with {@code null} key will be dropped.
     * <p>
     * The resulting {@link GlobalKTable} will be materialized in a local {@link KeyValueStore} with the given
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
     * Long valueForKey = localStore.get(key);
     * }</pre>
     * Note that {@link GlobalKTable} always applies {@code "auto.offset.reset"} strategy {@code "earliest"}
     * regardless of the specified value in {@link StreamsConfig}.
     *
     * @param topic     the topic name; cannot be {@code null}
     * @param storeName the state store name; cannot be {@code null}
     * @return a {@link GlobalKTable} for the specified topic
     */
    public <K, V> GlobalKTable<K, V> globalTable(final String topic,
                                                 final String storeName) {
        return globalTable(null, null, topic, storeName);
    }

    /**
     * Create a {@link GlobalKTable} for the specified topic.
     * The default key and value deserializers as specified in the {@link StreamsConfig config} are used.
     * Input {@link KeyValue records} with {@code null} key will be dropped.
     * <p>
     * The resulting {@link GlobalKTable} will be materialized in a local {@link KeyValueStore} with the given
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
     * Long valueForKey = localStore.get(key);
     * }</pre>
     * Note that {@link GlobalKTable} always applies {@code "auto.offset.reset"} strategy {@code "earliest"}
     * regardless of the specified value in {@link StreamsConfig}.
     *
     * @param keySerde  key serde used to send key-value pairs,
     *                  if not specified the default key serde defined in the configuration will be used
     * @param valSerde  value serde used to send key-value pairs,
     *                  if not specified the default value serde defined in the configuration will be used
     * @param topic     the topic name; cannot be {@code null}
     * @param storeName the state store name; cannot be {@code null}
     * @return a {@link GlobalKTable} for the specified topic
     */
    @SuppressWarnings("unchecked")
    public <K, V> GlobalKTable<K, V> globalTable(final Serde<K> keySerde,
                                                 final Serde<V> valSerde,
                                                 final String topic,
                                                 final String storeName) {
        final String sourceName = newName(KStreamImpl.SOURCE_NAME);
        final String processorName = newName(KTableImpl.SOURCE_NAME);
        final KTableSource<K, V> tableSource = new KTableSource<>(storeName);


        final Deserializer<K> keyDeserializer = keySerde == null ? null : keySerde.deserializer();
        final Deserializer<V> valueDeserializer = valSerde == null ? null : valSerde.deserializer();

        final StateStore store = new RocksDBKeyValueStoreSupplier<>(storeName,
                                                                    keySerde,
                                                                    valSerde,
                                                                    false,
                                                                    Collections.<String, String>emptyMap(),
                                                                    true).get();

        addGlobalStore(store, sourceName, keyDeserializer, valueDeserializer, topic, processorName, tableSource);
        return new GlobalKTableImpl(new KTableSourceValueGetterSupplier<>(storeName));
    }

