- tumblingWindow

***

    /**
     * Returns a stream of tuples which are aggregated results of a tumbling window with every {@code windowCount} of tuples.
     *
     * @param windowCount represents number of tuples in the window
     * @param windowStoreFactory intermediary tuple store for storing windowing tuples
     * @param inputFields projected fields for aggregator
     * @param aggregator aggregator to run on the window of tuples to compute the result and emit to the stream.
     * @param functionFields fields of values to emit with aggregation.
     *
     * @return the new stream with this operation.
     */
    public Stream tumblingWindow(int windowCount, WindowsStoreFactory windowStoreFactory,
                                      Fields inputFields, Aggregator aggregator, Fields functionFields) {
        return window(TumblingCountWindow.of(windowCount), windowStoreFactory, inputFields, aggregator, functionFields);
    }
    /**
     * Returns a stream of tuples which are aggregated results of a window that tumbles at duration of {@code windowDuration}
     *
     * @param windowDuration represents tumbling window duration configuration
     * @param windowStoreFactory intermediary tuple store for storing windowing tuples
     * @param inputFields projected fields for aggregator
     * @param aggregator aggregator to run on the window of tuples to compute the result and emit to the stream.
     * @param functionFields fields of values to emit with aggregation.
     *
     * @return the new stream with this operation.
     */
    public Stream tumblingWindow(BaseWindowedBolt.Duration windowDuration, WindowsStoreFactory windowStoreFactory,
                                     Fields inputFields, Aggregator aggregator, Fields functionFields) {
        return window(TumblingDurationWindow.of(windowDuration), windowStoreFactory, inputFields, aggregator, functionFields);
    }

- tumblingWindow

***

    /**
     * Returns a stream of tuples which are aggregated results of a sliding window with every {@code windowCount} of tuples
     * and slides the window after {@code slideCount}.
     *
     * @param windowCount represents tuples count of a window
     * @param slideCount the number of tuples after which the window slides
     * @param windowStoreFactory intermediary tuple store for storing windowing tuples
     * @param inputFields projected fields for aggregator
     * @param aggregator aggregator to run on the window of tuples to compute the result and emit to the stream.
     * @param functionFields fields of values to emit with aggregation.
     *
     * @return the new stream with this operation.
     */
    public Stream slidingWindow(int windowCount, int slideCount, WindowsStoreFactory windowStoreFactory,
                                     Fields inputFields, Aggregator aggregator, Fields functionFields) {
        return window(SlidingCountWindow.of(windowCount, slideCount), windowStoreFactory, inputFields, aggregator, functionFields);
    }

    /**
     * Returns a stream of tuples which are aggregated results of a window which slides at duration of {@code slidingInterval}
     * and completes a window at {@code windowDuration}
     *
     * @param windowDuration represents window duration configuration
     * @param slidingInterval the time duration after which the window slides
     * @param windowStoreFactory intermediary tuple store for storing windowing tuples
     * @param inputFields projected fields for aggregator
     * @param aggregator aggregator to run on the window of tuples to compute the result and emit to the stream.
     * @param functionFields fields of values to emit with aggregation.
     *
     * @return the new stream with this operation.
     */
    public Stream slidingWindow(BaseWindowedBolt.Duration windowDuration, BaseWindowedBolt.Duration slidingInterval,
                                    WindowsStoreFactory windowStoreFactory, Fields inputFields, Aggregator aggregator, Fields functionFields) {
        return window(SlidingDurationWindow.of(windowDuration, slidingInterval), windowStoreFactory, inputFields, aggregator, functionFields);
    }

window

***

    /**
     * Returns a stream of aggregated results based on the given window configuration which uses inmemory windowing tuple store.
     *
     * @param windowConfig window configuration like window length and slide length.
     * @param inputFields input fields
     * @param aggregator aggregator to run on the window of tuples to compute the result and emit to the stream.
     * @param functionFields fields of values to emit with aggregation.
     *
     * @return the new stream with this operation.
     */
    public Stream window(WindowConfig windowConfig, Fields inputFields, Aggregator aggregator, Fields functionFields) {
        // this store is used only for storing triggered aggregated results but not tuples as storeTuplesInStore is set
        // as false int he below call.
        InMemoryWindowsStoreFactory inMemoryWindowsStoreFactory = new InMemoryWindowsStoreFactory();
        return window(windowConfig, inMemoryWindowsStoreFactory, inputFields, aggregator, functionFields, false);
    }

    /**
     * Returns stream of aggregated results based on the given window configuration.
     *
     * @param windowConfig window configuration like window length and slide length.
     * @param windowStoreFactory intermediary tuple store for storing tuples for windowing
     * @param inputFields input fields
     * @param aggregator aggregator to run on the window of tuples to compute the result and emit to the stream.
     * @param functionFields fields of values to emit with aggregation.
     *
     * @return the new stream with this operation.
     */
    public Stream window(WindowConfig windowConfig, WindowsStoreFactory windowStoreFactory, Fields inputFields,
                         Aggregator aggregator, Fields functionFields) {
        return window(windowConfig, windowStoreFactory, inputFields, aggregator, functionFields, true);
    }

    private Stream window(WindowConfig windowConfig, WindowsStoreFactory windowStoreFactory, Fields inputFields, Aggregator aggregator,
                          Fields functionFields, boolean storeTuplesInStore) {
        projectionValidation(inputFields);
        windowConfig.validate();

        Fields fields = addTriggerField(functionFields);

        // when storeTuplesInStore is false then the given windowStoreFactory is only used to store triggers and
        // that store is passed to WindowStateUpdater to remove them after committing the batch.
        Stream stream = _topology.addSourcedNode(this,
                new ProcessorNode(_topology.getUniqueStreamId(),
                        _name,
                        fields,
                        fields,
                        new WindowTridentProcessor(windowConfig, _topology.getUniqueWindowId(), windowStoreFactory,
                                inputFields, aggregator, storeTuplesInStore)));

        Stream effectiveStream = stream.project(functionFields);

        // create StateUpdater with the given windowStoreFactory to remove triggered aggregation results form store
        // when they are successfully processed.
        StateFactory stateFactory = new WindowsStateFactory();
        StateUpdater stateUpdater = new WindowsStateUpdater(windowStoreFactory);
        stream.partitionPersist(stateFactory, new Fields(WindowTridentProcessor.TRIGGER_FIELD_NAME), stateUpdater, new Fields());

        return effectiveStream;
    }
