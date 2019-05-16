project function
***

    /**
     * Filters out fields from a stream, resulting in a Stream containing only the fields specified by `keepFields`.
     *
     * For example, if you had a Stream `mystream` containing the fields `["a", "b", "c","d"]`, calling"
     *
     * ```java
     * mystream.project(new Fields("b", "d"))
     * ```
     *
     * would produce a stream containing only the fields `["b", "d"]`.
     *
     *
     * @param keepFields The fields in the Stream to keep
     * @return
     */
    public Stream project(Fields keepFields) {
        projectionValidation(keepFields);
        return _topology.addSourcedNode(this, new ProcessorNode(_topology.getUniqueStreamId(), _name, keepFields, new Fields(), new ProjectedProcessor(keepFields)));
    }
