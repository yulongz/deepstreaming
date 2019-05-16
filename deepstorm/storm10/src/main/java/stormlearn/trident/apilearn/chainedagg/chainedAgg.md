- chainedAgg

***

    public ChainedAggregatorDeclarer chainedAgg() {
        return new ChainedAggregatorDeclarer(this, new BatchGlobalAggScheme());
    }