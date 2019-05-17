package com.yulongz.ignite.computegrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.compute.ComputeJobAdapter;
import org.apache.ignite.compute.ComputeJobResult;
import org.apache.ignite.compute.ComputeTaskAdapter;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author hadoop
 * @date 17-11-22
 */
public class ComputeTaskMapDemo {

    public static void main(String[] args) {
        Ignition.setClientMode(true);
        try(Ignite ignite = Ignition.start("default-config.xml")) {
            int cnt = ignite.compute().execute(MapExampleCharacterCountTask.class, "Hello Ignite Enabled World!");

            System.out.println();
            System.out.println(">>> Total number of characters in the phrase is '" + cnt + "'.");
        }
    }

    private static class MapExampleCharacterCountTask extends ComputeTaskAdapter<String, Integer>{

        @Nullable
        @Override
        public Map<? extends ComputeJob, ClusterNode> map(List<ClusterNode> nodes, @Nullable String arg) throws IgniteException {
            Map<ComputeJob, ClusterNode> map = new HashMap<>();
            Iterator<ClusterNode> it = nodes.iterator();
            ClusterNode node = it.next();

            for (String word : arg.split(" ")){
                map.put(new ComputeJobAdapter() {
                    @Override
                    public Object execute() throws IgniteException {
                        return word.length();
                    }
                }, node);
            }

            return map;
        }

        @Nullable
        @Override
        public Integer reduce(List<ComputeJobResult> results) throws IgniteException {
            int sum = 0;

            for (ComputeJobResult res : results) {
                sum += res.<Integer>getData();
            }

            return sum;
        }
    }
}
