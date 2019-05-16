# name 
aggregate

# return
KGroupedStream → KTable
KGroupedTable → KTable

# description

Rolling aggregation. Aggregates the values of (non-windowed) records by the grouped key. Aggregating is a generalization of reduce and allows, for example, the aggregate value to have a different type than the input values. (KGroupedStream details, KGroupedTable details)

When aggregating a grouped stream, you must provide an initializer (think: aggValue = 0) and an “adder” aggregator (think: aggValue + curValue). When aggregating a grouped table, you must additionally provide a “subtractor” aggregator (think: aggValue - oldValue).

Detailed behavior of KGroupedStream:

Input records with null keys are ignored in general.
When a record key is received for the first time, the initializer is called (and called before the adder).
Whenever a record with a non-null value is received, the adder is called.
Detailed behavior of KGroupedTable:

Input records with null keys are ignored in general.
When a record key is received for the first time, the initializer is called (and called before the adder and subtractor). Note that, in contrast to KGroupedStream, over time the initializer may be called more than once for a key as a result of having received input tombstone records for that key (see below).
When the first non-null value is received for a key (think: INSERT), then only the adder is called.
When subsequent non-null values are received for a key (think: UPDATE), then (1) the subtractor is called with the old value as stored in the table and (2) the adder is called with the new value of the input record that was just received. The order of execution for the subtractor and adder is not defined.
When a tombstone record – i.e. a record with a null value – is received for a key (think: DELETE), then only the subtractor is called. Note that, whenever the subtractor returns a null value itself, then the corresponding key is removed from the resulting KTable. If that happens, any next input record for that key will trigger the initializer again.
See the example at the bottom of this section for a visualization of the aggregation semantics.

