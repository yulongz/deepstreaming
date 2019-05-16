# name 
transformValues

# return
KStream -> KStream

# description

Applies a ValueTransformer to each record, while retaining the key of the original record. transformValues() allows you to leverage the Processor API from the DSL. (details)

Each input record is transformed into exactly one output record (zero output records or multiple output records are not possible). The ValueTransformer may return null as the new value for a record.

transformValues is preferable to transform because it will not cause data re-partitioning.

transformValues is essentially equivalent to adding the ValueTransformer via TopologyBuilder#addProcessor() to your processor topology.

# code
## KStream


    /**
     * Transform the value of each input record into a new value (with possible new type) of the output record.
     * A {@link ValueTransformer} (provided by the given {@link ValueTransformerSupplier}) is applies to each input
     * record value and computes a new value for it.
     * Thus, an input record {@code <K,V>} can be transformed into an output record {@code <K:V'>}.
     * This is a stateful record-by-record operation (cf. {@link #mapValues(ValueMapper)}).
     * Furthermore, via {@link ValueTransformer#punctuate(long)} the processing progress can be observed and additional
     * periodic actions get be performed.
     * <p>
     * In order to assign a state, the state must be created and registered beforehand:
     * <pre>{@code
     * // create store
     * StateStoreSupplier myStore = Stores.create("myValueTransformState")
     *     .withKeys(...)
     *     .withValues(...)
     *     .persistent() // optional
     *     .build();
     *
     * // register store
     * builder.addStore(myStore);
     *
     * KStream outputStream = inputStream.transformValues(new ValueTransformerSupplier() { ... }, "myValueTransformState");
     * }</pre>
     * <p>
     * Within the {@link ValueTransformer}, the state is obtained via the
     * {@link ProcessorContext}.
     * To trigger periodic actions via {@link ValueTransformer#punctuate(long) punctuate()}, a schedule must be
     * registered.
     * In contrast to {@link #transform(TransformerSupplier, String...) transform()}, no additional {@link KeyValue}
     * pairs should be emitted via {@link ProcessorContext#forward(Object, Object)
     * ProcessorContext.forward()}.
     * <pre>{@code
     * new ValueTransformerSupplier() {
     *     ValueTransformer get() {
     *         return new ValueTransformer() {
     *             private StateStore state;
     *
     *             void init(ProcessorContext context) {
     *                 this.state = context.getStateStore("myValueTransformState");
     *                 context.schedule(1000); // call #punctuate() each 1000ms
     *             }
     *
     *             NewValueType transform(V value) {
     *                 // can access this.state
     *                 return new NewValueType(); // or null
     *             }
     *
     *             NewValueType punctuate(long timestamp) {
     *                 // can access this.state
     *                 return null; // don't return result -- can also be "new NewValueType()" (current key will be used to build KeyValue pair)
     *             }
     *
     *             void close() {
     *                 // can access this.state
     *             }
     *         }
     *     }
     * }
     * }</pre>
     * <p>
     * Setting a new value preserves data co-location with respect to the key.
     * Thus, <em>no</em> internal data redistribution is required if a key based operator (like an aggregation or join)
     * is applied to the result {@code KStream}. (cf. {@link #transform(TransformerSupplier, String...)})
     *
     * @param valueTransformerSupplier a instance of {@link ValueTransformerSupplier} that generates a
     *                                 {@link ValueTransformer}
     * @param stateStoreNames          the names of the state stores used by the processor
     * @param <VR>                     the value type of the result stream
     * @return a {@code KStream} that contains records with unmodified key and new values (possibly of different type)
     * @see #mapValues(ValueMapper)
     * @see #transform(TransformerSupplier, String...)
     */
    <VR> KStream<K, VR> transformValues(final ValueTransformerSupplier<? super V, ? extends VR> valueTransformerSupplier,
                                        final String... stateStoreNames);
