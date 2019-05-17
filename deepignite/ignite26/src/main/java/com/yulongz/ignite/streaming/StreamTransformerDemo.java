package com.yulongz.ignite.streaming;

import com.yulongz.ignite.utils.ExamplesUtils;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.FieldsQueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.stream.StreamTransformer;

import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;
import java.util.List;
import java.util.Random;

/**
 * @author hadoop
 * @date 17-12-4
 */
public class StreamTransformerDemo {
    public static void main(String[] args) {
        Random random = new Random();
        try (Ignite ignite = Ignition.start("default-config.xml")){
            CacheConfiguration<Integer, Long> cacheCfg = new CacheConfiguration("randomNumbers");
            cacheCfg.setIndexedTypes(Integer.class, Long.class);

            try (IgniteCache cache = ignite.getOrCreateCache(cacheCfg)){
                try (IgniteDataStreamer<Integer, Long> streamer = ignite.dataStreamer("randomNumbers")){
                    streamer.allowOverwrite(true);

                    streamer.receiver(new StreamTransformer<Integer, Long>() {
                        @Override
                        public Object process(MutableEntry<Integer, Long> entry, Object... objects) throws EntryProcessorException {
                            Long value = entry.getValue();
                            entry.setValue(value == null ? 1L : value+1);

                            return null;
                        }
                    });

                    for (int i=0;i<10000;i++) {
                        streamer.addData(random.nextInt(100), 1L);
                        if (i % 5000 == 0) {
                            System.out.println("Number of tuples streamed into Ignite: " + i);
                        }
                    }

                    SqlFieldsQuery top10 = new SqlFieldsQuery("select _key, _val from Long order by _val desc limit 10");
                    List all = cache.query(top10).getAll();
                    System.out.println("Top 10 most popular numbers:");

                    // Print top 10 words.
                    ExamplesUtils.printQueryResults(all);
                }
            }finally {
                ignite.destroyCache("randomNumbers");
            }
        }
    }
}
