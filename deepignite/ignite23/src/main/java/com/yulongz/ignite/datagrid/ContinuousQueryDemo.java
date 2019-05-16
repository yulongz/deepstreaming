package com.yulongz.ignite.datagrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.ContinuousQuery;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.lang.IgniteBiPredicate;

import javax.cache.Cache;
import javax.cache.configuration.Factory;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.CacheEntryListenerException;
import javax.cache.event.CacheEntryUpdatedListener;

/**
 * Created by hadoop on 17-10-19.
 */

public class ContinuousQueryDemo {
    private static final String CACHE_NAME = ContinuousQueryDemo.class.getSimpleName();

    public static void main(String[] args) {
        try(Ignite ignite = Ignition.start("default-config.xml")){

            try(IgniteCache cache = ignite.getOrCreateCache(CACHE_NAME)){
                int keyCnt = 20;

                // These entries will be queried by initial predicate.
                for (int i = 0; i < keyCnt; i++) {
                    cache.put(i, Integer.toString(i));
                }

                ContinuousQuery<Integer, String> query = new ContinuousQuery();

                query.setInitialQuery(new ScanQuery<Integer, String>(
                        new IgniteBiPredicate<Integer, String>() {
                            @Override
                            public boolean apply(Integer key, String val) {
                                return key > 10;
                            }
                        }
                ));

                /**
                 *
                 * 当缓存被修改时（一个条目被插入、更新或者删除），更新对应的事件就会发送给持续查询的本地监听器，之后应用就可以做出对应的反应。
                 当事件通过了远程过滤器，他们就会被发送给客户端，通知哪里的本地监听器。
                 * */
                query.setLocalListener(new CacheEntryUpdatedListener<Integer, String>() {
                    @Override
                    public void onUpdated(Iterable<CacheEntryEvent<? extends Integer, ? extends String>> iterable)
                            throws CacheEntryListenerException {
                        for (CacheEntryEvent<? extends Integer, ? extends String> e : iterable){
                            System.out.println("Updated entry [key=" + e.getKey() + ", val=" + e.getValue() + ']');
                        }
                    }
                });

                /**
                 *
                 * 这个过滤器在给定键对应的主和备节点上执行，然后评估更新是否需要作为一个事件传播给该查询的本地监听器。
                 如果过滤器返回true，那么本地监听器就会收到通知，否则事件会被忽略。产生更新的特定主和备节点，会在主/备节点以及应用端执行的本地监听器之间，减少不必要的网络流量。
                 * */
                query.setRemoteFilterFactory(new Factory<CacheEntryEventFilter<Integer, String>>() {
                    @Override public CacheEntryEventFilter<Integer, String> create() {
                        return new CacheEntryEventFilter<Integer, String>() {
                            @Override public boolean evaluate(CacheEntryEvent<? extends Integer, ? extends String> e) {
                                return e.getKey() > 10;
                            }
                        };
                    }
                });

                try(QueryCursor<Cache.Entry<Integer, String>> cursor = cache.query(query)){
                    for (Cache.Entry<Integer,String> e : cursor.getAll()){
                        System.out.println("Queried existing entry [key=" + e.getKey() + ", val=" + e.getValue() + ']');
                    }
                    // Add a few more keys and watch more query notifications.
                    for (int i = keyCnt; i < keyCnt + 10; i++) {
                        cache.put(i, Integer.toString(i));
                    }

                    // Wait for a while while callback is notified about remaining puts.
                    Thread.sleep(2000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            } finally {
                ignite.destroyCache(CACHE_NAME);
            }
        }
    }
}
