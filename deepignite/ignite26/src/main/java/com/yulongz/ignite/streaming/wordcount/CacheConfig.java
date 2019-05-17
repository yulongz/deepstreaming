package com.yulongz.ignite.streaming.wordcount;

import org.apache.ignite.cache.affinity.AffinityUuid;
import org.apache.ignite.configuration.CacheConfiguration;

import javax.cache.configuration.FactoryBuilder;
import javax.cache.expiry.CreatedExpiryPolicy;
import java.util.concurrent.TimeUnit;

/**
 * Created by root on 17-9-4.
 */
public class CacheConfig {
    public static CacheConfiguration<AffinityUuid, String> wordCache(){
//        CacheConfiguration<String, Long> cfg = new CacheConfiguration<>("word");

        CacheConfiguration<AffinityUuid, String> cfg = new CacheConfiguration<>("word");
        // Index the words and their counts,
        // so we can use them for fast SQL querying.
        cfg.setIndexedTypes(AffinityUuid.class, String.class);

        // Sliding window of 5 seconds.
        cfg.setExpiryPolicyFactory(FactoryBuilder.factoryOf(
                new CreatedExpiryPolicy(
                        new javax.cache.expiry.Duration(TimeUnit.SECONDS,5))));

        return cfg;
    }
}
