package com.yulongz.ignite.computegrid.failover;

import com.yulongz.ignite.utils.ExamplesUtils;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.IgniteLogger;
import org.apache.ignite.Ignition;
import org.apache.ignite.compute.ComputeJobFailoverException;
import org.apache.ignite.compute.ComputeTaskSession;
import org.apache.ignite.compute.ComputeTaskSessionFullSupport;
import org.apache.ignite.lang.IgniteBiTuple;
import org.apache.ignite.lang.IgniteClosure;
import org.apache.ignite.resources.LoggerResource;
import org.apache.ignite.resources.TaskSessionResource;

import java.util.Arrays;
import java.util.List;

/**
 * @author hadoop
 * @date 17-11-22
 */
public class ComputeFailoverDemo {
    public static void main(String[] args) throws IgniteException {
        try (Ignite ignite = Ignition.start(ComputeFailoverNodeStartup.configuration())) {
            if (!ExamplesUtils.checkMinTopologySize(ignite.cluster(), 2))
                return;

            System.out.println();
            System.out.println("Compute failover example started.");

            // Number of letters.
            int charCnt = ignite.compute().apply(new CheckPointJob(), "Stage1 Stage2");

            System.out.println();
            System.out.println(">>> Finished executing fail-over example with checkpoints.");
            System.out.println(">>> Total number of characters in the phrase is '" + charCnt + "'.");
            System.out.println(">>> You should see exception stack trace from failed job on some node.");
            System.out.println(">>> Failed job will be failed over to another node.");
        }
    }

    @ComputeTaskSessionFullSupport
    private static final class CheckPointJob implements IgniteClosure<String, Integer> {
        /** Injected distributed task session. */
        @TaskSessionResource
        private ComputeTaskSession jobSes;

        /** Injected ignite logger. */
        @LoggerResource
        private IgniteLogger log;

        /** */
        private IgniteBiTuple<Integer, Integer> state;

        /** */
        private String phrase;

        /**
         * The job will check the checkpoint with key '{@code fail}' and if
         * it's {@code true} it will throw exception to simulate a failure.
         * Otherwise, it will execute enabled method.
         */
        @Override public Integer apply(String phrase) {
            System.out.println();
            System.out.println(">>> Executing fail-over example job.");

//            jobSes.setAttribute("grid.task.priority", 10);
//            System.out.println("----------------"+jobSes.getAttribute("grid.task.priority"));

            this.phrase = phrase;

            List<String> words = Arrays.asList(phrase.split(" "));

            final String cpKey = checkpointKey();

            IgniteBiTuple<Integer, Integer> state = jobSes.loadCheckpoint(cpKey);



            int idx = 0;
            int sum = 0;

            if (state != null) {
                this.state = state;

                // Last processed word index and total length.
                idx = state.get1();
                sum = state.get2();
            }

            for (int i = idx; i < words.size(); i++) {
                sum += words.get(i).length();

                this.state = new IgniteBiTuple<>(i + 1, sum);

                // Save checkpoint with scope of task execution.
                // It will be automatically removed when task completes.
                jobSes.saveCheckpoint(cpKey, this.state);

                // For example purposes, we fail on purpose after first stage.
                // This exception will cause job to be failed over to another node.
                if (i == 0) {
                    System.out.println();
                    System.out.println(">>> Job will be failed over to another node.");

                    throw new ComputeJobFailoverException("Expected example job exception.");
                }
            }

            return sum;
        }

        /**
         * Make reasonably unique checkpoint key.
         *
         * @return Checkpoint key.
         */
        private String checkpointKey() {
            return getClass().getName() + '-' + phrase;
        }
    }
}
