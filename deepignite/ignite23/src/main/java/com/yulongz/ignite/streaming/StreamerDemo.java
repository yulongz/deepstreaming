package com.yulongz.ignite.streaming;

import com.yulongz.ignite.utils.ExamplesUtils;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

/**
 * @author hadoop
 * @date 17-12-4
 */
public class StreamerDemo {
    public static void main(String[] args) {
        Ignition.setClientMode(true);
        try (Ignite ignite = Ignition.start("default-config.xml")){
            try(IgniteCache<Integer, Integer> cache = ignite.getOrCreateCache("mystreamcache")) {
                try(IgniteDataStreamer<Integer, Integer> streamer = ignite.dataStreamer("mystreamcache")) {
                    for (int i=0;i<10;i++){
                        streamer.addData(i, i);
                    }

                    // 缓存里有数据
                    ExamplesUtils.cacheGet(ignite, "mystreamcache", 1);
                }
            }
        }
    }


}
