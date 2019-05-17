package com.yulongz.ignite.servicegrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteServices;
import org.apache.ignite.Ignition;
import org.apache.ignite.services.ServiceConfiguration;

/**
 * @author hadoop
 * @date 17-11-28
 */
public class ServicesDemo {
    public static void main(String[] args) {
        try(Ignite ignite = Ignition.start("default-config.xml")){
            ServiceConfiguration sCfg = new ServiceConfiguration();
            sCfg.setNodeFilter(new ServiceFilterDemo());
            sCfg.setName("serviceName");
            // Setting the cache name and key's value for the affinity based deployment.
            sCfg.setCacheName("orgCache");
            sCfg.setAffinityKey(123);


            IgniteServices services = ignite.services();

            services.deploy(sCfg);
        }
    }
}