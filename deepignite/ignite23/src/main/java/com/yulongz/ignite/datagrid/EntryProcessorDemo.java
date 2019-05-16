package com.yulongz.ignite.datagrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;

import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;

/**
 * Created by hadoop on 17-10-18.
 * 当在缓存中执行puts和updates操作时，通常需要在网络中发送完整的状态数据，而EntryProcessor可以直接在主节点上处理数据，只需要传输增量数据而不是全量数据。
 * 可以在EntryProcessor中嵌入自定义逻辑，比如，获取之前缓存的数据然后加1.
 * EntryProcessor通过给键加锁以原子性方式执行。
 */
public class EntryProcessorDemo {
    public static void main(String[] args) {
        try(Ignite ignite = Ignition.start("default-config.xml")) {
            IgniteCache<String, Integer> cache = ignite.getOrCreateCache("mycache");
            for (int i = 0;i<10;i++){
                cache.invoke("myKey", new EntryProcessor<String, Integer, Void>() {
                    @Override
                    public Void process(MutableEntry<String, Integer> entry, Object... args) throws EntryProcessorException {
                        Integer val = entry.getValue();
                        entry.setValue(val == null ? 1 : val+1);
                        return null;
                    }
                });
            }
            System.out.println(cache.get("myKey"));
        }
    }
}
