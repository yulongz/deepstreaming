map function

> map自定义函数Values中fileds个数要和stream的fields个数保持一致,否则java.lang.IndexOutOfBoundsException。减少field使用project

***

    /**
     * Returns a stream consisting of the result of applying the given mapping function to the values of this stream.
     *
     * @param function a mapping function to be applied to each value in this stream.
     * @return the new stream
     */
    public Stream map(MapFunction function) {
        projectionValidation(getOutputFields());
        return _topology.addSourcedNode(this,
                                        new ProcessorNode(
                                                _topology.getUniqueStreamId(),
                                                _name,
                                                getOutputFields(),
                                                getOutputFields(),
                                                new MapProcessor(getOutputFields(), new MapFunctionExecutor(function))));
    }