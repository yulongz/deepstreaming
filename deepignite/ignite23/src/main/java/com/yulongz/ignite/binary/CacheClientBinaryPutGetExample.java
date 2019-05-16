package com.yulongz.ignite.binary;

import com.yulongz.ignite.model.Address;
import com.yulongz.ignite.model.Organization;
import com.yulongz.ignite.model.OrganizationType;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author hadoop
 * @date 17-12-5
 */
public class CacheClientBinaryPutGetExample {
    private static final String CACHE_NAME = CacheClientBinaryPutGetExample.class.getSimpleName();
    public static void main(String[] args) {
        try(Ignite ignite = Ignition.start("default-config.xml")){
            CacheConfiguration cacheCfg = new CacheConfiguration(CACHE_NAME);
            cacheCfg.setCacheMode(CacheMode.PARTITIONED);
            cacheCfg.setAtomicityMode(CacheAtomicityMode.ATOMIC);
            try(IgniteCache<Integer, Organization> cache = ignite.getOrCreateCache(cacheCfg)){

                putGet(cache);

                putGetBinary(cache);

                putGetAll(cache);

                putGetAllBinary(cache);

            }finally {
                ignite.destroyCache(CACHE_NAME);
            }
        }
    }

    public static void putGet(IgniteCache<Integer, Organization> cache){

        Organization org = new Organization(
                "Microsoft",
                new Address("1096 Eddy Street, San Francisco, CA", 94109),
                OrganizationType.PRIVATE,
                new Timestamp(System.currentTimeMillis()));


        cache.put(1, org);


        Organization orgFromCache = cache.get(1);

        System.out.println();
        System.out.println(">>> Retrieved organization instance from cache: " + orgFromCache);
    }

    public static void putGetBinary(IgniteCache<Integer, Organization> cache){
        Organization org = new Organization("Microsoft",
                new Address("1096 Eddy Street, San Francisco, CA", 94109),
                OrganizationType.PRIVATE,
                new Timestamp(System.currentTimeMillis()));

        cache.put(2, org);

        IgniteCache<Integer, BinaryObject> binaryCache = cache.withKeepBinary();

        BinaryObject binaryObject = binaryCache.get(2);

        Object name = binaryObject.field("name");

        System.out.println();
        System.out.println(">>> Retrieved organization name from binary object: " + name);
    }

    public static void putGetAll(IgniteCache<Integer, Organization> cache){
        Organization org1 = new Organization(
                "Microsoft",
                new Address("1096 Eddy Street, San Francisco, CA", 94109),
                OrganizationType.PRIVATE,
                new Timestamp(System.currentTimeMillis()));

        Organization org2 = new Organization(
                "Red Cross",
                new Address("184 Fidler Drive, San Antonio, TX", 78205),
                OrganizationType.NON_PROFIT,
                new Timestamp(System.currentTimeMillis()));

        Map<Integer, Organization> map = new HashMap<>();

        map.put(3, org1);
        map.put(4, org2);


        cache.putAll(map);


        Map<Integer, Organization> mapFromCache = cache.getAll(map.keySet());

        System.out.println();
        System.out.println(">>> Retrieved organization instances from cache:");

        for (Organization org : mapFromCache.values()) {
            System.out.println(">>>     " + org);
        }
    }

    public static void putGetAllBinary(IgniteCache<Integer, Organization> cache){

        Organization org1 = new Organization(
                "Microsoft",
                new Address("1096 Eddy Street, San Francisco, CA", 94109),
                OrganizationType.PRIVATE,
                new Timestamp(System.currentTimeMillis()));

        Organization org2 = new Organization(
                "Red Cross",
                new Address("184 Fidler Drive, San Antonio, TX", 78205),
                OrganizationType.NON_PROFIT,
                new Timestamp(System.currentTimeMillis()));

        Map<Integer, Organization> map = new HashMap<>();

        map.put(5, org1);
        map.put(6, org2);

        cache.putAll(map);

        IgniteCache<Integer, BinaryObject> binaryCache = cache.withKeepBinary();

        Map<Integer, BinaryObject> all = binaryCache.getAll(map.keySet());

        Collection<String> names = new HashSet<>();

         for (BinaryObject bo : all.values()){
            names.add(bo.field("name"));
         }

        System.out.println(">>> Retrieved organization names from binary objects: " + names);
    }
}
