package com.yulongz.ignite.computegrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.lang.IgniteFuture;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by hadoop on 17-11-21.
 */
public class ComputeCallAsyncDemo {
    public static void main(String[] args) {
        try(Ignite ignite = Ignition.start("default-config.xml")){
            IgniteCompute compute = ignite.compute();

            Collection<IgniteCallable<Integer>> calls = new ArrayList<>();

            for (final String word : "Count characters using callable".split(" ")){
                calls.add(new IgniteCallable<Integer>() {
                    @Override
                    public Integer call() throws Exception {

                        return word.length();
                    }
                });
            }

            IgniteFuture<Collection<Integer>> collectionIgniteFuture = compute.callAsync(calls);

            int sum = 0;

            for (Integer len : collectionIgniteFuture.get()){
                System.out.println(sum += len);
            }
        }
    }
}
