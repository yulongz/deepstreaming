package com.yulongz.ignite.servicegrid.mycountservice;

import org.apache.ignite.*;
import org.apache.ignite.lang.IgniteRunnable;
import org.apache.ignite.resources.ServiceResource;

/**
 * @author hadoop
 * @date 17-11-28
 */
public class MyCounterServiceMain {
    public static void main(String[] args) {
        Ignition.setClientMode(true);

        try(Ignite ignite = Ignition.start("default-config.xml")){
            IgniteCache<String, Integer> cache = ignite.getOrCreateCache("myCounterCache");

//            IgniteServices svs = ignite.services();
//
//            MyCounterService proxy = svs.serviceProxy("MyCounterService", MyCounterService.class, false);
//
//            proxy.increment();
//
//            System.out.println(proxy.get());

            compute2Service(ignite);
        }
    }


    public static void compute2Service(Ignite ignite){
        IgniteCompute compute = ignite.compute();

        compute.run(new IgniteRunnable() {
            // 通过@ServiceResource注解在计算中注入一个服务代理的实例
            @ServiceResource(serviceName = "MyCounterService")
            private MyCounterService cntrSvc;
            @Override
            public void run() {
                // Ivoke a method on 'MyCounterService' interface.
                int newValue = cntrSvc.increment();
                // Print latest counter value from our counter service.
                System.out.println("Incremented value : " + newValue);
            }
        });
    }
}
