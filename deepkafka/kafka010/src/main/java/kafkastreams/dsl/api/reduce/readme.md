# name 
reduce

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
     * Combine the values of records in this stream by the grouped key.
     * Records with {@code null} key or value are ignored.
     * Combining implies that the type of the aggregate result is the same as the type of the input value
     * (c.f. {@link #aggregate(Initializer, Aggregator, Serde, String)}).
     * The result is written into a local {@link KeyValueStore} (which is basically an ever-updating materialized view)
     * that can be queried using the provided {@code storeName}.
     * Furthermore, updates to the store are sent downstream into a {@link KTable} changelog stream.
     * <p>
     * The specified {@link Reducer} is applied for each input record and computes a new aggregate using the current
     * aggregate and the record's value.
     * If there is no current aggregate the {@link Reducer} is not applied and the new aggregate will be the record's
     * value as-is.
     * Thus, {@code reduce(Reducer, String)} can be used to compute aggregate functions like sum, min, or max.
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
     * KafkaStreams streams = ... // compute sum
     * ReadOnlyKeyValueStore<String,Long> localStore = streams.store(storeName, QueryableStoreTypes.<String, Long>keyValueStore());
     * String key = "some-key";
     * Long sumForKey = localStore.get(key); // key must be local (application state is shared over all running Kafka Streams instances)
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
     * @param reducer   a {@link Reducer} that computes a new aggregate result
     * @param storeName the name of the underlying {@link KTable} state store; valid characters are ASCII
     *                  alphanumerics, '.', '_' and '-'
     * @return a {@link KTable} that contains "update" records with unmodified keys, and values that represent the
     * latest (rolling) aggregate for each key
     */
    KTable<K, V> reduce(final Reducer<V> reducer,
                        final String storeName);

    /**
     * Combine the value of records in this stream by the grouped key.
     * Records with {@code null} key or value are ignored.
     * Combining implies that the type of the aggregate result is the same as the type of the input value
     * (c.f. {@link #aggregate(Initializer, Aggregator, StateStoreSupplier)}).
     * The result is written into a local {@link KeyValueStore} (which is basically an ever-updating materialized view)
     * provided by the given {@code storeSupplier}.
     * Furthermore, updates to the store are sent downstream into a {@link KTable} changelog stream.
     * <p>
     * The specified {@link Reducer} is applied for each input record and computes a new aggregate using the current
     * aggregate and the record's value.
     * If there is no current aggregate the {@link Reducer} is not applied and the new aggregate will be the record's
     * value as-is.
     * Thus, {@code reduce(Reducer, StateStoreSupplier)} can be used to compute aggregate functions like sum, min, or
     * max.
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
     * KafkaStreams streams = ... // compute sum
     * String storeName = storeSupplier.name();
     * ReadOnlyKeyValueStore<String,Long> localStore = streams.store(storeName, QueryableStoreTypes.<String, Long>keyValueStore());
     * String key = "some-key";
     * Long sumForKey = localStore.get(key); // key must be local (application state is shared over all running Kafka Streams instances)
     * }</pre>
     * For non-local keys, a custom RPC mechanism must be implemented using {@link KafkaStreams#allMetadata()} to
     * query the value of the key on a parallel running instance of your Kafka Streams application.
     *
     * @param reducer   a {@link Reducer} that computes a new aggregate result
     * @param storeSupplier user defined state store supplier
     * @return a {@link KTable} that contains "update" records with unmodified keys, and values that represent the
     * latest (rolling) aggregate for each key
     */
    KTable<K, V> reduce(final Reducer<V> reducer,
                        final StateStoreSupplier<KeyValueStore> storeSupplier);

    /**
     * Combine the number of records in this stream by the grouped key and the defined windows.
     * Records with {@code null} key or value are ignored.
     * Combining implies that the type of the aggregate result is the same as the type of the input value
     * (c.f. {@link #aggregate(Initializer, Aggregator, Windows, Serde, String)}).
     * The specified {@code windows} define either hopping time windows that can be overlapping or tumbling (c.f.
     * {@link TimeWindows}) or they define landmark windows (c.f. {@link UnlimitedWindows}).
     * The result is written into a local windowed {@link KeyValueStore} (which is basically an ever-updating
     * materialized view) that can be queried using the provided {@code storeName}.
     * Windows are retained until their retention time expires (c.f. {@link Windows#until(long)}).
     * Furthermore, updates to the store are sent downstream into a windowed {@link KTable} changelog stream, where
     * "windowed" implies that the {@link KTable} key is a combined key of the original record key and a window ID.
     * <p>
     * The specified {@link Reducer} is applied for each input record and computes a new aggregate using the current
     * aggregate and the record's value.
     * If there is no current aggregate the {@link Reducer} is not applied and the new aggregate will be the record's
     * value as-is.
     * Thus, {@code reduce(Reducer, Windows, String)} can be used to compute aggregate functions like sum, min, or max.
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
     * KafkaStreams streams = ... // compute sum
     * ReadOnlyWindowStore<String,Long> localWindowStore = streams.store(storeName, QueryableStoreTypes.<String, Long>windowStore());
     * String key = "some-key";
     * long fromTime = ...;
     * long toTime = ...;
     * WindowStoreIterator<Long> sumForKeyForWindows = localWindowStore.fetch(key, timeFrom, timeTo); // key must be local (application state is shared over all running Kafka Streams instances)
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
     * @param reducer   a {@link Reducer} that computes a new aggregate result
     * @param windows   the specification of the aggregation {@link Windows}
     * @param storeName the name of the state store created from this operation; valid characters are ASCII
     *                  alphanumerics, '.', '_' and '-'
     * @return a windowed {@link KTable} that contains "update" records with unmodified keys, and values that represent
     * the latest (rolling) aggregate for each key within a window
     */
    <W extends Window> KTable<Windowed<K>, V> reduce(final Reducer<V> reducer,
                                                     final Windows<W> windows,
                                                     final String storeName);

    /**
     * Combine the values of records in this stream by the grouped key and the defined windows.
     * Records with {@code null} key or value are ignored.
     * Combining implies that the type of the aggregate result is the same as the type of the input value
     * (c.f. {@link #aggregate(Initializer, Aggregator, Windows, Serde, String)}).
     * The specified {@code windows} define either hopping time windows that can be overlapping or tumbling (c.f.
     * {@link TimeWindows}) or they define landmark windows (c.f. {@link UnlimitedWindows}).
     * The result is written into a local windowed {@link KeyValueStore} (which is basically an ever-updating
     * materialized view) provided by the given {@code storeSupplier}.
     * Windows are retained until their retention time expires (c.f. {@link Windows#until(long)}).
     * Furthermore, updates to the store are sent downstream into a windowed {@link KTable} changelog stream, where
     * "windowed" implies that the {@link KTable} key is a combined key of the original record key and a window ID.
     * <p>
     * The specified {@link Reducer} is applied for each input record and computes a new aggregate using the current
     * aggregate and the record's value.
     * If there is no current aggregate the {@link Reducer} is not applied and the new aggregate will be the record's
     * value as-is.
     * Thus, {@code reduce(Reducer, Windows, StateStoreSupplier)} can be used to compute aggregate functions like sum,
     * min, or max.
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
     * KafkaStreams streams = ... // compute sum
     * Sting storeName = storeSupplier.name();
     * ReadOnlyWindowStore<String,Long> localWindowStore = streams.store(storeName, QueryableStoreTypes.<String, Long>windowStore());
     * String key = "some-key";
     * long fromTime = ...;
     * long toTime = ...;
     * WindowStoreIterator<Long> sumForKeyForWindows = localWindowStore.fetch(key, timeFrom, timeTo); // key must be local (application state is shared over all running Kafka Streams instances)
     * }</pre>
     * For non-local keys, a custom RPC mechanism must be implemented using {@link KafkaStreams#allMetadata()} to
     * query the value of the key on a parallel running instance of your Kafka Streams application.
     *
     * @param reducer       a {@link Reducer} that computes a new aggregate result
     * @param windows       the specification of the aggregation {@link Windows}
     * @param storeSupplier user defined state store supplier
     * @return a windowed {@link KTable} that contains "update" records with unmodified keys, and values that represent
     * the latest (rolling) aggregate for each key within a window
     */
    <W extends Window> KTable<Windowed<K>, V> reduce(final Reducer<V> reducer,
                                                     final Windows<W> windows,
                                                     final StateStoreSupplier<WindowStore> storeSupplier);

    /**
     * Combine values of this stream by the grouped key into {@link SessionWindows}.
     * Records with {@code null} key or value are ignored.
     * Combining implies that the type of the aggregate result is the same as the type of the input value
     * (c.f. {@link #aggregate(Initializer, Aggregator, Merger, SessionWindows, Serde, String)}).
     * The result is written into a local {@link SessionStore} (which is basically an ever-updating
     * materialized view) that can be queried using the provided {@code storeName}.
     * SessionWindows are retained until their retention time expires (c.f. {@link SessionWindows#until(long)}).
     * Furthermore, updates to the store are sent downstream into a windowed {@link KTable} changelog stream, where
     * "windowed" implies that the {@link KTable} key is a combined key of the original record key and a window ID.
     * <p>
     * The specified {@link Reducer} is applied for each input record and computes a new aggregate using the current
     * aggregate and the record's value.
     * If there is no current aggregate the {@link Reducer} is not applied and the new aggregate will be the record's
     * value as-is.
     * Thus, {@code reduce(Reducer, SessionWindows, String)} can be used to compute aggregate functions like sum, min,
     * or max.
     * <p>
     * Not all updates might get sent downstream, as an internal cache is used to deduplicate consecutive updates to
     * the same window and key.
     * The rate of propagated updates depends on your input data rate, the number of distinct keys, the number of
     * parallel running Kafka Streams instances, and the {@link StreamsConfig configuration} parameters for
     * {@link StreamsConfig#CACHE_MAX_BYTES_BUFFERING_CONFIG cache size}, and
     * {@link StreamsConfig#COMMIT_INTERVAL_MS_CONFIG commit intervall}.
     * <p>
     * To query the local {@link SessionStore} it must be obtained via
     * {@link KafkaStreams#store(String, QueryableStoreType) KafkaStreams#store(...)}:
     * <pre>{@code
     * KafkaStreams streams = ... // compute sum
     * ReadOnlySessionStore<String,Long> sessionStore = streams.store(storeName, QueryableStoreTypes.<String, Long>sessionStore());
     * String key = "some-key";
     * KeyValueIterator<Windowed<String>, Long> sumForKeyForSession = localWindowStore.fetch(key); // key must be local (application state is shared over all running Kafka Streams instances)
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
     * @param reducer           the instance of {@link Reducer}
     * @param sessionWindows    the specification of the aggregation {@link SessionWindows}
     * @param storeName         the name of the state store created from this operation; valid characters are ASCII
     *                          alphanumerics, '.', '_' and '-'
     * @return a windowed {@link KTable} that contains "update" records with unmodified keys, and values that represent
     * the latest (rolling) aggregate for each key within a window
     */
    KTable<Windowed<K>, V> reduce(final Reducer<V> reducer,
                                  final SessionWindows sessionWindows,
                                  final String storeName);

    /**
     * Combine values of this stream by the grouped key into {@link SessionWindows}.
     * Records with {@code null} key or value are ignored.
     * Combining implies that the type of the aggregate result is the same as the type of the input value
     * (c.f. {@link #aggregate(Initializer, Aggregator, Merger, SessionWindows, Serde, String)}).
     * The result is written into a local {@link SessionStore} (which is basically an ever-updating materialized view)
     * provided by the given {@code storeSupplier}.
     * SessionWindows are retained until their retention time expires (c.f. {@link SessionWindows#until(long)}).
     * Furthermore, updates to the store are sent downstream into a windowed {@link KTable} changelog stream, where
     * "windowed" implies that the {@link KTable} key is a combined key of the original record key and a window ID.
     * <p>
     * The specified {@link Reducer} is applied for each input record and computes a new aggregate using the current
     * aggregate and the record's value.
     * If there is no current aggregate the {@link Reducer} is not applied and the new aggregate will be the record's
     * value as-is.
     * Thus, {@code reduce(Reducer, SessionWindows, StateStoreSupplier)} can be used to compute aggregate functions like
     * sum, min, or max.
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
     * KafkaStreams streams = ... // compute sum
     * Sting storeName = storeSupplier.name();
     * ReadOnlySessionStore<String,Long> sessionStore = streams.store(storeName, QueryableStoreTypes.<String, Long>sessionStore());
     * String key = "some-key";
     * KeyValueIterator<Windowed<String>, Long> sumForKeyForSession = localWindowStore.fetch(key); // key must be local (application state is shared over all running Kafka Streams instances)
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
     * @param reducer           the instance of {@link Reducer}
     * @param sessionWindows    the specification of the aggregation {@link SessionWindows}
     * @param storeSupplier     user defined state store supplier
     * @return a windowed {@link KTable} that contains "update" records with unmodified keys, and values that represent
     * the latest (rolling) aggregate for each key within a window
     */
    KTable<Windowed<K>, V> reduce(final Reducer<V> reducer,
                                  final SessionWindows sessionWindows,
                                  final StateStoreSupplier<SessionStore> storeSupplier);


## KGroupedTable


    /**
     * Combine the value of records of the original {@link KTable} that got {@link KTable#groupBy(KeyValueMapper)
     * mapped} to the same key into a new instance of {@link KTable}.
     * Records with {@code null} key are ignored.
     * Combining implies that the type of the aggregate result is the same as the type of the input value
     * (c.f. {@link #aggregate(Initializer, Aggregator, Aggregator, Serde, String)}).
     * The result is written into a local {@link KeyValueStore} (which is basically an ever-updating materialized view)
     * that can be queried using the provided {@code storeName}.
     * Furthermore, updates to the store are sent downstream into a {@link KTable} changelog stream.
     * <p>
     * Each update to the original {@link KTable} results in a two step update of the result {@link KTable}.
     * The specified {@link Reducer adder} is applied for each update record and computes a new aggregate using the
     * current aggregate and the record's value by adding the new record to the aggregate.
     * The specified {@link Reducer substractor} is applied for each "replaced" record of the original {@link KTable}
     * and computes a new aggregate using the current aggregate and the record's value by "removing" the "replaced"
     * record from the aggregate.
     * If there is no current aggregate the {@link Reducer} is not applied and the new aggregate will be the record's
     * value as-is.
     * Thus, {@code reduce(Reducer, Reducer, String)} can be used to compute aggregate functions like sum.
     * For sum, the adder and substractor would work as follows:
     * <pre>{@code
     * public class SumAdder implements Reducer<Integer> {
     *   public Integer apply(Integer currentAgg, Integer newValue) {
     *     return currentAgg + newValue;
     *   }
     * }
     *
     * public class SumSubtractor implements Reducer<Integer> {
     *   public Integer apply(Integer currentAgg, Integer oldValue) {
     *     return currentAgg - oldValue;
     *   }
     * }
     * }</pre>
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
     * @param adder      a {@link Reducer} that adds a new value to the aggregate result
     * @param subtractor a {@link Reducer} that removed an old value from the aggregate result
     * @param storeName     the name of the underlying {@link KTable} state store; valid characters are ASCII alphanumerics,
     *                      '.', '_' and '-'
     * @return a {@link KTable} that contains "update" records with unmodified keys, and values that represent the
     * latest (rolling) aggregate for each key
     */
    KTable<K, V> reduce(final Reducer<V> adder,
                        final Reducer<V> subtractor,
                        final String storeName);

    /**
     * Combine the value of records of the original {@link KTable} that got {@link KTable#groupBy(KeyValueMapper)
     * mapped} to the same key into a new instance of {@link KTable}.
     * Records with {@code null} key are ignored.
     * Combining implies that the type of the aggregate result is the same as the type of the input value
     * (c.f. {@link #aggregate(Initializer, Aggregator, Aggregator, Serde, String)}).
     * The result is written into a local {@link KeyValueStore} (which is basically an ever-updating materialized view)
     * provided by the given {@code storeSupplier}.
     * Furthermore, updates to the store are sent downstream into a {@link KTable} changelog stream.
     * <p>
     * Each update to the original {@link KTable} results in a two step update of the result {@link KTable}.
     * The specified {@link Reducer adder} is applied for each update record and computes a new aggregate using the
     * current aggregate and the record's value by adding the new record to the aggregate.
     * The specified {@link Reducer substractor} is applied for each "replaced" record of the original {@link KTable}
     * and computes a new aggregate using the current aggregate and the record's value by "removing" the "replaced"
     * record from the aggregate.
     * If there is no current aggregate the {@link Reducer} is not applied and the new aggregate will be the record's
     * value as-is.
     * Thus, {@code reduce(Reducer, Reducer, String)} can be used to compute aggregate functions like sum.
     * For sum, the adder and substractor would work as follows:
     * <pre>{@code
     * public class SumAdder implements Reducer<Integer> {
     *   public Integer apply(Integer currentAgg, Integer newValue) {
     *     return currentAgg + newValue;
     *   }
     * }
     *
     * public class SumSubtractor implements Reducer<Integer> {
     *   public Integer apply(Integer currentAgg, Integer oldValue) {
     *     return currentAgg - oldValue;
     *   }
     * }
     * }</pre>
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
     * @param adder         a {@link Reducer} that adds a new value to the aggregate result
     * @param subtractor    a {@link Reducer} that removed an old value from the aggregate result
     * @param storeSupplier user defined state store supplier
     * @return a {@link KTable} that contains "update" records with unmodified keys, and values that represent the
     * latest (rolling) aggregate for each key
     */
    KTable<K, V> reduce(final Reducer<V> adder,
                        final Reducer<V> subtractor,
                        final StateStoreSupplier<KeyValueStore> storeSupplier);
                              