# code
## KGroupedStream


    /**
     * Aggregate the values of records in this stream by the grouped key.
     * Records with {@code null} key or value are ignored.
     * Aggregating is a generalization of {@link #reduce(Reducer, String) combining via reduce(...)} as it, for example,
     * allows the result to have a different type than the input values.
     * The result is written into a local {@link KeyValueStore} (which is basically an ever-updating materialized view)
     * that can be queried using the provided {@code storeName}.
     * Furthermore, updates to the store are sent downstream into a {@link KTable} changelog stream.
     * <p>
     * The specified {@link Initializer} is applied once directly before the first input record is processed to
     * provide an initial intermediate aggregation result that is used to process the first record.
     * The specified {@link Aggregator} is applied for each input record and computes a new aggregate using the current
     * aggregate (or for the very first record using the intermediate aggregation result provided via the
     * {@link Initializer}) and the record's value.
     * Thus, {@code aggregate(Initializer, Aggregator, Serde, String)} can be used to compute aggregate functions like
     * count (c.f. {@link #count(String)}).
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
     * KafkaStreams streams = ... // some aggregation on value type double
     * ReadOnlyKeyValueStore<String,Long> localStore = streams.store(storeName, QueryableStoreTypes.<String, Long>keyValueStore());
     * String key = "some-key";
     * Long aggForKey = localStore.get(key); // key must be local (application state is shared over all running Kafka Streams instances)
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
     * @param initializer   an {@link Initializer} that computes an initial intermediate aggregation result
     * @param aggregator    an {@link Aggregator} that computes a new aggregate result
     * @param aggValueSerde aggregate value serdes for materializing the aggregated table,
     *                      if not specified the default serdes defined in the configs will be used
     * @param storeName     the name of the state store created from this operation; valid characters are ASCII
     *                      alphanumerics, '.', '_' and '-'
     * @param <VR>          the value type of the resulting {@link KTable}
     * @return a {@link KTable} that contains "update" records with unmodified keys, and values that represent the
     * latest (rolling) aggregate for each key
     */
    <VR> KTable<K, VR> aggregate(final Initializer<VR> initializer,
                                 final Aggregator<? super K, ? super V, VR> aggregator,
                                 final Serde<VR> aggValueSerde,
                                 final String storeName);

    /**
     * Aggregate the values of records in this stream by the grouped key.
     * Records with {@code null} key or value are ignored.
     * Aggregating is a generalization of {@link #reduce(Reducer, StateStoreSupplier) combining via reduce(...)} as it,
     * for example, allows the result to have a different type than the input values.
     * The result is written into a local {@link KeyValueStore} (which is basically an ever-updating materialized view)
     * provided by the given {@code storeSupplier}.
     * Furthermore, updates to the store are sent downstream into a {@link KTable} changelog stream.
     * <p>
     * The specified {@link Initializer} is applied once directly before the first input record is processed to
     * provide an initial intermediate aggregation result that is used to process the first record.
     * The specified {@link Aggregator} is applied for each input record and computes a new aggregate using the current
     * aggregate (or for the very first record using the intermediate aggregation result provided via the
     * {@link Initializer}) and the record's value.
     * Thus, {@code aggregate(Initializer, Aggregator, StateStoreSupplier)} can be used to compute aggregate functions
     * like count (c.f. {@link #count(String)}).
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
     * KafkaStreams streams = ... // some aggregation on value type double
     * Sting storeName = storeSupplier.name();
     * ReadOnlyKeyValueStore<String,Long> localStore = streams.store(storeName, QueryableStoreTypes.<String, Long>keyValueStore());
     * String key = "some-key";
     * Long aggForKey = localStore.get(key); // key must be local (application state is shared over all running Kafka Streams instances)
     * }</pre>
     * For non-local keys, a custom RPC mechanism must be implemented using {@link KafkaStreams#allMetadata()} to
     * query the value of the key on a parallel running instance of your Kafka Streams application.
     *
     * @param initializer   an {@link Initializer} that computes an initial intermediate aggregation result
     * @param aggregator    an {@link Aggregator} that computes a new aggregate result
     * @param storeSupplier user defined state store supplier
     * @param <VR>          the value type of the resulting {@link KTable}
     * @return a {@link KTable} that contains "update" records with unmodified keys, and values that represent the
     * latest (rolling) aggregate for each key
     */
    <VR> KTable<K, VR> aggregate(final Initializer<VR> initializer,
                                 final Aggregator<? super K, ? super V, VR> aggregator,
                                 final StateStoreSupplier<KeyValueStore> storeSupplier);

    /**
     * Aggregate the values of records in this stream by the grouped key and defined windows.
     * Records with {@code null} key or value are ignored.
     * Aggregating is a generalization of {@link #reduce(Reducer, Windows, String) combining via reduce(...)} as it,
     * for example, allows the result to have a different type than the input values.
     * The specified {@code windows} define either hopping time windows that can be overlapping or tumbling (c.f.
     * {@link TimeWindows}) or they define landmark windows (c.f. {@link UnlimitedWindows}).
     * The result is written into a local windowed {@link KeyValueStore} (which is basically an ever-updating
     * materialized view) that can be queried using the provided {@code storeName}.
     * Windows are retained until their retention time expires (c.f. {@link Windows#until(long)}).
     * Furthermore, updates to the store are sent downstream into a windowed {@link KTable} changelog stream, where
     * "windowed" implies that the {@link KTable} key is a combined key of the original record key and a window ID.
     * <p>
     * The specified {@link Initializer} is applied once per window directly before the first input record is
     * processed to provide an initial intermediate aggregation result that is used to process the first record.
     * The specified {@link Aggregator} is applied for each input record and computes a new aggregate using the current
     * aggregate (or for the very first record using the intermediate aggregation result provided via the
     * {@link Initializer}) and the record's value.
     * Thus, {@code aggregate(Initializer, Aggregator, Windows, Serde, String)} can be used to compute aggregate
     * functions like count (c.f. {@link #count(String)}).
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
     * KafkaStreams streams = ... // some windowed aggregation on value type double
     * ReadOnlyWindowStore<String,Long> localWindowStore = streams.store(storeName, QueryableStoreTypes.<String, Long>windowStore());
     * String key = "some-key";
     * long fromTime = ...;
     * long toTime = ...;
     * WindowStoreIterator<Long> aggForKeyForWindows = localWindowStore.fetch(key, timeFrom, timeTo); // key must be local (application state is shared over all running Kafka Streams instances)
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
     *
     * @param initializer   an {@link Initializer} that computes an initial intermediate aggregation result
     * @param aggregator    an {@link Aggregator} that computes a new aggregate result
     * @param windows       the specification of the aggregation {@link Windows}
     * @param aggValueSerde aggregate value serdes for materializing the aggregated table,
     *                      if not specified the default serdes defined in the configs will be used
     * @param <VR>          the value type of the resulting {@link KTable}
     * @param storeName     the name of the state store created from this operation; valid characters are ASCII
     *                      alphanumerics, '.', '_' and '-'
     * @return a windowed {@link KTable} that contains "update" records with unmodified keys, and values that represent
     * the latest (rolling) aggregate for each key within a window
     */
    <W extends Window, VR> KTable<Windowed<K>, VR> aggregate(final Initializer<VR> initializer,
                                                             final Aggregator<? super K, ? super V, VR> aggregator,
                                                             final Windows<W> windows,
                                                             final Serde<VR> aggValueSerde,
                                                             final String storeName);

    /**
     * Aggregate the values of records in this stream by the grouped key and defined windows.
     * Records with {@code null} key or value are ignored.
     * Aggregating is a generalization of {@link #reduce(Reducer, Windows, StateStoreSupplier) combining via
     * reduce(...)} as it, for example, allows the result to have a different type than the input values.
     * The specified {@code windows} define either hopping time windows that can be overlapping or tumbling (c.f.
     * {@link TimeWindows}) or they define landmark windows (c.f. {@link UnlimitedWindows}).
     * The result is written into a local windowed {@link KeyValueStore} (which is basically an ever-updating
     * materialized view) provided by the given {@code storeSupplier}.
     * Windows are retained until their retention time expires (c.f. {@link Windows#until(long)}).
     * Furthermore, updates to the store are sent downstream into a windowed {@link KTable} changelog stream, where
     * "windowed" implies that the {@link KTable} key is a combined key of the original record key and a window ID.
     * <p>
     * The specified {@link Initializer} is applied once per window directly before the first input record is
     * processed to provide an initial intermediate aggregation result that is used to process the first record.
     * The specified {@link Aggregator} is applied for each input record and computes a new aggregate using the current
     * aggregate (or for the very first record using the intermediate aggregation result provided via the
     * {@link Initializer}) and the record's value.
     * Thus, {@code aggregate(Initializer, Aggregator, Windows, StateStoreSupplier)} can be used to compute aggregate
     * functions like count (c.f. {@link #count(String)}).
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
     * KafkaStreams streams = ... // some windowed aggregation on value type Long
     * Sting storeName = storeSupplier.name();
     * ReadOnlyWindowStore<String,Long> localWindowStore = streams.store(storeName, QueryableStoreTypes.<String, Long>windowStore());
     * String key = "some-key";
     * long fromTime = ...;
     * long toTime = ...;
     * WindowStoreIterator<Long> aggForKeyForWindows = localWindowStore.fetch(key, timeFrom, timeTo); // key must be local (application state is shared over all running Kafka Streams instances)
     * }</pre>
     * For non-local keys, a custom RPC mechanism must be implemented using {@link KafkaStreams#allMetadata()} to
     * query the value of the key on a parallel running instance of your Kafka Streams application.
     *
     * @param initializer   an {@link Initializer} that computes an initial intermediate aggregation result
     * @param aggregator    an {@link Aggregator} that computes a new aggregate result
     * @param windows       the specification of the aggregation {@link Windows}
     * @param <VR>          the value type of the resulting {@link KTable}
     * @param storeSupplier user defined state store supplier
     * @return a windowed {@link KTable} that contains "update" records with unmodified keys, and values that represent
     * the latest (rolling) aggregate for each key within a window
     */
    <W extends Window, VR> KTable<Windowed<K>, VR> aggregate(final Initializer<VR> initializer,
                                                             final Aggregator<? super K, ? super V, VR> aggregator,
                                                             final Windows<W> windows,
                                                             final StateStoreSupplier<WindowStore> storeSupplier);

    /**
     * Aggregate the values of records in this stream by the grouped key and defined {@link SessionWindows}.
     * Records with {@code null} key or value are ignored.
     * Aggregating is a generalization of {@link #reduce(Reducer, SessionWindows, String) combining via
     * reduce(...)} as it, for example, allows the result to have a different type than the input values.
     * The result is written into a local {@link SessionStore} (which is basically an ever-updating
     * materialized view) that can be queried using the provided {@code storeName}.
     * SessionWindows are retained until their retention time expires (c.f. {@link SessionWindows#until(long)}).
     * Furthermore, updates to the store are sent downstream into a windowed {@link KTable} changelog stream, where
     * "windowed" implies that the {@link KTable} key is a combined key of the original record key and a window ID.
     * <p>
     * The specified {@link Initializer} is applied once per session directly before the first input record is
     * processed to provide an initial intermediate aggregation result that is used to process the first record.
     * The specified {@link Aggregator} is applied for each input record and computes a new aggregate using the current
     * aggregate (or for the very first record using the intermediate aggregation result provided via the
     * {@link Initializer}) and the record's value.
     * Thus, {@code aggregate(Initializer, Aggregator, Merger, SessionWindows, Serde, String)} can be used to compute
     * aggregate functions like count (c.f. {@link #count(String)})
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
     * KafkaStreams streams = ... // some windowed aggregation on value type double
     * Sting storeName = storeSupplier.name();
     * ReadOnlySessionStore<String, Long> sessionStore = streams.store(storeName, QueryableStoreTypes.<String, Long>sessionStore());
     * String key = "some-key";
     * KeyValueIterator<Windowed<String>, Long> aggForKeyForSession = localWindowStore.fetch(key); // key must be local (application state is shared over all running Kafka Streams instances)
     * }</pre>
     * For non-local keys, a custom RPC mechanism must be implemented using {@link KafkaStreams#allMetadata()} to
     * query the value of the key on a parallel running instance of your Kafka Streams application.
     *
     * @param initializer    the instance of {@link Initializer}
     * @param aggregator     the instance of {@link Aggregator}
     * @param sessionMerger  the instance of {@link Merger}
     * @param sessionWindows the specification of the aggregation {@link SessionWindows}
     * @param aggValueSerde aggregate value serdes for materializing the aggregated table,
     *                      if not specified the default serdes defined in the configs will be used
     * @param <T>           the value type of the resulting {@link KTable}
     * @param storeName     the name of the state store created from this operation; valid characters are ASCII
     *                      alphanumerics, '.', '_' and '-'
     * @return a windowed {@link KTable} that contains "update" records with unmodified keys, and values that represent
     * the latest (rolling) aggregate for each key within a window
     */
    <T> KTable<Windowed<K>, T> aggregate(final Initializer<T> initializer,
                                         final Aggregator<? super K, ? super V, T> aggregator,
                                         final Merger<? super K, T> sessionMerger,
                                         final SessionWindows sessionWindows,
                                         final Serde<T> aggValueSerde,
                                         final String storeName);

    /**
     * Aggregate the values of records in this stream by the grouped key and defined {@link SessionWindows}.
     * Records with {@code null} key or value are ignored.
     * Aggregating is a generalization of {@link #reduce(Reducer, SessionWindows, String) combining via
     * reduce(...)} as it, for example, allows the result to have a different type than the input values.
     * The result is written into a local {@link SessionStore} (which is basically an ever-updating materialized view)
     * provided by the given {@code storeSupplier}.
     * SessionWindows are retained until their retention time expires (c.f. {@link SessionWindows#until(long)}).
     * Furthermore, updates to the store are sent downstream into a windowed {@link KTable} changelog stream, where
     * "windowed" implies that the {@link KTable} key is a combined key of the original record key and a window ID.
     * <p>
     * The specified {@link Initializer} is applied once per session directly before the first input record is
     * processed to provide an initial intermediate aggregation result that is used to process the first record.
     * The specified {@link Aggregator} is applied for each input record and computes a new aggregate using the current
     * aggregate (or for the very first record using the intermediate aggregation result provided via the
     * {@link Initializer}) and the record's value.
     * Thus, {@code #aggregate(Initializer, Aggregator, Merger, SessionWindows, Serde, StateStoreSupplier)} can be used
     * to compute aggregate functions like count (c.f. {@link #count(String)}).
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
     * KafkaStreams streams = ... // some windowed aggregation on value type double
     * Sting storeName = storeSupplier.name();
     * ReadOnlySessionStore<String, Long> sessionStore = streams.store(storeName, QueryableStoreTypes.<String, Long>sessionStore());
     * String key = "some-key";
     * KeyValueIterator<Windowed<String>, Long> aggForKeyForSession = localWindowStore.fetch(key); // key must be local (application state is shared over all running Kafka Streams instances)
     * }</pre>
     * For non-local keys, a custom RPC mechanism must be implemented using {@link KafkaStreams#allMetadata()} to
     * query the value of the key on a parallel running instance of your Kafka Streams application.
     *
     *
     * @param initializer    the instance of {@link Initializer}
     * @param aggregator     the instance of {@link Aggregator}
     * @param sessionMerger  the instance of {@link Merger}
     * @param sessionWindows the specification of the aggregation {@link SessionWindows}
     * @param aggValueSerde  aggregate value serdes for materializing the aggregated table,
     *                       if not specified the default serdes defined in the configs will be used
     * @param storeSupplier  user defined state store supplier
     * @param <T>           the value type of the resulting {@link KTable}
     * @return a windowed {@link KTable} that contains "update" records with unmodified keys, and values that represent
     * the latest (rolling) aggregate for each key within a window
     */
    <T> KTable<Windowed<K>, T> aggregate(final Initializer<T> initializer,
                                         final Aggregator<? super K, ? super V, T> aggregator,
                                         final Merger<? super K, T> sessionMerger,
                                         final SessionWindows sessionWindows,
                                         final Serde<T> aggValueSerde,
                                         final StateStoreSupplier<SessionStore> storeSupplier);
    }


