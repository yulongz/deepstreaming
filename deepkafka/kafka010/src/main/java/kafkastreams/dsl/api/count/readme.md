# name 
count

# return
KGroupedStream → KTable
KGroupedTable → KTable

# description

Rolling aggregation. Counts the number of records by the grouped key. (KGroupedStream details, KGroupedTable details)

Several variants of count exist, see Javadocs for details.

Detailed behavior for KGroupedStream:

Input records with null keys or values are ignored.
Detailed behavior for KGroupedTable:

Input records with null keys are ignored. Records with null values are not ignored but interpreted as “tombstones” for the corresponding key, which indicate the deletion of the key from the table.

# code
## KGroupedStream


    /**
     * Count the number of records in this stream by the grouped key.
     * Records with {@code null} key or value are ignored.
     * The result is written into a local {@link KeyValueStore} (which is basically an ever-updating materialized view)
     * that can be queried using the provided {@code storeName}.
     * Furthermore, updates to the store are sent downstream into a {@link KTable} changelog stream.
     * <p>
     * Not all updates might get sent downstream, as an internal cache is used to deduplicate consecutive updates to
     * the same key.
     * The rate of propagated updates depends on your input data rate, the number of distinct keys, the number of
     * parallel running Kafka Streams instances, and the {@link StreamsConfig configuration} parameters for
     * {@link StreamsConfig#CACHE_MAX_BYTES_BUFFERING_CONFIG cache size}, and
     * {@link StreamsConfig#COMMIT_INTERVAL_MS_CONFIG commit intervall}.
     * <p>
     * To query the local {@link KeyValueStore} it must be obtained via
     * {@link KafkaStreams#store(String, QueryableStoreType) KafkaStreams#store(...)}:
     * <pre>{@code
     * KafkaStreams streams = ... // counting words
     * ReadOnlyKeyValueStore<String,Long> localStore = streams.store(storeName, QueryableStoreTypes.<String, Long>keyValueStore());
     * String key = "some-word";
     * Long countForWord = localStore.get(key); // key must be local (application state is shared over all running Kafka Streams instances)
     * }</pre>
     * For non-local keys, a custom RPC mechanism must be implemented using {@link KafkaStreams#allMetadata()} to
     * query the value of the key on a parallel running instance of your Kafka Streams application.
     * <p>
     * For failure and recovery the store will be backed by an internal changelog topic that will be created in Kafka.
     * Therefore, the store name must be a valid Kafka topic name and cannot contain characters other than ASCII
     * alphanumerics, '.', '_' and '-'.
     * The changelog topic will be named "${applicationId}-${storeName}-changelog", where "applicationId" is
     * user-specified in {@link StreamsConfig} via parameter
     * {@link StreamsConfig#APPLICATION_ID_CONFIG APPLICATION_ID_CONFIG}, "storeName" is the
     * provide {@code storeName}, and "-changelog" is a fixed suffix.
     * You can retrieve all generated internal topic names via {@link KafkaStreams#toString()}.
     *
     * @param storeName the name of the underlying {@link KTable} state store; valid characters are ASCII
     *                  alphanumerics, '.', '_' and '-'
     * @return a {@link KTable} that contains "update" records with unmodified keys and {@link Long} values that
     * represent the latest (rolling) count (i.e., number of records) for each key
     */
    KTable<K, Long> count(final String storeName);

    /**
     * Count the number of records in this stream by the grouped key.
     * Records with {@code null} key or value are ignored.
     * The result is written into a local {@link KeyValueStore} (which is basically an ever-updating materialized view)
     * provided by the given {@code storeSupplier}.
     * Furthermore, updates to the store are sent downstream into a {@link KTable} changelog stream.
     * <p>
     * Not all updates might get sent downstream, as an internal cache is used to deduplicate consecutive updates to
     * the same key.
     * The rate of propagated updates depends on your input data rate, the number of distinct keys, the number of
     * parallel running Kafka Streams instances, and the {@link StreamsConfig configuration} parameters for
     * {@link StreamsConfig#CACHE_MAX_BYTES_BUFFERING_CONFIG cache size}, and
     * {@link StreamsConfig#COMMIT_INTERVAL_MS_CONFIG commit intervall}.
     * <p>
     * To query the local {@link KeyValueStore} it must be obtained via
     * {@link KafkaStreams#store(String, QueryableStoreType) KafkaStreams#store(...)}.
     * Use {@link StateStoreSupplier#name()} to get the store name:
     * <pre>{@code
     * KafkaStreams streams = ... // counting words
     * String storeName = storeSupplier.name();
     * ReadOnlyKeyValueStore<String,Long> localStore = streams.store(storeName, QueryableStoreTypes.<String, Long>keyValueStore());
     * String key = "some-word";
     * Long countForWord = localStore.get(key); // key must be local (application state is shared over all running Kafka Streams instances)
     * }</pre>
     * For non-local keys, a custom RPC mechanism must be implemented using {@link KafkaStreams#allMetadata()} to
     * query the value of the key on a parallel running instance of your Kafka Streams application.
     *
     * @param storeSupplier user defined state store supplier
     * @return a {@link KTable} that contains "update" records with unmodified keys and {@link Long} values that
     * represent the latest (rolling) count (i.e., number of records) for each key
     */
    KTable<K, Long> count(final StateStoreSupplier<KeyValueStore> storeSupplier);

    /**
     * Count the number of records in this stream by the grouped key and the defined windows.
     * Records with {@code null} key or value are ignored.
     * The specified {@code windows} define either hopping time windows that can be overlapping or tumbling (c.f.
     * {@link TimeWindows}) or they define landmark windows (c.f. {@link UnlimitedWindows}).
     * The result is written into a local windowed {@link KeyValueStore} (which is basically an ever-updating
     * materialized view) that can be queried using the provided {@code storeName}.
     * Windows are retained until their retention time expires (c.f. {@link Windows#until(long)}).
     * Furthermore, updates to the store are sent downstream into a windowed {@link KTable} changelog stream, where
     * "windowed" implies that the {@link KTable} key is a combined key of the original record key and a window ID.
     * <p>
     * Not all updates might get sent downstream, as an internal cache is used to deduplicate consecutive updates to
     * the same window and key.
     * The rate of propagated updates depends on your input data rate, the number of distinct keys, the number of
     * parallel running Kafka Streams instances, and the {@link StreamsConfig configuration} parameters for
     * {@link StreamsConfig#CACHE_MAX_BYTES_BUFFERING_CONFIG cache size}, and
     * {@link StreamsConfig#COMMIT_INTERVAL_MS_CONFIG commit intervall}.
     * <p>
     * To query the local windowed {@link KeyValueStore} it must be obtained via
     * {@link KafkaStreams#store(String, QueryableStoreType) KafkaStreams#store(...)}:
     * <pre>{@code
     * KafkaStreams streams = ... // counting words
     * ReadOnlyWindowStore<String,Long> localWindowStore = streams.store(storeName, QueryableStoreTypes.<String, Long>windowStore());
     * String key = "some-word";
     * long fromTime = ...;
     * long toTime = ...;
     * WindowStoreIterator<Long> countForWordsForWindows = localWindowStore.fetch(key, timeFrom, timeTo); // key must be local (application state is shared over all running Kafka Streams instances)
     * }</pre>
     * For non-local keys, a custom RPC mechanism must be implemented using {@link KafkaStreams#allMetadata()} to
     * query the value of the key on a parallel running instance of your Kafka Streams application.
     * <p>
     * For failure and recovery the store will be backed by an internal changelog topic that will be created in Kafka.
     * Therefore, the store name must be a valid Kafka topic name and cannot contain characters other than ASCII
     * alphanumerics, '.', '_' and '-'.
     * The changelog topic will be named "${applicationId}-${storeName}-changelog", where "applicationId" is
     * user-specified in {@link StreamsConfig StreamsConfig} via parameter
     * {@link StreamsConfig#APPLICATION_ID_CONFIG APPLICATION_ID_CONFIG}, "storeName" is the
     * provide {@code storeName}, and "-changelog" is a fixed suffix.
     * You can retrieve all generated internal topic names via {@link KafkaStreams#toString()}.
     *
     * @param windows   the specification of the aggregation {@link Windows}
     * @param storeName the name of the underlying {@link KTable} state store; valid characters are ASCII
     *                  alphanumerics, '.', '_' and '-'
     * @return a windowed {@link KTable} that contains "update" records with unmodified keys and {@link Long} values
     * that represent the latest (rolling) count (i.e., number of records) for each key within a window
     */
    <W extends Window> KTable<Windowed<K>, Long> count(final Windows<W> windows,
                                                       final String storeName);

    /**
     * Count the number of records in this stream by the grouped key and the defined windows.
     * Records with {@code null} key or value are ignored.
     * The specified {@code windows} define either hopping time windows that can be overlapping or tumbling (c.f.
     * {@link TimeWindows}) or they define landmark windows (c.f. {@link UnlimitedWindows}).
     * The result is written into a local windowed {@link KeyValueStore} (which is basically an ever-updating
     * materialized view) provided by the given {@code storeSupplier}.
     * Windows are retained until their retention time expires (c.f. {@link Windows#until(long)}).
     * Furthermore, updates to the store are sent downstream into a windowed {@link KTable} changelog stream, where
     * "windowed" implies that the {@link KTable} key is a combined key of the original record key and a window ID.
     * <p>
     * Not all updates might get sent downstream, as an internal cache is used to deduplicate consecutive updates to
     * the same window and key.
     * The rate of propagated updates depends on your input data rate, the number of distinct keys, the number of
     * parallel running Kafka Streams instances, and the {@link StreamsConfig configuration} parameters for
     * {@link StreamsConfig#CACHE_MAX_BYTES_BUFFERING_CONFIG cache size}, and
     * {@link StreamsConfig#COMMIT_INTERVAL_MS_CONFIG commit intervall}.
     * <p>
     * To query the local windowed {@link KeyValueStore} it must be obtained via
     * {@link KafkaStreams#store(String, QueryableStoreType) KafkaStreams#store(...)}.
     * Use {@link StateStoreSupplier#name()} to get the store name:
     * <pre>{@code
     * KafkaStreams streams = ... // counting words
     * String storeName = storeSupplier.name();
     * ReadOnlyWindowStore<String,Long> localWindowStore = streams.store(storeName, QueryableStoreTypes.<String, Long>windowStore());
     * String key = "some-word";
     * long fromTime = ...;
     * long toTime = ...;
     * WindowStoreIterator<Long> countForWordsForWindows = localWindowStore.fetch(key, timeFrom, timeTo); // key must be local (application state is shared over all running Kafka Streams instances)
     * }</pre>
     * For non-local keys, a custom RPC mechanism must be implemented using {@link KafkaStreams#allMetadata()} to
     * query the value of the key on a parallel running instance of your Kafka Streams application.
     *
     * @param windows       the specification of the aggregation {@link Windows}
     * @param storeSupplier user defined state store supplier
     * @return a windowed {@link KTable} that contains "update" records with unmodified keys and {@link Long} values
     * that represent the latest (rolling) count (i.e., number of records) for each key within a window
     */
    <W extends Window> KTable<Windowed<K>, Long> count(final Windows<W> windows,
                                                       final StateStoreSupplier<WindowStore> storeSupplier);


    /**
     * Count the number of records in this stream by the grouped key into {@link SessionWindows}.
     * Records with {@code null} key or value are ignored.
     * The result is written into a local {@link SessionStore} (which is basically an ever-updating
     * materialized view) that can be queried using the provided {@code storeName}.
     * SessionWindows are retained until their retention time expires (c.f. {@link SessionWindows#until(long)}).
     * Furthermore, updates to the store are sent downstream into a windowed {@link KTable} changelog stream, where
     * "windowed" implies that the {@link KTable} key is a combined key of the original record key and a window ID.
     * <p>
     * Not all updates might get sent downstream, as an internal cache is used to deduplicate consecutive updates to
     * the same window and key.
     * The rate of propagated updates depends on your input data rate, the number of distinct keys, the number of
     * parallel running Kafka Streams instances, and the {@link StreamsConfig configuration} parameters for
     * {@link StreamsConfig#CACHE_MAX_BYTES_BUFFERING_CONFIG cache size}, and
     * {@link StreamsConfig#COMMIT_INTERVAL_MS_CONFIG commit intervall}.
     * <p>
     * To query the local {@link SessionStore} it must be obtained via
     * {@link KafkaStreams#store(String, QueryableStoreType) KafkaStreams#store(...)}.
     * Use {@link StateStoreSupplier#name()} to get the store name:
     * <pre>{@code
     * KafkaStreams streams = ... // counting words
     * String storeName = storeSupplier.name();
     * ReadOnlySessionStore<String,Long> sessionStore = streams.store(storeName, QueryableStoreTypes.<String, Long>sessionStore());
     * String key = "some-word";
     * KeyValueIterator<Windowed<String>, Long> iterator = sessionStore.fetch(key); // key must be local (application state is shared over all running Kafka Streams instances)
     * }</pre>
     * For non-local keys, a custom RPC mechanism must be implemented using {@link KafkaStreams#allMetadata()} to
     * query the value of the key on a parallel running instance of your Kafka Streams application.
     *
     *
     * @param sessionWindows the specification of the aggregation {@link SessionWindows}
     * @param storeName      the name of the state store created from this operation; valid characters are ASCII
     *                       alphanumerics, '.', '_' and '-
     * @return a windowed {@link KTable} that contains "update" records with unmodified keys and {@link Long} values
     * that represent the latest (rolling) count (i.e., number of records) for each key within a window
     */
    KTable<Windowed<K>, Long> count(final SessionWindows sessionWindows, final String storeName);

    /**
     * Count the number of records in this stream by the grouped key into {@link SessionWindows}.
     * Records with {@code null} key or value are ignored.
     * The result is written into a local {@link SessionStore} (which is basically an ever-updating materialized view)
     * provided by the given {@code storeSupplier}.
     * SessionWindows are retained until their retention time expires (c.f. {@link SessionWindows#until(long)}).
     * Furthermore, updates to the store are sent downstream into a windowed {@link KTable} changelog stream, where
     * "windowed" implies that the {@link KTable} key is a combined key of the original record key and a window ID.
     * <p>
     * Not all updates might get sent downstream, as an internal cache is used to deduplicate consecutive updates to
     * the same window and key.
     * The rate of propagated updates depends on your input data rate, the number of distinct keys, the number of
     * parallel running Kafka Streams instances, and the {@link StreamsConfig configuration} parameters for
     * {@link StreamsConfig#CACHE_MAX_BYTES_BUFFERING_CONFIG cache size}, and
     * {@link StreamsConfig#COMMIT_INTERVAL_MS_CONFIG commit intervall}.
     * <p>
     * To query the local {@link SessionStore} it must be obtained via
     * {@link KafkaStreams#store(String, QueryableStoreType) KafkaStreams#store(...)}.
     * Use {@link StateStoreSupplier#name()} to get the store name:
     * <pre>{@code
     * KafkaStreams streams = ... // counting words
     * String storeName = storeSupplier.name();
     * ReadOnlySessionStore<String,Long> sessionStore = streams.store(storeName, QueryableStoreTypes.<String, Long>sessionStore());
     * String key = "some-word";
     * KeyValueIterator<Windowed<String>, Long> iterator = sessionStore.fetch(key); // key must be local (application state is shared over all running Kafka Streams instances)
     * }</pre>
     * For non-local keys, a custom RPC mechanism must be implemented using {@link KafkaStreams#allMetadata()} to
     * query the value of the key on a parallel running instance of your Kafka Streams application.
     *
     *
     * @param sessionWindows the specification of the aggregation {@link SessionWindows}
     * @param storeSupplier  user defined state store supplier
     * @return a windowed {@link KTable} that contains "update" records with unmodified keys and {@link Long} values
     * that represent the latest (rolling) count (i.e., number of records) for each key within a window
     */
    KTable<Windowed<K>, Long> count(final SessionWindows sessionWindows,
                                    final StateStoreSupplier<SessionStore> storeSupplier);





