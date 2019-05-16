filter function
***
        /**
         * Returns a stream consisting of the elements of this stream that match the given filter.
         *
         * @param filter the filter to apply to each trident tuple to determine if it should be included.
         * @return the new stream
         */
        public Stream filter(Filter filter) {
            return each(getOutputFields(), filter);
        }
    
        /**
         * Returns a stream consisting of the elements of this stream that match the given filter.
         *
         * @param inputFields the fields of the input trident tuple to be selected.
         * @param filter      the filter to apply to each trident tuple to determine if it should be included.
         * @return the new stream
         */
        public Stream filter(Fields inputFields, Filter filter) {
            return each(inputFields, filter);
        }
