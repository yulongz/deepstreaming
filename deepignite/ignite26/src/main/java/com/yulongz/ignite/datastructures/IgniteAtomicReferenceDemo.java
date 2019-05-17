package com.yulongz.ignite.datastructures;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteAtomicReference;
import org.apache.ignite.Ignition;

/**
 * @author hadoop
 * @date 17-12-6
 */
public class IgniteAtomicReferenceDemo {
    public static void main(String[] args) {
        try (Ignite ignite = Ignition.start("default-config.xml")) {
            String name = "reference";

            IgniteAtomicReference<String> reference = ignite.atomicReference(name, "initVal", true);

            String initVal = reference.get();
            System.out.println("initVal: " + initVal);

            String newVal = "newVal";

            // won't change
            reference.compareAndSet("wrongVal", newVal);

            pringVal(reference);

            reference.compareAndSet(initVal, newVal);

            pringVal(reference);
        }
    }

    public static void pringVal(IgniteAtomicReference<String> reference){
        System.out.println("reference value is: " + reference.get());
    }
}