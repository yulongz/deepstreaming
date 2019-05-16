package com.yulongz.ignite.servicegrid.mycountservice;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.services.*;

import javax.cache.CacheException;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.MutableEntry;

/**
 * @author hadoop
 * @date 17-11-28
 */
public class MyCounterServiceImpl implements Service, MyCounterService {
    /** Auto-injected instance of Ignite. */
    @IgniteInstanceResource
    private Ignite ignite;

    /** Distributed cache used to store counters. */
    private IgniteCache<String, Integer> cache;

    /** Service name. */
    private String svcName;

    @Override
    public void cancel(ServiceContext ctx) {
        cache.remove(svcName);

        System.out.println("Service was removed: " + svcName);
    }

    @Override
    public void init(ServiceContext ctx) throws Exception {
        cache = ignite.getOrCreateCache("myCounterCache");
        svcName = ctx.name();

        System.out.println("Service was initialized: " + svcName);
    }

    @Override
    public void execute(ServiceContext ctx) throws Exception {
        System.out.println("Executing distributed service:: " + svcName);
    }

    @Override
    public int increment() throws CacheException {
        return cache.invoke(svcName, new CounterEntryProcessor());
    }

    @Override
    public int get() throws CacheException {
        Integer i = cache.get(svcName);
        return i == null ? 0 : i;
    }

    private static class CounterEntryProcessor implements EntryProcessor<String, Integer, Integer> {
        @Override public Integer process(MutableEntry<String, Integer> e, Object... args) {
            int newVal = e.exists() ? e.getValue() + 1 : 1;
            // Update cache.
            e.setValue(newVal);
            return newVal;
        }
    }
}
