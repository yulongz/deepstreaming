# name 
foreach

# return
KStream → void
KTable → void

# description

Terminal operation. Performs a stateless action on each record. (KStream details, KTable details)

Note on processing guarantees: Any side effects of an action (such as writing to external systems) are not trackable by Kafka, which means they will typically not benefit from Kafka’s processing guarantees.

# code
## KStream

    /**
     * Perform an action on each record of {@code KStream}.
     * This is a stateless record-by-record operation (cf. {@link #process(ProcessorSupplier, String...)}).
     * Note that this is a terminal operation that returns void.
     *
     * @param action an action to perform on each record
     * @see #process(ProcessorSupplier, String...)
     */
    void foreach(final ForeachAction<? super K, ? super V> action);
    
## KTable

    /**
     * Perform an action on each update record of this {@code KTable}.
     * Note that this is a terminal operation that returns void.
     * <p>
     * Note that {@code foreach()} is not applied to the internal state store and only called for each new
     * {@code KTable} update record.
     *
     * @param action an action to perform on each record
     */
    void foreach(final ForeachAction<? super K, ? super V> action);