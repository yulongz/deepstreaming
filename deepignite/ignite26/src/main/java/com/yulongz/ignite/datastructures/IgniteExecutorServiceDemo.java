package com.yulongz.ignite.datastructures;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author hadoop
 * @date 17-12-6
 */
public class IgniteExecutorServiceDemo {
    public static void main(String[] args) throws InterruptedException {
        try (Ignite ignite = Ignition.start("default-config.xml")) {
            ExecutorService executorService = ignite.executorService();

            for(String word : "Print words using runnable".split(" ")){
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(word);
                    }
                });
            }

            executorService.shutdown();

            executorService.awaitTermination(0, TimeUnit.MILLISECONDS);
        }
    }
}