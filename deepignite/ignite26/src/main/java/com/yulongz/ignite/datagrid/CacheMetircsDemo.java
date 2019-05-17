package com.yulongz.ignite.datagrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMetrics;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

/**
 * Created by hadoop on 17-11-2.
 */
public class CacheMetircsDemo {
    private static String CACHE_NAME = CacheMetircsDemo.class.getSimpleName();
    public static void main(String[] args) {
        CacheConfiguration<String, String> cCfg = new CacheConfiguration<>(CACHE_NAME);
        cCfg.setStatisticsEnabled(true);

        IgniteConfiguration iCfg = new IgniteConfiguration();
        iCfg.setCacheConfiguration(cCfg);
        try(Ignite ignite = Ignition.start(iCfg)){
            try {
                IgniteCache<String, String> cache = ignite.getOrCreateCache(CACHE_NAME);

                cache.put("Hello", "World");
                cache.put("Apache", "Ignite");
                cache.put("Other", "Scala");
                cache.put("Alibaba", "Aliyun");

                System.out.println(cache.get("Hello"));

                CacheMetrics cm = cache.metrics();
                System.out.println("cache eviction: " + cm.getCacheEvictions());
                System.out.println("cache gets: " + cm.getCacheGets());
                System.out.println("cache average get time: " + cm.getAverageGetTime());
                System.out.println("cache puts: " + cm.getCachePuts());
                System.out.println("cache average put time: " + cm.getAveragePutTime());
                System.out.println("cache key type: " + cm.getKeyType());
            } finally {
                ignite.destroyCache(CACHE_NAME);
            }
        }
    }
}
