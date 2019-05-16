package com.yulongz.ignite.datastructures;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteAtomicLong;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.AtomicConfiguration;

/**
 * @author hadoop
 * @date 17-12-6
 */
public class IgniteAtomicLongDemo {
    public static void main(String[] args) {
        try(Ignite ignite = Ignition.start("default-config.xml")){
            String name = "long";

            AtomicConfiguration aCfg = new AtomicConfiguration();

            IgniteAtomicLong aLong = ignite.atomicLong(name, 10, true);

            // 11
            long l = aLong.addAndGet(1);

            // 12
            long andIncrement = aLong.getAndIncrement();

            System.out.println(aLong.get());
        }
    }
}
