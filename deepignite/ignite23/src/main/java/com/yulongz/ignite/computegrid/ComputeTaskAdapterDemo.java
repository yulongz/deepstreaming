package com.yulongz.ignite.computegrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.compute.*;

import java.util.*;

/**
 * @author hadoop
 * @date 17-11-27
 */
public class ComputeTaskAdapterDemo {
    public static void main(String[] args) {
        try(Ignite ignite = Ignition.start("default-config.xml")) {

            int cnt = ignite.compute().execute(CharacterCountTask.class, "Hello Grid Enabled World!");
            System.out.println(">>> Total number of characters in the phrase is '" + cnt + "'.");
        }
    }



    /**
     * Task to count non-white-space characters in a phrase.
     */
    private static class CharacterCountTask extends ComputeTaskAdapter<String, Integer> {
        // 1. Splits the received string into to words
        // 2. Creates a child job for each word
        // 3. Sends created jobs to other nodes for processing.
        @Override
        public Map<? extends ComputeJob, ClusterNode> map(List<ClusterNode> subgrid, String arg) {
            String[] words = arg.split(" ");
            Map<ComputeJob, ClusterNode> map = new HashMap<>(words.length);
            Iterator<ClusterNode> it = subgrid.iterator();
            for (final String word : arg.split(" ")) {
                // If we used all nodes, restart the iterator.
                if (!it.hasNext())
                    it = subgrid.iterator();
                ClusterNode node = it.next();
                map.put(new ComputeJobAdapter() {
                    @Override public Object execute() {
                        System.out.println(">>> Printing '" + word + "' on this node from grid job.");
                        // Return number of letters in the word.
                        return word.length();
                    }
                }, node);
            }
            return map;
        }
        @Override
        public Integer reduce(List<ComputeJobResult> results) {
            int sum = 0;
            for (ComputeJobResult res : results)
                sum += res.<Integer>getData();
            return sum;
        }
    }
}
