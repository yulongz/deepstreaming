package com.yulongz.ignite.computegrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.lang.IgniteClosure;
import org.apache.ignite.lang.IgniteFuture;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by hadoop on 17-11-21.
 */
public class ComputeCallableDemo {
    public static void main(String[] args) {
        try(Ignite ignite = Ignition.start("default-config.xml")){
            Collection<IgniteCallable<Integer>> calls = new ArrayList<>();

            // Iterate through all words in the sentence and create callable jobs.
            for (final String word : "Count characters using callable".split(" ")) {
                calls.add(new IgniteCallable<Integer>() {
                    @Override public Integer call() throws Exception {
                        System.out.println();
                        System.out.println(">>> Printing '" + word + "' on this node from ignite job.");

                        return word.length();
                    }
                });
            }

            // Execute collection of callables on the ignite.
            Collection<Integer> call = ignite.compute().call(calls);

            int sum = 0;

            // Add up individual word lengths received from remote nodes.
            for (int len : call)
                sum += len;

            System.out.println();
            System.out.println(">>> Total number of characters in the phrase is '" + sum + "'.");
            System.out.println(">>> Check all nodes for output (this node is also part of the cluster).");
        }
    }
}
