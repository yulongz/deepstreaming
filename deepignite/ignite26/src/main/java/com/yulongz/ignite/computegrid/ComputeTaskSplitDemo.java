package com.yulongz.ignite.computegrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.compute.ComputeJobResult;
import org.apache.ignite.compute.ComputeTaskSplitAdapter;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author hadoop
 * @date 17-11-22
 */
public class ComputeTaskSplitDemo {

    public static void main(String[] args) {
        try(Ignite ignite = Ignition.start("default-config.xml")) {
            int cnt = ignite.compute().execute(SpilCountExample.class, "Hello Ignite Enabled World!");

            System.out.println();
            System.out.println(">>> Total number of characters in the phrase is '" + cnt + "'.");
        }
    }


    public static class SpilCountExample extends ComputeTaskSplitAdapter<String, Integer> {


        @Override
        protected Collection<? extends ComputeJob> split(int gridSize, String arg) throws IgniteException {
            Collection<ComputeJob> jobs = new LinkedList<>();

            for (String word : arg.split(" ")){
                jobs.add(new ComputeJob() {
                    @Override
                    public void cancel() {

                    }

                    @Override
                    public Object execute() throws IgniteException {
                        return word.length();
                    }
                });
            }

            return jobs;
        }

        @Nullable
        @Override
        public Integer reduce(List<ComputeJobResult> results) throws IgniteException {
            int sum = 0;
            for (ComputeJobResult res : results){
                sum += res.<Integer>getData();
            }
            return sum;
        }
    }
}
