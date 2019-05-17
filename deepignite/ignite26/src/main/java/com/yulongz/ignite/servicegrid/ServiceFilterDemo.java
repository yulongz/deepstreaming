package com.yulongz.ignite.servicegrid;

import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.lang.IgnitePredicate;

/**
 * @author hadoop
 * @date 17-11-28
 */
public class ServiceFilterDemo implements IgnitePredicate<ClusterNode> {
    @Override
    public boolean apply(ClusterNode node) {
        return !node.isClient() && node.attributes().containsKey("west.coast.node");
    }
}