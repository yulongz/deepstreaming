package com.yulongz.ignite.datagrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteFuture;

/**
 * Created by hadoop on 17-10-18.
 */
public class AsyncDemo {
    public static void main(String[] args) {
        try(Ignite ignite = Ignition.start("default-config.xml")){
            IgniteCache asyncCache = ignite.getOrCreateCache("mycache").withAsync();
            asyncCache.getAndPut("1", 1);
            asyncCache.put("1", 1);
            IgniteFuture<Integer> fut = asyncCache.future();
            fut.listen(f -> System.out.println(f.get()));
        }
    }
}
