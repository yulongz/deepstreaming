flatMap function

> flatMap自定义函数Values中fileds个数要和stream的fields个数保持一致,否则java.lang.IndexOutOfBoundsException。减少field使用project

***

    /**
     * Returns a stream consisting of the results of replacing each value of this stream with the contents
     * produced by applying the provided mapping function to each value. This has the effect of applying
     * a one-to-many transformation to the values of the stream, and then flattening the resulting elements into a new stream.
     *
     * @param function a mapping function to be applied to each value in this stream which produces new values.
     * @return the new stream
     */
    public Stream flatMap(FlatMapFunction function) {
        projectionValidation(getOutputFields());
        return _topology.addSourcedNode(this,
                                        new ProcessorNode(
                                                _topology.getUniqueStreamId(),
                                                _name,
                                                getOutputFields(),
                                                getOutputFields(),
                                                new MapProcessor(getOutputFields(), new FlatMapFunctionExecutor(function))));
    }
