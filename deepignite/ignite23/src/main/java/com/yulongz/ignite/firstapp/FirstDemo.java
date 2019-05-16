package com.yulongz.ignite.firstapp;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteCallable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by hadoop on 17-10-18.
 */
public class FirstDemo {
    public static void main(String[] args) {
        try(Ignite ignite = Ignition.start("default-config.xml")) {
            Collection<IgniteCallable<Integer>> calls = new ArrayList<>();
            for (String word : "Count characters using callable".split(" ")){
                calls.add(new IgniteCallable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return word.length();
                    }
                });
            }

            Collection<Integer> res = ignite.compute().call(calls);

            int sum = 0;
            for (int len : res){
                sum += len;
            }
            System.out.println(sum);
        }
    }
}
