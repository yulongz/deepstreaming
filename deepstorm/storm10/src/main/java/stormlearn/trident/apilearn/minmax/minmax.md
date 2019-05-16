- minBy

***

    /**
     * This aggregator operation computes the minimum of tuples by the given {@code inputFieldName} and it is
     * assumed that its value is an instance of {@code Comparable}. If the value of tuple with field {@code inputFieldName} is not an
     * instance of {@code Comparable} then it throws {@code ClassCastException}
     *
     * @param inputFieldName input field name
     * @return the new stream with this operation.
     */
    public Stream minBy(String inputFieldName) {
        Aggregator<ComparisonAggregator.State> min = new Min(inputFieldName);
        return comparableAggregateStream(inputFieldName, min);
    }

    /**
     * This aggregator operation computes the minimum of tuples by the given {@code inputFieldName} in a stream by
     * using the given {@code comparator}. If the value of tuple with field {@code inputFieldName} is not an
     * instance of {@code T} then it throws {@code ClassCastException}
     *
     * @param inputFieldName input field name
     * @param comparator comparator used in for finding minimum of two tuple values of {@code inputFieldName}.
     * @param <T> type of tuple's given input field value.
     * @return the new stream with this operation.
     */
    public <T> Stream minBy(String inputFieldName, Comparator<T> comparator) {
        Aggregator<ComparisonAggregator.State> min = new MinWithComparator<>(inputFieldName, comparator);
        return comparableAggregateStream(inputFieldName, min);
    }


- min

***

    /**
     * This aggregator operation computes the minimum of tuples in a stream by using the given {@code comparator} with
     * {@code TridentTuple}s.
     *
     * @param comparator comparator used in for finding minimum of two tuple values.
     * @return the new stream with this operation.
     */
    public Stream min(Comparator<TridentTuple> comparator) {
        Aggregator<ComparisonAggregator.State> min = new MinWithComparator<>(comparator);
        return comparableAggregateStream(null, min);
    }

- maxBy

***

    /**
     * This aggregator operation computes the maximum of tuples by the given {@code inputFieldName} and it is
     * assumed that its value is an instance of {@code Comparable}. If the value of tuple with field {@code inputFieldName} is not an
     * instance of {@code Comparable} then it throws {@code ClassCastException}
     *
     * @param inputFieldName input field name
     * @return the new stream with this operation.
     */
    public Stream maxBy(String inputFieldName) {
        Aggregator<ComparisonAggregator.State> max = new Max(inputFieldName);
        return comparableAggregateStream(inputFieldName, max);
    }

    /**
     * This aggregator operation computes the maximum of tuples by the given {@code inputFieldName} in a stream by
     * using the given {@code comparator}. If the value of tuple with field {@code inputFieldName} is not an
     * instance of {@code T} then it throws {@code ClassCastException}
     *
     * @param inputFieldName input field name
     * @param comparator comparator used in for finding maximum of two tuple values of {@code inputFieldName}.
     * @param <T> type of tuple's given input field value.
     * @return the new stream with this operation.
     */
    public <T> Stream maxBy(String inputFieldName, Comparator<T> comparator) {
        Aggregator<ComparisonAggregator.State> max = new MaxWithComparator<>(inputFieldName, comparator);
        return comparableAggregateStream(inputFieldName, max);
    }

- max

***

    /**
     * This aggregator operation computes the maximum of tuples in a stream by using the given {@code comparator} with
     * {@code TridentTuple}s.
     *
     * @param comparator comparator used in for finding maximum of two tuple values.
     * @return the new stream with this operation.
     */
    public Stream max(Comparator<TridentTuple> comparator) {
        Aggregator<ComparisonAggregator.State> max = new MaxWithComparator<>(comparator);
        return comparableAggregateStream(null, max);
    }
