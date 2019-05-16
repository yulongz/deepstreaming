# name 
transform

# return
KStream -> KStream

# description

Applies a Transformer to each record. transform() allows you to leverage the Processor API from the DSL. (details)

Each input record is transformed into zero, one, or more output records (similar to the stateless flatMap). The Transformer must return null for zero output. You can modify the record’s key and value, including their types.

Marks the stream for data re-partitioning: Applying a grouping or a join after transform will result in re-partitioning of the records. If possible use transformValues instead, which will not cause data re-partitioning.

transform is essentially equivalent to adding the Transformer via TopologyBuilder#addProcessor() to your processor topology.

# code
## KStream


    /**
     * Transform each record of the input stream into zero or more records in the output stream (both key and value type
     * can be altered arbitrarily).
     * A {@link Transformer} (provided by the given {@link TransformerSupplier}) is applied to each input record and
     * computes zero or more output records.
     * Thus, an input record {@code <K,V>} can be transformed into output records {@code <K':V'>, <K'':V''>, ...}.
     * This is a stateful record-by-record operation (cf. {@link #flatMap(KeyValueMapper)}).
     * Furthermore, via {@link Transformer#punctuate(long)} the processing progress can be observed and additional
     * periodic actions can be performed.
     * <p>
     * In order to assign a state, the state must be created and registered beforehand:
     * <pre>{@code
     * // create store
     * StateStoreSupplier myStore = Stores.create("myTransformState")
     *     .withKeys(...)
     *     .withValues(...)
     *     .persistent() // optional
     *     .build();
     *
     * // register store
     * builder.addStore(myStore);
     *
     * KStream outputStream = inputStream.transform(new TransformerSupplier() { ... }, "myTransformState");
     * }</pre>
     * <p>
     * Within the {@link Transformer}, the state is obtained via the
     * {@link  ProcessorContext}.
     * To trigger periodic actions via {@link Transformer#punctuate(long) punctuate()}, a schedule must be registered.
     * The {@link Transformer} must return a {@link KeyValue} type in {@link Transformer#transform(Object, Object)
     * transform()} and {@link Transformer#punctuate(long) punctuate()}.
     * <pre>{@code
     * new TransformerSupplier() {
     *     Transformer get() {
     *         return new Transformer() {
     *             private ProcessorContext context;
     *             private StateStore state;
     *
     *             void init(ProcessorContext context) {
     *                 this.context = context;
     *                 this.state = context.getStateStore("myTransformState");
     *                 context.schedule(1000); // call #punctuate() each 1000ms
     *             }
     *
     *             KeyValue transform(K key, V value) {
     *                 // can access this.state
     *                 // can emit as many new KeyValue pairs as required via this.context#forward()
     *                 return new KeyValue(key, value); // can emit a single value via return -- can also be null
     *             }
     *
     *             KeyValue punctuate(long timestamp) {
     *                 // can access this.state
     *                 // can emit as many new KeyValue pairs as required via this.context#forward()
     *                 return null; // don't return result -- can also be "new KeyValue()"
     *             }
     *
     *             void close() {
     *                 // can access this.state
     *                 // can emit as many new KeyValue pairs as required via this.context#forward()
     *             }
     *         }
     *     }
     * }
     * }</pre>
     * <p>
     * Transforming records might result in an internal data redistribution if a key based operator (like an aggregation
     * or join) is applied to the result {@code KStream}.
     * (cf. {@link #transformValues(ValueTransformerSupplier, String...)})
     *
     * @param transformerSupplier a instance of {@link TransformerSupplier} that generates a {@link Transformer}
     * @param stateStoreNames     the names of the state stores used by the processor
     * @param <K1>                the key type of the new stream
     * @param <V1>                the value type of the new stream
     * @return a {@code KStream} that contains more or less records with new key and value (possibly of different type)
     * @see #flatMap(KeyValueMapper)
     * @see #transformValues(ValueTransformerSupplier, String...)
     * @see #process(ProcessorSupplier, String...)
     */
    <K1, V1> KStream<K1, V1> transform(final TransformerSupplier<? super K, ? super V, KeyValue<K1, V1>> transformerSupplier,
                                       final String... stateStoreNames);
