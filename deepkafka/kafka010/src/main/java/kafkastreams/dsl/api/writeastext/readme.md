# name 
map

# return
KStream → void
KTable → void

# description

Terminal operation. Write the records to a file. See Javadocs for serde and toString() caveats. (KStream details, KTable details)

# code
## KStream


    /**
     * Write the records of this stream to a file at the given path.
     * This function will use the generated name of the parent processor node to label the key/value pairs printed to
     * the file.
     * <p>
     * The default serde will be used to deserialize the key or value in case the type is {@code byte[]} before calling
     * {@code toString()} on the deserialized object.
     * <p>
     * Implementors will need to override {@code toString()} for keys and values that are not of type {@link String},
     * {@link Integer} etc. to get meaningful information.
     *
     * @param filePath name of the file to write to
     */
    void writeAsText(final String filePath);

    /**
     * Write the records of this stream to a file at the given path.
     * This function will use the given name to label the key/value printed to the file.
     * <p>
     * The default serde will be used to deserialize the key or value in case the type is {@code byte[]} before calling
     * {@code toString()} on the deserialized object.
     * <p>
     * Implementors will need to override {@code toString()} for keys and values that are not of type {@link String},
     * {@link Integer} etc. to get meaningful information.
     *
     * @param filePath   name of the file to write to
     * @param streamName the name used to label the key/value pairs written to the file
     */
    void writeAsText(final String filePath,
                     final String streamName);

    /**
     * Write the records of this stream to a file at the given path.
     * This function will use the generated name of the parent processor node to label the key/value pairs printed to
     * the file.
     * <p>
     * The provided serde will be used to deserialize the key or value in case the type is {@code byte[]} before calling
     * {@code toString()} on the deserialized object.
     * <p>
     * Implementors will need to override {@code toString()} for keys and values that are not of type {@link String},
     * {@link Integer} etc. to get meaningful information.
     *
     * @param filePath name of the file to write to
     * @param keySerde key serde used to deserialize key if type is {@code byte[]},
     * @param valSerde value serde used to deserialize value if type is {@code byte[]},
     */
    void writeAsText(final String filePath,
                     final Serde<K> keySerde,
                     final Serde<V> valSerde);

    /**
     * Write the records of this stream to a file at the given path.
     * This function will use the given name to label the key/value printed to the file.
     * <p>
     * The provided serde will be used to deserialize the key or value in case the type is {@code byte[]}
     * before calling {@code toString()} on the deserialized object.
     * <p>
     * Implementors will need to override {@code toString()} for keys and values that are not of type {@link String},
     * {@link Integer} etc. to get meaningful information.
     *
     * @param filePath   name of the file to write to
     * @param streamName the name used to label the key/value pairs written to the file
     * @param keySerde   key serde used to deserialize key if type is {@code byte[]},
     * @param valSerde   value serde used deserialize value if type is {@code byte[]},
     */
    void writeAsText(final String filePath,
                     final String streamName,
                     final Serde<K> keySerde,
                     final Serde<V> valSerde);

## KTable


    /**
     * Write the update records of this {@code KTable} to a file at the given path.
     * This function will use the generated name of the parent processor node to label the key/value pairs printed to
     * the file.
     * <p>
     * The default serde will be used to deserialize the key or value in case the type is {@code byte[]} before calling
     * {@code toString()} on the deserialized object.
     * <p>
     * Implementors will need to override {@code toString()} for keys and values that are not of type {@link String},
     * {@link Integer} etc. to get meaningful information.
     * <p>
     * Note that {@code writeAsText()} is not applied to the internal state store and only called for each new
     * {@code KTable} update record.
     *
     * @param filePath name of file to write to
     */
    void writeAsText(final String filePath);

    /**
     * Write the update records of this {@code KTable} to a file at the given path.
     * This function will use the given name to label the key/value printed to the file.
     * <p>
     * The default serde will be used to deserialize the key or value in case the type is {@code byte[]} before calling
     * {@code toString()} on the deserialized object.
     * <p>
     * Implementors will need to override {@code toString()} for keys and values that are not of type {@link String},
     * {@link Integer} etc. to get meaningful information.
     * <p>
     * Note that {@code writeAsText()} is not applied to the internal state store and only called for each new
     * {@code KTable} update record.
     *
     * @param filePath   name of file to write to
     * @param streamName the name used to label the key/value pairs printed out to the console
     */
    void writeAsText(final String filePath,
                     final String streamName);

    /**
     * Write the update records of this {@code KTable} to a file at the given path.
     * This function will use the generated name of the parent processor node to label the key/value pairs printed to
     * the file.
     * <p>
     * The provided serde will be used to deserialize the key or value in case the type is {@code byte[]} before calling
     * {@code toString()} on the deserialized object.
     * <p>
     * Implementors will need to override {@code toString()} for keys and values that are not of type {@link String},
     * {@link Integer} etc. to get meaningful information.
     * <p>
     * Note that {@code writeAsText()} is not applied to the internal state store and only called for each new
     * {@code KTable} update record.
     *
     * @param filePath name of file to write to
     * @param keySerde key serde used to deserialize key if type is {@code byte[]},
     * @param valSerde value serde used to deserialize value if type is {@code byte[]},
     */
    void  writeAsText(final String filePath,
                      final Serde<K> keySerde,
                      final Serde<V> valSerde);

    /**
     * Write the update records of this {@code KTable} to a file at the given path.
     * This function will use the given name to label the key/value printed to the file.
     * <p>
     * The default serde will be used to deserialize the key or value in case the type is {@code byte[]} before calling
     * {@code toString()} on the deserialized object.
     * <p>
     * Implementors will need to override {@code toString()} for keys and values that are not of type {@link String},
     * {@link Integer} etc. to get meaningful information.
     * <p>
     * Note that {@code writeAsText()} is not applied to the internal state store and only called for each new
     * {@code KTable} update record.
     *
     * @param filePath name of file to write to
     * @param streamName the name used to label the key/value pairs printed to the console
     * @param keySerde key serde used to deserialize key if type is {@code byte[]},
     * @param valSerde value serde used to deserialize value if type is {@code byte[]},
     */
    void writeAsText(final String filePath,
                     final String streamName,
                     final Serde<K> keySerde,
                     final Serde<V> valSerde);

    