package com.yulongz.ignite.computegrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteRunnable;

import java.util.concurrent.ExecutorService;

/**
 * @author hadoop
 * @date 17-11-22
 */
public class ExecutorServiceDemo {
    public static void main(String[] args) {
        try(Ignite ignite = Ignition.start("default-config.xml")) {
            ExecutorService exec = ignite.executorService();

            for (String word : "Print words using runnable".split(" ")){
                exec.submit(new IgniteRunnable() {
                    @Override
                    public void run() {
                        System.out.println(word.length());
                    }
                });
            }
        }
    }
}
