package com.yulongz.ignite.servicegrid.mycountservice;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteServices;
import org.apache.ignite.Ignition;

/**
 * @author hadoop
 * @date 17-11-28
 */
public class MyCounterServiceInit {
    public static void main(String[] args) {
        Ignition.setClientMode(true);
        try(Ignite ignite = Ignition.start("default-config.xml")){
            IgniteServices svs = ignite.services();

//            svs.deployMultiple("MyCounterService", new MyCounterServiceImpl(),3, 2);
            svs.deployNodeSingleton("MyCounterService", new MyCounterServiceImpl());
        }
    }
}
