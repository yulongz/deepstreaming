package com.yulongz.ignite.computegrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteClosure;
import org.apache.ignite.lang.IgniteReducer;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hadoop
 * @date 17-11-22
 */
public class ComputeReducerDemo {
    public static void main(String[] args) {
        try(Ignite ignite = Ignition.start("default-config.xml")){
            IgniteCompute compute = ignite.compute();

            Integer sum = compute.apply(new IgniteClosure<String, Integer>() {
                                              @Override
                                              public Integer apply(String s) {
                                                  return s.length();
                                              }
                                          },
                    Arrays.asList("Count characters using reducer".split(" ")),

                    new IgniteReducer<Integer, Integer>() {
                        private AtomicInteger sum = new AtomicInteger();

                        @Override
                        public boolean collect(@Nullable Integer integer) {
                            sum.addAndGet(integer);
                            return true;
                        }

                        @Override
                        public Integer reduce() {
                            return sum.get();
                        }
                    });

            System.out.println(">>> Total number of characters in the phrase is '" + sum + "'.");
        }
    }
}
