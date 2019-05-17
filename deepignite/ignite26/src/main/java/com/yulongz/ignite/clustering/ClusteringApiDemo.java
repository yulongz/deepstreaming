package com.yulongz.ignite.clustering;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCluster;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.cluster.ClusterMetrics;
import org.apache.ignite.cluster.ClusterNode;

import java.util.Collection;

/**
 * @author hadoop
 * @date 17-11-29
 */
public class ClusteringApiDemo {
    public static void main(String[] args) {
        Ignition.setClientMode(true);
        try (Ignite ignite = Ignition.start("default-config.xml")){
            IgniteCluster cluster = ignite.cluster();
            // 获取集群节点属性
//            ClusterGroup clusterGroup = cluster.forAttribute("", "");
            ClusterGroup clusterGroup = cluster.forRemotes();
            Collection<ClusterNode> nodes = clusterGroup.nodes();
            for (ClusterNode node : nodes){
                ClusterMetrics metrics = node.metrics();
                double cpuLoad = metrics.getCurrentCpuLoad();
                long usedHeap = metrics.getHeapMemoryUsed();
                int numberOfCores = metrics.getTotalCpus();
                int activeJobs = metrics.getCurrentActiveJobs();
                System.out.println(node.id()+": "+cpuLoad);
                System.out.println(node.id()+": "+usedHeap);
                System.out.println(node.id()+": "+numberOfCores);
                System.out.println(node.id()+": "+activeJobs);
            }

            ClusterNode node = cluster.localNode();
            System.out.println("localnode: "+node.id());
            System.out.println("localnode: "+cluster.forOldest().node().id());
        }
    }
}
