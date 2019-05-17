package com.yulongz.ignite.computegrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.lang.IgniteCallable;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author hadoop
 * @date 17-11-22
 */
public class ComputeNodeVarDemo {
    public static void main(String[] args) {
        Ignition.setClientMode(true);
        try (Ignite ignite = Ignition.start("default-config.xml")){


            IgniteCallable<Long> call = new IgniteCallable<Long>() {

                @Override
                public Long call() throws Exception {
                    ConcurrentMap<String, AtomicLong> nodeLocalMap = ignite.cluster().nodeLocalMap();
                    AtomicLong counter = nodeLocalMap.get("counter");

                    if (counter == null) {
                        AtomicLong old = nodeLocalMap.putIfAbsent("counter", counter = new AtomicLong());
                        if (old != null) {
                            counter = old;
                        }
                    }
                    System.out.println("------");
                    return counter.incrementAndGet();
                }
            };

            ClusterGroup cg = ignite.cluster().forLocal();
            IgniteCompute compute = ignite.compute(cg);

            Long res = compute.call(call);

            System.out.println(res);

            res = compute.call(call);

            System.out.println(res);


        }
    }
}
