package com.yulongz.ignite.datagrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheRebalanceMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.cache.eviction.fifo.FifoEvictionPolicy;
import org.apache.ignite.cache.eviction.lru.LruEvictionPolicy;
import org.apache.ignite.cache.eviction.sorted.SortedEvictionPolicy;
import org.apache.ignite.configuration.*;
import org.apache.ignite.transactions.Transaction;

import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.util.concurrent.locks.Lock;

/**
 * Created by hadoop on 17-10-18.
 */
public class DataGridDemo {
    public static void main(String[] args) {
        IgniteConfiguration cfg = new IgniteConfiguration();

        MemoryPolicyConfiguration mpCfg = new MemoryPolicyConfiguration();
        mpCfg.setName("memPolicy");
        mpCfg.setInitialSize(100L * 1024 * 1024);
        mpCfg.setMaxSize(500L * 1024 * 1024);
        // Setting data pages eviction algorithm.
        mpCfg.setPageEvictionMode(DataPageEvictionMode.RANDOM_2_LRU);
        // Memory metrics
        mpCfg.setMetricsEnabled(true);

        MemoryConfiguration memCfg = new MemoryConfiguration();
        memCfg.setDefaultMemoryPolicySize(4L * 1024 * 1024 * 1024);
        memCfg.setConcurrencyLevel(4);
        memCfg.setPageSize(4096);
        // Applying the memory policy
        memCfg.setMemoryPolicies(mpCfg);
        memCfg.setDefaultMemoryPolicyName("memPolicy");

        PersistentStoreConfiguration psCfg = new PersistentStoreConfiguration();
        psCfg.setPersistentStorePath("");
        psCfg.setWalHistorySize(10);
        psCfg.setWalSegmentSize(5);
        // Write Ahead Log
        psCfg.setWalMode(WALMode.LOG_ONLY);
        //checkpointing default 3min
        psCfg.setCheckpointingFrequency(180000L);
        cfg.setPersistentStoreConfiguration(psCfg);

        CacheConfiguration cacheCfg = new CacheConfiguration();
        cacheCfg.setName("Mycache");
        cacheCfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
        cacheCfg.setCacheMode(CacheMode.LOCAL);
        cacheCfg.setBackups(1);
        // metrics
        cacheCfg.setStatisticsEnabled(true);
        cacheCfg.setRebalanceMode(CacheRebalanceMode.ASYNC);
        cacheCfg.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
        // thread nums
        cacheCfg.setQueryParallelism(3);
        // on heap cache
        cacheCfg.setOnheapCacheEnabled(true);

        // eviction policy
        // Set the maximum cache size to 1 million (default is 100,000). lru
        cacheCfg.setEvictionPolicy(new LruEvictionPolicy(1000000));
        // Set the maximum cache size to 1 million (default is 100,000). FIFO
        cacheCfg.setEvictionPolicy(new FifoEvictionPolicy(1000000));
        // Set the maximum cache size to 1 million (default is 100,000). sort
        cacheCfg.setEvictionPolicy(new SortedEvictionPolicy(1000000));
        // Set the maximum cache size to 1 million (default is 100,000). random
//        cacheCfg.setEvictionPolicy(new RandomEvictionPolicy(100,000));

        // expiry policy
        cacheCfg.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ZERO));
        cacheCfg.setEagerTtl(true);

        // Setting a memory policy name to bind to a specific memory region.
        cacheCfg.setMemoryPolicyName("memPolicy");

        cfg.setPeerClassLoadingEnabled(true);
        cfg.setRebalanceThreadPoolSize(2);
        // Applying the cache configuration.
        cfg.setCacheConfiguration(cacheCfg);
        // Applying the new page memory configuration.
        cfg.setMemoryConfiguration(memCfg);

        //near cache
        NearCacheConfiguration ncfg = new NearCacheConfiguration();
        ncfg.setNearEvictionPolicy(new LruEvictionPolicy(10000));// huishoucelve
        ncfg.setNearStartSize(10000);//chushi daxiao


//        try(Ignite ignite = Ignition.start("/usr/local/ignite/examples/config/example-cache.xml")) {
        try(Ignite ignite = Ignition.start(cfg)) {
//            IgniteCache<Integer,String> cache = ignite.getOrCreateCache("Mycache");
            //Get Put
//            for (int i = 0;i<10;i++) {
//                cache.put(i,Integer.toString(i));
//            }
//            for (int i = 0;i<10;i++) {
//                System.out.println("key = " + i + " value = " + cache.get(i));
//            }


            IgniteCache<String, Integer> cache = ignite.getOrCreateCache(cacheCfg, ncfg);

            //getAndPutIfAbsent which returns previous value.
            Integer val = cache.getAndPutIfAbsent("Hello", 11); // null
            //putIfAbsent which returns boolean success flag.
            System.out.println(cache.putIfAbsent("World", 22)); // true
            //getAndReplace returns previous value.
            val = cache.getAndReplace("Hello",22); // 11
            cache.replace("World", 22, 2);
//            System.out.println("Hello Replaced: " + cache.get("Hello")); // 22
//            System.out.println("Hello Removed: " + cache.remove("Hello", 1));
//            System.out.println("World Replaced: " + cache.get("World"));
//            System.out.println("World Removed: " + cache.remove("World", 2));
//            System.out.println(cache.get("World"));

            //transaction
            try(Transaction tx = ignite.transactions().txStart()){
                System.out.println(cache.get("Hello"));
                Integer hello = cache.get("Hello");
                if (hello == 1)
                    cache.put("Hello", 11);
                cache.put("World", 24);

                tx.commit();
                System.out.println(cache.get("World"));
            }

            //lock cache key Hello
            Lock lock = cache.lock("Hello");
            lock.lock();
            try {
                cache.put("Hello", 11);
                cache.put("World", 22);
            }
            finally {
                lock.unlock();
            }
            System.out.println("----" + cache.get("Hello"));
            System.out.println(cache.get("World"));
        }
    }
}
