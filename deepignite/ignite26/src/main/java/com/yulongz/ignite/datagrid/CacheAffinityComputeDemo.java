package com.yulongz.ignite.datagrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.lang.IgniteRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created by hadoop on 17-10-20.
 *
 * @author hadoop
 * @date 2017/10/20
 */
public class CacheAffinityComputeDemo {
    private static final String CACHE_NAME = CacheAffinityComputeDemo.class.getSimpleName();
    private static final int KEY_CNT = 10;

    public static void main(String[] args) {
        try(Ignite ignite = Ignition.start("default-config.xml")){
            try(IgniteCache<Integer, String> cache = ignite.getOrCreateCache(CACHE_NAME)){
                for (int i = 0;i<KEY_CNT;i++){
                    cache.put(i, Integer.toString(i));
                }

                // Co-locates jobs with data using IgniteCompute.affinityRun(...) method.
                visitUsingAffinityRun();

                // Co-locates jobs with data using IgniteCluster.mapKeysToNodes(...) method.
                visitUsingMapKeysToNodes();
            }finally {
                ignite.destroyCache(CACHE_NAME);
            }
        }

    }

    private static void visitUsingAffinityRun(){
        Ignite ignite = Ignition.ignite();

        IgniteCache<Integer, String> cache = ignite.cache(CACHE_NAME);

        for (int i = 0; i<KEY_CNT; i++) {
            int key = i;

            ignite.compute().affinityRun(CACHE_NAME, key, new IgniteRunnable() {
                @Override
                public void run() {
                    // Peek is a local memory lookup, however, value should never be 'null'
                    // as we are co-located with node that has a given key.
                    System.out.println("Co-located using affinityRun [key= " + key + ", value=" + cache.localPeek(key) + ']');
                }
            });
        }
    }

    private static void visitUsingMapKeysToNodes(){
        Ignite ignite = Ignition.ignite();

        Collection<Integer> keys = new ArrayList<>(KEY_CNT);

        for (int i = 0; i<KEY_CNT; i++){
            keys.add(i);
        }

        Map<ClusterNode, Collection<Integer>>  mappings = ignite.<Integer>affinity(CACHE_NAME).mapKeysToNodes(keys);

        for (Map.Entry<ClusterNode, Collection<Integer>> map : mappings.entrySet()){
            ClusterNode node = map.getKey();
            Collection<Integer> mappedKeys = map.getValue();

            if (node != null){
                ignite.compute(ignite.cluster().forNode(node)).run(new IgniteRunnable() {
                    @Override
                    public void run() {
                        IgniteCache<Integer, String> cache = ignite.cache(CACHE_NAME);
                        // Peek is a local memory lookup, however, value should never be 'null'
                        // as we are co-located with node that has a given key.
                        for (Integer key : mappedKeys){
                            System.out.println("Co-located using mapKeysToNodes [key= " + key +
                                    ", value=" + cache.localPeek(key) + ']');
                        }
                    }
                });
            }
        }
    }
}
