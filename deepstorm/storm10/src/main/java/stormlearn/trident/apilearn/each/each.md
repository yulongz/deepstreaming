each function
***
    @Override
    public Stream each(Fields inputFields, Function function, Fields functionFields) {
        projectionValidation(inputFields);
        return _topology.addSourcedNode(this,
                new ProcessorNode(_topology.getUniqueStreamId(),
                        _name,
                        TridentUtils.fieldsConcat(getOutputFields(), functionFields),
                        functionFields,
                        new EachProcessor(inputFields, function)));
    }
    
    public Stream each(Function function, Fields functionFields) {
        return each(null, function, functionFields);
    }
    
    public Stream each(Fields inputFields, Filter filter) {
        return each(inputFields, new FilterExecutor(filter), new Fields());
    }