# name 
print

# return
KStream → void
KTable → void

# description

Terminal operation. Prints the records to System.out. See Javadocs for serde and toString() caveats. (KStream details, KTable details)

# code
## KStream


    /**
     * Print the records of this stream to {@code System.out}.
     * This function will use the generated name of the parent processor node to label the key/value pairs printed to
     * the console.
     * <p>
     * The default serde will be used to deserialize the key or value in case the type is {@code byte[]} before calling
     * {@code toString()} on the deserialized object.
     * <p>
     * Implementors will need to override {@code toString()} for keys and values that are not of type {@link String},
     * {@link Integer} etc. to get meaningful information.
     */
    void print();

    /**
     * Print the records of this stream to {@code System.out}.
     * This function will use the given name to label the key/value pairs printed to the console.
     * <p>
     * The default serde will be used to deserialize the key or value in case the type is {@code byte[]} before calling
     * {@code toString()} on the deserialized object.
     * <p>
     * Implementors will need to override {@code toString()} for keys and values that are not of type {@link String},
     * {@link Integer} etc. to get meaningful information.
     *
     * @param streamName the name used to label the key/value pairs printed to the console
     */
    void print(final String streamName);

    /**
     * Print the records of this stream to {@code System.out}.
     * This function will use the generated name of the parent processor node to label the key/value pairs printed to
     * the console.
     * <p>
     * The provided serde will be used to deserialize the key or value in case the type is {@code byte[]} before calling
     * {@code toString()} on the deserialized object.
     * <p>
     * Implementors will need to override {@code toString()} for keys and values that are not of type {@link String},
     * {@link Integer} etc. to get meaningful information.
     *
     * @param keySerde key serde used to deserialize key if type is {@code byte[]},
     * @param valSerde value serde used to deserialize value if type is {@code byte[]},
     */
    void print(final Serde<K> keySerde,
               final Serde<V> valSerde);

    /**
     * Print the records of this stream to {@code System.out}.
     * <p>
     * The provided serde will be used to deserialize the key or value in case the type is {@code byte[]} before calling
     * {@code toString()} on the deserialized object.
     * <p>
     * Implementors will need to override {@code toString()} for keys and values that are not of type {@link String},
     * {@link Integer} etc. to get meaningful information.
     *
     * @param keySerde   key serde used to deserialize key if type is {@code byte[]},
     * @param valSerde   value serde used to deserialize value if type is {@code byte[]},
     * @param streamName the name used to label the key/value pairs printed to the console
     */
    void print(final Serde<K> keySerde,
               final Serde<V> valSerde,
               final String streamName);

## KTable

    /**
     * Print the update records of this {@code KTable} to {@code System.out}.
     * This function will use the generated name of the parent processor node to label the key/value pairs printed to
     * the console.
     * <p>
     * The provided serde will be used to deserialize the key or value in case the type is {@code byte[]} before calling
     * {@code toString()} on the deserialized object.
     * <p>
     * Implementors will need to override {@code toString()} for keys and values that are not of type {@link String},
     * {@link Integer} etc. to get meaningful information.
     * <p>
     * Note that {@code print()} is not applied to the internal state store and only called for each new {@code KTable}
     * update record.
     */
    void print();

    /**
     * Print the update records of this {@code KTable} to {@code System.out}.
     * This function will use the given name to label the key/value pairs printed to the console.
     * <p>
     * The provided serde will be used to deserialize the key or value in case the type is {@code byte[]} before calling
     * {@code toString()} on the deserialized object.
     * <p>
     * Implementors will need to override {@code toString()} for keys and values that are not of type {@link String},
     * {@link Integer} etc. to get meaningful information.
     * <p>
     * Note that {@code print()} is not applied to the internal state store and only called for each new {@code KTable}
     * update record.
     *
     * @param streamName the name used to label the key/value pairs printed to the console
     */
    void print(final String streamName);

    /**
     * Print the update records of this {@code KTable} to {@code System.out}.
     * This function will use the generated name of the parent processor node to label the key/value pairs printed to
     * the console.
     * <p>
     * The provided serde will be used to deserialize the key or value in case the type is {@code byte[]} before calling
     * {@code toString()} on the deserialized object.
     * <p>
     * Implementors will need to override {@code toString()} for keys and values that are not of type {@link String},
     * {@link Integer} etc. to get meaningful information.
     * <p>
     * Note that {@code print()} is not applied to the internal state store and only called for each new {@code KTable}
     * update record.
     *
     * @param keySerde key serde used to deserialize key if type is {@code byte[]},
     * @param valSerde value serde used to deserialize value if type is {@code byte[]},
     */
    void print(final Serde<K> keySerde,
               final Serde<V> valSerde);

    /**
     * Print the update records of this {@code KTable} to {@code System.out}.
     * This function will use the given name to label the key/value pairs printed to the console.
     * <p>
     * The provided serde will be used to deserialize the key or value in case the type is {@code byte[]} before calling
     * {@code toString()} on the deserialized object.
     * <p>
     * Implementors will need to override {@code toString()} for keys and values that are not of type {@link String},
     * {@link Integer} etc. to get meaningful information.
     * <p>
     * Note that {@code print()} is not applied to the internal state store and only called for each new {@code KTable}
     * update record.
     *
     * @param keySerde   key serde used to deserialize key if type is {@code byte[]},
     * @param valSerde   value serde used to deserialize value if type is {@code byte[]},
     * @param streamName the name used to label the key/value pairs printed to the console
     */
    void print(final Serde<K> keySerde,
               final Serde<V> valSerde,
               final String streamName);
