- peek

***

    /**
     * Returns a stream consisting of the trident tuples of this stream, additionally performing the provided action on
     * each trident tuple as they are consumed from the resulting stream. This is mostly useful for debugging
     * to see the tuples as they flow past a certain point in a pipeline.
     *
     * @param action the action to perform on the trident tuple as they are consumed from the stream
     * @return the new stream
     */
    public Stream peek(Consumer action) {
        projectionValidation(getOutputFields());
        return _topology.addSourcedNode(this,
                                        new ProcessorNode(
                                                _topology.getUniqueStreamId(),
                                                _name,
                                                getOutputFields(),
                                                getOutputFields(),
                                                new MapProcessor(getOutputFields(), new ConsumerExecutor(action))));
    }