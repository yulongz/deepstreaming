# name 
process

# return
KStream -> void

# description

Terminal operation. Applies a Processor to each record. process() allows you to leverage the Processor API from the DSL. (details)

This is essentially equivalent to adding the Processor via TopologyBuilder#addProcessor() to your processor topology.

# code
## KStream


    /**
     * Process all records in this stream, one record at a time, by applying a {@link Processor} (provided by the given
     * {@link ProcessorSupplier}).
     * This is a stateful record-by-record operation (cf. {@link #foreach(ForeachAction)}).
     * Furthermore, via {@link Processor#punctuate(long)} the processing progress can be observed and additional
     * periodic actions can be performed.
     * Note that this is a terminal operation that returns void.
     * <p>
     * In order to assign a state, the state must be created and registered beforehand:
     * <pre>{@code
     * // create store
     * StateStoreSupplier myStore = Stores.create("myProcessorState")
     *     .withKeys(...)
     *     .withValues(...)
     *     .persistent() // optional
     *     .build();
     *
     * // register store
     * builder.addStore(myStore);
     *
     * inputStream.process(new ProcessorSupplier() { ... }, "myProcessorState");
     * }</pre>
     * <p>
     * Within the {@link Processor}, the state is obtained via the
     * {@link ProcessorContext}.
     * To trigger periodic actions via {@link Processor#punctuate(long) punctuate()},
     * a schedule must be registered.
     * <pre>{@code
     * new ProcessorSupplier() {
     *     Processor get() {
     *         return new Processor() {
     *             private StateStore state;
     *
     *             void init(ProcessorContext context) {
     *                 this.state = context.getStateStore("myProcessorState");
     *                 context.schedule(1000); // call #punctuate() each 1000ms
     *             }
     *
     *             void process(K key, V value) {
     *                 // can access this.state
     *             }
     *
     *             void punctuate(long timestamp) {
     *                 // can access this.state
     *             }
     *
     *             void close() {
     *                 // can access this.state
     *             }
     *         }
     *     }
     * }
     * }</pre>
     *
     * @param processorSupplier a instance of {@link ProcessorSupplier} that generates a {@link Processor}
     * @param stateStoreNames   the names of the state store used by the processor
     * @see #foreach(ForeachAction)
     * @see #transform(TransformerSupplier, String...)
     */
    void process(final ProcessorSupplier<? super K, ? super V> processorSupplier,
                 final String... stateStoreNames);
