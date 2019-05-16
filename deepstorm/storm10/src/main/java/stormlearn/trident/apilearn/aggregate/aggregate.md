- aggregate

***

    public Stream aggregate(Aggregator agg, Fields functionFields) {
        return aggregate(null, agg, functionFields);
    }

    public Stream aggregate(Fields inputFields, Aggregator agg, Fields functionFields) {
        projectionValidation(inputFields);
        return chainedAgg()
               .aggregate(inputFields, agg, functionFields)
               .chainEnd();
    }

    public Stream aggregate(CombinerAggregator agg, Fields functionFields) {
        return aggregate(null, agg, functionFields);
    }

    public Stream aggregate(Fields inputFields, CombinerAggregator agg, Fields functionFields) {
        projectionValidation(inputFields);
        return chainedAgg()
               .aggregate(inputFields, agg, functionFields)
               .chainEnd();
    }

    public Stream aggregate(ReducerAggregator agg, Fields functionFields) {
        return aggregate(null, agg, functionFields);
    }

    public Stream aggregate(Fields inputFields, ReducerAggregator agg, Fields functionFields) {
        projectionValidation(inputFields);
        return chainedAgg()
                .aggregate(inputFields, agg, functionFields)
                .chainEnd();
    }
