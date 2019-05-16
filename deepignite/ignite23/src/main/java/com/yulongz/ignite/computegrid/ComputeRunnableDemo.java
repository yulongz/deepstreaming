package com.yulongz.ignite.computegrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.compute.ComputeTaskFuture;
import org.apache.ignite.lang.IgniteRunnable;

/**
 * Created by hadoop on 17-11-21.
 */
public class ComputeRunnableDemo {
    public static void main(String[] args) {
        try(Ignite ignite = Ignition.start("default-config.xml")){
            IgniteCompute compute = ignite.compute();

            for (final String word : "Print words using runnable".split(" ")) {
                compute.run(new IgniteRunnable() {
                    @Override
                    public void run() {
                        System.out.println(">>> Printing '" + word + "' on this node from ignite job.");
                    }
                });
            }
        }
    }
}
