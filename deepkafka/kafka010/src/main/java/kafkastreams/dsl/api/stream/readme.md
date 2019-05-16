# name 
stream

# return
input topic(s) → KStream

# description

Create a KStream from the specified Kafka input topic(s), interpreting the data as a record stream. A KStream represents a partitioned record stream. (details)

Slightly simplified, in the case of a KStream, the local KStream instance of every application instance will be populated with data from only a subset of the partitions of the input topic. Collectively, i.e. across all application instances, all the partitions of the input topic will be read and processed.

When to provide serdes explicitly:

If you do not specify serdes explicitly, the default serdes from the configuration are used.
You must specificy serdes explicitly if the key and/or value types of the records in the Kafka input topic(s) do not match the configured default serdes.
See Data types and serialization for information about configuring default serdes, available serdes, and implementing your own custom serdes.
Several variants of stream exist to e.g. specify a regex pattern for input topics to read from.

# code
## KStreamBuilder


    /**
     * Create a {@link KStream} from the specified topics.
     * The default {@code "auto.offset.reset"} strategy and default key and value deserializers as specified in the
     * {@link StreamsConfig config} are used.
     * <p>
     * If multiple topics are specified there are no ordering guaranteed for records from different topics.
     * <p>
     * Note that the specified input topics must be partitioned by key.
     * If this is not the case it is the user's responsibility to repartition the date before any key based operation
     * (like aggregation or join) is applied to the returned {@link KStream}.
     *
     * @param topics the topic names; must contain at least one topic name
     * @return a {@link KStream} for the specified topics
     */
    public <K, V> KStream<K, V> stream(final String... topics) {
        return stream(null, null, null, topics);
    }

    /**
     * Create a {@link KStream} from the specified topics.
     * The default key and value deserializers as specified in the {@link StreamsConfig config} are used.
     * <p>
     * If multiple topics are specified there are no ordering guaranteed for records from different topics.
     * <p>
     * Note that the specified input topics must be partitioned by key.
     * If this is not the case it is the user's responsibility to repartition the date before any key based operation
     * (like aggregation or join) is applied to the returned {@link KStream}.
     *
     * @param offsetReset the {@code "auto.offset.reset"} policy to use for the specified topics if no valid committed
     *                    offsets are available
     * @param topics      the topic names; must contain at least one topic name
     * @return a {@link KStream} for the specified topics
     */
    public <K, V> KStream<K, V> stream(final AutoOffsetReset offsetReset,
                                       final String... topics) {
        return stream(offsetReset, null, null, topics);
    }

    /**
     * Create a {@link KStream} from the specified topic pattern.
     * The default {@code "auto.offset.reset"} strategy and default key and value deserializers as specified in the
     * {@link StreamsConfig config} are used.
     * <p>
     * If multiple topics are matched by the specified pattern, the created {@link KStream} will read data from all of
     * them and there is no ordering guarantee between records from different topics.
     * <p>
     * Note that the specified input topics must be partitioned by key.
     * If this is not the case it is the user's responsibility to repartition the date before any key based operation
     * (like aggregation or join) is applied to the returned {@link KStream}.
     *
     * @param topicPattern the pattern to match for topic names
     * @return a {@link KStream} for topics matching the regex pattern.
     */
    public <K, V> KStream<K, V> stream(final Pattern topicPattern) {
        return stream(null, null, null, topicPattern);
    }

    /**
     * Create a {@link KStream} from the specified topic pattern.
     * The default key and value deserializers as specified in the {@link StreamsConfig config} are used.
     * <p>
     * If multiple topics are matched by the specified pattern, the created {@link KStream} will read data from all of
     * them and there is no ordering guarantee between records from different topics.
     * <p>
     * Note that the specified input topics must be partitioned by key.
     * If this is not the case it is the user's responsibility to repartition the date before any key based operation
     * (like aggregation or join) is applied to the returned {@link KStream}.
     *
     * @param offsetReset  the {@code "auto.offset.reset"} policy to use for the matched topics if no valid committed
     *                     offsets are available
     * @param topicPattern the pattern to match for topic names
     * @return a {@link KStream} for topics matching the regex pattern.
     */
    public <K, V> KStream<K, V> stream(final AutoOffsetReset offsetReset, final Pattern topicPattern) {
        return stream(offsetReset, null, null, topicPattern);
    }

    /**
     * Create a {@link KStream} from the specified topics.
     * The default {@code "auto.offset.reset"} strategy as specified in the {@link StreamsConfig config} is used.
     * <p>
     * If multiple topics are specified there are no ordering guaranteed for records from different topics.
     * <p>
     * Note that the specified input topics must be partitioned by key.
     * If this is not the case it is the user's responsibility to repartition the date before any key based operation
     * (like aggregation or join) is applied to the returned {@link KStream}.
     *
     * @param keySerde key serde used to read this source {@link KStream},
     *                 if not specified the default serde defined in the configs will be used
     * @param valSerde value serde used to read this source {@link KStream},
     *                 if not specified the default serde defined in the configs will be used
     * @param topics   the topic names; must contain at least one topic name
     * @return a {@link KStream} for the specified topics
     */
    public <K, V> KStream<K, V> stream(final Serde<K> keySerde, final Serde<V> valSerde, final String... topics) {
        return stream(null, keySerde, valSerde, topics);
    }


    /**
     * Create a {@link KStream} from the specified topics.
     * <p>
     * If multiple topics are specified there are no ordering guaranteed for records from different topics.
     * <p>
     * Note that the specified input topics must be partitioned by key.
     * If this is not the case it is the user's responsibility to repartition the date before any key based operation
     * (like aggregation or join) is applied to the returned {@link KStream}.
     *
     * @param offsetReset the {@code "auto.offset.reset"} policy to use for the specified topics if no valid committed
     *                    offsets are available
     * @param keySerde    key serde used to read this source {@link KStream},
     *                    if not specified the default serde defined in the configs will be used
     * @param valSerde    value serde used to read this source {@link KStream},
     *                    if not specified the default serde defined in the configs will be used
     * @param topics      the topic names; must contain at least one topic name
     * @return a {@link KStream} for the specified topics
     */
    public <K, V> KStream<K, V> stream(final AutoOffsetReset offsetReset,
                                       final Serde<K> keySerde,
                                       final Serde<V> valSerde,
                                       final String... topics) {
        final String name = newName(KStreamImpl.SOURCE_NAME);

        addSource(offsetReset, name,  keySerde == null ? null : keySerde.deserializer(), valSerde == null ? null : valSerde.deserializer(), topics);

        return new KStreamImpl<>(this, name, Collections.singleton(name), false);
    }


    /**
     * Create a {@link KStream} from the specified topic pattern.
     * The default {@code "auto.offset.reset"} strategy as specified in the {@link StreamsConfig config} is used.
     * <p>
     * If multiple topics are matched by the specified pattern, the created {@link KStream} will read data from all of
     * them and there is no ordering guarantee between records from different topics.
     * <p>
     * Note that the specified input topics must be partitioned by key.
     * If this is not the case it is the user's responsibility to repartition the date before any key based operation
     * (like aggregation or join) is applied to the returned {@link KStream}.
     *
     * @param keySerde     key serde used to read this source {@link KStream},
     *                     if not specified the default serde defined in the configs will be used
     * @param valSerde     value serde used to read this source {@link KStream},
     *                     if not specified the default serde defined in the configs will be used
     * @param topicPattern the pattern to match for topic names
     * @return a {@link KStream} for topics matching the regex pattern.
     */
    public <K, V> KStream<K, V> stream(final Serde<K> keySerde,
                                       final Serde<V> valSerde,
                                       final Pattern topicPattern) {
        return stream(null, keySerde, valSerde, topicPattern);
    }

    /**
     * Create a {@link KStream} from the specified topic pattern.
     * <p>
     * If multiple topics are matched by the specified pattern, the created {@link KStream} will read data from all of
     * them and there is no ordering guarantee between records from different topics.
     * <p>
     * Note that the specified input topics must be partitioned by key.
     * If this is not the case it is the user's responsibility to repartition the date before any key based operation
     * (like aggregation or join) is applied to the returned {@link KStream}.
     *
     * @param offsetReset  the {@code "auto.offset.reset"} policy to use for the matched topics if no valid committed
     *                     offsets are available
     * @param keySerde     key serde used to read this source {@link KStream},
     *                     if not specified the default serde defined in the configs will be used
     * @param valSerde     value serde used to read this source {@link KStream},
     *                     if not specified the default serde defined in the configs will be used
     * @param topicPattern the pattern to match for topic names
     * @return a {@link KStream} for topics matching the regex pattern.
     */
    public <K, V> KStream<K, V> stream(final AutoOffsetReset offsetReset,
                                       final Serde<K> keySerde,
                                       final Serde<V> valSerde,
                                       final Pattern topicPattern) {
        final String name = newName(KStreamImpl.SOURCE_NAME);

        addSource(offsetReset, name, keySerde == null ? null : keySerde.deserializer(), valSerde == null ? null : valSerde.deserializer(), topicPattern);

        return new KStreamImpl<>(this, name, Collections.singleton(name), false);
    }
