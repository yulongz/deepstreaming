package com.yulongz.ignite.datastructures;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCountDownLatch;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteRunnable;

/**
 * @author hadoop
 * @date 17-12-6
 */
public class IgniteCountDownLatchDemo {
    public static void main(String[] args) {
        try (Ignite ignite = Ignition.start("default-config.xml")) {
            String name = "downLatch";
            IgniteCountDownLatch downLatch = ignite.countDownLatch(name, 10, true, true);

            System.out.println("initVal: " + downLatch.count());

            DownLatchClosure closure = new DownLatchClosure(name);
            for (int i = 0; i < 10; i++){
                ignite.compute().run(closure);
            }

            downLatch.await();

            System.out.println("end");
        }
    }

    private static class DownLatchClosure implements IgniteRunnable{

        private final String name;

        public DownLatchClosure(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            IgniteCountDownLatch latch = Ignition.ignite().countDownLatch(name, 1, false, true);

            int newCnt = latch.countDown();

            System.out.println("Counted down [newCnt=" + newCnt + ", nodeId=" + Ignition.ignite().cluster().localNode().id() + ']');
        }
    }
}