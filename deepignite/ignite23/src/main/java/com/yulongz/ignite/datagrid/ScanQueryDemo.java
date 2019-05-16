package com.yulongz.ignite.datagrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.lang.IgniteBiPredicate;
import org.apache.ignite.lang.IgniteClosure;

import javax.cache.Cache;
import java.util.List;

/**
 * Created by hadoop on 17-10-19.
 */
public class ScanQueryDemo {
    public static void main(String[] args) {
        try(Ignite ignite = Ignition.start("default-config.xml")){
            try{
                CacheConfiguration<String, Integer> cacheCfg = new CacheConfiguration<>();
                cacheCfg.setName("queryCache");
                IgniteCache cache = ignite.getOrCreateCache(cacheCfg);
                cache.put("Hello", 100);
                cache.put("World", 50);
                cache.put("Ignite", 20);
                cache.put("Java", 150);
                //scan query
//            IgniteBiPredicate<String, Integer> filter = new IgniteBiPredicate<String, Integer>() {
//                @Override
//                public boolean apply(String key, Integer val) {
//                    return val >= 100;
//                }
//            };
//
//            try(QueryCursor cursor = cache.query(new ScanQuery(filter))){
//                for (Object o : cursor){
//                    System.out.println(o);
//                }
//            }
                //return key
                List<String> keys = cache.query(new ScanQuery(new IgniteBiPredicate<String, Integer>() {
                            @Override
                            public boolean apply(String s, Integer i) {
                                return i >= 100;
                            }
                        }),
                        new IgniteClosure<Cache.Entry<String, Integer>, String>() {
                            @Override
                            public String apply(Cache.Entry<String, Integer> e) {
                                return e.getKey();
                            }
                        }).getAll();
                System.out.println(keys.get(0));
            }finally {
                ignite.destroyCache("queryCache");
            }
        }
    }
}
