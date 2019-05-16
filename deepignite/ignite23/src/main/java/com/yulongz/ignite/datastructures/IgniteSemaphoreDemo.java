package com.yulongz.ignite.datastructures;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteSemaphore;
import org.apache.ignite.Ignition;

/**
 * @author hadoop
 * @date 17-12-7
 */
public class IgniteSemaphoreDemo {
    public static void main(String[] args) throws InterruptedException {
        try (Ignite ignite = Ignition.start("default-config.xml")) {
            int poolSize = ignite.configuration().getIgfsThreadPoolSize();

            String name = "semaphore";
            IgniteSemaphore semaphore = ignite.semaphore(name, 0, true, true);

            for (int i=0;i<10;i++) {
                semaphore.release();
            }
            System.out.println(semaphore.availablePermits());

//            for (int i=0;i<10;i++){
//                semaphore.acquire();
//                System.out.println("Access: " + i);
//                System.out.println("Avalable: " + semaphore.availablePermits());
//
//                Thread.sleep((long) (Math.random()*10000));
//                semaphore.release();
//            }
        }
    }
}