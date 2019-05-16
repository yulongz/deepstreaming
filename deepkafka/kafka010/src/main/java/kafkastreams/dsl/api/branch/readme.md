# name 
branch

# return
KStream → KStream[]

# description

Branch (or split) a KStream based on the supplied predicates into one or more KStream instances. (details)

Predicates are evaluated in order. A record is placed to one and only one output stream on the first match: if the n-th predicate evaluates to true, the record is placed to n-th stream. If no predicate matches, the the record is dropped.

Branching is useful, for example, to route records to different downstream topics.

# code
## KStream

    /**
     * Creates an array of {@code KStream} from this stream by branching the records in the original stream based on
     * the supplied predicates.
     * Each record is evaluated against the supplied predicates, and predicates are evaluated in order.
     * Each stream in the result array corresponds position-wise (index) to the predicate in the supplied predicates.
     * The branching happens on first-match: A record in the original stream is assigned to the corresponding result
     * stream for the first predicate that evaluates to true, and is assigned to this stream only.
     * A record will be dropped if none of the predicates evaluate to true.
     * This is a stateless record-by-record operation.
     *
     * @param predicates the ordered list of {@link Predicate} instances
     * @return multiple distinct substreams of this {@code KStream}
     */
    @SuppressWarnings("unchecked")
    KStream<K, V>[] branch(final Predicate<? super K, ? super V>... predicates);

