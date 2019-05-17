package com.yulongz.ignite.datagrid;

import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.configuration.TopologyValidator;

import java.util.Collection;

/**
 * Created by hadoop on 17-10-20.
 *
 * @author hadoop
 * @date 2017/10/20
 *
 *
 *  对缓存允许更新操作情况如下：
 *  cache1:集群具有两个节点时
 *  cache2:集群至少有两个节点时
 */
public class CacheTopologyValidatorDemo {
    private static final String CACHE_NAME = CacheTopologyValidatorDemo.class.getSimpleName();

    public static void main(String[] args) {
        IgniteConfiguration iCfg = new IgniteConfiguration();
        for (CacheConfiguration cCfg : iCfg.getCacheConfiguration()){
            if (cCfg.getName() != null) {
                if ("cache1".equals(cCfg.getName())){
                    cCfg.setTopologyValidator(new TopologyValidator() {
                        @Override
                        public boolean validate(Collection<ClusterNode> collection) {
                            return collection.size() == 2;
                        }
                    });
                } else if ("cache2".equals(cCfg.getName())){
                    cCfg.setTopologyValidator(new TopologyValidator() {
                        @Override
                        public boolean validate(Collection<ClusterNode> collection) {
                            return collection.size() >= 2;
                        }
                    });
                }
            }
        }
    }
}
