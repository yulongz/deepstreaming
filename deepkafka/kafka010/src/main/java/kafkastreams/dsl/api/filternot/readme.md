# name 
filterNot

# return
KStream → KStream

KTable → KTable

# description

Evaluates a boolean function for each element and drops those for which the function returns true. (KStream details, KTable details)

# code
## KStream

    /**
     * Create a new {@code KStream} that consists all records of this stream which do <em>not</em> satisfy the given
     * predicate.
     * All records that <em>do</em> satisfy the predicate are dropped.
     * This is a stateless record-by-record operation.
     *
     * @param predicate a filter {@link Predicate} that is applied to each record
     * @return a {@code KStream} that contains only those records that do <em>not</em> satisfy the given predicate
     * @see #filter(Predicate)
     */
    KStream<K, V> filterNot(Predicate<? super K, ? super V> predicate);
    
    
## KTable

    /**
     * Create a new {@code KTable} that consists all records of this {@code KTable} which do <em>not</em> satisfy the
     * given predicate.
     * All records that <em>do</em> satisfy the predicate are dropped.
     * For each {@code KTable} update the filter is evaluated on the update record to produce an update record for the
     * result {@code KTable}.
     * This is a stateless record-by-record operation.
     * <p>
     * Note that {@code filterNot} for a <i>changelog stream</i> works different to {@link KStream#filterNot(Predicate)
     * record stream filters}, because {@link KeyValue records} with {@code null} values (so-called tombstone records)
     * have delete semantics.
     * Thus, for tombstones the provided filter predicate is not evaluated but the tombstone record is forwarded
     * directly if required (i.e., if there is anything to be deleted).
     * Furthermore, for each record that gets dropped (i.e., does satisfy the given predicate) a tombstone record is
     * forwarded.
     *
     * @param predicate a filter {@link Predicate} that is applied to each record
     * @return a {@code KTable} that contains only those records that do <em>not</em> satisfy the given predicate
     * @see #filter(Predicate)
     */
    KTable<K, V> filterNot(final Predicate<? super K, ? super V> predicate);
