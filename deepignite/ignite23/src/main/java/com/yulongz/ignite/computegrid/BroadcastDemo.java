package com.yulongz.ignite.computegrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.compute.ComputeTaskFuture;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.lang.IgniteFuture;
import org.apache.ignite.lang.IgniteInClosure;
import org.apache.ignite.lang.IgniteRunnable;

import java.util.Collection;

/**
 * Created by hadoop on 17-11-13.
 */
public class BroadcastDemo {
    public static void main(String[] args) {
        Ignition.setClientMode(true);
        try (Ignite ignite = Ignition.start("default-config.xml")) {
            // Limit broadcast to remote nodes only and
            // enable asynchronous mode.
            IgniteCompute compute = ignite.compute(ignite.cluster().forRemotes()).withAsync();

            // Print out hello message on remote nodes in the cluster group.
            compute.broadcast(new IgniteRunnable() {
                    @Override public void run() {
                        // Print ID of remote node on remote node.
                        System.out.println(">>> Hello Node: " + ignite.cluster().localNode().id());
                    }
                }
            );

            ComputeTaskFuture<?> fut = compute.future();

            fut.listen(new IgniteInClosure<IgniteFuture<?>>() {
                @Override
                public void apply(IgniteFuture<?> fut) {
                    System.out.println("Finished sending broadcast job to cluster.");
                }
            });

            gatherSystemInfo(ignite);
        }
    }

    public static void gatherSystemInfo(Ignite ignite){
        Collection<String> res = ignite.compute().broadcast(new IgniteCallable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println();
                System.out.println("Executing task on node: " + ignite.cluster().localNode().id());

                return "Node ID: " + ignite.cluster().localNode().id() + "\n" +
                        "OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version") + " " +
                        System.getProperty("os.arch") + "\n" +
                        "User: " + System.getProperty("user.name") + "\n" +
                        "JRE: " + System.getProperty("java.runtime.name") + " " +
                        System.getProperty("java.runtime.version");
            }
        });

        // Print result.
        System.out.println();
        System.out.println("Nodes system information:");
        System.out.println();

        for (String r : res) {
            System.out.println(r);
            System.out.println();
        }
    }
}
