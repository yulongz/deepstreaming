package com.yulongz.ignite.datastructures;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteAtomicSequence;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.AtomicConfiguration;

/**
 * @author hadoop
 * @date 17-12-6
 */
public class IgniteAtomicSequenceDemo {
    public static void main(String[] args) {
        try (Ignite ignite = Ignition.start("default-config.xml")) {
            String name = "sequence";
            int retries = 10;

            AtomicConfiguration aCfg = new AtomicConfiguration();
//            aCfg.setAtomicSequenceReserveSize(5);

            IgniteAtomicSequence sequence = ignite.atomicSequence(name, aCfg, 0, true);

            long firstVal = sequence.get();
            System.out.println("firstVal: " + sequence.get());

            for (int i=0;i<retries;i++){
                System.out.println("Sequence [currentValue=" + sequence.get() + ", afterIncrement=" +
                        sequence.incrementAndGet() + ']');
            }

            System.out.println("Sequence after incrementing [expected=" + (firstVal + retries) + ", actual=" +
                    sequence.get() + ']');
        }
    }
}