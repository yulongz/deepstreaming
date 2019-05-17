package com.yulongz.ignite.computegrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteFuture;
import org.apache.ignite.lang.IgniteRunnable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by hadoop on 17-11-21.
 */
public class ComputeRunAsyncDemo {
    public static void main(String[] args) {
        try(Ignite ignite = Ignition.start("default-config.xml")){
            IgniteCompute compute = ignite.compute();
            Collection<IgniteFuture<?>> futs = new ArrayList<>();

            for (final String word : "Print words using runnable".split(" ")) {
                IgniteFuture<Void> fut = compute.runAsync(new IgniteRunnable() {
                    @Override
                    public void run() {
                        System.out.println(">>> Printing '" + word + "' on this node from ignite job.");
                    }
                });

                futs.add(fut);
            }
            for (IgniteFuture future : futs) {
                future.get();
            }
        }
    }
}
