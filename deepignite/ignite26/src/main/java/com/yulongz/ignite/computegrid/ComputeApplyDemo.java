package com.yulongz.ignite.computegrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteClosure;
import org.apache.ignite.lang.IgniteFuture;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by hadoop on 17-11-21.
 */
public class ComputeApplyDemo {
    public static void main(String[] args) {
        try (Ignite ignite = Ignition.start("default-config.xml")) {
            System.out.println();
            System.out.println(">>> Compute closure example started.");

            // Execute closure on all cluster nodes.
            Collection<Integer> res = ignite.compute().apply(
                    new IgniteClosure<String, Integer>() {
                        @Override public Integer apply(String word) {
                            System.out.println();
                            System.out.println(">>> Printing '" + word + "' on this node from ignite job.");

                            // Return number of letters in the word.
                            return word.length();
                        }
                    },

                    // Job parameters. Ignite will create as many jobs as there are parameters.
                    Arrays.asList("Count characters using closure".split(" "))
            );

            int sum = 0;

            // Add up individual word lengths received from remote nodes
            for (int len : res) {
                sum += len;
            }

            System.out.println();
            System.out.println(">>> Total number of characters in the phrase is '" + sum + "'.");
            System.out.println(">>> Check all nodes for output (this node is also part of the cluster).");
        }
    }
}