## KGroupedTable


    /**
     * Count number of records of the original {@link KTable} that got {@link KTable#groupBy(KeyValueMapper) mapped} to
     * the same key into a new instance of {@link KTable}.
     * Records with {@code null} key are ignored.
     * The result is written into a local {@link KeyValueStore} (which is basically an ever-updating materialized view)
     * that can be queried using the provided {@code storeName}.
     * Furthermore, updates to the store are sent downstream into a {@link KTable} changelog stream.
     * <p>
     * Not all updates might get sent downstream, as an internal cache is used to deduplicate consecutive updates to
     * the same key.
     * The rate of propagated updates depends on your input data rate, the number of distinct keys, the number of
     * parallel running Kafka Streams instances, and the {@link StreamsConfig configuration} parameters for
     * {@link StreamsConfig#CACHE_MAX_BYTES_BUFFERING_CONFIG cache size}, and
     * {@link StreamsConfig#COMMIT_INTERVAL_MS_CONFIG commit intervall}.
     * <p>
     * To query the local {@link KeyValueStore} it must be obtained via
     * {@link KafkaStreams#store(String, QueryableStoreType) KafkaStreams#store(...)}:
     * <pre>{@code
     * KafkaStreams streams = ... // counting words
     * ReadOnlyKeyValueStore<String,Long> localStore = streams.store(storeName, QueryableStoreTypes.<String, Long>keyValueStore());
     * String key = "some-word";
     * Long countForWord = localStore.get(key); // key must be local (application state is shared over all running Kafka Streams instances)
     * }</pre>
     * For non-local keys, a custom RPC mechanism must be implemented using {@link KafkaStreams#allMetadata()} to
     * query the value of the key on a parallel running instance of your Kafka Streams application.
     * <p>
     * For failure and recovery the store will be backed by an internal changelog topic that will be created in Kafka.
     * The changelog topic will be named "${applicationId}-${storeName}-changelog", where "applicationId" is
     * user-specified in {@link StreamsConfig} via parameter
     * {@link StreamsConfig#APPLICATION_ID_CONFIG APPLICATION_ID_CONFIG}, "storeName" is the
     * provide {@code storeName}, and "-changelog" is a fixed suffix.
     * The store name must be a valid Kafka topic name and cannot contain characters other than ASCII alphanumerics,
     * '.', '_' and '-'.
     * You can retrieve all generated internal topic names via {@link KafkaStreams#toString()}.
     *
     * @param storeName     the name of the underlying {@link KTable} state store; valid characters are ASCII
     *                      alphanumerics, '.', '_' and '-'
     * @return a {@link KTable} that contains "update" records with unmodified keys and {@link Long} values that
     * represent the latest (rolling) count (i.e., number of records) for each key
     */
    KTable<K, Long> count(final String storeName);

    /**
     * Count number of records of the original {@link KTable} that got {@link KTable#groupBy(KeyValueMapper) mapped} to
     * the same key into a new instance of {@link KTable}.
     * Records with {@code null} key are ignored.
     * The result is written into a local {@link KeyValueStore} (which is basically an ever-updating materialized view)
     * provided by the given {@code storeSupplier}.
     * Furthermore, updates to the store are sent downstream into a {@link KTable} changelog stream.
     * <p>
     * Not all updates might get sent downstream, as an internal cache is used to deduplicate consecutive updates to
     * the same key.
     * The rate of propagated updates depends on your input data rate, the number of distinct keys, the number of
     * parallel running Kafka Streams instances, and the {@link StreamsConfig configuration} parameters for
     * {@link StreamsConfig#CACHE_MAX_BYTES_BUFFERING_CONFIG cache size}, and
     * {@link StreamsConfig#COMMIT_INTERVAL_MS_CONFIG commit intervall}.
     * <p>
     * To query the local {@link KeyValueStore} it must be obtained via
     * {@link KafkaStreams#store(String, QueryableStoreType) KafkaStreams#store(...)}:
     * <pre>{@code
     * KafkaStreams streams = ... // counting words
     * String storeName = storeSupplier.name();
     * ReadOnlyKeyValueStore<String,Long> localStore = streams.store(storeName, QueryableStoreTypes.<String, Long>keyValueStore());
     * String key = "some-word";
     * Long countForWord = localStore.get(key); // key must be local (application state is shared over all running Kafka Streams instances)
     * }</pre>
     * For non-local keys, a custom RPC mechanism must be implemented using {@link KafkaStreams#allMetadata()} to
     * query the value of the key on a parallel running instance of your Kafka Streams application.
     * <p>
     * For failure and recovery the store will be backed by an internal changelog topic that will be created in Kafka.
     * The changelog topic will be named "${applicationId}-${storeName}-changelog", where "applicationId" is
     * user-specified in {@link StreamsConfig} via parameter
     * {@link StreamsConfig#APPLICATION_ID_CONFIG APPLICATION_ID_CONFIG}, "storeName" is the
     * provide {@code storeName}, and "-changelog" is a fixed suffix.
     * You can retrieve all generated internal topic names via {@link KafkaStreams#toString()}.
     *
     * @param storeSupplier user defined state store supplier
     * @return a {@link KTable} that contains "update" records with unmodified keys and {@link Long} values that
     * represent the latest (rolling) count (i.e., number of records) for each key
     */
    KTable<K, Long> count(final StateStoreSupplier<KeyValueStore> storeSupplier);

                                   
