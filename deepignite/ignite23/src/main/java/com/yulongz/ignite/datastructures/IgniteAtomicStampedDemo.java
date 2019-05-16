package com.yulongz.ignite.datastructures;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteAtomicStamped;
import org.apache.ignite.Ignition;

/**
 * @author hadoop
 * @date 17-12-6
 */
public class IgniteAtomicStampedDemo {
    public static void main(String[] args) {
        try (Ignite ignite = Ignition.start("default-config.xml")) {
            String name = "stamp";

            IgniteAtomicStamped<String, String> stamped = ignite.atomicStamped(name, "initVal", "initStamp", true);
            printVal("initVal: ", stamped);

            stamped.compareAndSet("wrongVal", "newVal", "wrongStamp", "newStamp");
            printVal("wrong: ", stamped);

            stamped.compareAndSet("initVal", "newVal", "initStamp", "newStamp");
            printVal("correct: ", stamped);
        }
    }

    public static void printVal(String string, IgniteAtomicStamped<String, String> stamped){
        System.out.println(string);
        System.out.println("Atomic stamped [value=" + stamped.value() + ", stamp=" + stamped.stamp() + ']');
    }
}