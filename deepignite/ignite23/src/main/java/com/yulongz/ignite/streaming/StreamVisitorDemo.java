package com.yulongz.ignite.streaming;

import com.yulongz.ignite.utils.ExamplesUtils;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.stream.StreamVisitor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author hadoop
 * @date 17-12-4
 */
public class StreamVisitorDemo {
    /** Random number generator. */
    private static final Random RAND = new Random();

    /** The list of instruments. */
    private static final String[] INSTRUMENTS = {"IBM", "GOOG", "MSFT", "GE", "EBAY", "YHOO", "ORCL", "CSCO", "AMZN", "RHT"};

    /** The list of initial instrument prices. */
    private static final double[] INITIAL_PRICES = {194.9, 893.49, 34.21, 23.24, 57.93, 45.03, 44.41, 28.44, 378.49, 69.50};

    public static void main(String[] args) {
        try(Ignite ignite = Ignition.start("default-config.xml")) {
            CacheConfiguration marketCfg = new CacheConfiguration("marketTicks");
            CacheConfiguration<String, Instrument> instCfg = new CacheConfiguration("instCache");
            instCfg.setIndexedTypes(String.class, Instrument.class);

            try(IgniteCache<String, Double> marketCache = ignite.getOrCreateCache(marketCfg);
                IgniteCache<String, Instrument> instCache = ignite.getOrCreateCache(instCfg)){

                try(IgniteDataStreamer<String, Double> streamer = ignite.dataStreamer("marketTicks")){
                    streamer.receiver(new StreamVisitor<String, Double>() {
                        @Override
                        public void apply(IgniteCache<String, Double> cache, Map.Entry<String, Double> entry) {
                            String symbol = entry.getKey();
                            Double price = entry.getValue();

                            Instrument instrument = instCache.get(symbol);

                            if (instrument == null){
                                instrument = new Instrument(symbol);
                            }

                            Double open = instrument.getOpen();
                            if (open == null || open == 0){
                                instrument.setOpen(price);
                            }

                            instrument.setLatest(price);
                            instCache.put(symbol,instrument);
                        }
                    });
                    for (int i = 1; i <= 10000; i++) {
                        int idx = RAND.nextInt(INSTRUMENTS.length);

                        // Use gaussian distribution to ensure that
                        // numbers closer to 0 have higher probability.
                        double price = round2(INITIAL_PRICES[idx] + RAND.nextGaussian());

                        // 缓存里没有数据
                        streamer.addData(INSTRUMENTS[idx], price);

                        if (i % 5000 == 0) {
                            System.out.println("Number of tuples streamed into Ignite: " + i);
                        }
                    }

                    // Select top 3 best performing instruments.
                    SqlFieldsQuery top3qry = new SqlFieldsQuery(
                            "select symbol, (latest - open) from Instrument order by (latest - open) desc limit 3");

                    // Execute queries.
                    List<List<?>> top3 = instCache.query(top3qry).getAll();

                    System.out.println("Top performing financial instruments: ");

                    // Print top 10 words.
                    ExamplesUtils.printQueryResults(top3);
                }

            }finally {
                ignite.destroyCache("marketTicks");
                ignite.destroyCache("instCache");
            }
        }
    }

    public static double round2(double val) {
        return Math.floor(100 * val + 0.5) / 100;
    }

    public static class Instrument implements Serializable {
        /** Instrument symbol. */
        @QuerySqlField(index = true)
        private final String symbol;

        /** Open price. */
        @QuerySqlField(index = true)
        private double open;

        /** Close price. */

        @QuerySqlField(index = true)
        private double latest;

        public String getSymbol() {
            return symbol;
        }

        public double getOpen() {
            return open;
        }

        public void setOpen(double open) {
            this.open = open;
        }

        public double getLatest() {
            return latest;
        }

        public void setLatest(double latest) {
            this.latest = latest;
        }

        /**
         * @param symbol Symbol.
         */
        public Instrument(String symbol) {
            this.symbol = symbol;
        }

        /**
         * Updates this instrument based on the latest market tick price.
         *
         * @param price Latest price.
         */
        public void update(double price) {
            if (open == 0) {
                open = price;
            }

            this.latest = price;
        }
    }
}
