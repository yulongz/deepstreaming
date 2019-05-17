package com.yulongz.ignite.datastructures;

import org.apache.ignite.*;

/**
 * @author hadoop
 * @date 17-12-7
 */
public class Demo {
    public static void main(String[] args) {
        try (Ignite ignite = Ignition.start("default-config.xml")) {
            IgniteAtomicReference<Object> reference =
                    ignite.atomicReference("reference", null, true);
            System.out.println(reference.get());

            IgniteAtomicSequence sequence = ignite.atomicSequence("sequence", 0, false);
            System.out.println(sequence.get());

            IgniteAtomicStamped<Object, Object> stamp = ignite.atomicStamped("stamp", null, null, true);
            System.out.println(stamp.value().toString()+stamp.stamp().toString());
        }
    }
}