## KGroupedTable


    /**
     * Aggregate the value of records of the original {@link KTable} that got {@link KTable#groupBy(KeyValueMapper)
     * mapped} to the same key into a new instance of {@link KTable} using default serializers and deserializers.
     * Records with {@code null} key are ignored.
     * Aggregating is a generalization of {@link #reduce(Reducer, Reducer, String) combining via reduce(...)} as it,
     * for example, allows the result to have a different type than the input values.
     * If the result value type does not match the {@link StreamsConfig#VALUE_SERDE_CLASS_CONFIG default value
     * serde} you should use {@link KGroupedTable#aggregate(Initializer, Aggregator, Aggregator, Serde, String)
     * aggregate(Initializer, Aggregator, Aggregator, Serde, String)}.
     * The result is written into a local {@link KeyValueStore} (which is basically an ever-updating materialized view)
     * provided by the given {@code storeSupplier}.
     * Furthermore, updates to the store are sent downstream into a {@link KTable} changelog stream.
     * <p>
     * The specified {@link Initializer} is applied once directly before the first input record is processed to
     * provide an initial intermediate aggregation result that is used to process the first record.
     * Each update to the original {@link KTable} results in a two step update of the result {@link KTable}.
     * The specified {@link Aggregator adder} is applied for each update record and computes a new aggregate using the
     * current aggregate (or for the very first record using the intermediate aggregation result provided via the
     * {@link Initializer}) and the record's value by adding the new record to the aggregate.
     * The specified {@link Aggregator substractor} is applied for each "replaced" record of the original {@link KTable}
     * and computes a new aggregate using the current aggregate and the record's value by "removing" the "replaced"
     * record from the aggregate.
     * Thus, {@code aggregate(Initializer, Aggregator, Aggregator, String)} can be used to compute aggregate functions
     * like sum.
     * For sum, the initializer, adder, and substractor would work as follows:
     * <pre>{@code
     * // in this example, LongSerde.class must be set as default value serde in StreamsConfig
     * public class SumInitializer implements Initializer<Long> {
     *   public Long apply() {
     *     return 0L;
     *   }
     * }
     *
     * public class SumAdder implements Aggregator<String, Integer, Long> {
     *   public Long apply(String key, Integer newValue, Long aggregate) {
     *     return aggregate + newValue;
     *   }
     * }
     *
     * public class SumSubstractor implements Aggregator<String, Integer, Long> {
     *   public Long apply(String key, Integer oldValue, Long aggregate) {
     *     return aggregate - oldValue;
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
     * You can retrieve all generated internal topic names via {@link KafkaStreams#toString()}.
     *
     * @param initializer a {@link Initializer} that provides an initial aggregate result value
     * @param adder       a {@link Aggregator} that adds a new record to the aggregate result
     * @param subtractor  a {@link Aggregator} that removed an old record from the aggregate result
     * @param storeName   the name of the underlying {@link KTable} state store
     * @param <VR>        the value type of the aggregated {@link KTable}
     * @return a {@link KTable} that contains "update" records with unmodified keys, and values that represent the
     * latest (rolling) aggregate for each key
     */
    <VR> KTable<K, VR> aggregate(final Initializer<VR> initializer,
                                 final Aggregator<? super K, ? super V, VR> adder,
                                 final Aggregator<? super K, ? super V, VR> subtractor,
                                 final String storeName);

    /**
     * Aggregate the value of records of the original {@link KTable} that got {@link KTable#groupBy(KeyValueMapper)
     * mapped} to the same key into a new instance of {@link KTable} using default serializers and deserializers.
     * Records with {@code null} key are ignored.
     * Aggregating is a generalization of {@link #reduce(Reducer, Reducer, String) combining via reduce(...)} as it,
     * for example, allows the result to have a different type than the input values.
     * The result is written into a local {@link KeyValueStore} (which is basically an ever-updating materialized view)
     * that can be queried using the provided {@code storeName}.
     * Furthermore, updates to the store are sent downstream into a {@link KTable} changelog stream.
     * <p>
     * The specified {@link Initializer} is applied once directly before the first input record is processed to
     * provide an initial intermediate aggregation result that is used to process the first record.
     * Each update to the original {@link KTable} results in a two step update of the result {@link KTable}.
     * The specified {@link Aggregator adder} is applied for each update record and computes a new aggregate using the
     * current aggregate (or for the very first record using the intermediate aggregation result provided via the
     * {@link Initializer}) and the record's value by adding the new record to the aggregate.
     * The specified {@link Aggregator substractor} is applied for each "replaced" record of the original {@link KTable}
     * and computes a new aggregate using the current aggregate and the record's value by "removing" the "replaced"
     * record from the aggregate.
     * Thus, {@code aggregate(Initializer, Aggregator, Aggregator, String)} can be used to compute aggregate functions
     * like sum.
     * For sum, the initializer, adder, and substractor would work as follows:
     * <pre>{@code
     * public class SumInitializer implements Initializer<Long> {
     *   public Long apply() {
     *     return 0L;
     *   }
     * }
     *
     * public class SumAdder implements Aggregator<String, Integer, Long> {
     *   public Long apply(String key, Integer newValue, Long aggregate) {
     *     return aggregate + newValue;
     *   }
     * }
     *
     * public class SumSubstractor implements Aggregator<String, Integer, Long> {
     *   public Long apply(String key, Integer oldValue, Long aggregate) {
     *     return aggregate - oldValue;
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
     * @param initializer   a {@link Initializer} that provides an initial aggregate result value
     * @param adder         a {@link Aggregator} that adds a new record to the aggregate result
     * @param subtractor    a {@link Aggregator} that removed an old record from the aggregate result
     * @param aggValueSerde aggregate value serdes for materializing the aggregated table,
     *                      if not specified the default serdes defined in the configs will be used
     * @param storeName     the name of the underlying {@link KTable} state store; valid characters are ASCII
     *                      alphanumerics, '.', '_' and '-'
     * @param <VR>          the value type of the aggregated {@link KTable}
     * @return a {@link KTable} that contains "update" records with unmodified keys, and values that represent the
     * latest (rolling) aggregate for each key
     */
    <VR> KTable<K, VR> aggregate(final Initializer<VR> initializer,
                                 final Aggregator<? super K, ? super V, VR> adder,
                                 final Aggregator<? super K, ? super V, VR> subtractor,
                                 final Serde<VR> aggValueSerde,
                                 final String storeName);

    /**
     * Aggregate the value of records of the original {@link KTable} that got {@link KTable#groupBy(KeyValueMapper)
     * mapped} to the same key into a new instance of {@link KTable} using default serializers and deserializers.
     * Records with {@code null} key are ignored.
     * Aggregating is a generalization of {@link #reduce(Reducer, Reducer, String) combining via reduce(...)} as it,
     * for example, allows the result to have a different type than the input values.
     * The result is written into a local {@link KeyValueStore} (which is basically an ever-updating materialized view)
     * provided by the given {@code storeSupplier}.
     * Furthermore, updates to the store are sent downstream into a {@link KTable} changelog stream.
     * <p>
     * The specified {@link Initializer} is applied once directly before the first input record is processed to
     * provide an initial intermediate aggregation result that is used to process the first record.
     * Each update to the original {@link KTable} results in a two step update of the result {@link KTable}.
     * The specified {@link Aggregator adder} is applied for each update record and computes a new aggregate using the
     * current aggregate (or for the very first record using the intermediate aggregation result provided via the
     * {@link Initializer}) and the record's value by adding the new record to the aggregate.
     * The specified {@link Aggregator substractor} is applied for each "replaced" record of the original {@link KTable}
     * and computes a new aggregate using the current aggregate and the record's value by "removing" the "replaced"
     * record from the aggregate.
     * Thus, {@code aggregate(Initializer, Aggregator, Aggregator, String)} can be used to compute aggregate functions
     * like sum.
     * For sum, the initializer, adder, and substractor would work as follows:
     * <pre>{@code
     * public class SumInitializer implements Initializer<Long> {
     *   public Long apply() {
     *     return 0L;
     *   }
     * }
     *
     * public class SumAdder implements Aggregator<String, Integer, Long> {
     *   public Long apply(String key, Integer newValue, Long aggregate) {
     *     return aggregate + newValue;
     *   }
     * }
     *
     * public class SumSubstractor implements Aggregator<String, Integer, Long> {
     *   public Long apply(String key, Integer oldValue, Long aggregate) {
     *     return aggregate - oldValue;
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
     * @param initializer   a {@link Initializer} that provides an initial aggregate result value
     * @param adder         a {@link Aggregator} that adds a new record to the aggregate result
     * @param subtractor    a {@link Aggregator} that removed an old record from the aggregate result
     * @param storeSupplier user defined state store supplier
     * @param <VR>          the value type of the aggregated {@link KTable}
     * @return a {@link KTable} that contains "update" records with unmodified keys, and values that represent the
     * latest (rolling) aggregate for each key
     */
    <VR> KTable<K, VR> aggregate(final Initializer<VR> initializer,
                                 final Aggregator<? super K, ? super V, VR> adder,
                                 final Aggregator<? super K, ? super V, VR> subtractor,
                                 final StateStoreSupplier<KeyValueStore> storeSupplier);

}
