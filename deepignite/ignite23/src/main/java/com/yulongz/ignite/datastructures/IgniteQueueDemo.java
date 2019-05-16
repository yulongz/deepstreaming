package com.yulongz.ignite.datastructures;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteQueue;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CollectionConfiguration;
import org.apache.ignite.lang.IgniteRunnable;

import java.util.UUID;

/**
 * @author hadoop
 * @date 17-12-1
 */
public class IgniteQueueDemo {
    private static final int RETRIES = 20;

    public static void main(String[] args) {
        Ignition.setClientMode(true);
        try(Ignite ignite = Ignition.start("default-config.xml")) {

            String queueName = UUID.randomUUID().toString();

            IgniteQueue<String> queue = initializeQueue(ignite, queueName);

            readFromQueue(ignite, queue);

            writeToQueue(ignite, queue);

            clearAndRemoveQueue(ignite,queue);
        }
    }

    public static IgniteQueue<String> initializeQueue(Ignite ignite, String name){
        CollectionConfiguration cCfg = new CollectionConfiguration();
        cCfg.setCacheMode(CacheMode.PARTITIONED);

        IgniteQueue<String> queue = ignite.queue(name, 0, cCfg);

        for (int i = 0; i < RETRIES * 2; i++){
            queue.put(Integer.toString(i));
        }

        return queue;
    }

    public static void readFromQueue(Ignite ignite, IgniteQueue<String> queue){
        ignite.compute().broadcast(new QueueClosure(queue, false));
        System.out.println("Queue size after reading [expected=0, actual=" + queue.size() + ']');
    }

    public static void writeToQueue(Ignite ignite, IgniteQueue<String> queue){
        ignite.compute().broadcast(new QueueClosure(queue, true));

        System.out.println("Queue size after writing [expected=" + ignite.cluster().nodes().size() * RETRIES +
                ", actual=" + queue.size() + ']');

        System.out.println("Iterate over queue.");

        // Iterate over queue.
        for (String item : queue) {
            System.out.println("Queue item: " + item);
        }
    }

    public static void clearAndRemoveQueue(Ignite ignite, IgniteQueue<String> queue){
        System.out.println("Queue size before clearing: " + queue.size());

        // Clear queue.
        queue.clear();

        System.out.println("Queue size after clearing: " + queue.size());

        // Remove queue.
        queue.close();

        // Try to work with removed queue.
        try {
            queue.poll();
        }
        catch (IllegalStateException expected) {
            System.out.println("Expected exception - " + expected.getMessage());
        }
    }

    private static class QueueClosure implements IgniteRunnable{

        private IgniteQueue queue;

        private Boolean put;

        public QueueClosure(IgniteQueue queue, Boolean put) {
            this.queue = queue;
            this.put = put;
        }

        @Override
        public void run() {
            if (put) {
                for (int i=0;i<RETRIES;i++){
                    String item = Ignition.ignite().cluster().localNode().id()+"_"+Integer.toString(i);
                    queue.put(item);
                    System.out.println(item);
                }
            }else {
                    // Take items from queue head.
                    for (int i = 0; i < RETRIES; i++) {
                        System.out.println("Queue item has been read from queue head: " + queue.take());
                    }

                    // Take items from queue head once again.
                    for (int i = 0; i < RETRIES; i++) {
                        System.out.println("Queue item has been read from queue head once again: " + queue.poll());
                    }
            }
        }
    }
}
