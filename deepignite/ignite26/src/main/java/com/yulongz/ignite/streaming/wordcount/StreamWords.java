package com.yulongz.ignite.streaming.wordcount;

import com.yulongz.ignite.utils.ExamplesUtils;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.affinity.AffinityUuid;

import java.io.*;

/**
 * Created by root on 17-9-4.
 */
public class StreamWords {
    public static void main(String[] args) throws Exception {
        Ignition.setClientMode(true);
        try (Ignite ignite = Ignition.start("default-config.xml")) {
            if (!ExamplesUtils.hasServerNodes(ignite))
                return;

            // The cache is configured with sliding window holding 1 second of the streaming data.
            IgniteCache<AffinityUuid, String> stmCache = ignite.getOrCreateCache(CacheConfig.wordCache());

            try (IgniteDataStreamer<AffinityUuid, String> stmr = ignite.dataStreamer(stmCache.getName())) {
                // Stream words from "alice-in-wonderland" book.
                while (true) {
                    // 数据文件和.class文件在同一目录
                    InputStream in = StreamWords.class.getResourceAsStream("alice-in-wonderland.txt");

                    try (LineNumberReader rdr = new LineNumberReader(new InputStreamReader(in))) {
                        for (String line = rdr.readLine(); line != null; line = rdr.readLine()) {
                            for (String word : line.split(" ")) {
                                if (!word.isEmpty()){
                                    // Stream words into Ignite.
                                    // By using AffinityUuid we ensure that identical
                                    // words are processed on the same cluster node.
                                    stmr.addData(new AffinityUuid(word), word);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
