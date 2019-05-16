package com.yulongz.ignite.datagrid;

import com.yulongz.ignite.model.Employee;
import com.yulongz.ignite.model.Organization;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.lang.IgniteRunnable;

/**
 * Created by hadoop on 17-10-20.
 *
 * @author hadoop
 * @date 2017/10/20
 */
public class CacheAffinityDemo {
    private static final String EMP_CACHE = "empcache";
    private static final String ORG_CACHE = "orgcache";
    public static void main(String[] args) {
        try(Ignite ignite = Ignition.start("default-config.xml")){
            try{
                IgniteCache<Object, Employee> empCache = ignite.getOrCreateCache(EMP_CACHE);
                IgniteCache<Integer, Organization> orgCache = ignite.getOrCreateCache(ORG_CACHE);

                //EmployeeKey employeekey1 = new EmployeeKey(1, 111);
                //EmployeeKey employeekey2 = new EmployeeKey(2, 111);

                Object employeekey1 = new AffinityKey(1,111);
                Object employeekey2 = new AffinityKey(2,111);

                Employee employee1 = new Employee(employeekey1, "John");
                Employee employee2 = new Employee(employeekey2, "Cook");

                orgCache.put(111, new Organization("ApacheIgnite"));
                empCache.put(employeekey1, employee1);
                empCache.put(employeekey2, employee2);

                System.out.println(orgCache.get(111));

                System.out.println(empCache.get(new AffinityKey<>(1, 111)));

                ignite.compute().affinityRun(EMP_CACHE, "ApacheIgnite", new IgniteRunnable() {
                    @Override
                    public void run() {
                        Organization organization = orgCache.get(111);
                        Employee person1 = empCache.get(employeekey1);
                        Employee person2 = empCache.get(employeekey2);
                        System.out.println("person1.name:"+person1.getName());
                        System.out.println("person2.name:"+person2.getName());
                        System.out.println("organization.name:"+organization.getName());
                    }
                });

            } finally {
                ignite.destroyCache(EMP_CACHE);
                ignite.destroyCache(ORG_CACHE);
            }
        }
    }
}
